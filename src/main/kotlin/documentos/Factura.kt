package documentos

import documentos.GenerarDocumentos.Companion.generarClave
import utils.mensajeNulo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Validation
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

class Factura {


    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()
    private val codigoNumerico = "12345678" // Codigo Quemado en guía del SRI
    private val versionXML = "1.0"
    private val encodingXML = "UTF-8"
    private val standaloneXML = "yes"
    private val nombreEtiquetaFactura = "factura"
    private val idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    private val versionFacturaXML = "1.1.0" // Codigo Quemado en guía del SRI

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoFactura $mensajeNulo")
    var infoFactura: InformacionFactura

    @NotNull(message = "detalles $mensajeNulo")
    var detalles: ArrayList<Detalle>

    var infoAdicional: ArrayList<CampoAdicional>?

    var ambiente: String

    constructor(
        infoTributario: InformacionTributaria,
        infoFactura: InformacionFactura,
        detalles: ArrayList<Detalle>,
        infoAdicional: ArrayList<CampoAdicional>,
        ambiente: String = "1"
    ) {
        this.infoTributario = infoTributario
        this.infoFactura = infoFactura
        this.detalles = detalles
        this.infoAdicional = infoAdicional
        this.ambiente = ambiente
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoFactura.fechaEmision)

        this.infoTributario.claveAcceso = generarClave(
            fecha,
            this.infoTributario.codDoc,
            this.infoTributario.ruc,
            this.ambiente,
            (this.infoTributario.estab + this.infoTributario.ptoEmision),
            this.infoTributario.secuencial,
            this.codigoNumerico,
            this.infoTributario.tipoEmision
        )
    }

    fun validar(): ArrayList<String> {
        val errores = arrayListOf<String>()

        val violationsInfoTributaria = validator.validate(this.infoTributario)

        for (violation in violationsInfoTributaria) {
            errores.add(violation.message)
        }

        val violationsInformacionFactura = validator.validate(this.infoFactura)

        for (violation in violationsInformacionFactura) {
            errores.add(violation.message)
        }

        this.infoFactura.totalConImpuestos.forEach {
            val violationsTotalConImpuestos = validator.validate(it)

            for (violation in violationsTotalConImpuestos) {
                errores.add(violation.message)
            }
        }

        this.infoFactura.pagos.forEach {
            val violationsPagos = validator.validate(it)

            for (violation in violationsPagos) {
                errores.add(violation.message)
            }
        }

        this.detalles.forEach {
            val violationsDetalle = validator.validate(it)

            for (violation in violationsDetalle) {
                errores.add(violation.message)
            }

            it.detallesAdicionales?.forEach {
                val violationsDetallesAdicionales = validator.validate(it)

                for (violation in violationsDetallesAdicionales) {
                    errores.add(violation.message)
                }
            }

            it.impuestos?.forEach {
                val violationsImpuestos = validator.validate(it)

                for (violation in violationsImpuestos) {
                    errores.add(violation.message)
                }
            }

        }

        this.infoAdicional?.forEach {
            val violationsInfoAdicional = validator.validate(it)

            for (violation in violationsInfoAdicional) {
                errores.add(violation.message)
            }
        }


        return errores
    }

    fun generarFacturaXML(): String {
        var xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" standalone=\"$standaloneXML\"?>\n" +
                    "<$nombreEtiquetaFactura id=\"${idComprobante}\" version=\"$versionFacturaXML\">\n" +
                    generarCuerpoFactura() +
                    "</$nombreEtiquetaFactura>"
        return xmlString
    }

    private fun generarCuerpoFactura(): String {

        val contenidoFactura = generarInformacionTributaria() +
                generarInformacionFactura() +
                generarDetalles()

        return contenidoFactura
    }

    private fun generarInformacionTributaria(): String {
        val nombreEtiquetaInformacionTributaria = "infoTributaria"

        val informacionTributaria = ("<$nombreEtiquetaInformacionTributaria>\n"
                + "        <ambiente>${this.infoTributario.ambiente}</ambiente>\n"
                + "        <tipoEmision>${this.infoTributario.tipoEmision}</tipoEmision>\n"
                + "        <razonSocial>${this.infoTributario.razonSocial}</razonSocial>\n"
                + "        <nombreComercial>${if (this.infoTributario.nombreComercial != null) this.infoTributario.nombreComercial else ""}</nombreComercial>\n"
                + "        <ruc>${this.infoTributario.ruc}</ruc>\n"
                + "        <claveAcceso>${this.infoTributario.claveAcceso}</claveAcceso>\n"
                + "        <codDoc>${this.infoTributario.codDoc}</codDoc>\n"
                + "        <estab>${this.infoTributario.estab}</estab>\n"
                + "        <ptoEmi>${this.infoTributario.ptoEmision}</ptoEmi>\n"
                + "        <secuencial>${this.infoTributario.secuencial}</secuencial>\n"
                + "        <dirMatriz>${this.infoTributario.dirMatriz}</dirMatriz>\n"
                + "</$nombreEtiquetaInformacionTributaria>\n")

        return informacionTributaria
    }

    private fun generarInformacionFactura(): String {
        val nombreEtiquetaInformacionFactura = "infoFactura"

        val informacionFactura = ("<$nombreEtiquetaInformacionFactura>\n"
                + "        <fechaEmision>${this.infoFactura.fechaEmision}</fechaEmision>\n"
                + "        <dirEstablecimiento>${this.infoFactura.dirEstablecimiento ?: ""}</dirEstablecimiento>\n"
                + "        <contribuyenteEspecial>${this.infoFactura.contribuyenteEspecial
            ?: ""}</contribuyenteEspecial>\n"
                + "        <obligadoContabilidad>${this.infoFactura.obligadoContabilidad
            ?: ""}</obligadoContabilidad>\n"
                + "        <tipoIdentificacionComprador>${this.infoFactura.tipoIdentificacionComprador}</tipoIdentificacionComprador>\n"
                + "        <razonSocialComprador>${this.infoFactura.razonSocialComprador}</razonSocialComprador>\n"
                + "        <identificacionComprador>${this.infoFactura.identificacionComprador}</identificacionComprador>\n"
                + "        <totalSinImpuestos>${this.infoFactura.totalSinImpuestos}</totalSinImpuestos>\n"
                + "        <totalDescuento>${this.infoFactura.totalDescuento}</totalDescuento>\n"
                + generarTotalConImpuestos()
                + "         <propina>${this.infoFactura.propina}</propina>\n"
                + "         <importeTotal>${this.infoFactura.importeTotal}</importeTotal>\n"
                + "         <moneda>${this.infoFactura.moneda}</moneda>\n"
                + generarPagos()
                + "         <valorRetIva>${this.infoFactura.valorRetIva}</valorRetIva>\n"
                + "         <valorRetRenta>${this.infoFactura.valorRetRenta}</valorRetRenta>\n"
                + "</$nombreEtiquetaInformacionFactura>\n")
        return informacionFactura
    }

    private fun generarTotalConImpuestos(): String {
        val nombreEtiquetaTotalConImpuestos = "totalConImpuestos"
        val totalConImpuestos = ("        <$nombreEtiquetaTotalConImpuestos>\n"
                + generarTotalImpuesto(this.infoFactura.totalConImpuestos)
                + "         </$nombreEtiquetaTotalConImpuestos>\n")
        return totalConImpuestos
    }

    private fun generarTotalImpuesto(totalImpuestosArreglo: ArrayList<TotalImpuesto>): String {
        val nombreEtiquetaTotalImpuestos = "totalImpuesto"
        var totalImpuestos = ""
        totalImpuestosArreglo.forEach {
            totalImpuestos += ("            <$nombreEtiquetaTotalImpuestos>\n"
                    + "                <codigo>${it.codigo}</codigo>\n"
                    + "                <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
                    + "                <descuentoAdicional>${it.descuentoAdicional ?: ""}</descuentoAdicional>\n"
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + "             </$nombreEtiquetaTotalImpuestos>\n")
        }
        return totalImpuestos
    }

    private fun generarPagos(): String {
        val nombreEtiquetaPagos = "pagos"
        val totalConImpuestos = ("        <$nombreEtiquetaPagos>\n"
                + generarPago(this.infoFactura.pagos)
                + "         </$nombreEtiquetaPagos>\n")
        return totalConImpuestos
    }

    private fun generarPago(totalPagos: ArrayList<Pago>): String {
        val nombreEtiquetaPago = "pago"
        var totalImpuestos = ""
        totalPagos.forEach {
            totalImpuestos += ("            <$nombreEtiquetaPago>\n"
                    + "                <formaPago>${it.formaPago}</formaPago>\n"
                    + "                <total>${it.total}</total>\n"
                    + "                <plazo>${it.plazo ?: ""}</plazo>\n"
                    + "                <unidadTiempo>${it.unidadTiempo ?: ""}</unidadTiempo>\n"
                    + "             </$nombreEtiquetaPago>\n")
        }
        return totalImpuestos
    }

    fun generarDetalles(): String {
        val nombreEtiquetaDetalles = "detalles"
        val totalConImpuestos = ("<$nombreEtiquetaDetalles>\n"
                + generarDetalle(this.detalles)
                + "</$nombreEtiquetaDetalles>\n")
        return totalConImpuestos

    }

    private fun generarDetalle(totalDetalles: ArrayList<Detalle>): String {
        val nombreEtiquetaDetalle = "detalle"
        var totalDetalle = ""
        totalDetalles.forEach {
            var detallesAdicionales = ""
            if (it.detallesAdicionales != null) {
                detallesAdicionales = generarDetallesAdicionales(it.detallesAdicionales)
            }


            totalDetalle += ("         <$nombreEtiquetaDetalle>\n"
                    + "         <codigoPrincipal>${it.codigoPrincipal ?: ""}</codigoPrincipal>\n"
                    + "         <codigoAuxiliar>${it.codigoAuxiliar ?: ""}</codigoAuxiliar>\n"
                    + "         <descripcion>${it.descripcion}</descripcion>\n"
                    + "         <cantidad>${it.cantidad}</cantidad>\n"
                    + "         <precioUnitario>${it.precioUnitario}</precioUnitario>\n"
                    + "         <descuento>${it.descuento}</descuento>\n"
                    + "         <precioTotalSinImpuesto>${it.precioTotalSinImpuesto}</precioTotalSinImpuesto>\n"
                    + detallesAdicionales
                    + generarImpuestos(it.impuestos)
                    + "         </$nombreEtiquetaDetalle>\n")
        }
        return totalDetalle
    }

    private fun generarDetallesAdicionales(detallesAdicionales: ArrayList<DetalleAdicional>?): String {
        val nombreEtiquetaDetallesAdicionales = "detallesAdicionales"
        val totalDetallesAdicionales = ("         <$nombreEtiquetaDetallesAdicionales>\n"
                + generarDetalleAdicional(detallesAdicionales)
                + "         </$nombreEtiquetaDetallesAdicionales>\n")
        return totalDetallesAdicionales
    }

    private fun generarDetalleAdicional(detallesAdicionales: ArrayList<DetalleAdicional>?): String {
        var totalDetalle = ""
        detallesAdicionales?.forEach {
            totalDetalle += ("            <detAdicional nombre=\"${it.nombre}\" valor=\"${it.valor}\"/>\n")
        }
        return totalDetalle
    }

    private fun generarImpuestos(totalImpuestos: ArrayList<Impuesto>): String {
        val nombreEtiquetaImpuestos = "impuestos"
        val totalConImpuestos = ("        <$nombreEtiquetaImpuestos>\n"
                + generarImpuesto(totalImpuestos)
                + "         </$nombreEtiquetaImpuestos>\n")
        return totalConImpuestos
    }

    private fun generarImpuesto(totalImpuestos: ArrayList<Impuesto>): String {
        val nombreEtiquetaImpuesto = "impuesto"
        var totalImpuesto = ""
        totalImpuestos.forEach {
            totalImpuesto += ("            <$nombreEtiquetaImpuesto>\n"
                    + "                <codigo>${it.codigo}</codigo>\n"
                    + "                <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + "             </$nombreEtiquetaImpuesto>\n")
        }
        return totalImpuesto
    }


}