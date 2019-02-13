package documentos

import ec.gob.sri.comprobantes.util.AutorizacionComprobantesWs
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesOfflineService
import java.net.URL
import javax.xml.namespace.QName

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
        fun autorizarComprobante(claveDeAcceso: String): RespuestaComprobante {
            //produccion
            //cel.sri.gob.ec
            return AutorizacionComprobantesWs("$host$segmentoURLComprobantesElectronicos$queryParamsComprobantesElectronicos")
                .llamadaWSAutorizacionInd(
                    claveDeAcceso
                )
        }

    }


}