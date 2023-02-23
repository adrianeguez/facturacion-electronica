package documentos

import java.util.*

class ReembolsoDetalle(
    var tipoIdentificacionProveedorReembolso: String?,
    var identificacionProveedor: String?,
    var tipoProveedorReembolso: String?,
    var codPaisPagoProveedorReembolso: String?,
    var codDocReembolso: String?,
    var estabDocReembolso: String?,
    var ptoEmiDocReembolso: String?,
    var secuencialDocReembolso: String?,
    var fechaEmisionDocReembolso: String?,
    var numeroautorizacionDocReemb: String?,
    var detalleImpuestos: ArrayList<ImpuestoReembolso>?
) {

    fun getTipoIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedorReembolso!!)
    }

    fun getIdentificacionProveedor(): Optional<String> {
        return Optional.of(identificacionProveedor!!)
    }

    fun getTipoProveedorReembolso(): Optional<String> {
        return Optional.of(tipoProveedorReembolso!!)
    }

    fun getCodPaisPagoProveedorReembolso(): Optional<String> {
        return Optional.of(codPaisPagoProveedorReembolso!!)
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

    fun getNumeroautorizacionDocReemb(): Optional<String> {
        return Optional.of(numeroautorizacionDocReemb!!)
    }

    fun getDetalleImpuestos(): Optional<ArrayList<ImpuestoReembolso>> {
        return Optional.of(detalleImpuestos!!)
    }

}