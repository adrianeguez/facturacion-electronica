package documentos

import java.util.*


open class Impuesto(
    var codigo: String,
    var codigoPorcentaje: String,
    baseImponible: String,
    var valor: String,
    var tarifa: String?
) : ImpuestoBase(baseImponible) {

    fun getTarifa(): Optional<String> {
        return Optional.of(tarifa!!)
    }
}