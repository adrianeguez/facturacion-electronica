package documentos.comprobanteretencion

import documentos.ImpuestoBase



class ImpuestoDocSustento(
    baseImponible: String,
    var codImpuestoDocSustento: String,
    var codigoPorcentaje: String,
    var tarifa: String,
    var valorImpuesto: String
) : ImpuestoBase(baseImponible) {


}