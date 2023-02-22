package documentos.notacredito

import documentos.Detalle
import documentos.DetalleAdicional
import documentos.Impuesto
import kotlin.collections.ArrayList

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