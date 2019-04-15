package documentos.guiaremision

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import documentos.*
import documentos.factura.Factura
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.util.ArchivoUtils
import firma.XAdESBESSignature
import utils.UtilsFacturacionElectronica
import utils.mensajeNulo
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
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
        debug: Boolean = true,
        versionXML: String?
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
        if (versionXML != null) {
            this.versionXML = versionXML
        }
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
                detalle.detallesAdicionales?.forEach { detalleAdicional ->
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
                    generarCuerpoGuiaRemision() +
                    "</$nombreEtiquetaGuiaRemision>"
        this.stringGuiaRemisionXML = GenerarDocumentos.removerCaracteresEspeciales(xmlString)
        return xmlString
    }

    fun generarArchivoGuiaRemisionXML(
        directorioAGuardarArchivo: String,
        nombreArchivoXMLAGuardar: String,
        stringGuiaRemisionXML: String? = null
    ): String? {
        val xml = stringGuiaRemisionXML ?: this.stringGuiaRemisionXML
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


    fun enviarGuiaRemision(json: String): String {

        val nombreDocumento = "Guia Remision"

        val resultado = Klaxon()
            .parse<GuiaRemision?>(
                json
            )
        try {
            val errores = resultado?.validar()
            if (errores?.size ?: 0 > 0) {
                if (debug) {
                    println("Error")
                }
                var erroresObjeto = ""
                errores?.forEachIndexed { indice, mensajeError ->
                    if (debug) {
                        println(mensajeError)
                    }
                    erroresObjeto += erroresObjeto + """
                        {
                            "mensaje": "${this.eliminarCaracteresEspeciales(mensajeError)}"
                        }${if (indice != (errores.size - 1)) "," else ""}
                    """.trimIndent()
                }
                val erroresAEnviar = "[" + erroresObjeto + "]"

                return """
                        {
                            "mensaje":"Errores en parseo de $nombreDocumento.",
                            "error": 400,
                            "data": {
                                "errores":${erroresAEnviar}
                            }
                        }
                        """.trimIndent()
            } else {
                resultado?.generarGuiaRemisionXML()
                resultado?.generarArchivoGuiaRemisionXML(
                    resultado.directorioGuardarXML + "/",
                    resultado.nombreArchivoXML,
                    resultado.stringGuiaRemisionXML
                )

                val archivoGenerado = resultado?.generarArchivoGuiaRemisionXML(
                    resultado.directorioGuardarXML,
                    resultado.nombreArchivoXML
                )
                println("archivo generado: $archivoGenerado")

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
                                        var autorizaciones = ""
                                        respuestaComprobante?.autorizaciones?.autorizacion?.forEachIndexed { index, it ->

                                            if (debug) {
                                                println("numeroAutorizacion ${it.numeroAutorizacion}")
                                                println("comprobante ${it.comprobante}")
                                                println("estado ${it.estado}")
                                                println("fechaAutorizacion ${it.fechaAutorizacion}")
                                                println("Mensajes:")
                                            }
                                            val comprobanteString = eliminarCaracteresEspeciales(it.comprobante)
                                            var autorizacion = """
                                                {
                                                    "numeroAutorizacion" : "${it.numeroAutorizacion}",
                                                    "comprobante" : "${comprobanteString}",
                                                    "estado" : "${it.estado}",
                                                    "fechaAutorizacion" : "${it.fechaAutorizacion}",
                                            """.trimIndent()


                                            var mensajeString = ""
                                            it.mensajes.mensaje.forEachIndexed { indiceMensaje, mensaje ->
                                                if (debug) {
                                                    println("identificador ${mensaje.identificador}")
                                                    println("informacionAdicional ${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}")
                                                    println("mensaje ${mensaje.mensaje}")
                                                    println("tipo ${mensaje.tipo}")
                                                }
                                                mensajeString += """
                                                    {
                                                        "identificador":"${mensaje.identificador}",
                                                        "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
                                                        "mensaje":"${mensaje.mensaje}",
                                                        "tipo":"${mensaje.tipo}"
                                                    }${if (indiceMensaje != (it.mensajes.mensaje.size - 1)) "," else ""}
                                                """.trimIndent()
                                            }
                                            val mensajeArreglo = "[" + mensajeString + "]"
                                            autorizacion += """
                                                "mensajes": ${mensajeArreglo}
                                                }${if (index != ((respuestaComprobante.autorizaciones?.autorizacion?.size
                                                    ?: 1) - 1)
                                            ) "," else ""}
                                            """.trimIndent()

                                            autorizaciones += autorizacion
                                        }
                                        val autorizacionCompleta = "[" + autorizaciones + "]"
                                        return """
                                            {
                                                "mensaje":"Se recibieron autorizaciones",
                                                "error":null,
                                                "data":{
                                                    "estadoSolicitud":"RECIBIDA",
                                                    "claveAccesoConsultada":"${respuestaComprobante?.claveAccesoConsultada}",
                                                    "numeroComprobantes":"${respuestaComprobante?.numeroComprobantes}",
                                                    "autorizaciones":${autorizacionCompleta}
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
                                    var mensajes = ""
                                    respuestaSolicitud.comprobantes.comprobante.forEach {
                                        it.mensajes.mensaje.forEachIndexed { index, mensaje ->
                                            if (debug) {
                                                println(mensaje.tipo)
                                                println(mensaje.identificador)
                                                println(mensaje.informacionAdicional)
                                                println(mensaje.mensaje)
                                            }
                                            mensajes += """
                                                {
                                                    "tipo":"${mensaje.tipo}",
                                                    "identificador":"${mensaje.identificador}",
                                                    "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
                                                    "mensaje":"${eliminarCaracteresEspeciales(mensaje.mensaje)}"
                                                }${if (index != (it.mensajes.mensaje.size - 1)) "," else ""}
                                            """.trimIndent()

                                        }
                                    }
                                    var mensajesRespuestaSolicitudNoRecibida = "[" + mensajes + "]"
                                    return """
                                        {
                                            "mensaje": "Estado diferente a recibido",
                                            "error": 400,
                                            "data": {
                                                "comprobante": "${this.eliminarCaracteresEspeciales(resultado.stringGuiaRemisionXML)}",
                                                "estadoSolicitud": "${respuestaSolicitud.estado}",
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
                "mensaje":"Errores en parseo de $nombreDocumento.",
                "error": 400,
                "data": {
                    "message":"${this.eliminarCaracteresEspeciales(e.message.toString())}",
                    "cause":"${this.eliminarCaracteresEspeciales(e.cause.toString())}"
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
                "error": "400",
                "data": {
                    "message":"${this.eliminarCaracteresEspeciales(e.message.toString())}",
                    "cause":"${this.eliminarCaracteresEspeciales(e.cause.toString())}"
                }
            }
            """.trimIndent()
        }

    }


    private fun generarCuerpoGuiaRemision(): String {
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
                    + generarDetallesAdicionales(it.detallesAdicionales ?: arrayListOf())
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

    private fun eliminarCaracteresEspeciales(texto: String): String {
        return texto.replace("\"", "\\\"").replace("\n", "")
            .replace("\r", "")
    }


}