package documentos.guiaremision

import documentos.InformacionTributaria
import utils.mensajeNulo
import javax.validation.Validation
import javax.validation.constraints.NotNull

class GuiaRemision {
    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI

    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var nombreEtiquetaGuiaRemision = "guiaRemision"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionGuiaRemisionXML = "1.0.0" // Codigo Quemado en guía del SRI
    var stringGuiaRemisionXML = ""

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    constructor(
        infoTributario: InformacionTributaria
    ) {
        this.infoTributario = infoTributario
    }





}