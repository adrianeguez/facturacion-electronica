package documentos.comprobanteretencion

import documentos.Impuesto
import documentos.InformacionTributaria
import utils.mensajeNulo
import javax.validation.Validation
import javax.validation.constraints.NotNull

class ComprobanteRetencion {

    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()


    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var nombreEtiquetaFactura = "comprobanteRetencion"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionFacturaXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringFacturaXML = ""

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoCompRetencion $mensajeNulo")
    var infoCompRetencion: InformacionComprobanteRetencion

    @NotNull(message = "impuestos $mensajeNulo")
    var impuestos: ArrayList<Impuesto>

    constructor(
        infoTributario: InformacionTributaria,
        infoCompRetencion: InformacionComprobanteRetencion,
        impuestos: ArrayList<Impuesto>
        ) {
        this.infoTributario = infoTributario
        this.infoCompRetencion = infoCompRetencion
        this.impuestos =impuestos
    }

}