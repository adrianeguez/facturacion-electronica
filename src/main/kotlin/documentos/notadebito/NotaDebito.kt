package documentos.notadebito

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import documentos.*
import documentos.notacredito.NotaCredito
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

class NotaDebito {

    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI

    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiquetaNotaDebito = "notaDebito"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionNotaDebitoXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringNotaDebitoXML = ""

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoNotaDebito $mensajeNulo")
    var infoNotaDebito: InformacionNotaDebito

    @NotNull(message = "motivos $mensajeNulo")
    var motivos: ArrayList<Motivo>

    @NotNull(message = "motivos $mensajeNulo")
    var infoAdicional: ArrayList<CampoAdicional>


    var directorioGuardarXML: String
    var directorioGuardarXMLFirmados: String
    var nombreArchivoXML: String
    var nombreArchivoXMLFirmado: String
    var clave: String
    var directorioYNombreArchivoRegistroCivilP12: String

    val debug: Boolean

    constructor(
        infoTributario: InformacionTributaria,
        infoNotaDebito: InformacionNotaDebito,
        motivos: ArrayList<Motivo>,
        infoAdicional: ArrayList<CampoAdicional>,
        directorioGuardarXML: String,
        directorioGuardarXMLFirmados: String,
        nombreArchivoXML: String,
        nombreArchivoXMLFirmado: String,
        clave: String,
        directorioYNombreArchivoRegistroCivilP12: String,
        debug: Boolean = true
    ) {
        this.infoTributario = infoTributario
        this.infoNotaDebito = infoNotaDebito
        this.motivos = motivos
        this.infoAdicional = infoAdicional

        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoNotaDebito.fechaEmision)

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

        val violationsInformacionNotaDebito = validator.validate(this.infoNotaDebito)

        for (violation in violationsInformacionNotaDebito) {
            errores.add(violation.message)
        }


        this.infoNotaDebito.impuestos.forEach {
            val violationsImpuestos = validator.validate(it)

            for (violation in violationsImpuestos) {
                errores.add(violation.message)
            }
        }

        this.infoNotaDebito.pagos.forEach {
            val violationsPagos = validator.validate(it)

            for (violation in violationsPagos) {
                errores.add(violation.message)
            }
        }

        val violationsMotivos = validator.validate(this.motivos)

        for (violation in violationsMotivos) {
            errores.add(violation.message)
        }

        this.motivos.forEach {
            val violationsMotivo = validator.validate(it)

            for (violation in violationsMotivo) {
                errores.add(violation.message)
            }

        }

        val violationsInfoAdicional = validator.validate(this.infoAdicional)

        for (violation in violationsInfoAdicional) {
            errores.add(violation.message)
        }

        this.infoAdicional.forEach {
            val violationsCampoAdicional = validator.validate(it)

            for (violation in violationsCampoAdicional) {
                errores.add(violation.message)
            }

        }

