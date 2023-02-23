package documentos.comprobanteretencion
import java.util.*


class ReembolsoDetalle(

    var tipoIdentificacionProveedorReembolso: String?,
    var identificacionProveedorReembolso: String?,
    var codPaisPagoProveedorReembolso: String?,
    var tipoProveedorReembolso: String?,
    var codDocReembolso: String?,
    var estabDocReembolso: String?,
    var ptoEmiDocReembolso: String?,
    var secuencialDocReembolso: String?,
    var fechaEmisionDocReembolso: String?,
    var numeroAutorizacionDocReemb: String?,
    var detalleImpuestos: ArrayList<DetalleImpuestoRetencion>?
) {

    fun getTipoIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedorReembolso!!)
    }

    fun getIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(identificacionProveedorReembolso!!)
    }

    fun getCodPaisPagoProveedorReembolso(): Optional<String> {
        return Optional.of(codPaisPagoProveedorReembolso!!)
    }

    fun getTipoProveedorReembolso(): Optional<String> {
        return Optional.of(tipoProveedorReembolso!!)
    }

    fun getCodDocReembolso(): Optional<String> {
        return Optional.of(codDocReembolso!!)
    }

    fun getEstabDocReembolso(): Optional<String> {
        return Optional.of(estabDocReembolso!!)
    }

    fun getPtoEmiDocReembolso(): Optional<String> {
        return Optional.of(ptoEmiDocReembolso!!)
    }

    fun getSecuencialDocReembolso(): Optional<String> {
        return Optional.of(secuencialDocReembolso!!)
    }

    fun getFechaEmisionDocReembolso(): Optional<String> {
        return Optional.of(fechaEmisionDocReembolso!!)
    }

    fun getNumeroAutorizacionDocReemb(): Optional<String> {
        return Optional.of(numeroAutorizacionDocReemb!!)
    }

    fun getDetalleImpuestos(): Optional<ArrayList<DetalleImpuestoRetencion>> {
        return Optional.of(detalleImpuestos!!)
    }
}