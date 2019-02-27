package documentos

import utils.mensajeValores
import java.util.*
import javax.validation.constraints.Pattern

class TotalImpuesto : Impuesto {

    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "descuentoAdicional $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var descuentoAdicional: String?

    fun getDescuentoAdicional(): Optional<String> {
        return Optional.of(descuentoAdicional!!)
    }

    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "valorDevolucionIva $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
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