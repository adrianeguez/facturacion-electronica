package documentos.comprobanteretencion

import java.util.*

class CompraCajBanano(
    var NumCajBan: String?,
    var PrecCajBan: String?
) {


    fun getNumCajBan(): Optional<String> {
        return Optional.of(NumCajBan!!)
    }

    fun getPrecCajBan(): Optional<String> {
        return Optional.of(PrecCajBan!!)
    }
}