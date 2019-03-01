package documentos.guiaremision

import documentos.GenerarDocumentos
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class InformacionGuiaRemision {

    @NotEmpty(message = "dirEstablecimiento $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirEstablecimiento $mensajeValores de 1 a 300 caracteres")
    var dirEstablecimiento: String?

    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }

    @NotNull(message = "dirPartida $mensajeNulo")
    @NotEmpty(message = "dirPartida $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirPartida $mensajeValores de 1 a 300 caracteres")
    var dirPartida: String

    @NotNull(message = "razonSocialTransportista $mensajeNulo")
    @NotEmpty(message = "razonSocialTransportista $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialTransportista $mensajeValores de 1 a 300 caracteres")
    var razonSocialTransportista: String


    @NotNull(message = "tipoIdentificacionTransportista $mensajeNulo")
    @Pattern(
        regexp = "04|05|06|07|08|09",
        message = "tipoIdentificacionTransportista $mensajeValores de 04|05|06|07|08|09"
    )
    var tipoIdentificacionTransportista: String

    @NotNull(message = "rucTransportista $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{13}\$",
        message = "rucTransportista $mensajeValores de 13 digitos de numeros del 0-9"
    )
    var rucTransportista: String


    @Size(min = 1, max = 40, message = "razonSocialTransportista $mensajeValores de 1 a 40 caracteres")
    var rise: String?

    fun getRise(): Optional<String> {
        return Optional.of(rise!!)
    }

    @Pattern(
        regexp = "SI|NO",
        message = "obligadoContabilidad $mensajeValores SI|NO"
    )
    var obligadoContabilidad: String?

    fun getObligadoContabilidad(): Optional<String> {
        return Optional.of(obligadoContabilidad!!)
    }

    @NotEmpty(message = "contribuyenteEspecial $mensajeVacio")
    @Size(min = 1, max = 13, message = "contribuyenteEspecial $mensajeValores de 1 a 13 caracteres")
    var contribuyenteEspecial: String?

    fun getContribuyenteEspecial(): Optional<String> {
        return Optional.of(contribuyenteEspecial!!)
    }

    @NotNull(message = "fechaIniTransporte $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "fechaIniTransporte $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaIniTransporte: String

    @NotNull(message = "fechaFinTransporte $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "fechaFinTransporte $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaFinTransporte: String

    @NotNull(message = "placa $mensajeNulo")
    @NotEmpty(message = "placa $mensajeVacio")
    @Size(min = 1, max = 20, message = "placa $mensajeValores de 1 a 20 caracteres")
    var placa: String

    constructor(
        dirEstablecimiento: String?,
        dirPartida: String,
        razonSocialTransportista: String,
        tipoIdentificacionTransportista: String,
        rucTransportista: String,
        rise: String?,
        obligadoContabilidad: String?,
        contribuyenteEspecial: String?,
        fechaIniTransporte: String,
        fechaFinTransporte: String,
        placa: String
    ) {
        this.dirEstablecimiento =
                if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(
                    dirEstablecimiento
                )
        this.dirPartida = GenerarDocumentos.removerCaracteresEspeciales(dirPartida)
        this.razonSocialTransportista = GenerarDocumentos.removerCaracteresEspeciales(razonSocialTransportista)
        this.tipoIdentificacionTransportista = tipoIdentificacionTransportista
        this.rucTransportista = rucTransportista
        this.obligadoContabilidad = obligadoContabilidad
        this.rise = if (rise == null) null else GenerarDocumentos.removerCaracteresEspeciales(rise)
        this.contribuyenteEspecial =
                if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(
                    contribuyenteEspecial
                )
        this.fechaIniTransporte = fechaIniTransporte
        this.fechaFinTransporte = fechaFinTransporte
        this.placa = placa
    }
}