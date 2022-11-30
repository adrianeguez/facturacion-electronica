package documentos.comprobanteretencion
import documentos.Pago
import utils.mensajeNulo
import java.util.*
import javax.validation.constraints.NotNull


class DocSustento {

    @NotNull(message = "codSustento $mensajeNulo")
    var codSustento: String

    @NotNull(message = "codDocSustento $mensajeNulo")
    var codDocSustento: String

    var numDocSustento: String?
    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }

    @NotNull(message = "fechaEmisionDocSustento $mensajeNulo")
    var fechaEmisionDocSustento: String

    var fechaRegistroContable: String?
    fun getFechaRegistroContable(): Optional<String> {
        return Optional.of(fechaRegistroContable!!)
    }

    var numAutDocSustento: String?
    fun getNumAutDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }

    @NotNull(message = "pagoLocExt $mensajeNulo")
    var pagoLocExt: String

    var tipoRegi: String?
    fun getTipoRegi(): Optional<String> {
        return Optional.of(tipoRegi!!)
    }

    var paisEfecPago: String?
    fun getPaisEfecPago(): Optional<String> {
        return Optional.of(paisEfecPago!!)
    }

    var aplicConvDobTrib: String?
    fun getAplicConvDobTrib(): Optional<String> {
        return Optional.of(aplicConvDobTrib!!)
    }

    var pagExtSujRetNorLeg: String?
    fun getPagExtSujRetNorLeg(): Optional<String> {
        return Optional.of(pagExtSujRetNorLeg!!)
    }

    var pagoRegFis: String?
    fun getPagoRegFis(): Optional<String> {
        return Optional.of(pagoRegFis!!)
    }

    var totalComprobantesReembolse: String?
    fun getTotalComprobantesReembolse(): Optional<String> {
        return Optional.of(totalComprobantesReembolse!!)
    }

    var totalBaseImponibleReembolso: String?
    fun getTotalBaseImponibleReembolso(): Optional<String> {
        return Optional.of(totalBaseImponibleReembolso!!)
    }

    var totalImpuestoReembolso: String?
    fun getTotalImpuestoReembolso(): Optional<String> {
        return Optional.of(totalImpuestoReembolso!!)
    }

    @NotNull(message = "totalSinImpuestos $mensajeNulo")
    var totalSinImpuestos: String

    @NotNull(message = "importeTotal $mensajeNulo")
    var importeTotal: String

    @NotNull(message = "impuestosDocSustento $mensajeNulo")
    var impuestosDocSustento: ArrayList<ImpuestoDocSustento>

    var retenciones: ArrayList<Retencion>?
    fun getRetenciones(): Optional<ArrayList<Retencion>> {
        return Optional.of(retenciones!!)
    }

    var reembolsos: ArrayList<ReembolsoDetalle>?
    fun getReembolsos(): Optional<ArrayList<ReembolsoDetalle>> {
        return Optional.of(reembolsos!!)
    }

    @NotNull(message = "pagos $mensajeNulo")
    var pagos: ArrayList<Pago>



    constructor(
        codSustento: String,
        codDocSustento: String,
        numDocSustento: String?,
        fechaEmisionDocSustento: String,
        fechaRegistroContable: String?,
        numAutDocSustento: String?,
        pagoLocExt: String,
        tipoRegi: String?,
        paisEfecPago: String?,
        aplicConvDobTrib: String?,
        pagExtSujRetNorLeg: String?,
        pagoRegFis: String?,
        totalComprobantesReembolse: String?,
        totalBaseImponibleReembolso: String?,
        totalImpuestoReembolso: String?,
        totalSinImpuestos: String,
        importeTotal: String,
        impuestosDocSustento: ArrayList<ImpuestoDocSustento>,
        retenciones: ArrayList<Retencion>?,
        reembolsos: ArrayList<ReembolsoDetalle>?,
        pagos: ArrayList<Pago>
    ) {
        this.codSustento = codSustento
        this.codDocSustento = codDocSustento
        this.numDocSustento = numDocSustento
        this.fechaEmisionDocSustento = fechaEmisionDocSustento
        this.fechaRegistroContable = fechaRegistroContable
        this.numAutDocSustento = numAutDocSustento
        this.pagoLocExt = pagoLocExt
        this.tipoRegi = tipoRegi
        this.paisEfecPago = paisEfecPago
        this.aplicConvDobTrib = aplicConvDobTrib
        this.pagExtSujRetNorLeg = pagExtSujRetNorLeg
        this.pagoRegFis = pagoRegFis
        this.totalComprobantesReembolse = totalComprobantesReembolse
        this.totalBaseImponibleReembolso = totalBaseImponibleReembolso
        this.totalImpuestoReembolso = totalImpuestoReembolso
        this.totalSinImpuestos = totalSinImpuestos
        this.importeTotal = importeTotal
        this.impuestosDocSustento = impuestosDocSustento
        this.retenciones = retenciones
        this.reembolsos = reembolsos
        this.pagos = pagos
    }
}