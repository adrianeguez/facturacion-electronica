package documentos

import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class DetalleAdicional {

    @NotNull(message = "nombre $mensajeNulo")
    @NotEmpty(message = "nombre $mensajeVacio")
    @Size(min = 1, max = 300, message = "nombre $mensajeValores de 1 a 300 caracteres")
    var nombre: String

    @NotNull(message = "valor $mensajeNulo")
    @NotEmpty(message = "valor $mensajeVacio")
    @Size(min = 1, max = 300, message = "valor $mensajeValores de 1 a 300 caracteres")
    var valor: String

    constructor(
        nombre: String,
        valor: String
    ) {
        this.nombre = nombre
        this.valor = valor
    }
}