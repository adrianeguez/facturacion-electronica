package documentos

class DetalleAdicional(
    var nombre: String,
    var valor: String
) {

  init{
        this.nombre = GenerarDocumentos.removerCaracteresEspeciales(nombre)
        this.valor = GenerarDocumentos.removerCaracteresEspeciales(valor)
    }
}