package documentos

import javax.validation.constraints.*
import java.util.Optional
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE


class InformacionTributaria {

    @NotNull(message = "ambiente no puede ser nulo")
    @Pattern(
        regexp = "1|2",
        flags = [CASE_INSENSITIVE],
        message = "ambiente debe tener valores 1|2"
    )
    var ambiente: String

    @NotNull(message = "tipoEmision no puede ser nulo")
    @Pattern(
        regexp = "1",
        flags = [CASE_INSENSITIVE],
        message = "tipoEmision debe tener valor 1"
    )
    var tipoEmision: String

    @NotNull(message = "razonSocial no puede ser nulo")
    @NotEmpty(message = "razonSocial no puede estar vacio")
    @Size(min = 1, max = 300, message = "razonSocial puede tener de 1 a 300 caracteres")
    var razonSocial: String

    @Size(min = 1, max = 300, message = "nombreComercial puede tener de 1 a 300 caracteres")
    var nombreComercial: String?

    fun getNombreComercial(): Optional<String> {
        return Optional.of(nombreComercial!!)
    }

    @NotNull(message = "ruc no puede ser nulo")
    @Size(min = 13, max = 13, message = "ruc debe tener 13 caracteres")
    var ruc: String

    @NotNull(message = "claveAcceso no puede ser nulo")
    @Size(min = 49, max = 49, message = "claveAcceso debe tener 49 caracteres")
    var claveAcceso: String

    @Pattern(
        regexp = "01|04|05|06|07",
        flags = [CASE_INSENSITIVE],
        message = "codDoc debe tener valores 01|04|05|06|07"
    )
    var codDoc: String

    @NotNull(message = "estab no puede ser nulo")
    @Size(min = 3, max = 3, message = "estab debe tener 3 caracteres")
    var estab: String

    @NotNull(message = "ptoEmision no puede ser nulo")
    @Size(min = 3, max = 3, message = "ptoEmision debe tener 3 caracteres")
    var ptoEmision: String

    @NotNull(message = "secuencial no puede ser nulo")
    @Size(min = 9, max = 9, message = "secuencial debe tener 9 caracteres")
    var secuencial: String

    @NotNull(message = "dirMatriz no puede ser nulo")
    @NotEmpty(message = "dirMatriz no puede estar vacio")
    @Size(min = 1, max = 300, message = "dirMatriz debe tener de 1 a 300 caracteres")
    var dirMatriz: String


    constructor(
        ambiente: String,
        tipoEmision: String,
        razonSocial: String,
        nombreComercial: String?,
        ruc: String,
        claveAcceso: String,
        codDoc: String,
        estab: String,
        ptoEmision: String,
        secuencial: String,
        dirMatriz: String
    ) {
        this.ambiente = ambiente
        this.tipoEmision = tipoEmision
        this.razonSocial = razonSocial
        this.nombreComercial = nombreComercial
        this.ruc = ruc
        this.claveAcceso = claveAcceso
        this.codDoc = codDoc
        this.estab = estab
        this.ptoEmision = ptoEmision
        this.secuencial = secuencial
        this.dirMatriz = dirMatriz
    }
}

