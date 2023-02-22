package documentos

import java.util.*

class InfoLiquidacionCompra(
    var fechaEmision: String,
    var dirEstablecimiento: String?,
    var contribuyenteEspecial: String?,
    var obligadoContabilidad: String?,
    var tipoIdentificacionProveedor: String?,
    var razonSocialProveedor: String,
    var identificacionProveedor: String,
    var direccionProveedor: String?,
    var totalSinImpuestos: String?,
    var totalDescuento: String?,
    var codDocReembolso: String?,
    var totalComprobantesReembolso: String?,
    var totalBaseImponibleReembolso: String?,
    var totalImpuestoReembolso: String?,
    var totalConImpuestos: ArrayList<TotalImpuesto>,
    var importeTotal: String,
    var moneda: String,
    var pagos: ArrayList<Pago>
) {

    init {

        this.dirEstablecimiento =
            if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento!!)
        this.contribuyenteEspecial =
            if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(
                contribuyenteEspecial!!
            )

        this.razonSocialProveedor = GenerarDocumentos.removerCaracteresEspeciales(razonSocialProveedor)
        this.identificacionProveedor = GenerarDocumentos.removerCaracteresEspeciales(identificacionProveedor)
        this.direccionProveedor =
            if (direccionProveedor != null) GenerarDocumentos.removerCaracteresEspeciales(direccionProveedor!!) else direccionProveedor

    }


    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }

    fun getContribuyenteEspecial(): Optional<String> {
        return Optional.of(contribuyenteEspecial!!)
    }

    fun getObligadoContabilidad(): Optional<String> {
        return Optional.of(obligadoContabilidad!!)
    }

    fun getTipoIdentificacionProveedor(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedor!!)
    }

    fun getDireccionProveedor(): Optional<String> {
        return Optional.of(direccionProveedor!!)
    }

    fun getTotalSinImpuestos(): Optional<String> {
        return Optional.of(totalSinImpuestos!!)
    }

    fun getTotalDescuento(): Optional<String> {
        return Optional.of(totalDescuento!!)
    }

    fun getCodDocReembolso(): Optional<String> {
        return Optional.of(codDocReembolso!!)
    }

    fun getTotalComprobantesReembolso(): Optional<String> {
        return Optional.of(totalComprobantesReembolso!!)
    }

    fun getTotalBaseImponibleReembolso(): Optional<String> {
        return Optional.of(totalBaseImponibleReembolso!!)
    }

    fun getTotalImpuestoReembolso(): Optional<String> {
        return Optional.of(totalImpuestoReembolso!!)
    }

}