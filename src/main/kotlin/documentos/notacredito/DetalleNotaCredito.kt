package documentos.notacredito

import documentos.Detalle
import documentos.DetalleAdicional
import documentos.Impuesto
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class DetalleNotaCredito : Detalle {

    @NotEmpty(message = "codigoInterno $mensajeVacio")
    @Size(min = 1, max = 25, message = "codigoInterno $mensajeValores de 1 a 25 caracteres")
    var codigoInterno: String?

    fun getCodigoInterno(): Optional<String> {
        return Optional.of(codigoInterno!!)
    }

    @NotEmpty(message = "codigoAdicional $mensajeVacio")
    @Size(min = 1, max = 25, message = "codigoAdicional $mensajeValores de 1 a 25 caracteres")
    var codigoAdicional: String?

    fun getCodigoAdicional(): Optional<String> {
        return Optional.of(codigoAdicional!!)
    }


    constructor(
        codigoInterno: String?,
        codigoAdicional: String?,
        codigoPrincipal: String? = null,
        codigoAuxiliar: String? = null,
        descripcion: String,
        cantidad: String,
        precioUnitario: String,
        descuento: String,
        precioTotalSinImpuesto: String,
        detallesAdicionales: ArrayList<DetalleAdicional>?,
        impuestos: ArrayList<Impuesto>
    ) : super(
        codigoPrincipal,
        codigoAuxiliar,
        descripcion,
        cantidad,
        precioUnitario,
        descuento,
        precioTotalSinImpuesto,
        detallesAdicionales,
        impuestos
    ) {
        this.codigoInterno = codigoInterno
        this.codigoAdicional = codigoAdicional
    }
}