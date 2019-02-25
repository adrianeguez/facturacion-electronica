package documentos

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import documentos.GenerarDocumentos.Companion.generarClave
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.util.ArchivoUtils
import firma.XAdESBESSignature
import utils.UtilsFacturacionElectronica
import utils.mensajeNulo
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Validation
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList
import java.io.FileOutputStream
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger


class Factura {


    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI
    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiquetaFactura = "factura"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionFacturaXML = "1.1.0" // Codigo Quemado en guía del SRI
    var stringFacturaXML = ""

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoFactura $mensajeNulo")
    var infoFactura: InformacionFactura

    @NotNull(message = "detalles $mensajeNulo")
    var detalles: ArrayList<Detalle>

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
        infoFactura: InformacionFactura,
        detalles: ArrayList<Detalle>,
        infoAdicional: ArrayList<CampoAdicional>?,
        directorioGuardarXML: String,
        directorioGuardarXMLFirmados: String,
        nombreArchivoXML: String,
        nombreArchivoXMLFirmado: String,
        clave: String,
        directorioYNombreArchivoRegistroCivilP12: String,
        debug: Boolean = true
    ) {
        this.debug = debug
        this.directorioGuardarXML = directorioGuardarXML
        this.directorioGuardarXMLFirmados = directorioGuardarXMLFirmados
        this.nombreArchivoXML = nombreArchivoXML
        this.nombreArchivoXMLFirmado = nombreArchivoXMLFirmado
        this.clave = clave
        this.directorioYNombreArchivoRegistroCivilP12 = directorioYNombreArchivoRegistroCivilP12
        this.infoTributario = infoTributario
        this.infoFactura = infoFactura
        this.detalles = detalles
        this.infoAdicional = infoAdicional
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoFactura.fechaEmision)

        this.infoTributario.claveAcceso = generarClave(
            fecha,
            this.infoTributario.codDoc,
            this.infoTributario.ruc,
            this.infoTributario.ambiente,
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
        val xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" standalone=\"$standaloneXML\"?>\n" +
                    "<$nombreEtiquetaFactura id=\"${idComprobante}\" version=\"$versionFacturaXML\">\n" +
                    generarCuerpoFactura() +
                    "</$nombreEtiquetaFactura>"
        this.stringFacturaXML = xmlString
        return xmlString
    }

    fun generarArchivoFacturaXML(
        directorioAGuardarArchivo: String,
        nombreArchivoXMLAGuardar: String,
        stringFacturaXML: String? = null
    ): String? {
        val xml = stringFacturaXML ?: this.stringFacturaXML
        var archivoPathNombre = directorioAGuardarArchivo + nombreArchivoXMLAGuardar

        try {

            val stringBuilder = StringBuilder()
            println(xml)
            stringBuilder.append(xml)

            val carpetaSalida = File(directorioAGuardarArchivo)

            if (!carpetaSalida.exists()) {
                carpetaSalida.mkdirs()
            }

            val archivoSalida = File(archivoPathNombre)

            archivoSalida.writeText(xml)

            println("Archivo generado")

            return archivoPathNombre

        } catch (ex: FileNotFoundException) {

            Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

        } catch (ex: IOException) {

            Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

        }
        return null
    }

    private fun generarCuerpoFactura(): String {

        val contenidoFactura = generarInformacionTributaria() +
                generarInformacionFactura() +
                generarDetalles() +
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

    private fun generarInformacionFactura(): String {
        val nombreEtiquetaInformacionFactura = "infoFactura"
        var obligadoContabilidad = ""
        if (this.infoFactura.obligadoContabilidad != null) {
            obligadoContabilidad =
                "        <obligadoContabilidad>${this.infoFactura.obligadoContabilidad}</obligadoContabilidad>\n"
        }
        var contribuyenteEspecial = ""
        println("this.infoFactura.contribuyenteEspecial")
        println(this.infoFactura.contribuyenteEspecial)
        if (this.infoFactura.contribuyenteEspecial != null) {
            contribuyenteEspecial =
                "        <contribuyenteEspecial>${this.infoFactura.contribuyenteEspecial}</contribuyenteEspecial>\n"
        }
        var direccionEstablecimiento = ""
        if (this.infoFactura.dirEstablecimiento != null) {
            direccionEstablecimiento =
                "        <dirEstablecimiento>${this.infoFactura.dirEstablecimiento ?: ""}</dirEstablecimiento>\n"
        }

        val informacionFactura = ("<$nombreEtiquetaInformacionFactura>\n"
                + "        <fechaEmision>${this.infoFactura.fechaEmision}</fechaEmision>\n"
                + direccionEstablecimiento
                + contribuyenteEspecial
                + obligadoContabilidad
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

            var descuentoAdicional = ""
            if (it.descuentoAdicional != null) {
                descuentoAdicional =
                    "                <descuentoAdicional>${it.descuentoAdicional}</descuentoAdicional>\n"
            }

            var tarifa = ""
            if (it.tarifa != null) {
                tarifa =
                    "                <tarifa>${it.tarifa}</tarifa>\n"
            }

            var valorDevolucionIva = ""
            if (it.valorDevolucionIva != null) {
                valorDevolucionIva =
                    "                <valorDevolucionIva>${it.valorDevolucionIva}</valorDevolucionIva>\n"
            }

            totalImpuestos += ("            <$nombreEtiquetaTotalImpuestos>\n"
                    + "                <codigo>${it.codigo}</codigo>\n"
                    + "                <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
                    + descuentoAdicional
                    + tarifa
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + valorDevolucionIva
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
        var totalPago = ""
        totalPagos.forEach {
            var plazo = ""
            if (it.plazo != null) {
                plazo = "                <plazo>${it.plazo}</plazo>\n"
            }
            var unidadTiempo = ""
            if (it.unidadTiempo != null) {
                unidadTiempo = "                <unidadTiempo>${it.unidadTiempo}</unidadTiempo>\n"
            }
            totalPago += ("            <$nombreEtiquetaPago>\n"
                    + "                <formaPago>${it.formaPago}</formaPago>\n"
                    + "                <total>${it.total}</total>\n"
                    + plazo
                    + unidadTiempo
                    + "             </$nombreEtiquetaPago>\n")
        }
        return totalPago
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
            var codigoAuxiliar = ""
            if (it.codigoAuxiliar != null) {
                codigoAuxiliar = "         <codigoAuxiliar>${it.codigoAuxiliar ?: ""}</codigoAuxiliar>\n"
            }
            var codigoPrincipal = ""
            if (it.codigoPrincipal != null) {
                codigoPrincipal = "         <codigoPrincipal>${it.codigoPrincipal ?: ""}</codigoPrincipal>\n"
            }

            totalDetalle += ("         <$nombreEtiquetaDetalle>\n"
                    + codigoPrincipal
                    + codigoAuxiliar
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
        if (detallesAdicionales != null) {
            val totalDetallesAdicionales = ("         <$nombreEtiquetaDetallesAdicionales>\n"
                    + generarDetalleAdicional(detallesAdicionales)
                    + "         </$nombreEtiquetaDetallesAdicionales>\n")
            return totalDetallesAdicionales
        } else {
            return ""
        }
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
                    + "                <tarifa>${it.tarifa}</tarifa>\n"
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + "             </$nombreEtiquetaImpuesto>\n")
        }
        return totalImpuesto
    }

    private fun generarInformacionAdicional(): String {
        val nombreEtiquetaInformacionAdicional = "infoAdicional"
        if (this.infoAdicional != null && this.infoAdicional?.size ?: 0 > 0) {
            val informacionAdicional = ("<$nombreEtiquetaInformacionAdicional>\n"
                    + generarCampoAdicional(this.infoAdicional!!)
                    + "</$nombreEtiquetaInformacionAdicional>\n")
            return informacionAdicional
        } else {
            return ""
        }
    }

    fun generarCampoAdicional(informacionAdicional: ArrayList<CampoAdicional>): String {
        var totalCamposAdicionales = ""
        informacionAdicional.forEach {
            totalCamposAdicionales += ("         <campoAdicional nombre=\"${it.nombre}\">${it.valor}</campoAdicional>\n")
        }
        return totalCamposAdicionales
    }


    fun enviarFactura(json: String): String {
        val resultado = Klaxon()
            .parse<Factura?>(
                json
            )
        try {
            val errores = resultado?.validar()
            if (errores?.size ?: 0 > 0) {
                if (debug) {
                    println("Error")
                }
                var erroresAEnviar = "["
                errores?.forEach {
                    if (debug) {
                        println(it)
                    }
                    erroresAEnviar += erroresAEnviar + "\"mensaje\":\"${it}\""
                }
                erroresAEnviar += "]"

                return """
                        {
                            "mensaje":"Errores en parseo de factura.",
                            "error": 400,
                            "data":${erroresAEnviar}
                        }
                        """.trimIndent()
            } else {
                resultado?.generarFacturaXML()

                val archivoGenerado = resultado?.generarArchivoFacturaXML(
                    resultado.directorioGuardarXML,
                    resultado.nombreArchivoXML
                )

                if (archivoGenerado != null) {
                    val archivoFirmado = XAdESBESSignature
                        .firmar(
                            resultado.directorioGuardarXML + resultado.nombreArchivoXML,
                            resultado.nombreArchivoXMLFirmado,
                            resultado.clave,
                            resultado.directorioYNombreArchivoRegistroCivilP12,
                            resultado.directorioGuardarXMLFirmados
                        )
                    if (archivoFirmado) {
                        if (debug) {
                            println("Se firmo archivos")
                        }
                        val directorioYNombreArchivoXMLFirmado =
                            resultado.directorioGuardarXMLFirmados + File.separator + resultado.nombreArchivoXMLFirmado

                        val datos = UtilsFacturacionElectronica.convertirBytes(directorioYNombreArchivoXMLFirmado)
                        if (debug) {
                            println("Convirtiendo datos")
                        }

                        if (datos != null) {
                            val respuestaSolicitud = AutorizarDocumentos.validar(datos)
                            if (debug) {
                                println("Validando Datos")
                            }
                            if (respuestaSolicitud != null && (respuestaSolicitud.comprobantes != null ?: false)) {
                                if (debug) {
                                    println("Validando si solicitud es RECIBIDA")
                                    println("Estado: ${respuestaSolicitud.estado}")
                                }
                                if (respuestaSolicitud.estado == "RECIBIDA") {
                                    try {
                                        if (debug) {
                                            println("ESTADO RECIBIDA")
                                        }
                                        val respuestaComprobante =
                                            AutorizarDocumentos.autorizarComprobante(resultado.infoTributario.claveAcceso!!)
                                        if (debug) {
                                            println("recibimos respuesta")
                                            println("numeroComprobantes ${respuestaComprobante?.numeroComprobantes}")
                                        }
                                        var autorizaciones = "["
                                        respuestaComprobante?.autorizaciones?.autorizacion?.forEachIndexed { index, it ->

                                            if (debug) {
                                                println("numeroAutorizacion ${it.numeroAutorizacion}")
                                                println("comprobante ${it.comprobante}")
                                                println("estado ${it.estado}")
                                                println("fechaAutorizacion ${it.fechaAutorizacion}")
                                                println("Mensajes:")
                                            }
                                            val comprobanteString = it.comprobante.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
                                            var autorizacion = """
                                                {
                                                    "numeroAutorizacion" : "${it.numeroAutorizacion}",
                                                    "comprobante" : "${comprobanteString}",
                                                    "estado" : "${it.estado}",
                                                    "fechaAutorizacion" : "${it.fechaAutorizacion}",
                                            """.trimIndent()


                                            var mensajeString = "["
                                            it.mensajes.mensaje.forEachIndexed { indiceMensaje, mensaje ->
                                                if (debug) {
                                                    println("identificador ${mensaje.identificador}")
                                                    println("informacionAdicional ${mensaje.informacionAdicional}")
                                                    println("mensaje ${mensaje.mensaje}")
                                                    println("tipo ${mensaje.tipo}")
                                                }
                                                mensajeString += """
                                                    {
                                                        "identificador":"${mensaje.identificador}",
                                                        "informacionAdicional":"${mensaje.informacionAdicional}",
                                                        "mensaje":"${mensaje.mensaje}",
                                                        "tipo":"${mensaje.tipo}"
                                                    }${if (indiceMensaje != (it.mensajes.mensaje.size - 1)) "," else ""}
                                                """.trimIndent()
                                            }
                                            mensajeString += "]"
                                            autorizacion += """
                                                "mensajes":${mensajeString}
                                                }${if (index != ((respuestaComprobante.autorizaciones?.autorizacion?.size
                                                    ?: 1) - 1)
                                            ) "," else ""}
                                            """.trimIndent()

                                            autorizaciones += "${autorizacion}"
                                        }
                                        autorizaciones += "]"
                                        return """
                                            {
                                                "mensaje":"Se recibieron autorizaciones",
                                                "error":null,
                                                "data":{
                                                    "estadoSolicitud":"RECIBIDA",
                                                    "claveAccesoConsultada":"${respuestaComprobante?.claveAccesoConsultada}",
                                                    "numeroComprobantes":"${respuestaComprobante?.numeroComprobantes}",
                                                    "autorizaciones":${autorizaciones}
                                                }
                                            }
                                        """.trimIndent()

                                    } catch (ex: RespuestaAutorizacionException) {
                                        if (debug) {
                                            println("Respuesta solicitud NO recibida")
                                        }
                                        return """
                                                {
                                                    "mensaje":"Respuesta solicitud NO recibida.",
                                                    "error": 500
                                                }
                                                """
                                    }
                                } else {
                                    var mensajesRespuestaSolicitudNoRecibida = "["
                                    respuestaSolicitud.comprobantes.comprobante.forEach {
                                        it.mensajes.mensaje.forEachIndexed { index, mensaje ->
                                            if (debug) {
                                                println(mensaje.tipo)
                                                println(mensaje.identificador)
                                                println(mensaje.informacionAdicional)
                                                println(mensaje.mensaje)
                                            }
                                            mensajesRespuestaSolicitudNoRecibida += """
                                                {
                                                    "tipo":"${mensaje.tipo}",
                                                    "identificador":"${mensaje.identificador}",
                                                    "informacionAdicional":"${mensaje.informacionAdicional}",
                                                    "mensaje":"${mensaje.mensaje}"
                                                }${if (index != (it.mensajes.mensaje.size - 1)) "," else ""}
                                            """.trimIndent()

                                        }
                                    }
                                    mensajesRespuestaSolicitudNoRecibida += "]"
                                    return """
                                        {
                                            "mensaje": "Estado diferente a recibido",
                                            "error": 400,
                                            "data": {
                                                "estadoSolicitud", "${respuestaSolicitud.estado}"
                                                "mensajes": ${mensajesRespuestaSolicitudNoRecibida}
                                            }
                                        }
                                    """.trimIndent()
                                }
                            } else {
                                if (debug) {
                                    println("Error en respuesta solicitud")
                                }
                                return """
                                    {
                                        "mensaje":"Error convirtiendo archivo a bytes.",
                                        "error": 500
                                    }
                                    """.trimIndent()
                            }
                        } else {
                            if (debug) {
                                println("Error convirtiendo archivo a bytes")
                            }
                            return """
                                    {
                                        "mensaje":"Error convirtiendo archivo a bytes.",
                                        "error": 500
                                    }
                                    """.trimIndent()
                        }

                    } else {
                        if (debug) {
                            println("Error firmando archivo")
                        }
                        return """
                        {
                            "mensaje":"Error firmando archivo.",
                            "error": 500
                        }
                        """.trimIndent()
                    }
                } else {
                    if (debug) {
                        println("Error firmando archivo")
                    }
                    return """
                        {
                            "mensaje":"Error creando archivo XML.",
                            "error": 500
                        }
                        """.trimIndent()
                }
            }
        } catch (e: KlaxonException) {
            if (debug) {
                println(e)
            }
            return """
            {
                "mensaje":"Errores en parseo de factura.",
                "error": 400,
                "data": {
                    "message":"${e.message}",
                    "cause":"${e.cause}"
                }
            }
            """.trimIndent()
        } catch (e: Exception) {
            if (debug) {
                println(e)
            }
            return """
            {
                "mensaje":"Error del servidor.",
                "error": 400,
                "data": {
                    "message":"${e.message}",
                    "cause":"${e.cause}"
                }
            }
            """.trimIndent()
        }

    }

}
