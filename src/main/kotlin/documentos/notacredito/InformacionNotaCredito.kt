package documentos.notacredito

import documentos.GenerarDocumentos
import documentos.TotalImpuesto
import java.util.*


class InformacionNotaCredito(
    var fechaEmision: String,
    var dirEstablecimiento: String?,
    var tipoIdentificacionComprador: String,
    var razonSocialComprador: String,
    var identificacionComprador: String,
    var contribuyenteEspecial: String?,
    var obligadoContabilidad: String?,
    var rise: String?,
    var codDocModificado: String,
    var numDocModificado: String,
    var fechaEmisionDocSustento: String,
    var totalSinImpuestos: String,
    var valorModificacion: String,
    var moneda: String,
    var totalConImpuesto: ArrayList<TotalImpuesto>,
    var motivo: String
) {


    init {


        this.dirEstablecimiento = if (dirEstablecimiento == null) null else GenerarDocumentos
            .removerCaracteresEspeciales(dirEstablecimiento!!)

        this.razonSocialComprador = GenerarDocumentos.removerCaracteresEspeciales(razonSocialComprador)
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

    fun getRise(): Optional<String> {
        return Optional.of(rise!!)
    }


}