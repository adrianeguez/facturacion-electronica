package documentos

import utils.mensajeNulo
import utils.mensajeValores
import utils.tarifaICE
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

abstract class ImpuestoBase {

    @NotNull(message = "codigo $mensajeNulo")
    @Pattern(
        regexp = "2|3|5",
        message = "codigo $mensajeValores de 2|3|5"
    )
    var codigo: String

    @NotNull(message = "codigoPorcentaje $mensajeNulo")
    @Pattern(
        regexp = tarifaICE,
        message = "codigoPorcentaje $mensajeValores de $tarifaICE"
    )
    var codigoPorcentaje: String

    @NotNull(message = "baseImponible $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "baseImponible $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var baseImponible: String

    @NotNull(message = "valor $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "valor $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var valor: String

    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "tarifa $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var tarifa: String?

    fun getTarifa(): Optional<String> {
        return Optional.of(tarifa!!)
    }

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        baseImponible: String,
        valor: String,
        tarifa: String?
    ) {
        this.codigo = codigo
        this.codigoPorcentaje = codigoPorcentaje
        this.baseImponible = baseImponible
        this.valor = valor
        this.tarifa = tarifa
    }
}