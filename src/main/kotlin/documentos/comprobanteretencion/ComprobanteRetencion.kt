package documentos.comprobanteretencion

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

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoCompRetencion: InformacionComprobanteRetencion

    constructor(
        infoTributario: InformacionTributaria,
        infoCompRetencion: InformacionComprobanteRetencion
        ) {
        this.infoTributario = infoTributario
        this.infoCompRetencion = infoCompRetencion
    }

}