        return errores

    }


    fun generarNotaDebitoXML(): String {
        val xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" standalone=\"$standaloneXML\"?>\n" +
                    "<$nombreEtiquetaNotaDebito id=\"${idComprobante}\" version=\"$versionNotaDebitoXML\">\n" +
                    generarCuerpoNotaDebito() +
                    "</$nombreEtiquetaNotaDebito>"
        this.stringNotaDebitoXML = GenerarDocumentos.removerCaracteresEspeciales(xmlString)
        return xmlString
    }

    fun generarArchivoNotaDebitoXML(
        directorioAGuardarArchivo: String,
        nombreArchivoXMLAGuardar: String,
        stringGuiaRemisionXML: String? = null
    ): String? {
        val xml = stringGuiaRemisionXML ?: this.stringNotaDebitoXML
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


    fun enviarNotaDebito(json: String): String {

        val nombreDocumento = "Nota Debito"

        val resultado = Klaxon()
            .parse<NotaDebito?>(
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
                            "mensaje":"Errores en parseo de $nombreDocumento.",
                            "error": 400,
                            "data":${erroresAEnviar}
                        }
                        """.trimIndent()
            } else {
                resultado?.generarNotaDebitoXML()

                val archivoGenerado = resultado?.generarArchivoNotaDebitoXML(
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
                                        var autorizaciones = "["
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


                                            var mensajeString = "["
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
                                                    "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
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
                "mensaje":"Errores en parseo de $nombreDocumento.",
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




    private fun generarCuerpoNotaDebito(): String {
        val contenidoFactura = generarInformacionTributaria() +
                generarInformacionNotaDebito() +
                generarMotivos() +
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

    private fun generarInformacionNotaDebito(): String {
        val nombreEtiquetaInformacionNotaDebito = "infoNotaDebito"

        var dirEstablecimiento = ""
        if (this.infoNotaDebito.dirEstablecimiento != null) {
            dirEstablecimiento =
                    "        <dirEstablecimiento>${this.infoNotaDebito.dirEstablecimiento}</dirEstablecimiento>\n"
        }

        var contribuyenteEspecial = ""
        if (this.infoNotaDebito.contribuyenteEspecial != null) {
            contribuyenteEspecial =
                    "        <contribuyenteEspecial>${this.infoNotaDebito.contribuyenteEspecial}</contribuyenteEspecial>\n"
        }

        var obligadoContabilidad = ""
        if (this.infoNotaDebito.obligadoContabilidad != null) {
            obligadoContabilidad =
                    "        <obligadoContabilidad>${this.infoNotaDebito.obligadoContabilidad}</obligadoContabilidad>\n"
        }


        val informacionFactura = ("<$nombreEtiquetaInformacionNotaDebito>\n"
                + "        <fechaEmision>${this.infoNotaDebito.fechaEmision}</fechaEmision>\n"
                + dirEstablecimiento
                + "        <tipoIdentificacionComprador>${this.infoNotaDebito.tipoIdentificacionComprador}</tipoIdentificacionComprador>\n"
                + "        <razonSocialComprador>${this.infoNotaDebito.razonSocialComprador}</razonSocialComprador>\n"
                + "        <identificacionComprador>${this.infoNotaDebito.identificacionComprador}</identificacionComprador>\n"
                + contribuyenteEspecial
                + obligadoContabilidad
                + "        <codDocModificado>${this.infoNotaDebito.codDocModificado}</codDocModificado>\n"
                + "        <numDocModificado>${this.infoNotaDebito.numDocModificado}</numDocModificado>\n"
                + "        <fechaEmisionDocSustento>${this.infoNotaDebito.fechaEmisionDocSustento}</fechaEmisionDocSustento>\n"
                + "        <totalSinImpuestos>${this.infoNotaDebito.totalSinImpuestos}</totalSinImpuestos>\n"
                + generarImpuestos()
                + generarPagos()
                + "</$nombreEtiquetaInformacionNotaDebito>\n")
        return informacionFactura
    }

    private fun generarImpuestos(): String {
        val nombreEtiquetaImpuestos = "impuestos"
        val totalConImpuestos = ("        <$nombreEtiquetaImpuestos>\n"
                + generarImpuesto(this.infoNotaDebito.impuestos)
                + "         </$nombreEtiquetaImpuestos>\n")
        return totalConImpuestos
    }

    private fun generarImpuesto(totalImpuestosArreglo: ArrayList<Impuesto>): String {
        val nombreEtiquetaImpuesto = "impuesto"
        var totalImpuestos = ""
        totalImpuestosArreglo.forEach {

            var tarifa = ""
            if (it.tarifa != null) {
                tarifa =
                        "                <tarifa>${it.tarifa}</tarifa>\n"
            }

            totalImpuestos += ("            <$nombreEtiquetaImpuesto>\n"
                    + "                <codigo>${it.codigo}</codigo>\n"
                    + "                <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
                    + tarifa
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + "             </$nombreEtiquetaImpuesto>\n")
        }
        return totalImpuestos
    }

    private fun generarPagos(): String {
        val nombreEtiquetaPagos = "pagos"
        val totalPagos = ("        <$nombreEtiquetaPagos>\n"
                + generarPago(this.infoNotaDebito.pagos)
                + "         </$nombreEtiquetaPagos>\n")
        return totalPagos
    }

    private fun generarPago(totalPagosArreglo: ArrayList<Pago>): String {
        val nombreEtiquetaPago = "pago"
        var totalPagos = ""
        totalPagosArreglo.forEach {

            var plazo = ""
            if (it.plazo != null) {
                plazo =
                        "                <plazo>${it.plazo}</plazo>\n"
            }

            var unidadTiempo = ""
            if (it.unidadTiempo!= null) {
                unidadTiempo =
                        "                <unidadTiempo>${it.unidadTiempo}</unidadTiempo>\n"
            }

            totalPagos += ("            <$nombreEtiquetaPago>\n"
                    + "                <formaPago>${it.formaPago}</formaPago>\n"
                    + "                <total>${it.total}</total>\n"
                    + plazo
                    + unidadTiempo
                    + "             </$nombreEtiquetaPago>\n")
        }
        return totalPagos
    }

    private fun generarMotivos(): String {
        val nombreEtiquetaMotivos = "motivos"
        val totalMotivos = ("        <$nombreEtiquetaMotivos>\n"
                + generarMotivo(this.motivos)
                + "         </$nombreEtiquetaMotivos>\n")
        return totalMotivos
    }

    private fun generarMotivo(totalMotivosArreglo: ArrayList<Motivo>): String {
        val nombreEtiquetaMotivo = "motivo"
        var totalMotivos = ""
        totalMotivosArreglo.forEach {
            totalMotivos += ("            <$nombreEtiquetaMotivo>\n"
                    + "                <razon>${it.razon}</razon>\n"
                    + "                <valor>${it.valor}</valor>\n"
                    + "             </$nombreEtiquetaMotivo>\n")
        }
        return totalMotivos
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