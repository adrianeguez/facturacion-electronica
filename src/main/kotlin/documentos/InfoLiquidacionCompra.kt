package documentos

import utils.*
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE
import javax.validation.constraints.Size

class InfoLiquidacionCompra {

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
        message = "tipoIdentificacionProveedor $mensajeValores de 04|05|06|07|08|09"
    )
    var tipoIdentificacionProveedor: String?


    fun getTipoIdentificacionProveedor(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedor!!)
    }

    @NotNull(message = "razonSocialComprador $mensajeNulo")
    @NotEmpty(message = "razonSocialComprador $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialComprador $mensajeValores de 1 a 300 caracteres")
    var razonSocialProveedor: String

    @NotEmpty(message = "identificacionComprador $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionComprador $mensajeValores de 1 a 20 caracteres")
    var identificacionProveedor: String

    @NotEmpty(message = "direccionComprador $mensajeVacio")
    @Size(min = 1, max = 300, message = "direccionComprador $mensajeValores de 1 a 300 caracteres")
    var direccionProveedor: String?

    fun getDireccionComprador(): Optional<String> {
        return Optional.of(direccionProveedor!!)
    }

    @NotNull(message = "totalSinImpuestos $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalSinImpuestos $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalSinImpuestos: String?

    fun getTotalSinImpuestos(): Optional<String> {
        return Optional.of(totalSinImpuestos!!)
    }

    @NotNull(message = "totalDescuento $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalDescuento $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalDescuento: String?

    fun getTotalDescuento(): Optional<String> {
        return Optional.of(totalDescuento!!)
    }


    @NotEmpty(message = "codDocReembolso $mensajeVacio")
    @Size(min = 1, max = 2, message = "codDocReembolso $mensajeValores de 1 a 2 caracteres")
    var codDocReembolso: String?

    fun getCodDocReembolso(): Optional<String> {
        return Optional.of(codDocReembolso!!)
    }

    @NotNull(message = "totalComprobantesReembolso $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalComprobantesReembolso $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalComprobantesReembolso: String?

    fun getTotalComprobantesReembolso(): Optional<String> {
        return Optional.of(totalComprobantesReembolso!!)
    }

    @NotNull(message = "totalBaseImponibleReembolso $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalBaseImponibleReembolso $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalBaseImponibleReembolso: String?

    fun getTotalBaseImponibleReembolso(): Optional<String> {
        return Optional.of(totalBaseImponibleReembolso!!)
    }

    @NotNull(message = "totalImpuestoReembolso $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "totalImpuestoReembolso $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var totalImpuestoReembolso: String?

    fun getTotalImpuestoReembolso(): Optional<String> {
        return Optional.of(totalImpuestoReembolso!!)
    }

    @NotNull(message = "totalDescuento $mensajeNulo")
    var totalConImpuestos: ArrayList<TotalImpuesto>

    @NotNull(message = "importeTotal $mensajeNulo")
    @Pattern(
        regexp = expresionRegularMoneda,
        message = "importeTotal $mensajeValores de 1 a 14 enteros y desde 1 hasta 6 decimales separados por punto"
    )
    var importeTotal: String

    @NotNull(message = "moneda $mensajeNulo")
    @Size(min = 1, max = 15, message = "moneda $mensajeValores de 1 a 300 caracteres")
    var moneda: String

    @NotNull(message = "pagos $mensajeNulo")
    var pagos: ArrayList<Pago>

    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        tipoIdentificacionProveedor: String?,
        razonSocialProveedor: String,
        identificacionProveedor: String,
        direccionProveedor: String?,
        totalSinImpuestos: String?,
        totalDescuento: String?,
        codDocReembolso: String?,
        totalComprobantesReembolso: String?,
        totalBaseImponibleReembolso: String?,
        totalImpuestoReembolso: String?,
        totalConImpuestos: ArrayList<TotalImpuesto>,
        importeTotal: String,
        moneda: String,
        pagos: ArrayList<Pago>
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento =
                if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento)
        this.contribuyenteEspecial =
                if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(contribuyenteEspecial)
        this.obligadoContabilidad = obligadoContabilidad
        this.tipoIdentificacionProveedor = tipoIdentificacionProveedor
        this.razonSocialProveedor = GenerarDocumentos.removerCaracteresEspeciales(razonSocialProveedor)
        this.identificacionProveedor = GenerarDocumentos.removerCaracteresEspeciales(identificacionProveedor)
        this.direccionProveedor = if(direccionProveedor != null) GenerarDocumentos.removerCaracteresEspeciales(direccionProveedor) else direccionProveedor
        this.totalSinImpuestos = totalSinImpuestos
        this.totalDescuento = totalDescuento
        this.codDocReembolso = codDocReembolso
        this.totalComprobantesReembolso = totalComprobantesReembolso
        this.totalBaseImponibleReembolso = totalBaseImponibleReembolso
        this.totalImpuestoReembolso = totalImpuestoReembolso
        this.totalConImpuestos = totalConImpuestos
        this.importeTotal = importeTotal
        this.moneda = moneda
        this.pagos = pagos
    }
}