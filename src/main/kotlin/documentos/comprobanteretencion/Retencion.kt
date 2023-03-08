package documentos.comprobanteretencion


import java.util.*
import kotlin.collections.ArrayList


class Retencion(

    var codigo: String,
    var codigoRetencion: String,
    var baseImponible: String,
    var porcentajeRetener: String,
    var valorRetenido: String,
    var dividendos: Dividendos?,
    var compraCajBanano: CompraCajBanano?

){

    fun getDividendos(): Optional<Dividendos> {
        return Optional.of(dividendos!!)
    }

    fun getCompraCajBanano(): Optional<CompraCajBanano> {
        return Optional.of(compraCajBanano!!)
    }



}