package documentos

import utils.expresionRegularMoneda
import utils.mensajeNulo
import utils.mensajeValores
import utils.tarifaICE
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

open class Impuesto : ImpuestoBase {

    @NotNull(message = "codigo $mensajeNulo")
    @Pattern(
        regexp = "2|3|5",
        message = "codigo $mensajeValores de 2|3|5"
    )
    var codigo: String


    @NotNull(message = "valor $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "valor $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var valor: String

    @NotNull(message = "codigoPorcentaje $mensajeNulo")
    @Pattern(
        regexp = tarifaICE,
        message = "codigoPorcentaje $mensajeValores de $tarifaICE"
    )
    var codigoPorcentaje: String

    @Pattern(
        regexp = expresionRegularMoneda,
        message = "tarifa $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
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
    ) : super(baseImponible) {
        this.valor = valor
        this.codigoPorcentaje = codigoPorcentaje
        this.tarifa = tarifa
        this.codigo = codigo
    }
}