package documentos.comprobanteretencion

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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.validation.Validation
import javax.validation.constraints.NotNull
import kotlin.Any as Any1

class ComprobanteRetencion(

    var infoTributario: InformacionTributaria,
    var infoCompRetencion: InformacionComprobanteRetencion,
    var impuestos: ArrayList<ImpuestoRetencion>,
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
    var nombreEtiquetaComprobante = "comprobanteRetencion"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionComprobanteRetencionXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringComprobanteRetencionXML = ""

    init {

        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoCompRetencion.fechaEmision)


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

    fun getInfoAdicional(): Optional<ArrayList<CampoAdicional>>{
        return Optional.of<ArrayList<CampoAdicional>>(infoAdicional!!)
    }

    fun getVersionXML(): Optional<String> {
        return Optional.of(versionXML)
    }



}