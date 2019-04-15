package documentos.notadebito

import documentos.GenerarDocumentos
import documentos.Impuesto
import documentos.Pago
import documentos.TotalImpuesto
import utils.mensajeNulo
import utils.mensajeVacio
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class InformacionNotaDebito {

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

    @NotNull(message = "codDocModificado $mensajeNulo")
    @Pattern(
        regexp = "01|04|05|06|07",
        message = "codDocModificado $mensajeValores 01|04|05|06|07"
    )
    var codDocModificado: String

    @Pattern(
        regexp = "^[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\$",
        message = "numDocModificado $mensajeValores de este formato 001-001-000000001"
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
        regexp = "^([0-9]{1,14}(\\.[0-9]{1,6}))?\$",
        message = "totalSinImpuestos $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalSinImpuestos: String

    @NotNull(message = "totalConImpuesto $mensajeNulo")
    var impuestos: ArrayList<Impuesto>

    @NotNull(message = "valorTotal $mensajeNulo")
    @Pattern(
        regexp = "^([0-9]{1,14}(\\.[0-9]{1,6}))?\$",
        message = "valorTotal $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var valorTotal: String

    @NotNull(message = "pagos $mensajeNulo")
    var pagos: ArrayList<Pago>


    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        tipoIdentificacionComprador: String,
        razonSocialComprador: String,
        identificacionComprador: String,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        codDocModificado: String,
        numDocModificado: String,
        fechaEmisionDocSustento: String,
        totalSinImpuestos: String,
        impuestos: ArrayList<Impuesto>,
        valorTotal: String,
        pagos: ArrayList<Pago>
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento = if (dirEstablecimiento == null) null else GenerarDocumentos
            .removerCaracteresEspeciales(dirEstablecimiento)
        this.tipoIdentificacionComprador = tipoIdentificacionComprador
        this.razonSocialComprador = GenerarDocumentos
            .removerCaracteresEspeciales(razonSocialComprador)
        this.identificacionComprador = identificacionComprador
        this.contribuyenteEspecial = contribuyenteEspecial
        this.obligadoContabilidad = obligadoContabilidad
        this.codDocModificado = codDocModificado
        this.numDocModificado = numDocModificado
        this.fechaEmisionDocSustento = fechaEmisionDocSustento
        this.totalSinImpuestos = totalSinImpuestos
        this.impuestos = impuestos
        this.valorTotal = valorTotal
        this.pagos = pagos
    }

}