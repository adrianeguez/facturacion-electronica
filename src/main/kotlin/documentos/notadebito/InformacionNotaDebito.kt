package documentos.notadebito

import documentos.GenerarDocumentos
import documentos.Impuesto
import documentos.Pago
import java.util.*

class InformacionNotaDebito(

    var fechaEmision: String,
    var dirEstablecimiento: String?,
    var tipoIdentificacionComprador: String,
    var razonSocialComprador: String,
    var identificacionComprador: String,
    var contribuyenteEspecial: String?,
    var obligadoContabilidad: String?,
    var codDocModificado: String,
    var numDocModificado: String,
    var fechaEmisionDocSustento: String,
    var totalSinImpuestos: String,
    var impuestos: ArrayList<Impuesto>,
    var valorTotal: String,
    var pagos: ArrayList<Pago>
) {

    init {

        this.dirEstablecimiento = if (dirEstablecimiento == null) null else GenerarDocumentos
            .removerCaracteresEspeciales(dirEstablecimiento!!)
        this.razonSocialComprador = GenerarDocumentos
            .removerCaracteresEspeciales(razonSocialComprador)

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

}