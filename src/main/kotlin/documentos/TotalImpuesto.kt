package documentos

import utils.expresionRegularMoneda
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.Pattern

class TotalImpuesto : Impuesto {

    @Pattern(
        regexp = expresionRegularMoneda,
        message = "descuentoAdicional $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var descuentoAdicional: String?

    fun getDescuentoAdicional(): Optional<String> {
        return Optional.of(descuentoAdicional!!)
    }

    @Pattern(
        regexp = expresionRegularMoneda,
        message = "valorDevolucionIva $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var valorDevolucionIva: String?

    fun getValorDevolucionIva(): Optional<String> {
        return Optional.of(valorDevolucionIva!!)
    }

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        descuentoAdicional: String?,
        baseImponible: String,
        tarifa: String?,
        valor: String,
        valorDevolucionIva: String?
    ) : super(codigo, codigoPorcentaje, baseImponible, valor, tarifa) {
        this.descuentoAdicional = descuentoAdicional
        this.valorDevolucionIva = valorDevolucionIva
    }

}