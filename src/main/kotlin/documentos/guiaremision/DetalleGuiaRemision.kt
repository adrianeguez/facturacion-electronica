package documentos.guiaremision

import documentos.DetalleAdicional
import documentos.GenerarDocumentos
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import kotlin.collections.ArrayList

class DetalleGuiaRemision {
    @Size(min = 1, max = 300, message = "codigoInterno $mensajeValores de 1 a 300 caracteres")
    var codigoInterno: String?

    fun getCodigoInterno(): Optional<String> {
        return Optional.of(codigoInterno!!)
    }

    @Size(min = 1, max = 300, message = "codigoAdicional $mensajeValores de 1 a 300 caracteres")
    var codigoAdicional: String?

    fun getCodigoAdicional(): Optional<String> {
        return Optional.of(codigoAdicional!!)
    }

    @NotNull(message = "descripcion $mensajeNulo")
    @NotEmpty(message = "descripcion $mensajeVacio")
    @Size(min = 1, max = 300, message = "descripcion $mensajeValores de 1 a 300 caracteres")
    var descripcion: String

    @NotNull(message = "cantidad $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0]{2})?\$",
        message = "cantidad $mensajeValores de 1 a 14 enteros y 2 decimales con valor 00 separados por punto"
    )
    var cantidad: String

    @NotNull(message = "detallesAdicionales $mensajeNulo")
    var detallesAdicionales: ArrayList<DetalleAdicional>

    constructor(
        codigoInterno: String?,
        codigoAdicional: String?,
        descripcion: String,
        cantidad: String,
        detallesAdicionales: ArrayList<DetalleAdicional>
    ) {
        this.codigoInterno = codigoInterno
        this.codigoAdicional = codigoAdicional
        this.descripcion = GenerarDocumentos.removerCaracteresEspeciales(descripcion)
        this.cantidad = cantidad
        this.detallesAdicionales = detallesAdicionales
    }
}