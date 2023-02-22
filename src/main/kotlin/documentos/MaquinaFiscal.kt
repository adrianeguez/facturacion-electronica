package documentos

import java.util.*

class MaquinaFiscal(
    var marca: String?,
    var modelo: String?,
    var serie: String?
){

     fun getMarca(): Optional<String> {
        return Optional.of(marca!!)
    }

    fun getModelo(): Optional<String> {
        return Optional.of(modelo!!)
    }

    fun getSerie(): Optional<String> {
        return Optional.of(serie!!)
    }

}