package documentos

import utils.mensajeNulo
import utils.mensajeTamano
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE
import javax.validation.constraints.Size

class InformacionFactura {

    @NotNull(message = "fechaEmision $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [CASE_INSENSITIVE],
        message = "fechaEmision $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaEmision: String


    @NotEmpty(message = "dirEstablecimiento $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirEstablecimiento $mensajeValores de 1 a 300 caracteres")
    var dirEstablecimiento: String?

    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }


    @NotEmpty(message = "contribuyenteEspecial $mensajeVacio")
    @Size(min = 1, max = 13, message = "contribuyenteEspecial $mensajeValores de 1 a 13 caracteres")
    var contribuyenteEspecial: String?

    fun getContribuyenteEspecial(): Optional<String> {
        return Optional.of(contribuyenteEspecial!!)
    }

    @Pattern(
        regexp = "SI|NO",
        message = "obligadoContabilidad $mensajeValores SI|NO"
    )
    var obligadoContabilidad: String?

    fun getObligadoContabilidad(): Optional<String> {
        return Optional.of(obligadoContabilidad!!)
    }

    @Pattern(
        regexp = "04|05|06|07|08|09",
        flags = [CASE_INSENSITIVE],
        message = "tipoIdentificacionComprador $mensajeValores 04|05|06|07|08|09"
    )
    var tipoIdentificacionComprador: String?

    fun getTipoIdentificacionComprador(): Optional<String> {
        return Optional.of(tipoIdentificacionComprador!!)
    }


    @Pattern(
        regexp = "^[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\$",
        flags = [CASE_INSENSITIVE],
        message = "tipoIdentificacionComprador $mensajeValores 04|05|06|07|08|09"
    )
    val guiaRemision: String?

    fun getGuiaRemision(): Optional<String> {
        return Optional.of(guiaRemision!!)
    }

    @NotNull(message = "razonSocialComprador $mensajeNulo")
    @NotEmpty(message = "razonSocialComprador $mensajeVacio")
    var razonSocialComprador: String


    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        tipoIdentificacionComprador: String?,
        guiaRemision: String?,
        razonSocialComprador: String
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento = dirEstablecimiento
        this.contribuyenteEspecial = contribuyenteEspecial
        this.obligadoContabilidad = obligadoContabilidad
        this.tipoIdentificacionComprador = tipoIdentificacionComprador
        this.guiaRemision = guiaRemision
        this.razonSocialComprador = razonSocialComprador
    }
}