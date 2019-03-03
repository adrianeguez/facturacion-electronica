package documentos.notadebito

import documentos.GenerarDocumentos
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class Motivo {
    @NotNull(message = "razon $mensajeNulo")
    @NotEmpty(message = "razon $mensajeVacio")
    @Size(min = 1, max = 300, message = "razon $mensajeValores de 1 a 300 caracteres")
    var razon: String

    @NotNull(message = "valor $mensajeNulo")
    @NotEmpty(message = "valor $mensajeVacio")
    @Size(min = 1, max = 300, message = "valor $mensajeValores de 1 a 300 caracteres")
    var valor: String

    constructor(
        razon: String,
        valor: String
    ) {
        this.razon = GenerarDocumentos.removerCaracteresEspeciales(razon)
        this.valor = GenerarDocumentos.removerCaracteresEspeciales(valor)
    }
}