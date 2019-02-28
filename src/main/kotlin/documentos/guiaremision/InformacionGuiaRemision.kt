package documentos.guiaremision

import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class InformacionGuiaRemision {

    @NotEmpty(message = "dirEstablecimiento $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirEstablecimiento $mensajeValores de 1 a 300 caracteres")
    var dirEstablecimiento: String?

    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }

    @NotNull(message = "dirPartida $mensajeNulo")
    @NotEmpty(message = "dirPartida $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirPartida $mensajeValores de 1 a 300 caracteres")
    var dirPartida: String

    constructor(
        dirEstablecimiento: String?,
        dirPartida: String
    ) {
        this.dirEstablecimiento = dirEstablecimiento
        this.dirPartida = dirPartida
    }
}