package firma

import java.io.File
import java.security.KeyStoreException
import java.util.Enumeration
import java.security.KeyStore
import java.io.IOException
import java.security.NoSuchAlgorithmException
import java.io.FileInputStream
import es.mityc.firmaJava.libreria.xades.DataToSign
import es.mityc.firmaJava.libreria.xades.FirmaXML
import es.mityc.firmaJava.libreria.xades.elementos.xades.ObjectIdentifier
import java.security.UnrecoverableKeyException
import java.security.PrivateKey
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.net.URI

abstract class GenericXMLSignature {
    protected val instanceName = "PKCS12"
    var pathSignature: String? = null

    var passSignature: String? = null

    protected abstract val signatureFileName: String?
    protected abstract val pathOut: String?

    private val keyStore: KeyStore?
        get() {
            var ks: KeyStore? = null
            try {
                ks = KeyStore.getInstance(instanceName)
                ks!!.load(FileInputStream(pathSignature), passSignature!!.toCharArray())
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ks
        }

    protected fun execute() {


        val keyStore = keyStore
        if (keyStore == null) {
            System.err.println("No se pudo obtener keyStore de firma.")
            return
        }
        val alias = getAlias(keyStore)


        var certificate: X509Certificate? = null
        try {
            certificate = keyStore.getCertificate(alias) as X509Certificate
            if (certificate == null) {
                System.err.println("No existe ningún certificado para firmar.")
                return
            }
        } catch (e1: KeyStoreException) {
            e1.printStackTrace()
        }


        var privateKey: PrivateKey? = null
        val tmpKs = keyStore
        try {
            privateKey = tmpKs.getKey(alias, this.passSignature!!.toCharArray()) as PrivateKey
        } catch (e: UnrecoverableKeyException) {
            System.err.println("No existe clave privada para firmar.")
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            System.err.println("No existe clave privada para firmar.")
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            System.err.println("No existe clave privada para firmar.")
            e.printStackTrace()
        }

        val provider = keyStore.provider
        val dataToSign = createDataToSign()
        val firmaXML = FirmaXML()

        var documentoFirmado: Document? = null
        try {
            val res = firmaXML.signFile(certificate, dataToSign, privateKey, provider)
            documentoFirmado = res[0] as Document?
        } catch (ex: Exception) {
            System.err.println("Error firmando documento")
            ex.printStackTrace()
            return
        }

        val filePath = pathOut + File.separator + signatureFileName
        println("Firma guardad en: $filePath")
        saveDocumenteDisk(documentoFirmado, filePath)
    }

    /**
     *
     * Crea el objeto DataToSign que contiene toda la información de la firma
     * que se desea realizar. Todas las implementaciones deberán proporcionar
     * una implementación de este método
     *
     *
     *
     * @return El objeto DataToSign que contiene toda la información de la firma
     * a realizar
     */
    protected abstract fun createDataToSign(
        id: String = "comprobante",
        descripcion: String = "contenido comprobante",
        objectIdentifier: ObjectIdentifier? = null,
        mymeTipe: String = "text/xml",
        encoding: URI? = null,
        parentSignNode:String = "comprobante",
        codificacionXML:String = "UTF-8"
    ): DataToSign


    protected fun getDocument(resource: String): Document? {
        var doc: Document? = null
        val dbf = DocumentBuilderFactory.newInstance()
        dbf.setNamespaceAware(true)
        val file = File(resource)
        try {
            val db = dbf.newDocumentBuilder()

            doc = db.parse(file)
        } catch (ex: ParserConfigurationException) {
            System.err.println("Error al parsear el documento")
            ex.printStackTrace()
            System.exit(-1)
        } catch (ex: SAXException) {
            System.err.println("Error al parsear el documento")
            ex.printStackTrace()
            System.exit(-1)
        } catch (ex: IOException) {
            System.err.println("Error al parsear el documento")
            ex.printStackTrace()
            System.exit(-1)
        } catch (ex: IllegalArgumentException) {
            System.err.println("Error al parsear el documento")
            ex.printStackTrace()
            System.exit(-1)
        }

        return doc
    }

    companion object {


        private fun getAlias(keyStore: KeyStore): String? {
            var alias: String? = null
            val nombres: Enumeration<*>
            try {
                nombres = keyStore.aliases()

                while (nombres.hasMoreElements()) {
                    val tmpAlias = nombres.nextElement() as String
                    if (keyStore.isKeyEntry(tmpAlias))
                        alias = tmpAlias
                }
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

            return alias
        }

        fun saveDocumenteDisk(document: Document?, pathXml: String) {
            try {
                val source = DOMSource(document)
                val result = StreamResult(File(pathXml))

                val transformerFactory = TransformerFactory.newInstance()
                val transformer: Transformer
                transformer = transformerFactory.newTransformer()
                transformer.transform(source, result)
            } catch (e: TransformerConfigurationException) {
                e.printStackTrace()
            } catch (e: TransformerException) {
                e.printStackTrace()
            }

        }
    }
}
