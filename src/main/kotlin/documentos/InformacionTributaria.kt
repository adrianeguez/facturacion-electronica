package documentos

import org.intellij.lang.annotations.RegExp
import utils.mensajeNulo
import utils.mensajeTamano
import utils.mensajeVacio
import utils.mensajeValores
import javax.validation.constraints.*
import java.util.Optional
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE


class InformacionTributaria {

    @NotNull(message = "ambiente $mensajeNulo")
    @Pattern(
        regexp = "1|2",
        flags = [CASE_INSENSITIVE],
        message = "ambiente $mensajeValores 1|2"
    )
    var ambiente: String

    @NotNull(message = "tipoEmision $mensajeNulo")
    @Pattern(
        regexp = "1",
        flags = [CASE_INSENSITIVE],
        message = "tipoEmision $mensajeValores de 1"
    )
    var tipoEmision: String

    @NotNull(message = "razonSocial $mensajeNulo")
    @NotEmpty(message = "razonSocial $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocial $mensajeValores de 1 a 300 caracteres")
    var razonSocial: String

    @Size(min = 1, max = 300, message = "nombreComercial $mensajeValores de 1 a 300 caracteres")
    var nombreComercial: String?

    fun getNombreComercial(): Optional<String> {
        return Optional.of(nombreComercial!!)
    }

    @NotNull(message = "ruc $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{13}\$",
        message = "ruc $mensajeValores de numeros 0-9"
    )
    @Size(min = 13, max = 13, message = "ruc $mensajeTamano 13 caracteres")
    var ruc: String


    @Size(min = 49, max = 49, message = "claveAcceso $mensajeTamano 49 caracteres")
    var claveAcceso: String?

    @Pattern(
        regexp = "01|04|05|06|07",
        flags = [CASE_INSENSITIVE],
        message = "codDoc $mensajeValores 01|04|05|06|07"
    )
    var codDoc: String

    @NotNull(message = "estab $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{3}\$",
        message = "estab $mensajeValores de numeros 0-9"
    )
    @Size(min = 3, max = 3, message = "estab $mensajeTamano 3 caracteres")
    var estab: String

    @NotNull(message = "ptoEmision $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{3}\$",
        message = "ptoEmision $mensajeValores de numeros 0-9"
    )
    @Size(min = 3, max = 3, message = "ptoEmision $mensajeTamano 3 caracteres")
    var ptoEmision: String

    @NotNull(message = "secuencial $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{9}\$",
        message = "secuencial $mensajeValores de numeros 0-9"
    ) // 9 o 10 digitos!
    @Size(min = 9, max = 9, message = "secuencial $mensajeTamano 9 caracteres")
    var secuencial: String

    @NotNull(message = "dirMatriz $mensajeNulo")
    @NotEmpty(message = "dirMatriz $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirMatriz $mensajeValores de 1 a 300 caracteres")
    var dirMatriz: String


    constructor(
        ambiente: String,
        tipoEmision: String,
        razonSocial: String,
        nombreComercial: String?,
        ruc: String,
        claveAcceso: String?,
        codDoc: String,
        estab: String,
        ptoEmision: String,
        secuencial: String,
        dirMatriz: String
    ) {
        this.ambiente = ambiente
        this.tipoEmision = tipoEmision
        this.razonSocial = GenerarDocumentos.removerCaracteresEspeciales(razonSocial)
        this.nombreComercial =
                if (nombreComercial == null) null else GenerarDocumentos.removerCaracteresEspeciales(nombreComercial)
        this.ruc = ruc
        this.claveAcceso = claveAcceso
        this.codDoc = codDoc
        this.estab = estab
        this.ptoEmision = ptoEmision
        this.secuencial = secuencial
        this.dirMatriz = GenerarDocumentos.removerCaracteresEspeciales(dirMatriz)
    }


}

