package documentos.factura


import documentos.*
import documentos.GenerarDocumentos.Companion.generarClave
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class Factura(
    var infoTributario: InformacionTributaria,
    var infoFactura: InformacionFactura,
    var detalles: ArrayList<Detalle>,
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
    var nombreEtiquetaFactura = "factura"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionFacturaXML = "1.1.0" // Codigo Quemado en guía del SRI
    var stringFacturaXML = ""

    init {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoFactura.fechaEmision)
        if (this.infoTributario.claveAcceso == null) {
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
        } else {
            this.infoTributario.claveAcceso = infoTributario.claveAcceso
        }

        if (versionXML != null) {
            this.versionXML = versionXML
        }

    }

    fun getVersionXML(): Optional<String> {
        return Optional.of(versionXML)
    }

    fun getInfoAdicional(): Optional<ArrayList<CampoAdicional>>{
        return Optional.of<ArrayList<CampoAdicional>>(infoAdicional!!)
    }

}
