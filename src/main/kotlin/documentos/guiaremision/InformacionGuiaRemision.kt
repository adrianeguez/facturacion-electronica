package documentos.guiaremision

import documentos.GenerarDocumentos
import java.util.*

class InformacionGuiaRemision(
    var dirEstablecimiento: String?,
    var dirPartida: String,
    var razonSocialTransportista: String,
    var tipoIdentificacionTransportista: String,
    var rucTransportista: String,
    var rise: String?,
    var obligadoContabilidad: String?,
    var contribuyenteEspecial: String?,
    var fechaIniTransporte: String,
    var fechaFinTransporte: String,
    var placa: String
) {

    init {
        this.dirEstablecimiento =
            if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento!!)

        this.dirPartida = GenerarDocumentos.removerCaracteresEspeciales(dirPartida)

        this.razonSocialTransportista = GenerarDocumentos.removerCaracteresEspeciales(razonSocialTransportista)


        this.rise = if (rise == null) null else GenerarDocumentos.removerCaracteresEspeciales(rise!!)

        this.contribuyenteEspecial =
            if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(contribuyenteEspecial!!)
    }


    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }

    fun getRise(): Optional<String> {
        return Optional.of(rise!!)
    }

    fun getObligadoContabilidad(): Optional<String> {
        return Optional.of(obligadoContabilidad!!)
    }


    fun getContribuyenteEspecial(): Optional<String> {
        return Optional.of(contribuyenteEspecial!!)
    }

}