package documentos.comprobanteretencion
import java.util.*


class Retencion(

    var codigo: String,
    var codigoRetencion: String,
    var baseImponible: String,
    var porcentajeRetener: String,
    var valorRetenido: String,
    var fechaPagoDiv: String?,
    var imRentaSoc: String?,
    var ejerFisUtDiv: String?,
    var compraCajBanano: String?,
    var NumCajBan: String?,
    var PrecCajBan: String?
){


    fun getFechaPagoDiv(): Optional<String> {
        return Optional.of(fechaPagoDiv!!)
    }

    fun getImRentaSoc(): Optional<String> {
        return Optional.of(imRentaSoc!!)
    }

    fun getEjerFisUtDiv(): Optional<String> {
        return Optional.of(ejerFisUtDiv!!)
    }

    fun getCompraCajBanano(): Optional<String> {
        return Optional.of(compraCajBanano!!)
    }

    fun getNumCajBan(): Optional<String> {
        return Optional.of(NumCajBan!!)
    }

    fun getPrecCajBan(): Optional<String> {
        return Optional.of(PrecCajBan!!)
    }
}