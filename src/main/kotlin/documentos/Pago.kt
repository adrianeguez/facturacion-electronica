package documentos

import java.util.*


class Pago(
    var formaPago: String,
    var total: String,
    var plazo: String?,
    var unidadTiempo: String?
) {

    fun getPlazo(): Optional<String> {
        return Optional.of(plazo!!)
    }

    fun getUnidadTiempo(): Optional<String> {
        return Optional.of(unidadTiempo!!)
    }


}