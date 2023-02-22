package documentos

import java.util.*

open class ImpuestoReembolso(
    var codigo: String?,
    var codigoPorcentaje: String?,
    var baseImponibleReembolso: String?,
    var tarifa: String?,
    var impuestoReembolso: String?
){

    fun getCodigo(): Optional<String> {
        return Optional.of(codigo!!)
    }

    fun getCodigoPorcentaje(): Optional<String> {
        return Optional.of(codigoPorcentaje!!)
    }

    fun getBaseImponibleReembolsoTarifa(): Optional<String> {
        return Optional.of(baseImponibleReembolso!!)
    }

    fun getTarifa(): Optional<String> {
        return Optional.of(tarifa!!)
    }

    fun getImpuestoReembolso(): Optional<String> {
        return Optional.of(impuestoReembolso!!)
    }

}