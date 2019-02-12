package documentos

import ec.gob.sri.comprobantes.modelo.InfoTributaria
import ec.gob.sri.comprobantes.modelo.reportes.InformacionAdicional
import utils.mensajeNulo
import javax.validation.constraints.NotNull

class Factura {

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoFactura $mensajeNulo")
    var infoFactura: InformacionFactura

    @NotNull(message = "detalles $mensajeNulo")
    var detalles: Array<Detalle>

    var infoAdicional: Array<InformacionAdicional>?


    constructor(
        infoTributario: InformacionTributaria,
        infoFactura: InformacionFactura,
        detalles: Array<Detalle>,
        infoAdicional: Array<InformacionAdicional>
    ) {
        this.infoTributario = infoTributario
        this.infoFactura = infoFactura
        this.detalles = detalles
        this.infoAdicional = infoAdicional
    }
}