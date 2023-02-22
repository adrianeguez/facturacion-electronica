package documentos.guiaremision

import documentos.DetalleAdicional
import documentos.GenerarDocumentos
import java.util.*

import kotlin.collections.ArrayList

class DetalleGuiaRemision(
    var codigoInterno: String?,
    var codigoAdicional: String?,
    var descripcion: String,
    var cantidad: String,
    var detallesAdicionales: ArrayList<DetalleAdicional>?
) {
    
    init {
        this.descripcion = GenerarDocumentos.removerCaracteresEspeciales(descripcion)
    }


    fun getCodigoInterno(): Optional<String> {
        return Optional.of(codigoInterno!!)
    }


    fun getCodigoAdicional(): Optional<String> {
        return Optional.of(codigoAdicional!!)
    }

    fun getDetallesAdicionales(): Optional<ArrayList<DetalleAdicional>> {
        return Optional.of<ArrayList<DetalleAdicional>>(detallesAdicionales!!)
    }

}