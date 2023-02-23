package documentos.comprobanteretencion
import documentos.Pago
import java.util.*


class DocSustento(
    var codSustento: String,
    var codDocSustento: String,
    var numDocSustento: String?,
    var fechaEmisionDocSustento: String,
    var fechaRegistroContable: String?,
    var numAutDocSustento: String?,
    var pagoLocExt: String,
    var tipoRegi: String?,
    var paisEfecPago: String?,
    var aplicConvDobTrib: String?,
    var pagExtSujRetNorLeg: String?,
    var pagoRegFis: String?,
    var totalComprobantesReembolse: String?,
    var totalBaseImponibleReembolso: String?,
    var totalImpuestoReembolso: String?,
    var totalSinImpuestos: String,
    var importeTotal: String,
    var impuestosDocSustento: ArrayList<ImpuestoDocSustento>,
    var retenciones: ArrayList<Retencion>?,
    var reembolsos: ArrayList<ReembolsoDetalle>?,
    var pagos: ArrayList<Pago>
) {


    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }

    fun getFechaRegistroContable(): Optional<String> {
        return Optional.of(fechaRegistroContable!!)
    }

    fun getNumAutDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }

    fun getTipoRegi(): Optional<String> {
        return Optional.of(tipoRegi!!)
    }

    fun getPaisEfecPago(): Optional<String> {
        return Optional.of(paisEfecPago!!)
    }

    fun getAplicConvDobTrib(): Optional<String> {
        return Optional.of(aplicConvDobTrib!!)
    }

    fun getPagExtSujRetNorLeg(): Optional<String> {
        return Optional.of(pagExtSujRetNorLeg!!)
    }

    fun getPagoRegFis(): Optional<String> {
        return Optional.of(pagoRegFis!!)
    }

    fun getTotalComprobantesReembolse(): Optional<String> {
        return Optional.of(totalComprobantesReembolse!!)
    }

    fun getTotalBaseImponibleReembolso(): Optional<String> {
        return Optional.of(totalBaseImponibleReembolso!!)
    }

    fun getTotalImpuestoReembolso(): Optional<String> {
        return Optional.of(totalImpuestoReembolso!!)
    }

    fun getRetenciones(): Optional<ArrayList<Retencion>> {
        return Optional.of(retenciones!!)
    }

    fun getReembolsos(): Optional<ArrayList<ReembolsoDetalle>> {
        return Optional.of(reembolsos!!)
    }

}