package documentos.notacredito

import documentos.Detalle
import documentos.DetalleAdicional
import documentos.GenerarDocumentos
import documentos.Impuesto
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class DetalleNotaCredito(
    codigoPrincipal: String?,
    codigoAuxiliar: String?,
    descripcion: String,
    unidadMedida: String,
    cantidad: String,
    precioUnitario: String,
    descuento: String,
    precioTotalSinImpuesto: String,
    detallesAdicionales: ArrayList<DetalleAdicional>?,
    impuestos: ArrayList<Impuesto>
) : Detalle(
    codigoPrincipal,
    codigoAuxiliar,
    descripcion,
    unidadMedida,
    cantidad,
    precioUnitario,
    descuento,
    precioTotalSinImpuesto,
    detallesAdicionales,
    impuestos
) {

}