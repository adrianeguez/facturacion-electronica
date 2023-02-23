package documentos

import java.util.*

class InformacionFactura(

    var fechaEmision: String,
    var dirEstablecimiento: String?,
    var contribuyenteEspecial: String?,
    var obligadoContabilidad: String?,
    var tipoIdentificacionComprador: String,
    var guiaRemision: String?,
    var razonSocialComprador: String,
    var identificacionComprador: String,
    var direccionComprador: String,
    var totalSinImpuestos: String,
    var totalDescuento: String,
    var totalConImpuestos: ArrayList<TotalImpuesto>,
    var propina: String,
    var importeTotal: String,
    var moneda: String,
    var pagos: ArrayList<Pago>,
    var valorRetIva: String?,
    var valorRetRenta: String?
) {

    init {

        this.dirEstablecimiento =
            if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento!!)
        this.contribuyenteEspecial =
            if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(contribuyenteEspecial!!)
        this.razonSocialComprador = GenerarDocumentos.removerCaracteresEspeciales(razonSocialComprador)
        this.identificacionComprador = GenerarDocumentos.removerCaracteresEspeciales(identificacionComprador)
        this.direccionComprador = GenerarDocumentos.removerCaracteresEspeciales(direccionComprador)
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

    fun getGuiaRemision(): Optional<String> {
        return Optional.of(guiaRemision!!)
    }

    fun getDireccionComprador(): Optional<String> {
        return Optional.of(direccionComprador!!)
    }

    fun getValorRetIva(): Optional<String> {
        return Optional.of(valorRetIva!!)
    }

    fun getValorRetRenta(): Optional<String> {
        return Optional.of(valorRetRenta!!)
    }
}