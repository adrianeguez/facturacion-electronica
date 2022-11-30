package documentos.comprobanteretencion
import utils.mensajeNulo
import java.util.*
import javax.validation.constraints.NotNull


class Retencion {

    @NotNull(message = "codigo $mensajeNulo")
    var codigo: String

    @NotNull(message = "codigoRetencion $mensajeNulo")
    var codigoRetencion: String

    @NotNull(message = "baseImponible $mensajeNulo")
    var baseImponible: String

    @NotNull(message = "porcentajeRetener $mensajeNulo")
    var porcentajeRetener: String

    @NotNull(message = "valorRetenido $mensajeNulo")
    var valorRetenido: String

    var fechaPagoDiv: String?
    fun getFechaPagoDiv(): Optional<String> {
        return Optional.of(fechaPagoDiv!!)
    }

    var imRentaSoc: String?
    fun getImRentaSoc(): Optional<String> {
        return Optional.of(imRentaSoc!!)
    }

    var ejerFisUtDiv: String?
    fun getEjerFisUtDiv(): Optional<String> {
        return Optional.of(ejerFisUtDiv!!)
    }

    var compraCajBanano: String?
    fun getCompraCajBanano(): Optional<String> {
        return Optional.of(compraCajBanano!!)
    }

    var NumCajBan: String?
    fun getNumCajBan(): Optional<String> {
        return Optional.of(NumCajBan!!)
    }

    var PrecCajBan: String?
    fun getPrecCajBan(): Optional<String> {
        return Optional.of(PrecCajBan!!)
    }

    constructor(
        codigo: String,
        codigoRetencion: String,
        baseImponible: String,
        porcentajeRetener: String,
        valorRetenido: String,
        fechaPagoDiv: String?,
        imRentaSoc: String?,
        ejerFisUtDiv: String?,
        compraCajBanano: String?,
        NumCajBan: String?,
        PrecCajBan: String?
    ) {
        this.codigo = codigo
        this.codigoRetencion = codigoRetencion
        this.baseImponible = baseImponible
        this.porcentajeRetener = porcentajeRetener
        this.valorRetenido = valorRetenido
        this.fechaPagoDiv = fechaPagoDiv
        this.imRentaSoc = imRentaSoc
        this.ejerFisUtDiv = ejerFisUtDiv
        this.compraCajBanano = compraCajBanano
        this.NumCajBan = NumCajBan
        this.PrecCajBan = PrecCajBan
    }
}