package documentos.comprobanteretencion

import documentos.ImpuestoBase
import utils.mensajeNulo
import javax.validation.constraints.NotNull


class ImpuestoDocSustento : ImpuestoBase {

    @NotNull(message = "codImpuestoDocSustento $mensajeNulo")
    var codImpuestoDocSustento: String

    @NotNull(message = "codigoPorcentaje $mensajeNulo")
    var codigoPorcentaje: String

    @NotNull(message = "tarifa $mensajeNulo")
    var tarifa: String

    @NotNull(message = "valorImpuesto $mensajeNulo")
    var valorImpuesto: String

    constructor(
        baseImponible: String,
        codImpuestoDocSustento: String,
        codigoPorcentaje: String,
        tarifa: String,
        valorImpuesto: String
    ) : super(baseImponible) {
        this.codImpuestoDocSustento = codImpuestoDocSustento
        this.codigoPorcentaje = codigoPorcentaje
        this.tarifa = tarifa
        this.valorImpuesto = valorImpuesto
    }
}