package documentos

import utils.expresionRegularMoneda
import utils.mensajeNulo
import utils.mensajeValores
import utils.tarifaICE
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

open class ImpuestoReembolso {

    @NotNull(message = "codigo $mensajeNulo")
    @Pattern(
        regexp = "2|3|5",
        message = "codigo $mensajeValores de 2|3|5"
    )
    var codigo: String?

    fun getCodigo(): Optional<String> {
        return Optional.of(codigo!!)
    }

    @NotNull(message = "codigoPorcentaje $mensajeNulo")
    @Pattern(
        regexp = tarifaICE,
        message = "codigoPorcentaje $mensajeValores de $tarifaICE"
    )
    var codigoPorcentaje: String?

    fun getCodigoPorcentaje(): Optional<String> {
        return Optional.of(codigoPorcentaje!!)
    }

    @Pattern(
        regexp = expresionRegularMoneda,
        message = "tarifa $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var tarifa: String?

    fun getTarifa(): Optional<String> {
        return Optional.of(tarifa!!)
    }

    @NotNull(message = "baseImponibleReembolso $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "baseImponibleReembolso $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var baseImponibleReembolso: String?

    fun getBaseImponibleReembolsoTarifa(): Optional<String> {
        return Optional.of(baseImponibleReembolso!!)
    }

    @NotNull(message = "impuestoReembolso $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "impuestoReembolso $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var impuestoReembolso: String?

    fun getImpuestoReembolso(): Optional<String> {
        return Optional.of(impuestoReembolso!!)
    }

    constructor(
        codigo: String?,
        codigoPorcentaje: String?,
        baseImponibleReembolso: String?,
        tarifa: String?,
        impuestoReembolso: String?
    ) {
        this.codigoPorcentaje = codigoPorcentaje
        this.tarifa = tarifa
        this.codigo = codigo
        this.baseImponibleReembolso = baseImponibleReembolso
        this.impuestoReembolso = impuestoReembolso
    }
}