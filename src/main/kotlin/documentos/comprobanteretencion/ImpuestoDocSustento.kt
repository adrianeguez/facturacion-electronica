package documentos.comprobanteretencion

import documentos.ImpuestoBase



class ImpuestoDocSustento(
    var codImpuestoDocSustento: String,
    var codigoPorcentaje: String,
    baseImponible: String,
    var tarifa: String,
    var valorImpuesto: String
) : ImpuestoBase(baseImponible) {


}