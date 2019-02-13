package firma

import es.mityc.javasign.xml.refs.InternObjectToSign
import es.mityc.javasign.xml.refs.ObjectToSign
import es.mityc.firmaJava.libreria.xades.XAdESSchemas
import es.mityc.firmaJava.libreria.xades.DataToSign
import java.io.File
import es.mityc.firmaJava.libreria.xades.elementos.xades.ObjectIdentifier
import java.net.URI

class XAdESBESSignature : GenericXMLSignature {

    private val fileToSign: String

    override val signatureFileName: String?
        get() = XAdESBESSignature.nameFile

    override val pathOut: String?
        get() = XAdESBESSignature.pathFile

    constructor(fileToSign: String) : super() {
        this.fileToSign = fileToSign
    }

    override fun createDataToSign(
        id: String,
        descripcion: String,
        objectIdentifier: ObjectIdentifier?,
        mymeTipe: String,
        encoding: URI?,
        parentSignNode: String,
        codificacionXML: String
    ): DataToSign {

        val datosAFirmar = DataToSign()

        datosAFirmar.xadesFormat = es.mityc.javasign.EnumFormatoFirma.XAdES_BES
        datosAFirmar.esquema = XAdESSchemas.XAdES_132
        datosAFirmar.xmlEncoding = codificacionXML
        datosAFirmar.isEnveloped = true
        datosAFirmar.addObject(
            ObjectToSign(
                InternObjectToSign(id),
                descripcion,
                objectIdentifier,
                mymeTipe,
                encoding
            )
        )
        datosAFirmar.parentSignNode = parentSignNode

        val docToSign = getDocument(fileToSign)
        datosAFirmar.document = docToSign

        return datosAFirmar
    }

    companion object {

        private var nameFile: String? = null
        private var pathFile: String? = null

        /**
         *
         * Punto de entrada al programa
         *
         *
         *
         * @param args Argumentos del programa
         */
        fun firmar(
            directorioXMLConNombreArchivo: String,
            nombreNuevoArchivoXMLFirmado: String,
            claveFirmaRegistroCivil: String,
            directorioYNombreArchivoRegistroCivilP12: String,
            directorioAGuardarArchivoFirmado: String
        ): Boolean {
            val signature = XAdESBESSignature(directorioXMLConNombreArchivo)
            signature.passSignature = claveFirmaRegistroCivil
            signature.pathSignature = directorioYNombreArchivoRegistroCivilP12
            pathFile = directorioAGuardarArchivoFirmado
            val folder = File(pathFile!!)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            nameFile = nombreNuevoArchivoXMLFirmado
            return signature.execute()
        }
    }

}