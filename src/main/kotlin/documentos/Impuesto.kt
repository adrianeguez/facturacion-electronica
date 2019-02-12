package documentos

import utils.mensajeValores
import java.util.*
import javax.validation.constraints.Pattern

class Impuesto : ImpuestoBase {

    @Pattern(
        regexp = "^([0-9]{1,2}(\\.[0-9]{2}))|100.00?\$",
        message = "tarifa $mensajeValores de 100.00 a 0.01 de porcentaje"
    )
    var tarifa: String?

    fun getDescuentoAdicional(): Optional<String> {
        return Optional.of(tarifa!!)
    }

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        tarifa: String?,
        baseImponible: String,
        valor: String
    ) : super(codigo, codigoPorcentaje, baseImponible, valor) {
        this.tarifa = tarifa
    }
}