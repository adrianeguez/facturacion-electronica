package documentos.comprobanteretencion

import documentos.GenerarDocumentos
import java.util.*

class InformacionComprobanteRetencion(
    var fechaEmision: String,
    var dirEstablecimiento: String?,
    var contribuyenteEspecial: String?,
    var obligadoContabilidad: String?,
    var tipoIdentificacionSujetoRetenido: String?,
    var tipoSujetoRetenido: String?,
    var parteRel: String?,
    var razonSocialSujetoRetenido: String?,
    var identificacionSujetoRetenido: String,
    var periodoFiscal: String
){
    init {

        this.dirEstablecimiento =
            if (dirEstablecimiento == null) null else GenerarDocumentos.removerCaracteresEspeciales(dirEstablecimiento!!)
        this.contribuyenteEspecial =
            if (contribuyenteEspecial == null) null else GenerarDocumentos.removerCaracteresEspeciales(
                contribuyenteEspecial!!
            )
        this.razonSocialSujetoRetenido = if(razonSocialSujetoRetenido != null)
            GenerarDocumentos.removerCaracteresEspeciales(razonSocialSujetoRetenido!!) else null
    }

    fun getDirEstablecimiento(): Optional<String> {
        return Optional.of(dirEstablecimiento!!)
    }

    fun getContribuyenteEspecial(): Optional<String> {
        return Optional.of(contribuyenteEspecial!!)
    }

    fun getObligadoContabilidad(): Optional<String> {
        return Optional.of(obligadoContabilidad!!)
    }

    fun getTipoIdentificacionSujetoRetenido(): Optional<String> {
        return Optional.of(tipoIdentificacionSujetoRetenido!!)
    }

    fun getTipoSujetoRetenido(): Optional<String> {
        return Optional.of(tipoSujetoRetenido!!)
    }

    fun getParteRel(): Optional<String> {
        return Optional.of(parteRel!!)
    }

    fun getRazonSocialSujetoRetenido(): Optional<String> {
        return Optional.of(razonSocialSujetoRetenido!!)
    }


}