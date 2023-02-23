package documentos.notadebito

import documentos.GenerarDocumentos

class Motivo(
    var razon: String,
    var valor: String
) {
    init {
        this.razon = GenerarDocumentos.removerCaracteresEspeciales(razon)
        this.valor = GenerarDocumentos.removerCaracteresEspeciales(valor)
    }

}