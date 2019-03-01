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

class Destinatario {

    @NotNull(message = "identificacionDestinatario $mensajeNulo")
    @NotEmpty(message = "identificacionDestinatario $mensajeVacio")
    @Size(min = 1, max = 20, message = "identificacionDestinatario $mensajeValores de 1 a 20 caracteres")
    var identificacionDestinatario: String

    @NotNull(message = "razonSocialTransportista $mensajeNulo")
    @NotEmpty(message = "razonSocialTransportista $mensajeVacio")
    @Size(min = 1, max = 300, message = "razonSocialTransportista $mensajeValores de 1 a 300 caracteres")
    var razonSocialDestinatario: String

    @NotNull(message = "dirDestinatario $mensajeNulo")
    @NotEmpty(message = "dirDestinatario $mensajeVacio")
    @Size(min = 1, max = 300, message = "dirDestinatario $mensajeValores de 1 a 300 caracteres")
    var dirDestinatario: String


    @NotNull(message = "motivoTraslado $mensajeNulo")
    @NotEmpty(message = "motivoTraslado $mensajeVacio")
    @Size(min = 1, max = 300, message = "motivoTraslado $mensajeValores de 1 a 300 caracteres")
    var motivoTraslado: String

    @Size(min = 1, max = 20, message = "docAduaneroUnico $mensajeValores de 1 a 20 caracteres")
    var docAduaneroUnico: String?

    fun getDocAduaneroUnico(): Optional<String> {
        return Optional.of(docAduaneroUnico!!)
    }

    @Pattern(
        regexp = "^[0-9]{3}\$",
        message = "codEstabDestino $mensajeValores de numeros 0-9"
    )
    var codEstabDestino: String?

    fun getCodEstabDestino(): Optional<String> {
        return Optional.of(docAduaneroUnico!!)
    }

    @Size(min = 1, max = 300, message = "ruta $mensajeValores de 1 a 300 caracteres")
    var ruta: String?

    fun getRuta(): Optional<String> {
        return Optional.of(ruta!!)
    }

    @Pattern(
        regexp = "01|04|05|06|07",
        message = "codDocSustento $mensajeValores de numeros 01|04|05|06|07"
    )
    var codDocSustento: String?

    fun getCodDocSustento(): Optional<String> {
        return Optional.of(codDocSustento!!)
    }

    @Pattern(
        regexp = "^[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\$",
        message = "numDocSustento $mensajeValores de este formato 001-001-000000001"
    )
    var numDocSustento: String?

    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }

    @Pattern(
        regexp = "([0-9]{10,49})",
        message = "numAutDocSustento $mensajeValores de 10 digitos numericos, 37 digitos numeros o 49 digitos numericos"
    )
    var numAutDocSustento: String?

    fun getNumAutDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }

    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        message = "fechaEmisionDocSustento $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaEmisionDocSustento: String?

    fun getFechaEmisionDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }

    @NotNull(message = "infoTributario $mensajeNulo")
    var detalles: ArrayList<DetalleGuiaRemision>

    constructor(
        identificacionDestinatario: String,
        razonSocialDestinatario: String,
        dirDestinatario: String,
        motivoTraslado: String,
        docAduaneroUnico: String?,
        codEstabDestino: String?,
        ruta: String?,
        codDocSustento: String?,
        numDocSustento: String?,
        numAutDocSustento: String?,
        fechaEmisionDocSustento: String?,
        detalles: ArrayList<DetalleGuiaRemision>
    ) {
        this.identificacionDestinatario = identificacionDestinatario
        this.razonSocialDestinatario = GenerarDocumentos.removerCaracteresEspeciales(razonSocialDestinatario)
        this.dirDestinatario = GenerarDocumentos.removerCaracteresEspeciales(dirDestinatario)
        this.motivoTraslado = GenerarDocumentos.removerCaracteresEspeciales(motivoTraslado)
        this.docAduaneroUnico = docAduaneroUnico
        this.codEstabDestino = codEstabDestino
        this.ruta = if (ruta == null) null else GenerarDocumentos.removerCaracteresEspeciales(ruta)
        this.codDocSustento = codDocSustento
        this.numDocSustento = numDocSustento
        this.numAutDocSustento = numAutDocSustento
        this.fechaEmisionDocSustento = fechaEmisionDocSustento
        this.detalles = detalles
    }

}