package documentos

import utils.*
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE
import javax.validation.constraints.Size

class ReembolsoDetalle {


    @Pattern(
        regexp = "04|05|06|07|08|09",
        flags = [CASE_INSENSITIVE],
        message = "tipoIdentificacionProveedorReembolso $mensajeValores de 04|05|06|07|08|09"
    )
    val tipoIdentificacionProveedorReembolso: String?

    fun getTipoIdentificacionProveedorReembolso(): Optional<String> {
        return Optional.of(tipoIdentificacionProveedorReembolso!!)
    }



    @NotEmpty(message = "identificacionProveedor $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionProveedor $mensajeValores de 1 a 20 caracteres")
    var identificacionProveedor: String?

    fun getIdentificacionProveedor(): Optional<String> {
        return Optional.of(identificacionProveedor!!)
    }


    @NotEmpty(message = "codPaisPagoProveedorReembolso $mensajeVacio")
    @Size(min = 3, max = 3, message = "codPaisPagoProveedorReembolso $mensajeValores de 3 a 3 caracteres")
    var codPaisPagoProveedorReembolso: String?

    fun getCodPaisPagoProveedorReembolso(): Optional<String> {
        return Optional.of(codPaisPagoProveedorReembolso!!)
    }


    @Pattern(
        regexp = "01|02",
        flags = [CASE_INSENSITIVE],
        message = "tipoProveedorReembolso $mensajeValores de 01|02"
    )
    val tipoProveedorReembolso: String?

    fun getTipoProveedorReembolso(): Optional<String> {
        return Optional.of(tipoProveedorReembolso!!)
    }


    @NotNull(message = "codDocReembolso $mensajeNulo")
    @Pattern(
        regexp = "1|2",
        flags = [CASE_INSENSITIVE],
        message = "codDocReembolso $mensajeValores 1|2"
    )
    val codDocReembolso: String?

    fun getCodDocReembolso(): Optional<String> {
        return Optional.of(codDocReembolso!!)
    }

    @NotNull(message = "estabDocReembolso $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{3}\$",
        message = "estabDocReembolso $mensajeValores de numeros 0-9"
    )
    @Size(min = 3, max = 3, message = "estabDocReembolso $mensajeTamano 3 caracteres")
    var estabDocReembolso: String?

    fun getEstabDocReembolso(): Optional<String> {
        return Optional.of(estabDocReembolso!!)
    }



    @NotNull(message = "ptoEmiDocReembolso $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{3}\$",
        message = "ptoEmiDocReembolso $mensajeValores de numeros 0-9"
    )
    @Size(min = 3, max = 3, message = "ptoEmiDocReembolso $mensajeTamano 3 caracteres")
    var ptoEmiDocReembolso: String?

    fun getPtoEmiDocReembolso(): Optional<String> {
        return Optional.of(ptoEmiDocReembolso!!)
    }


    @NotNull(message = "secuencialDocReembolso $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{9}\$",
        message = "secuencialDocReembolso $mensajeValores de numeros 0-9"
    ) // 9 o 10 digitos!
    @Size(min = 9, max = 9, message = "secuencialDocReembolso $mensajeTamano 9 caracteres")
    var secuencialDocReembolso: String?

    fun getSecuencialDocReembolso(): Optional<String> {
        return Optional.of(secuencialDocReembolso!!)
    }


    @NotNull(message = "fechaEmisionDocReembolso $mensajeNulo")
    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [CASE_INSENSITIVE],
        message = "fechaEmisionDocReembolso $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaEmisionDocReembolso: String?

    fun getFechaEmisionDocReembolso(): Optional<String> {
        return Optional.of(fechaEmisionDocReembolso!!)
    }

    @NotNull(message = "numeroautorizacionDocReemb $mensajeNulo")
    @Size(min = 10, max = 49, message = "numeroautorizacionDocReemb $mensajeTamano 3 caracteres")
    var numeroautorizacionDocReemb: String?

    fun getNumeroautorizacionDocReemb(): Optional<String> {
        return Optional.of(numeroautorizacionDocReemb!!)
    }


    var detalleImpuestos: ArrayList<ImpuestoReembolso>?

    fun getDetalleImpuestos(): Optional<ArrayList<ImpuestoReembolso>> {
        return Optional.of(detalleImpuestos!!)
    }



    constructor(
        tipoIdentificacionProveedorReembolso: String?,
        identificacionProveedor: String?,
        tipoProveedorReembolso: String?,
        codPaisPagoProveedorReembolso: String?,
        codDocReembolso: String?,
        estabDocReembolso: String?,
        ptoEmiDocReembolso: String?,
        secuencialDocReembolso: String?,
        fechaEmisionDocReembolso: String?,
        numeroautorizacionDocReemb: String?,
        detalleImpuestos: ArrayList<ImpuestoReembolso>?
    ) {
        this.tipoIdentificacionProveedorReembolso = tipoIdentificacionProveedorReembolso
        this.identificacionProveedor = identificacionProveedor
        this.tipoProveedorReembolso = tipoProveedorReembolso
        this.codPaisPagoProveedorReembolso = codPaisPagoProveedorReembolso
        this.codDocReembolso = codDocReembolso
        this.estabDocReembolso = estabDocReembolso
        this.ptoEmiDocReembolso = ptoEmiDocReembolso
        this.secuencialDocReembolso = secuencialDocReembolso
        this.fechaEmisionDocReembolso = fechaEmisionDocReembolso
        this.numeroautorizacionDocReemb = numeroautorizacionDocReemb
        this.detalleImpuestos = detalleImpuestos
    }
}