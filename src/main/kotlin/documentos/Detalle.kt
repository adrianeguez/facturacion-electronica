package documentos

import java.util.*
import kotlin.collections.ArrayList

open class Detalle(
    var codigoPrincipal: String?,
    var codigoAuxiliar: String?,
    var descripcion: String,
    var unidadMedida: String?,
    var cantidad: String,
    var precioUnitario: String,
    var descuento: String,
    var precioTotalSinImpuesto: String,
    var detallesAdicionales: ArrayList<DetalleAdicional>?,
    var impuestos: ArrayList<Impuesto>
) {

    init {

        this.codigoPrincipal =
            if (codigoPrincipal == null) null else GenerarDocumentos.removerCaracteresEspeciales(codigoPrincipal!!)
        this.codigoAuxiliar =
            if (codigoAuxiliar == null) null else GenerarDocumentos.removerCaracteresEspeciales(codigoAuxiliar!!)
        this.descripcion = GenerarDocumentos.removerCaracteresEspeciales(descripcion)
        this.unidadMedida =
            if(unidadMedida == null) null else GenerarDocumentos.removerCaracteresEspeciales(unidadMedida!!)

    }

    fun getCodigoPrincipal(): Optional<String> {
        return Optional.of(codigoPrincipal!!)
    }

    fun getCodigoAuxiliar(): Optional<String> {
        return Optional.of(codigoAuxiliar!!)
    }

    fun getUnidadMedida(): Optional<String> {
        return Optional.of(unidadMedida!!)
    }

    fun getDetallesAdicionales(): Optional<ArrayList<DetalleAdicional>> {
        return Optional.of<ArrayList<DetalleAdicional>>(detallesAdicionales!!)
    }
}