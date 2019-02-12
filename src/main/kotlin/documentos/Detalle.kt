package documentos

import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class Detalle {

    @NotEmpty(message = "codigoPrincipal $mensajeVacio")
    @Size(min = 1, max = 25, message = "codigoPrincipal $mensajeValores de 1 a 25 caracteres")
    var codigoPrincipal: String?

    fun getCodigoPrincipal(): Optional<String> {
        return Optional.of(codigoPrincipal!!)
    }

    @NotEmpty(message = "codigoAuxiliar $mensajeVacio")
    @Size(min = 1, max = 25, message = "codigoAuxiliar $mensajeValores de 1 a 25 caracteres")
    var codigoAuxiliar: String?

    fun getCodigoAuxiliar(): Optional<String> {
        return Optional.of(codigoPrincipal!!)
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

    @NotNull(message = "precioUnitario $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "precioUnitario $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var precioUnitario: String

    @NotNull(message = "descuento $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "descuento $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var descuento: String

    @NotNull(message = "precioTotalSinImpuesto $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "precioTotalSinImpuesto $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var precioTotalSinImpuesto: String

    var detallesAdicionales: Array<DetalleAdicional>?

    constructor(
        codigoPrincipal: String?,
        codigoAuxiliar: String?,
        descripcion: String,
        cantidad: String,
        precioUnitario: String,
        descuento: String,
        precioTotalSinImpuesto: String,
        detallesAdicionales: Array<DetalleAdicional>?
    ) {
        this.codigoPrincipal = codigoPrincipal
        this.codigoAuxiliar = codigoAuxiliar
        this.descripcion = descripcion
        this.cantidad = cantidad
        this.precioUnitario = precioUnitario
        this.descuento = descuento
        this.precioTotalSinImpuesto = precioTotalSinImpuesto
        this.detallesAdicionales = detallesAdicionales
    }
}