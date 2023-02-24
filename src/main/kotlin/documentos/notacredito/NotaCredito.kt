package documentos.notacredito

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import documentos.*
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.util.ArchivoUtils
import firma.XAdESBESSignature
import utils.UtilsFacturacionElectronica
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList


class NotaCredito(
    var infoTributario: InformacionTributaria,
    var infoNotaCredito: InformacionNotaCredito,
    var detalles: ArrayList<DetalleNotaCredito>,
    var infoAdicional: ArrayList<CampoAdicional>?,
    var directorioGuardarXML: String,
    var directorioGuardarXMLFirmados: String,
    var nombreArchivoXML: String,
    var nombreArchivoXMLFirmado: String,
    var clave: String,
    var directorioYNombreArchivoRegistroCivilP12: String,
    var debug: Boolean = true,
    versionXML: String?
) {



    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI
    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiquetaNotaCredito = "notaCredito"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionNotaCreditoXML = "1.1.0" // Codigo Quemado en guía del SRI
    var stringNotaCreditoXML = ""

    init {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoNotaCredito.fechaEmision)

        if (this.infoTributario.claveAcceso == null) {
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
        } else {
            this.infoTributario.claveAcceso = infoTributario.claveAcceso
        }

        if (versionXML != null) {
            this.versionXML = versionXML
        }

    }

    fun getInfoAdicional(): Optional<ArrayList<CampoAdicional>> {
        return Optional.of<ArrayList<CampoAdicional>>(infoAdicional!!)
    }

    fun getVersionXML(): Optional<String> {
        return Optional.of(versionXML)
    }


    fun validar(): ArrayList<String> {
        val errores = arrayListOf<String>()

        /*val violationsInfoTributaria = validator.validate(this.infoTributario)

        for (violation in violationsInfoTributaria) {
            errores.add(violation.message)
        }

        val violationsInformacionNotaCredito = validator.validate(this.infoNotaCredito)

        for (violation in violationsInformacionNotaCredito) {
            errores.add(violation.message)
        }


        this.infoNotaCredito.totalConImpuesto.forEach {
            val violationsTotalConImpuesto = validator.validate(it)

            for (violation in violationsTotalConImpuesto) {
                errores.add(violation.message)
            }
        }

        this.detalles.forEach {
            val violationsDetalle = validator.validate(it)

            for (violation in violationsDetalle) {
                errores.add(violation.message)
            }

            it.detallesAdicionales?.forEach { detalleAdicional ->
                val violationsDetalleAdicional = validator.validate(detalleAdicional)
                for (violation in violationsDetalleAdicional) {
                    errores.add(violation.message)
                }
            }

            it.impuestos.forEach { impuesto ->
                val violationsImpuesto = validator.validate(impuesto)
                for (violation in violationsImpuesto) {
                    errores.add(violation.message)
                }
            }
        }

        this.infoAdicional?.forEach {
            val violationsInfoAdicional = validator.validate(it)

            for (violation in violationsInfoAdicional) {
                errores.add(violation.message)
            }
        }*/
        return errores

    }

    fun generarNotaCreditoXML(): String {
        val xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" standalone=\"$standaloneXML\"?>\n" +
                    "<$nombreEtiquetaNotaCredito id=\"${idComprobante}\" version=\"$versionNotaCreditoXML\">\n" +
                    generarCuerpoNotaCredito() +
                    "</$nombreEtiquetaNotaCredito>"
        this.stringNotaCreditoXML = GenerarDocumentos.removerCaracteresEspeciales(xmlString)
        return xmlString
    }

    fun generarArchivoNotaCreditoXML(
        directorioAGuardarArchivo: String,
        nombreArchivoXMLAGuardar: String,
        stringGuiaRemisionXML: String? = null
    ): String? {
        val xml = stringGuiaRemisionXML ?: this.stringNotaCreditoXML
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

    fun enviarNotaCredito(json: String): String {

        val nombreDocumento = "Nota Credito"

        val resultado = Klaxon()
            .parse<NotaCredito?>(
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
                resultado?.generarNotaCreditoXML()
                resultado?.generarArchivoNotaCreditoXML(
                    resultado.directorioGuardarXML + "/",
                    resultado.nombreArchivoXML,
                    resultado.stringNotaCreditoXML
                )

                val archivoGenerado = resultado?.generarArchivoNotaCreditoXML(
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

                            if (resultado.infoTributario.ambiente == "2") {
                                AutorizarDocumentos.host = "https://cel.sri.gob.ec"
                            }

                            if (resultado.infoTributario.ambiente == "1") {
                                AutorizarDocumentos.host = "https://celcer.sri.gob.ec"
                            }

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

                                            val fechaActual = LocalDateTime.now()
                                            val formatterFecha = DateTimeFormatter.ISO_LOCAL_DATE
                                            val fechaString = fechaActual.format(formatterFecha)
                                            val formatterHoraMinutoSegundo = DateTimeFormatter.ISO_TIME
                                            val horaMinutoSegundoString = fechaActual.format(formatterHoraMinutoSegundo)

                                            val xmlCompleto = """
                                            <?xml version=\"1.0\" encoding=\"UTF-8\"?>
                                            <autorizacion>
                                              <estado>${it.estado}</estado>
                                              <numeroAutorizacion>${it.numeroAutorizacion}</numeroAutorizacion>
                                              <fechaAutorizacion class=\"fechaAutorizacion\">${fechaString} ${horaMinutoSegundoString}</fechaAutorizacion>
                                              <comprobante>${comprobanteString}</comprobante>
                                              <mensajes/>
                                            </autorizacion>
                                            """.trimIndent()
                                            var autorizacion = """
                                                {
                                                    "numeroAutorizacion" : "${it.numeroAutorizacion}",
                                                    "comprobante" : "${comprobanteString}",
                                                    "estado" : "${it.estado}",
                                                    "fechaAutorizacion" : "${it.fechaAutorizacion}",
                                                    "xmlCompleto": "${eliminarEspacios(xmlCompleto)}",
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
                                                }${
                                                if (index != ((respuestaComprobante.autorizaciones?.autorizacion?.size
                                                        ?: 1) - 1)
                                                ) "," else ""
                                            }
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
                                                mensaje.informacionAdicional =
                                                    if (mensaje.informacionAdicional == null) "ninguno" else mensaje.informacionAdicional
                                                println(mensaje.tipo)
                                                println(mensaje.identificador)
                                                println(mensaje.informacionAdicional)
                                                println(mensaje.mensaje)
                                            }
                                            val fechaActual = LocalDateTime.now()
                                            val formatterFecha = DateTimeFormatter.ISO_LOCAL_DATE
                                            val fechaString = fechaActual.format(formatterFecha)
                                            val formatterHoraMinutoSegundo = DateTimeFormatter.ISO_TIME
                                            val horaMinutoSegundoString = fechaActual.format(formatterHoraMinutoSegundo)
                                            var xmlCompleto = ""
                                            if (mensaje.identificador == "45" || mensaje.identificador == "43") {
                                                xmlCompleto = """
                                           ,"xmlCompleto": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
                                            <autorizacion>
                                              <estado>AUTORIZADO</estado>
                                              <numeroAutorizacion>${resultado.infoTributario.claveAcceso}</numeroAutorizacion>
                                              <fechaAutorizacion class=\"fechaAutorizacion\">${fechaString} ${horaMinutoSegundoString}</fechaAutorizacion>
                                              <comprobante>${
                                                    eliminarCaracteresEspeciales(
                                                        File(
                                                            directorioYNombreArchivoXMLFirmado
                                                        ).readText()
                                                    )
                                                }</comprobante>
                                              <mensajes/>
                                            </autorizacion>"
                                            """.trimIndent()
                                            }
                                            mensajes += """
                                                {
                                                    "tipo":"${mensaje.tipo}",
                                                    "identificador":"${mensaje.identificador}",
                                                    "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
                                                    "mensaje":"${eliminarCaracteresEspeciales(mensaje.mensaje)}"${
                                                eliminarEspacios(
                                                    xmlCompleto
                                                )
                                            }
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
                                                "comprobante": "${this.eliminarCaracteresEspeciales(resultado.stringNotaCreditoXML)}",
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
                                        "mensaje":"Error en respuesta de solicitud. Puede ser error del servidor del SRI intentelo de nuevo. Revise los logs.",
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

    private fun generarCuerpoNotaCredito(): String {
        val contenidoFactura = generarInformacionTributaria() +
                generarInformacionNotaCredito() +
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

    private fun generarInformacionNotaCredito(): String {
        val nombreEtiquetaInformacionNotaCredito = "infoNotaCredito"

        var dirEstablecimiento = ""
        if (this.infoNotaCredito.dirEstablecimiento != null) {
            dirEstablecimiento =
                "        <dirEstablecimiento>${this.infoNotaCredito.dirEstablecimiento}</dirEstablecimiento>\n"
        }

        var contribuyenteEspecial = ""
        if (this.infoNotaCredito.contribuyenteEspecial != null) {
            contribuyenteEspecial =
                "        <contribuyenteEspecial>${this.infoNotaCredito.contribuyenteEspecial}</contribuyenteEspecial>\n"
        }

        var obligadoContabilidad = ""
        if (this.infoNotaCredito.obligadoContabilidad != null) {
            obligadoContabilidad =
                "        <obligadoContabilidad>${this.infoNotaCredito.obligadoContabilidad}</obligadoContabilidad>\n"
        }

        var rise = ""
        if (this.infoNotaCredito.rise != null) {
            rise =
                "        <rise>${this.infoNotaCredito.rise}</rise>\n"
        }


        val informacionFactura = ("<$nombreEtiquetaInformacionNotaCredito>\n"
                + "        <fechaEmision>${this.infoNotaCredito.fechaEmision}</fechaEmision>\n"
                + dirEstablecimiento
                + "        <tipoIdentificacionComprador>${this.infoNotaCredito.tipoIdentificacionComprador}</tipoIdentificacionComprador>\n"
                + "        <razonSocialComprador>${this.infoNotaCredito.razonSocialComprador}</razonSocialComprador>\n"
                + "        <identificacionComprador>${this.infoNotaCredito.identificacionComprador}</identificacionComprador>\n"
                + contribuyenteEspecial
                + obligadoContabilidad
                + rise
                + "        <codDocModificado>${this.infoNotaCredito.codDocModificado}</codDocModificado>\n"
                + "        <numDocModificado>${this.infoNotaCredito.numDocModificado}</numDocModificado>\n"
                + "        <fechaEmisionDocSustento>${this.infoNotaCredito.fechaEmisionDocSustento}</fechaEmisionDocSustento>\n"
                + "        <totalSinImpuestos>${this.infoNotaCredito.totalSinImpuestos}</totalSinImpuestos>\n"
                + "        <valorModificacion>${this.infoNotaCredito.valorModificacion}</valorModificacion>\n"
                + "        <moneda>${this.infoNotaCredito.moneda}</moneda>\n"
                + generarTotalConImpuestos()
                + "        <motivo>${this.infoNotaCredito.motivo}</motivo>\n"
                + "</$nombreEtiquetaInformacionNotaCredito>\n")
        return informacionFactura
    }

    private fun generarTotalConImpuestos(): String {
        val nombreEtiquetaTotalConImpuestos = "totalConImpuestos"
        val totalConImpuestos = ("        <$nombreEtiquetaTotalConImpuestos>\n"
                + generarTotalImpuesto(this.infoNotaCredito.totalConImpuesto)
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

    private fun generarDetalles(): String {
        val nombreEtiquetaDetalles = "detalles"
        val totalConImpuestos = ("<$nombreEtiquetaDetalles>\n"
                + generarDetalle(this.detalles)
                + "</$nombreEtiquetaDetalles>\n")
        return totalConImpuestos

    }

    private fun generarDetalle(detalles: ArrayList<DetalleNotaCredito>): String {
        val nombreEtiquetaDetalle = "detalle"
        var totalDetalles = ""
        detalles.forEach {

            var codigoInterno = ""
            if (it.codigoPrincipal != null) {
                codigoInterno = "                <codigoInterno>${it.codigoPrincipal}</codigoInterno>\n"
            }

            var codigoAdicional = ""
            if (it.codigoAuxiliar != null) {
                codigoAdicional = "                <codigoAdicional>${it.codigoAuxiliar}</codigoAdicional>\n"
            }
            var detallesAdicionales = ""
            if (it.detallesAdicionales != null) {
                detallesAdicionales = generarDetallesAdicionales(it.detallesAdicionales)
            }

            totalDetalles += ("            <$nombreEtiquetaDetalle>\n"
                    + codigoInterno
                    + codigoAdicional
                    + "                <descripcion>${it.descripcion}</descripcion>\n"
                    + "                <cantidad>${it.cantidad}</cantidad>\n"
                    + "                <precioUnitario>${it.precioUnitario}</precioUnitario>\n"
                    + "                <descuento>${it.descuento}</descuento>\n"
                    + "                <precioTotalSinImpuesto>${it.precioTotalSinImpuesto}</precioTotalSinImpuesto>\n"
                    + detallesAdicionales
                    + generarImpuestos(it.impuestos)
                    + "             </$nombreEtiquetaDetalle>\n")
        }
        return totalDetalles
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

    private fun eliminarCaracteresEspeciales(texto: String): String {
        return texto
            .replace("\"", "\\\"").replace("\n", "")
            .replace("\r", "")
    }

    private fun eliminarEspacios(texto: String): String {
        return texto
            .replace("\n", "")
            .replace("\r", "")
    }


}