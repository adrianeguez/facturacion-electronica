package documentos
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.Pattern

class TotalImpuesto : ImpuestoBase {

    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "descuentoAdicional $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var descuentoAdicional: String?

    fun getDescuentoAdicional(): Optional<String> {
        return Optional.of(descuentoAdicional!!)
    }

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        descuentoAdicional: String?,
        baseImponible: String,
        valor: String
    ) : super(codigo, codigoPorcentaje, baseImponible, valor) {
        this.descuentoAdicional = descuentoAdicional
    }

}