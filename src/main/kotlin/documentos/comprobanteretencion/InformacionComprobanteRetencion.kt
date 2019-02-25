package documentos.comprobanteretencion

import documentos.GenerarDocumentos
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.Validation
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class InformacionComprobanteRetencion {

    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    @NotNull(message = "fechaEmision $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
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
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "tipoIdentificacionComprador $mensajeValores de 04|05|06|07|08|09"
    )
    var tipoIdentificacionSujetoRetenido: String


    @NotNull(message = "razonSocialSujetoRetenido $mensajeNulo")
    @NotEmpty(message = "razonSocialSujetoRetenido $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialSujetoRetenido $mensajeValores de 1 a 300 caracteres")
    var razonSocialSujetoRetenido: String

    @NotEmpty(message = "identificacionSujetoRetenido $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionSujetoRetenido $mensajeValores de 1 a 20 caracteres")
    var identificacionSujetoRetenido: String

    @NotNull(message = "periodoFiscal $mensajeNulo")
    @Pattern(
        regexp = "^(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "periodoFiscal $mensajeValores de fecha mm/aaaa"
    )
    var periodoFiscal: String

    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        tipoIdentificacionSujetoRetenido: String,
        razonSocialSujetoRetenido: String,
        identificacionSujetoRetenido: String,
        periodoFiscal: String
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento =
            if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento)
        this.contribuyenteEspecial =
            if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(
                contribuyenteEspecial
            )
        this.obligadoContabilidad = obligadoContabilidad
        this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido
        this.razonSocialSujetoRetenido = razonSocialSujetoRetenido
        this.identificacionSujetoRetenido = identificacionSujetoRetenido
        this.periodoFiscal = periodoFiscal
    }
}