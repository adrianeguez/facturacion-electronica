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
        message = "tipoIdentificacionComprador $mensajeValores de 04|05|06|07|08|09"
    )
    var tipoIdentificacionComprador: String


    @Pattern(
        regexp = "^[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\$",
        flags = [CASE_INSENSITIVE],
        message = "tipoIdentificacionComprador $mensajeValores de este formato 001-001-000000001"
    )
    val guiaRemision: String?

    fun getGuiaRemision(): Optional<String> {
        return Optional.of(guiaRemision!!)
    }

    @NotNull(message = "razonSocialComprador $mensajeNulo")
    @NotEmpty(message = "razonSocialComprador $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialComprador $mensajeValores de 1 a 300 caracteres")
    var razonSocialComprador: String

    @NotEmpty(message = "identificacionComprador $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionComprador $mensajeValores de 1 a 20 caracteres")
    val identificacionComprador: String

    @NotEmpty(message = "direccionComprador $mensajeVacio")
    @Size(min = 1, max = 20, message = "direccionComprador $mensajeValores de 1 a 300 caracteres")
    val direccionComprador: String

    fun getDireccionComprador(): Optional<String> {
        return Optional.of(direccionComprador!!)
    }

    @NotNull(message = "totalSinImpuestos $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "totalSinImpuestos $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var totalSinImpuestos: String

    @NotNull(message = "totalDescuento $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "totalDescuento $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var totalDescuento: String

    @NotNull(message = "totalDescuento $mensajeNulo")
    var totalConImpuestos: Array<TotalImpuesto>

    @NotNull(message = "propina $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "propina $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var propina: String

    @NotNull(message = "importeTotal $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "importeTotal $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var importeTotal: String

    @Pattern(
        regexp = "DOLAR",
        message = "moneda $mensajeValores de DOLAR"
    )
    var moneda: String

    @NotNull(message = "pagos $mensajeNulo")
    var pagos: Array<Pago>


    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "valorRetIva $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var valorRetIva: String?

    fun getValorRetIva(): Optional<String> {
        return Optional.of(valorRetIva!!)
    }


    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{1,2})?\$",
        message = "valorRetRenta $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var valorRetRenta: String?

    fun getValorRetRenta(): Optional<String> {
        return Optional.of(valorRetRenta!!)
    }

    constructor(
        fechaEmision: String,
        dirEstablecimiento: String?,
        contribuyenteEspecial: String?,
        obligadoContabilidad: String?,
        tipoIdentificacionComprador: String,
        guiaRemision: String?,
        razonSocialComprador: String,
        identificacionComprador: String,
        direccionComprador: String,
        totalSinImpuestos: String,
        totalDescuento: String,
        totalConImpuestos: Array<TotalImpuesto>,
        propina: String,
        importeTotal: String,
        moneda: String,
        pagos: Array<Pago>,
        valorRetIva: String?,
        valorRetRenta: String?
    ) {
        this.fechaEmision = fechaEmision
        this.dirEstablecimiento = dirEstablecimiento
        this.contribuyenteEspecial = contribuyenteEspecial
        this.obligadoContabilidad = obligadoContabilidad
        this.tipoIdentificacionComprador = tipoIdentificacionComprador
        this.guiaRemision = guiaRemision
        this.razonSocialComprador = razonSocialComprador
        this.identificacionComprador = identificacionComprador
        this.direccionComprador = direccionComprador
        this.totalSinImpuestos = totalSinImpuestos
        this.totalDescuento = totalDescuento
        this.totalConImpuestos = totalConImpuestos
        this.propina = propina
        this.importeTotal = importeTotal
        this.moneda = moneda
        this.pagos = pagos
        this.valorRetIva = valorRetIva
        this.valorRetRenta = valorRetRenta

    }
}