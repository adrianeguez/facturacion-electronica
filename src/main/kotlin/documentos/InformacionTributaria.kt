package documentos

import java.util.Optional



class InformacionTributaria(
    var ambiente: String,
    var tipoEmision: String,
    var razonSocial: String,
    var nombreComercial: String?,
    var ruc: String,
    var claveAcceso: String?,
    var codDoc: String,
    var estab: String,
    var ptoEmision: String,
    var secuencial: String,
    var dirMatriz: String

) {
    init {
        this.razonSocial = GenerarDocumentos.removerCaracteresEspeciales(razonSocial)
        this.nombreComercial =
            if (nombreComercial == null) null else GenerarDocumentos.removerCaracteresEspeciales(nombreComercial!!)

        this.dirMatriz = GenerarDocumentos.removerCaracteresEspeciales(dirMatriz)
   }
    fun getNombreComercial(): Optional<String> {
        return Optional.of(nombreComercial!!)
    }

    fun getClaveAcceso(): Optional<String> {
        return Optional.of(claveAcceso!!)
    }

}

