package documentos

import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.util.AutorizacionComprobantesWs
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesOfflineService
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger
import javax.xml.namespace.QName
import javax.xml.ws.WebServiceException

class AutorizarDocumentos {
    companion object {

        var host: String = "https://celcer.sri.gob.ec"
        var segmentoURLComprobantesElectronicos: String = "/comprobantes-electronicos-ws/RecepcionComprobantesOffline"
        var queryParamsComprobantesElectronicos: String = "?wsdl"
        var namespaceURIRecepcion: String = "http://ec.gob.sri.ws.recepcion"
        var localPartRecepcion: String = "RecepcionComprobantesOfflineService"

        fun validar(datos: ByteArray): RespuestaSolicitud? {
            try {
                //produccion
                //cel.sri.gob.ec
                val url = URL("$host$segmentoURLComprobantesElectronicos$queryParamsComprobantesElectronicos")
                val qname = QName(namespaceURIRecepcion, localPartRecepcion)
                val service = RecepcionComprobantesOfflineService(url, qname)
                val portRec = service.recepcionComprobantesOfflinePort
                return portRec.validarComprobante(datos)

            } catch (ex: Exception) {
                val response = RespuestaSolicitud()
                response.estado = ex.message
                return response
            }

        }

        @Throws(RespuestaAutorizacionException::class)
        fun autorizarComprobante(claveDeAcceso: String): RespuestaComprobante? {
            //produccion
            //cel.sri.gob.ec
            println("Autorizar comprobante")
            println("Clave de acceso $claveDeAcceso")
            println("$host$segmentoURLComprobantesElectronicos$queryParamsComprobantesElectronicos")
            try {
                return AutorizacionComprobantesWs("$host$segmentoURLComprobantesElectronicos$queryParamsComprobantesElectronicos")
                    .llamadaWSAutorizacionInd(
                        claveDeAcceso
                    )
            } catch (ex: WebServiceException) {
                Logger.getLogger(AutorizacionComprobantesWs::class.java.name).log(Level.SEVERE, null as String?, ex)
                println("ERROR EN WEB SERVICE")
            }
            return null

        }

    }


}