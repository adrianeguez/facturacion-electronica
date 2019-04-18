package documentos.notacredito

import documentos.Detalle
import documentos.GenerarDocumentos
import documentos.TotalImpuesto
import utils.expresionRegularMoneda
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class InformacionNotaCredito {
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

    @NotNull(message = "tipoIdentificacionComprador $mensajeNulo")
    @Pattern(
        regexp = "04|05|06|07|08|09",
        message = "tipoIdentificacionComprador $mensajeValores de 04|05|06|07|08|09"
    )
    var tipoIdentificacionComprador: String

    @NotNull(message = "razonSocialComprador $mensajeNulo")
    @NotEmpty(message = "razonSocialComprador $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialComprador $mensajeValores de 1 a 300 caracteres")
    var razonSocialComprador: String

    @NotNull(message = "identificacionComprador $mensajeNulo")
    @NotEmpty(message = "identificacionComprador $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionComprador $mensajeValores de 1 a 20 caracteres")
    var identificacionComprador: String

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

    @Size(min = 1, max = 40, message = "rise $mensajeValores de 1 a 40 caracteres")
    var rise: String?

    fun getRise(): Optional<String> {
        return Optional.of(rise!!)
    }

    @NotNull(message = "codDocModificado $mensajeNulo")
    @Pattern(
        regexp = "01|04|05|06|07",
        message = "codDocModificado $mensajeValores 01|04|05|06|07"
    )
    var codDocModificado: String


    @Pattern(
        regexp = "^(([0-9]{3}-[0-9]{3}-[0-9]{9})|([0-9]{3}-[0-9]{3}-[0-9]{10}))\$",
        message = "numDocModificado $mensajeValores de este formato 001-001-000000001 o 10 al final"
    )
    var numDocModificado: String

    @NotNull(message = "fechaEmisionDocSustento $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        message = "fechaEmisionDocSustento $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaEmisionDocSustento: String

    @NotNull(message = "totalSinImpuestos $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalSinImpuestos $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalSinImpuestos: String

    @NotNull(message = "valorModificacion $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "valorModificacion $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var valorModificacion: String

    @NotNull(message = "moneda $mensajeNulo")
    @Size(min = 1, max = 15, message = "moneda $mensajeValores de 1 a 300 caracteres")
    var moneda: String

    @NotNull(message = "totalConImpuesto $mensajeNulo")
    var totalConImpuesto: ArrayList<TotalImpuesto>

    @NotNull(message = "motivo $mensajeNulo")
    @NotEmpty(message = "motivo $mensajeVacio")
    @Size(min = 1, max = 300, message = "motivo $mensajeValores de 1 a 300 caracteres")
    var motivo: String

    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        tipoIdentificacionComprador: String,
        razonSocialComprador: String,
        identificacionComprador: String,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        rise: String?,
        codDocModificado: String,
        numDocModificado: String,
        fechaEmisionDocSustento: String,
        totalSinImpuestos: String,
        valorModificacion: String,
        moneda: String,
        totalConImpuesto: ArrayList<TotalImpuesto>,
        motivo: String
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento = if (dirEstablecimiento == null) null else GenerarDocumentos
            .removerCaracteresEspeciales(dirEstablecimiento)
        this.tipoIdentificacionComprador = tipoIdentificacionComprador
        this.razonSocialComprador = GenerarDocumentos.removerCaracteresEspeciales(razonSocialComprador)
        this.identificacionComprador = identificacionComprador
        this.contribuyenteEspecial = contribuyenteEspecial
        this.obligadoContabilidad = obligadoContabilidad
        this.rise = rise
        this.codDocModificado = codDocModificado
        this.numDocModificado = numDocModificado
        this.fechaEmisionDocSustento = fechaEmisionDocSustento
        this.totalSinImpuestos = totalSinImpuestos
        this.valorModificacion = valorModificacion
        this.moneda = moneda
        this.totalConImpuesto = totalConImpuesto
        this.motivo = motivo
    }
}