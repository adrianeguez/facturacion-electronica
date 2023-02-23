package documentos.comprobanteretencion

import documentos.ImpuestoBase
import java.util.*

class ImpuestoRetencion(
    var codigo: String,
    var codigoRetencion: String,
    baseImponible: String,
    var porcentajeRetener: String,
    var valorRetenido: String,
    var codDocSustento: String,
    var numDocSustento: String?,
    var fechaEmisionDocSustento: String
) : ImpuestoBase(baseImponible) {

    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }

}