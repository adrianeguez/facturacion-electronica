package utils

import ec.gob.sri.comprobantes.util.ArchivoUtils
import java.io.*
import java.util.logging.Level
import java.util.logging.Logger
import java.io.IOException
import java.io.FileOutputStream
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.File
import ec.gob.sri.comprobantes.util.xml.LectorXPath
import javax.xml.xpath.XPathConstants
import ec.gob.sri.comprobantes.util.xml.XStreamUtil
import ec.gob.sri.comprobantes.ws.aut.Autorizacion
import ec.gob.sri.comprobantes.util.xml.Java2XML
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.exception.ZipException
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.io.OutputStreamWriter
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.SAXException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathFactory

class UtilsFacturacionElectronica {

    companion object {
        fun archivoToString(rutaArchivo: String): String? {
            val stringBuffer = StringBuffer()
            val codificacion = "UTF-8"
            try {
                val fileInputStream = FileInputStream(rutaArchivo)
                val inputStreamReader = InputStreamReader(fileInputStream, codificacion)
                val bufferedReader = BufferedReader(inputStreamReader)
                var caracter: Int = bufferedReader.read()
                while (caracter > -1) {
                    stringBuffer.append(caracter.toChar())
                    caracter = bufferedReader.read()
                }
                bufferedReader.close()
                return stringBuffer.toString()
            } catch (e: IOException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, e)
            }
            return null
        }

