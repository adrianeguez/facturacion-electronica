package documentos.guiaremision

import documentos.*
import utils.mensajeNulo
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Validation
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

class GuiaRemision {
    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI

    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiquetaGuiaRemision = "guiaRemision"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionGuiaRemisionXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringGuiaRemisionXML = ""

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoGuiaRemision: InformacionGuiaRemision

    @NotNull(message = "destinatario $mensajeNulo")
    var destinatarios: ArrayList<Destinatario>

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoAdicional: ArrayList<CampoAdicional>?

    var directorioGuardarXML: String
    var directorioGuardarXMLFirmados: String
    var nombreArchivoXML: String
    var nombreArchivoXMLFirmado: String
    var clave: String
    var directorioYNombreArchivoRegistroCivilP12: String

    val debug: Boolean

    constructor(
        infoTributario: InformacionTributaria,
        infoGuiaRemision: InformacionGuiaRemision,
        destinatarios: ArrayList<Destinatario>,

        infoAdicional: ArrayList<CampoAdicional>?,
        directorioGuardarXML: String,
        directorioGuardarXMLFirmados: String,
        nombreArchivoXML: String,
        nombreArchivoXMLFirmado: String,
        clave: String,
        directorioYNombreArchivoRegistroCivilP12: String,
        debug: Boolean = true
    ) {
        this.infoTributario = infoTributario
        this.infoGuiaRemision = infoGuiaRemision
        this.destinatarios = destinatarios
        this.infoAdicional = infoAdicional

        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoGuiaRemision.fechaIniTransporte)

        this.infoTributario.claveAcceso = GenerarDocumentos.generarClave(
            fecha,
            this.infoTributario.codDoc,
            this.infoTributario.ruc,
            this.infoTributario.ambiente,
            (this.infoTributario.estab + this.infoTributario.ptoEmision),
            this.infoTributario.secuencial,
            this.codigoNumerico,
            this.infoTributario.tipoEmision
        )

