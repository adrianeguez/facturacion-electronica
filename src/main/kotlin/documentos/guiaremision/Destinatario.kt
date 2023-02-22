package documentos.guiaremision

import documentos.GenerarDocumentos
import java.util.*


class Destinatario(
    var identificacionDestinatario: String,
    var razonSocialDestinatario: String,
    var dirDestinatario: String,
    var motivoTraslado: String,
    var docAduaneroUnico: String?,
    var codEstabDestino: String?,
    var ruta: String?,
    var codDocSustento: String?,
    var numDocSustento: String?,
    var numAutDocSustento: String?,
    var fechaEmisionDocSustento: String?,
    var detalles: ArrayList<DetalleGuiaRemision>
) {



    init {
        this.razonSocialDestinatario = GenerarDocumentos.removerCaracteresEspeciales(razonSocialDestinatario)
        this.dirDestinatario = GenerarDocumentos.removerCaracteresEspeciales(dirDestinatario)
        this.motivoTraslado = GenerarDocumentos.removerCaracteresEspeciales(motivoTraslado)

        this.ruta = if (ruta == null) null else GenerarDocumentos.removerCaracteresEspeciales(ruta!!)
    }





    fun getDocAduaneroUnico(): Optional<String> {
        return Optional.of(docAduaneroUnico!!)
    }



    fun getCodEstabDestino(): Optional<String> {
        return Optional.of(docAduaneroUnico!!)
    }


    fun getRuta(): Optional<String> {
        return Optional.of(ruta!!)
    }



    fun getCodDocSustento(): Optional<String> {
        return Optional.of(codDocSustento!!)
    }



    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }



    fun getNumAutDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }



    fun getFechaEmisionDocSustento(): Optional<String> {
        return Optional.of(numAutDocSustento!!)
    }

}