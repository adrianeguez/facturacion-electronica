package documentos.notadebito


import documentos.*
import java.text.SimpleDateFormat
import java.util.*

class NotaDebito(
    var infoTributario: InformacionTributaria,
    var infoNotaDebito: InformacionNotaDebito,
    var motivos: ArrayList<Motivo>,
    var infoAdicional: ArrayList<CampoAdicional>,
    var directorioGuardarXML: String,
    var directorioGuardarXMLFirmados: String,
    var nombreArchivoXML: String,
    var nombreArchivoXMLFirmado: String,
    var clave: String,
    var directorioYNombreArchivoRegistroCivilP12: String,
    var debug: Boolean = true
) {


    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI
    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiquetaNotaDebito = "notaDebito"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionNotaDebitoXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringNotaDebitoXML = ""

    init {

        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoNotaDebito.fechaEmision)


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

    }

}