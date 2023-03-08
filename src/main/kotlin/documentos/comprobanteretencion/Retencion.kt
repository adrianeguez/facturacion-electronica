package documentos.comprobanteretencion


import java.util.*
import kotlin.collections.ArrayList


class Retencion(

    var codigo: String,
    var codigoRetencion: String,
    var baseImponible: String,
    var porcentajeRetener: String,
    var valorRetenido: String,
    var dividendos: ArrayList<Dividendos>?,
    var compraCajBanano: ArrayList<CompraCajBanano>?

){

    fun getDividendos(): Optional<ArrayList<Dividendos>> {
        return Optional.of(dividendos!!)
    }

    fun getCompraCajBanano(): Optional<ArrayList<CompraCajBanano>> {
        return Optional.of(compraCajBanano!!)
    }



}