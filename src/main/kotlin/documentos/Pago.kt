package documentos

import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE
import javax.validation.constraints.Size

class Pago {

    @NotNull(message = "formaPago $mensajeNulo")
    @Pattern(
        regexp = "01|15|16|17|18|19|20|21",
        flags = [CASE_INSENSITIVE],
        message = "formaPago $mensajeValores de 01|15|16|17|18|19|20|21"
    )
    val formaPago: String

    @NotNull(message = "total $mensajeNulo")
    @Pattern(
        regexp = "^([0-9]{1,14}(\\.[0-9]{1,6}))?\$",
        message = "total $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    val total: String

    @NotEmpty(message = "plazo $mensajeVacio")
    @Size(min = 1, max = 14, message = "plazo $mensajeValores de 1 a 14 caracteres")
    val plazo: String?

    fun getPlazo(): Optional<String> {
        return Optional.of(plazo!!)
    }

    @NotEmpty(message = "unidadTiempo $mensajeVacio")
    @Size(min = 1, max = 10, message = "unidadTiempo $mensajeValores de 1 a 10 caracteres")
    val unidadTiempo: String?

    fun getUnidadTiempo(): Optional<String> {
        return Optional.of(unidadTiempo!!)
    }

    constructor(
        formaPago: String,
        total: String,
        plazo: String?,
        unidadTiempo: String?
    ) {
        this.formaPago = formaPago
        this.total = total
        this.plazo = plazo
        this.unidadTiempo = unidadTiempo
    }
}