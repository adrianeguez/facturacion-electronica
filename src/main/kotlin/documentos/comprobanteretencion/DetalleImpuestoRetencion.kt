package documentos.comprobanteretencion

import java.util.*

open class DetalleImpuestoRetencion(
    var codigo: String?,
    var codigoPorcentaje: String?,
    var tarifa: String?,
    var baseImponibleReembolso: String?,
    var impuestoReembolso: String?
) {

    fun getCodigo(): Optional<String> {
        return Optional.of(codigo!!)
    }

    fun getCodigoPorcentaje(): Optional<String> {
        return Optional.of(codigoPorcentaje!!)
    }

    fun getTarifa(): Optional<String> {
        return Optional.of(tarifa!!)
    }

    fun getBaseImponibleReebolso(): Optional<String> {
        return Optional.of(baseImponibleReembolso!!)
    }

    fun getImpuestoReembolso(): Optional<String> {
        return Optional.of(impuestoReembolso!!)
    }


}