        fun stringToArchivo(rutaArchivo: String, contenidoArchivo: String): File? {
            var fileOutputStream: FileOutputStream? = null
            val archivoCreado: File?
            val codificacion = "UTF-8"
            try {
                fileOutputStream = FileOutputStream(rutaArchivo)

                val outputStreamWriter = OutputStreamWriter(fileOutputStream, codificacion)

                for (i in 0 until contenidoArchivo.length) {
                    outputStreamWriter.write(contenidoArchivo[i].toInt())
                }
                outputStreamWriter.close()
                archivoCreado = File(rutaArchivo)

            } catch (ex: Exception) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
                return null
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (ex: Exception) {
                    Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
            return archivoCreado
        }

        @Throws(IOException::class)
        fun archivoToByte(file: File): ByteArray {
            val bufferByteArray = ByteArray(file.length().toInt())
            var inputStream: InputStream? = null
            try {
                inputStream = FileInputStream(file)
                if (inputStream != null) {
                    if (inputStream.read(bufferByteArray) === -1) {
                        throw IOException("Fin de archivo alcanzado mientras se intentaba leer todo el archivo.")
                    }
                }
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, e)
                }
            }
            return bufferByteArray
        }

        fun byteToFile(arrayBytes: ByteArray, rutaArchivo: String): Boolean {
            var respuesta = false
            try {

                val file = File(rutaArchivo)
                file.createNewFile()

                val fileInputStream = FileInputStream(rutaArchivo)
                val byteArrayInputStream = ByteArrayInputStream(arrayBytes)
                val fileOutputStream = FileOutputStream(rutaArchivo)
                var caracter: Int = byteArrayInputStream.read()
                while (caracter != -1) {
                    fileOutputStream.write(caracter)
                    caracter = byteArrayInputStream.read()
                }
                fileInputStream.close()
                fileOutputStream.close()
                respuesta = true
            } catch (ex: IOException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            }

            return respuesta
        }

        fun obtenerValorXML(xmlDocument: File, expression: String): String? {
            var valor: String? = null
            try {
                val reader = LectorXPath(xmlDocument.path)
                valor = reader.leerArchivo(expression, XPathConstants.STRING) as String
            } catch (e: Exception) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, e)
            }

            return valor
        }

        fun obtieneClaveAccesoAutorizacion(
            item: Autorizacion,
            expresionRegularDeLaClaveDeAcceso: String = "/*/infoTributaria/claveAcceso"
        ): String? {
            var claveAcceso: String? = null
            val xmlAutorizacion = XStreamUtil.getRespuestaLoteXStream().toXML(item)
            val archivoTemporal = File("temp.xml")
            stringToArchivo(archivoTemporal.path, xmlAutorizacion)
            val contenidoXML = decodeArchivoBase64(archivoTemporal.path)
            if (contenidoXML != null) {
                stringToArchivo(archivoTemporal.path, contenidoXML)
                claveAcceso = obtenerValorXML(archivoTemporal, expresionRegularDeLaClaveDeAcceso)
            }
            return claveAcceso
        }

        fun decodeArchivoBase64(
            pathArchivo: String,
            expresionRegularDelComprobante: String = "/*/comprobante"
        ): String? {
            var xmlDecodificado: String? = null
            try {
                val archivo = File(pathArchivo)
                if (archivo.exists()) {
                    val valorDeXml = obtenerValorXML(archivo, expresionRegularDelComprobante)
                    xmlDecodificado = valorDeXml
                } else {
                    print("Archivo no encontrado durante la decodificacion")
                }
            } catch (e: Exception) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, e)
            }
            return xmlDecodificado
        }

        fun anadirMotivosRechazo(archivo: File, respuestaRecepcion: RespuestaSolicitud): Boolean {
            var exito = false
            val respuesta = File("respuesta.xml")
            Java2XML.marshalRespuestaSolicitud(respuestaRecepcion, respuesta.getPath());
            if (adjuntarArchivo(respuesta, archivo) === true) {
                exito = true
                respuesta.delete()
            }
            return exito
        }

        fun adjuntarArchivo(respuesta: File, comprobante: File, expresionParaMerge: String = "*"): Boolean {
            val exito = false
            val codificacion = "UTF-8"
            try {
                val document = merge(expresionParaMerge, arrayOf(comprobante, respuesta))
                val source = DOMSource(document)
                val result = StreamResult(OutputStreamWriter(FileOutputStream(comprobante), codificacion))
                val transFactory = TransformerFactory.newInstance()
                val transformer = transFactory.newTransformer()
                transformer.transform(source, result)
            } catch (ex: Exception) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            }
            return exito
        }

        @Throws(Exception::class)
        private fun merge(expresion: String, archivos: Array<File>): Document {
            val xPathFactory = XPathFactory.newInstance()
            val xpath = xPathFactory.newXPath()
            val expression = xpath.compile(expresion)
            val docBuilderFactory = DocumentBuilderFactory.newInstance()
            docBuilderFactory.setIgnoringElementContentWhitespace(true)
            val docBuilder = docBuilderFactory.newDocumentBuilder()
            val base = docBuilder.parse(archivos[0])
            val nodoDeResultados = expression.evaluate(base, XPathConstants.NODE) as Node?
                ?: throw IOException(archivos[0].toString() + ": La expresion no pudo evaluar el nodo")

            for (i in 1 until archivos.size) {
                val merge = docBuilder.parse(archivos[i])
                val nextResults = expression.evaluate(merge, XPathConstants.NODE) as Node?
                nodoDeResultados.appendChild(base.importNode(nextResults, true))
            }

            return base
        }

        fun copiarArchivo(archivoOrigen: File, pathDestino: String): Boolean {
            var fileReader: FileReader? = null
            var resultado = false

            try {
                val outputFile = File(pathDestino)
                fileReader = FileReader(archivoOrigen)
                val fileWriter = FileWriter(outputFile)
                var caracter: Int = fileReader.read()
                while (caracter != -1) {
                    fileWriter.write(caracter)
                    caracter = fileReader.read()
                }
                fileReader.close()
                fileWriter.close()
                resultado = true
            } catch (ex: Exception) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            } finally {
                try {
                    fileReader?.close()
                } catch (ex: IOException) {
                    Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
            return resultado
        }

        fun convertirBytes(pathArchivo: String): ByteArray? {
            var byteArrayOutputStream: ByteArrayOutputStream? = null
            try {
                //tipoAmbiente();
                var fileInputStream: FileInputStream?
                //fileInputStream = new FileInputStream(Utilidades.DirXMLPrincipal + Utilidades.DirFirmados + prefijo + establecimiento + "-" + puntoemision + "-" + secuencial + ".xml");
                fileInputStream = FileInputStream(pathArchivo)
                val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()
                val document: Document = documentBuilder.parse(fileInputStream)
                val domSourse: Source = DOMSource(document)
                byteArrayOutputStream = ByteArrayOutputStream()
                val result: Result = StreamResult(byteArrayOutputStream)
                val factoryT: TransformerFactory = TransformerFactory.newInstance()
                val transformer: Transformer = factoryT.newTransformer();
                transformer.transform(domSourse, result)

            } catch (ex: FileNotFoundException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

            } catch (ex: ParserConfigurationException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

            } catch (ex: TransformerConfigurationException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

            } catch (ex: TransformerException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: SAXException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: IOException) {
                Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)
            }
            return byteArrayOutputStream?.toByteArray()
        }

        fun zipFile(archivo: File, pathArchivo: String, extension: String = ".xml"): String {
            val extensionZip = ".zip"
            val metodoDeCompresion = Zip4jConstants.COMP_DEFLATE
            val nivelDeCompresion = Zip4jConstants.DEFLATE_LEVEL_NORMAL
            try {
                val zipFile = ZipFile(pathArchivo.replace(extension, extensionZip))
                val parametros = ZipParameters()
                parametros.setCompressionMethod(metodoDeCompresion)
                parametros.setCompressionLevel(nivelDeCompresion)
                zipFile.addFile(archivo, parametros)
                return pathArchivo.replace(extension, extensionZip)
            } catch (ex: ZipException) {
                ex.getStackTrace()
            }
            return ""
        }

    }

}
