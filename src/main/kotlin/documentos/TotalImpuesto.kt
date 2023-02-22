package documentos


import java.util.*


class TotalImpuesto(
    codigo: String,
    codigoPorcentaje: String,
    var descuentoAdicional: String?,
    baseImponible: String,
    tarifa: String?,
    valor: String,
    var valorDevolucionIva: String?
) : Impuesto(codigo, codigoPorcentaje, baseImponible, valor, tarifa) {

    init {
        fun getValorDevolucionIva(): Optional<String> {
            return Optional.of(valorDevolucionIva!!)
        }
    }


}