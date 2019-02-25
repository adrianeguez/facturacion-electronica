package documentos

open class Impuesto : ImpuestoBase {

    constructor(
        codigo: String,
        codigoPorcentaje: String,
        baseImponible: String,
        valor: String,
        tarifa: String?
    ) : super(codigo, codigoPorcentaje, baseImponible, valor, tarifa) {
    }
}