package documentos

import utils.expresionRegularMoneda
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE
import javax.validation.constraints.Size

class MaquinaFiscal {

    @NotEmpty(message = "marca $mensajeVacio")
    @Size(min = 1, max = 100, message = "marca $mensajeValores de 1 a 100 caracteres")
    val marca: String?

    fun getMarca(): Optional<String> {
        return Optional.of(marca!!)
    }

    @NotEmpty(message = "modelo $mensajeVacio")
    @Size(min = 1, max = 100, message = "modelo $mensajeValores de 1 a 100 caracteres")
    val modelo: String?

    fun getModelo(): Optional<String> {
        return Optional.of(modelo!!)
    }

    @NotEmpty(message = "serie $mensajeVacio")
    @Size(min = 1, max = 30, message = "serie $mensajeValores de 1 a 30 caracteres")
    val serie: String?

    fun getSerie(): Optional<String> {
        return Optional.of(serie!!)
    }

    constructor(
        marca: String,
        modelo: String,
        serie: String
    ) {
        this.marca = marca
        this.modelo = modelo
        this.serie = serie
    }
}