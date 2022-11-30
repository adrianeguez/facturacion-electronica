package documentos.comprobanteretencion
import utils.mensajeNulo
import java.util.*
import javax.validation.constraints.NotNull


class ReembolsoDetalle {

    var tipoIdentificacionProveedorReembolso: String?
    fun getTipoIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedorReembolso!!)
    }

    var identificacionProveedorReembolso: String?
    fun getIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(identificacionProveedorReembolso!!)
    }

    var codPaisPagoProveedorReembolso: String?
    fun getCodPaisPagoProveedorReembolso(): Optional<String> {
        return Optional.of(codPaisPagoProveedorReembolso!!)
    }

    var tipoProveedorReembolso: String?
    fun getTipoProveedorReembolso(): Optional<String> {
        return Optional.of(tipoProveedorReembolso!!)
    }

    var codDocReembolso: String?
    fun getCodDocReembolso(): Optional<String> {
        return Optional.of(codDocReembolso!!)
    }

    var estabDocReembolso: String?
    fun getEstabDocReembolso(): Optional<String> {
        return Optional.of(estabDocReembolso!!)
    }

    var ptoEmiDocReembolso: String?
    fun getPtoEmiDocReembolso(): Optional<String> {
        return Optional.of(ptoEmiDocReembolso!!)
    }

    var secuencialDocReembolso: String?
    fun getSecuencialDocReembolso(): Optional<String> {
        return Optional.of(secuencialDocReembolso!!)
    }

    var fechaEmisionDocReembolso: String?
    fun getFechaEmisionDocReembolso(): Optional<String> {
        return Optional.of(fechaEmisionDocReembolso!!)
    }

    var numeroAutorizacionDocReemb: String?
    fun getNumeroAutorizacionDocReemb(): Optional<String> {
        return Optional.of(numeroAutorizacionDocReemb!!)
    }

    var detalleImpuestos: ArrayList<DetalleImpuestoRetencion>?
    fun getDetalleImpuestos(): Optional<ArrayList<DetalleImpuestoRetencion>> {
        return Optional.of(detalleImpuestos!!)
    }

    constructor(
        tipoIdentificacionProveedorReembolso: String?,
        identificacionProveedorReembolso: String?,
        codPaisPagoProveedorReembolso: String?,
        tipoProveedorReembolso: String?,
        codDocReembolso: String?,
        estabDocReembolso: String?,
        ptoEmiDocReembolso: String?,
        secuencialDocReembolso: String?,
        fechaEmisionDocReembolso: String?,
        numeroAutorizacionDocReemb: String?,
        detalleImpuestos: ArrayList<DetalleImpuestoRetencion>?
    ) {
        this.tipoIdentificacionProveedorReembolso = tipoIdentificacionProveedorReembolso
        this.identificacionProveedorReembolso = identificacionProveedorReembolso
        this.codPaisPagoProveedorReembolso = codPaisPagoProveedorReembolso
        this.tipoProveedorReembolso = tipoProveedorReembolso
        this.codDocReembolso = codDocReembolso
        this.estabDocReembolso = estabDocReembolso
        this.ptoEmiDocReembolso = ptoEmiDocReembolso
        this.secuencialDocReembolso = secuencialDocReembolso
        this.fechaEmisionDocReembolso = fechaEmisionDocReembolso
        this.numeroAutorizacionDocReemb = numeroAutorizacionDocReemb
        this.detalleImpuestos = detalleImpuestos
    }
}