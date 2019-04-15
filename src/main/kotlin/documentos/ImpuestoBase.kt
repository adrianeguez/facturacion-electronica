package documentos

import utils.mensajeNulo
import utils.mensajeValores
import utils.tarifaICE
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

abstract class ImpuestoBase {



    @NotNull(message = "baseImponible $mensajeNulo")
    @Pattern(
        regexp = "^([0-9]{1,14}(\\.[0-9]{1,6}))?\$",
        message = "baseImponible $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var baseImponible: String


    constructor(
        baseImponible: String
    ) {
        this.baseImponible = baseImponible
    }
}