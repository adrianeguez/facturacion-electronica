package documentos.comprobanteretencion

import utils.mensajeNulo
import javax.validation.constraints.NotNull

open class DetalleImpuestoRetencion {

    @NotNull(message = "codigo $mensajeNulo")
    var codigo: String

    @NotNull(message = "codigoPorcentaje $mensajeNulo")
    var codigoPorcentaje: String

    @NotNull(message = "tarifa $mensajeNulo")
    var tarifa: String

    @NotNull(message = "baseImponibleReembolso $mensajeNulo")
    var baseImponibleReembolso: String

    @NotNull(message = "impuestoReembolso $mensajeNulo")
    var impuestoReembolso: String

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        tarifa: String,
        baseImponibleReembolso: String,
        impuestoReembolso: String
    ) {
        this.codigo = codigo
        this.codigoPorcentaje = codigoPorcentaje
        this.tarifa = tarifa
        this.baseImponibleReembolso = baseImponibleReembolso
        this.impuestoReembolso = impuestoReembolso
    }
}