        this.directorioGuardarXML = directorioGuardarXML
        this.directorioGuardarXMLFirmados = directorioGuardarXMLFirmados
        this.nombreArchivoXML = nombreArchivoXML
        this.nombreArchivoXMLFirmado = nombreArchivoXMLFirmado
        this.clave = clave
        this.directorioYNombreArchivoRegistroCivilP12 = directorioYNombreArchivoRegistroCivilP12
        this.debug = debug
    }

    fun validar(): ArrayList<String> {
        val errores = arrayListOf<String>()

        val violationsInfoTributaria = validator.validate(this.infoTributario)

        for (violation in violationsInfoTributaria) {
            errores.add(violation.message)
        }

        val violationsInformacionGuiaRemision = validator.validate(this.infoGuiaRemision)

        for (violation in violationsInformacionGuiaRemision) {
            errores.add(violation.message)
        }


        this.destinatarios.forEach {
            val violationsDestinatario = validator.validate(it)

            for (violation in violationsDestinatario) {
                errores.add(violation.message)
            }
            it.detalles.forEach { detalle ->
                val violationsDetalle = validator.validate(detalle)
                for (violation in violationsDetalle) {
                    errores.add(violation.message)
                }
                detalle.detallesAdicionales.forEach { detalleAdicional ->
                    val violationsDetalleAdicional = validator.validate(detalleAdicional)
                    for (violation in violationsDetalleAdicional) {
                        errores.add(violation.message)
                    }
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

    fun generarGuiaRemisionXML(): String {
        val xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" standalone=\"$standaloneXML\"?>\n" +
                    "<$nombreEtiquetaGuiaRemision id=\"${idComprobante}\" version=\"$versionGuiaRemisionXML\">\n" +
                    generarCuerpoFactura() +
                    "</$nombreEtiquetaGuiaRemision>"
        this.stringGuiaRemisionXML = xmlString
        return xmlString
    }

    private fun generarCuerpoFactura(): String {
        val contenidoFactura = generarInformacionTributaria() +
                generarInformacionGuiaRemision() +
                generarDestinatarios() +
                generarInformacionAdicional()

        return contenidoFactura
    }

    private fun generarInformacionTributaria(): String {
        val nombreEtiquetaInformacionTributaria = "infoTributaria"
        var nombreComercial = ""
        if (this.infoTributario.nombreComercial != null) {
            nombreComercial = "        <nombreComercial>${this.infoTributario.nombreComercial}</nombreComercial>\n"
        }
        val informacionTributaria = ("<$nombreEtiquetaInformacionTributaria>\n"
                + "        <ambiente>${this.infoTributario.ambiente}</ambiente>\n"
                + "        <tipoEmision>${this.infoTributario.tipoEmision}</tipoEmision>\n"
                + "        <razonSocial>${this.infoTributario.razonSocial}</razonSocial>\n"
                + nombreComercial
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

    private fun generarInformacionGuiaRemision(): String {
        val nombreEtiquetaInformacionGuiaRemision = "infoGuiaRemision"  

        var dirEstablecimiento = ""
        if (this.infoGuiaRemision.dirEstablecimiento != null) {
            dirEstablecimiento =
                    "        <dirEstablecimiento>${this.infoGuiaRemision.dirEstablecimiento}</dirEstablecimiento>\n"
        }

        var rise = ""
        if (this.infoGuiaRemision.rise != null) {
            rise =
                    "        <rise>${this.infoGuiaRemision.rise}</rise>\n"
        }

        var obligadoContabilidad = ""
        if (this.infoGuiaRemision.obligadoContabilidad != null) {
            obligadoContabilidad =
                    "        <obligadoContabilidad>${this.infoGuiaRemision.obligadoContabilidad}</obligadoContabilidad>\n"
        }

        var contribuyenteEspecial = ""
        if (this.infoGuiaRemision.contribuyenteEspecial != null) {
            contribuyenteEspecial =
                    "        <contribuyenteEspecial>${this.infoGuiaRemision.contribuyenteEspecial}</contribuyenteEspecial>\n"
        }


        val informacionFactura = ("<$nombreEtiquetaInformacionGuiaRemision>\n"
                + dirEstablecimiento
                + "        <dirPartida>${this.infoGuiaRemision.dirPartida}</dirPartida>\n"
                + "        <razonSocialTransportista>${this.infoGuiaRemision.razonSocialTransportista}</razonSocialTransportista>\n"
                + "        <tipoIdentificacionTransportista>${this.infoGuiaRemision.tipoIdentificacionTransportista}</tipoIdentificacionTransportista>\n"
                + "        <rucTransportista>${this.infoGuiaRemision.rucTransportista}</rucTransportista>\n"
                + rise
                + obligadoContabilidad
                + contribuyenteEspecial
                + "        <fechaIniTransporte>${this.infoGuiaRemision.fechaIniTransporte}</fechaIniTransporte>\n"
                + "        <fechaFinTransporte>${this.infoGuiaRemision.fechaFinTransporte}</fechaFinTransporte>\n"
                + "        <placa>${this.infoGuiaRemision.placa}</placa>\n"
                + "</$nombreEtiquetaInformacionGuiaRemision>\n")
        return informacionFactura
    }

    private fun generarDestinatarios(): String {
        val nombreEtiquetaDestinatarios = "destinatarios"
        val totalDestinatarios = ("        <$nombreEtiquetaDestinatarios>\n"
                + generarDestinatario(this.destinatarios)
                + "         </$nombreEtiquetaDestinatarios>\n")
        return totalDestinatarios
    }

    private fun generarDestinatario(destinatarios: ArrayList<Destinatario>): String {
        val nombreEtiquetaDestinatario = "destinatario"
        var totalDestinatarios = ""
        destinatarios.forEach {
            var docAduaneroUnico = ""
            if (it.docAduaneroUnico != null) {
                docAduaneroUnico = "                <docAduaneroUnico>${it.docAduaneroUnico}</docAduaneroUnico>\n"
            }

            var codEstabDestino = ""
            if (it.codEstabDestino != null) {
                codEstabDestino = "                <codEstabDestino>${it.codEstabDestino}</codEstabDestino>\n"
            }

            var ruta = ""
            if (it.ruta != null) {
                ruta = "                <ruta>${it.ruta}</ruta>\n"
            }

            var codDocSustento = ""
            if (it.codDocSustento != null) {
                codDocSustento = "                <codDocSustento>${it.codDocSustento}</codDocSustento>\n"
            }

            var numDocSustento = ""
            if (it.numDocSustento != null) {
                codDocSustento = "                <numDocSustento>${it.numDocSustento}</numDocSustento>\n"
            }

            var numAutDocSustento = ""
            if (it.numAutDocSustento != null) {
                numAutDocSustento = "                <numAutDocSustento>${it.numAutDocSustento}</numAutDocSustento>\n"
            }

            var fechaEmisionDocSustento = ""
            if (it.fechaEmisionDocSustento != null) {
                fechaEmisionDocSustento =
                        "                <fechaEmisionDocSustento>${it.fechaEmisionDocSustento}</fechaEmisionDocSustento>\n"
            }




            totalDestinatarios += ("            <$nombreEtiquetaDestinatario>\n"
                    + "                <identificacionDestinatario>${it.identificacionDestinatario}</identificacionDestinatario>\n"
                    + "                <razonSocialDestinatario>${it.razonSocialDestinatario}</razonSocialDestinatario>\n"
                    + "                <dirDestinatario>${it.dirDestinatario}</dirDestinatario>\n"
                    + "                <motivoTraslado>${it.motivoTraslado}</motivoTraslado>\n"
                    + docAduaneroUnico
                    + codEstabDestino
                    + ruta
                    + codDocSustento
                    + numAutDocSustento
                    + fechaEmisionDocSustento
                    + generarDetalles(it.detalles)
                    + "             </$nombreEtiquetaDestinatario>\n")
        }
        return totalDestinatarios
    }


    private fun generarDetalles(detalles: ArrayList<DetalleGuiaRemision>): String {
        val nombreEtiquetaDetalles = "detalles"
        val totalDetalles = ("        <$nombreEtiquetaDetalles>\n"
                + generarDetalle(detalles)
                + "         </$nombreEtiquetaDetalles>\n")
        return totalDetalles
    }

    private fun generarDetalle(detalles: ArrayList<DetalleGuiaRemision>): String {
        val nombreEtiquetaDetalle = "detalle"
        var totalDetalles = ""
        detalles.forEach {

            var codigoInterno = ""
            if (it.codigoInterno != null) {
                codigoInterno = "                <codigoInterno>${it.codigoInterno}</codigoInterno>\n"
            }

            var codigoAdicional = ""
            if (it.codigoAdicional != null) {
                codigoAdicional = "                <codigoAdicional>${it.codigoAdicional}</codigoAdicional>\n"
            }


            totalDetalles += ("            <$nombreEtiquetaDetalle>\n"
                    + codigoInterno
                    + codigoAdicional
                    + "                <descripcion>${it.descripcion}</descripcion>\n"
                    + "                <cantidad>${it.cantidad}</cantidad>\n"
                    + generarDetallesAdicionales(it.detallesAdicionales)
                    + "             </$nombreEtiquetaDetalle>\n")
        }
        return totalDetalles
    }

    private fun generarDetallesAdicionales(detallesAdicionales: ArrayList<DetalleAdicional>): String {
        val nombreEtiquetaDetallesAdicionales = "detallesAdicionales"
        val totalDetallesAdicionales = ("        <$nombreEtiquetaDetallesAdicionales>\n"
                + generarDetalleAdicional(detallesAdicionales)
                + "         </$nombreEtiquetaDetallesAdicionales>\n")
        return totalDetallesAdicionales
    }

    private fun generarDetalleAdicional(detallesAdicionales: ArrayList<DetalleAdicional>): String {
        val nombreEtiquetaDetalle = "detAdicional"
        var totalDetallesAdicionales = ""
        detallesAdicionales.forEach {
            totalDetallesAdicionales += ("                <$nombreEtiquetaDetalle nombre=\"${it.nombre}\" valor=\"${it.valor}\">\n")
        }
        return totalDetallesAdicionales
    }

    private fun generarInformacionAdicional(): String {
        val nombreEtiquetaInformacionAdicional = "infoAdicional"
        if (this.infoAdicional != null && this.infoAdicional?.size ?: 0 > 0) {
            val informacionAdicional = ("<$nombreEtiquetaInformacionAdicional>\n"
                    + generarCampoAdicional(this.infoAdicional ?: arrayListOf())
                    + "</$nombreEtiquetaInformacionAdicional>\n")
            return informacionAdicional
        } else {
            return ""
        }
    }

    private fun generarCampoAdicional(informacionAdicional: ArrayList<CampoAdicional>): String {
        var totalCamposAdicionales = ""
        informacionAdicional.forEach {
            totalCamposAdicionales += ("         <campoAdicional nombre=\"${it.nombre}\">${it.valor}</campoAdicional>\n")
        }
        return totalCamposAdicionales
    }


}