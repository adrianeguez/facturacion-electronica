package documentos.comprobanteretencion

import java.util.*

class Dividendos(
    var fechaPagoDiv: String?,
    var imRentaSoc: String?,
    var ejerFisUtDiv: String?
) {
    fun getFechaPagoDiv(): Optional<String> {
        return Optional.of(fechaPagoDiv!!)
    }

    fun getImRentaSoc(): Optional<String> {
        return Optional.of(imRentaSoc!!)
    }

    fun getEjerFisUtDiv(): Optional<String> {
        return Optional.of(ejerFisUtDiv!!)
    }
}