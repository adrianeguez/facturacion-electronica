package documentos

import utils.mensajeValores
import java.util.*
import javax.validation.constraints.Pattern

class Impuesto : ImpuestoBase {

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        tarifa: String?,
        baseImponible: String,
        valor: String
    ) : super(codigo, codigoPorcentaje, baseImponible, valor, tarifa) {
    }
}