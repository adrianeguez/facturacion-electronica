package documentos.factura


import documentos.*
import documentos.GenerarDocumentos.Companion.generarClave
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LiquidacionBienesServicios(
    var infoTributario: InformacionTributaria,
    var infoLiquidacionCompra: InfoLiquidacionCompra,
    var detalles: ArrayList<Detalle>,
    var reembolsos: ArrayList<ReembolsoDetalle>?,
    var maquinaFiscal: MaquinaFiscal?,
    var infoAdicional: ArrayList<CampoAdicional>?,
    var directorioGuardarXML: String,
    var directorioGuardarXMLFirmados: String,
    var nombreArchivoXML: String,
    var nombreArchivoXMLFirmado: String,
    var clave: String,
    var directorioYNombreArchivoRegistroCivilP12: String,
    var debug: Boolean = true,
    versionXML: String?
){


    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI
    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var standaloneXML = "yes"
    var nombreEtiqueta = "liquidacionCompra"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionFacturaXML = "1.1.0" // Codigo Quemado en guía del SRI
    var stringFacturaXML = ""

    init {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoLiquidacionCompra.fechaEmision)
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



    fun getReembolsos(): Optional<ArrayList<ReembolsoDetalle>> {
        return Optional.of(reembolsos!!)
    }

    fun getMaquinaFiscal(): Optional<MaquinaFiscal> {
        return Optional.of(maquinaFiscal!!)
    }

    fun getInfoAdicional(): Optional<ArrayList<CampoAdicional>> {
        return Optional.of(infoAdicional!!)
    }

    fun getVersionXML(): Optional<String> {
        return Optional.of(versionXML)
    }
}
