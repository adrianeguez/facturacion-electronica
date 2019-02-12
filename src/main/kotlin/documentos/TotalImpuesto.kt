package documentos

import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import utils.tarifaICE
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class TotalImpuesto {

    @Pattern(
        regexp = "2|3|5",
        message = "codigo $mensajeValores de 2|3|5"
    )
    var codigo: String

    @Pattern(
        regexp = tarifaICE,
        message = "codigoPorcentaje $mensajeValores de $tarifaICE"
    )
    var codigoPorcentaje: String

    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "descuentoAdicional $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var descuentoAdicional: String?

    fun getDescuentoAdicional(): Optional<String> {
        return Optional.of(descuentoAdicional!!)
    }

    @NotNull(message = "baseImponible $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "baseImponible $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var baseImponible: String

    @NotNull(message = "valor $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "valor $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var valor: String





    constructor(
        codigo: String,
        codigoPorcentaje: String,
        descuentoAdicional: String?,
        baseImponible: String,
        valor: String
    ) {
        this.codigo = codigo
        this.codigoPorcentaje = codigoPorcentaje
        this.descuentoAdicional = descuentoAdicional
        this.baseImponible = baseImponible
        this.valor = valor
    }

}