import com.beust.klaxon.Klaxon
import documentos.*
import com.beust.klaxon.KlaxonException
import documentos.comprobanteretencion.ComprobanteRetencion
import documentos.factura.Factura
import org.apache.log4j.BasicConfigurator


fun main(args: Array<String>) {

    BasicConfigurator.configure()

    var infoTributaria = InformacionTributaria(
        "1",
        "1",
        "A",
        null,
        "1234567890123",
        null,
        "01",
        "002",
        "001",
        "000000001",
        "Enrique Guerrero Portilla OE1-34 AV. Galo Plaza Lasso"
    )


    val impuestos = arrayListOf(
        TotalImpuesto(
            "2",
            "2",
            null,
            "5.00",
            "68.19",
            "7.58",
            null
        ),
        TotalImpuesto(
            "3",
            "3072",
            null,
            "1.00",
            "64.94",
            "3.25",
            null
        )
    )

    val pagos = arrayListOf(
        Pago(
            "15",
            "12.00",
            "30",
            "dias"
        )
    )

    val infoFactura = InformacionFactura(
        "11/06/1111",
        "Alpallana",
        "5368",
        "SI",
        "04",
        "001-001-000000001",
        "PRUEBAS SERVICIO DERENTAS INTERNAS",
        "1760013210001",
        "salinas y santiago",
        "64.94",
        "5.00",
        impuestos,
        "22.22",
        "11.00",
        "DOLAR",
        pagos,
        "11.00",
        "11.00"
    )

    val impuesto = Impuesto(
        "2",
        "3610",
        "0.01",
        "22.22",
        "22.33"
    )

    val detallesAdicionales = arrayListOf<DetalleAdicional>(
        DetalleAdicional("Marca Chevrolet", "Chevrolet"),
        DetalleAdicional("Modelo", "2012")
    )

    val detalle = Detalle(
        "125BJC-01",
        "1234D56789-A",
        "CAMIONETA 4X4 DIESEL 3.7",
        "10.00",
        "300000.00",
        "5000.00",
        "295000.00",
        detallesAdicionales,
        arrayListOf(impuesto)
    )

    val infoAdicional = CampoAdicional(
        "Codigo Impuesto ISD",
        "4580"
    )

//    val factura = Factura(
//        infoTributaria,
//        infoFactura,
//        arrayListOf(detalle),
//        null
//    )
//
//    val errores = factura.validar()
//
//    errores.forEach {
//        println(it)
//    }
//    if (errores.size > 0) {
//        println("Hay Errores")
//    } else {
//        println("No hay errores")
//    }

    // println(factura.generarFacturaXML())


    // Facura

/*
try {
   val directorioGuardarXML = "/home/dev-02/Documents/ComprobanteRetencion"
   val directorioGuardarXMLFirmados = "/home/dev-02/Documents/ComprobanteRetencion/ComprobanteRetencionFirmado"
   val nombreArchivoXML = "factura-prueba-01.xml"
   val nombreArchivoXMLFirmado = "factura-prueba-01-firmado.xml"
   val clave = "LuisPadilla2115"
   val directorioYNombreArchivoRegistroCivilP12 =
       "/home/dev-02/Documents/Github/adrianeguez/facturacion-electronica/documentacion/luis_alfredo_padilla_camuendo.p12"

   val facturaEstructuraString = """
               {
                   "directorioGuardarXML":"${directorioGuardarXML}",
                   "directorioGuardarXMLFirmados":"${directorioGuardarXMLFirmados}",
                   "nombreArchivoXML":"${nombreArchivoXML}",
                   "nombreArchivoXMLFirmado":"${nombreArchivoXMLFirmado}",
                   "clave":"${clave}",
                   "directorioYNombreArchivoRegistroCivilP12":"${directorioYNombreArchivoRegistroCivilP12}",
                   "debug": true
                   "infoTributario": {
                       "ambiente": "1",
                       "tipoEmision": "1",
                       "razonSocial": "PADILLA CAMUENDO LUIS ALFREDO",
                       "nombreComercial": "COMERCIAL BRENDA",
                       "ruc": "1710361658001",
                       "claveAcceso": null,
                       "codDoc": "01",
                       "estab": "001",
                       "ptoEmision": "001",
                       "secuencial": "000000016",
                       "dirMatriz": "PICHINCHA / QUITO / QUITO/ LLANO CHICO"
                   },
                   "infoFactura": {
                       "fechaEmision": "13/02/2019",
                       "dirEstablecimiento": "PICHINCHA / CAYAMBE / CAYAMBE / ROCAFUERTE N0-45 Y LIBERTAD",
                       "contribuyenteEspecial": "0047",
                       "obligadoContabilidad": "SI",
                       "tipoIdentificacionComprador": "04",
                       "guiaRemision": null,
                       "razonSocialComprador": "BAZAR Y PAPELERIA MEXICO",
                       "identificacionComprador": "1800095612001",
                       "direccionComprador": "CHAMBO 1067 Y GUAYLLABAMBA",
                       "totalSinImpuestos": "161.79",
                       "totalDescuento": "20.48",
                       "totalConImpuestos": [
                           {
                               "codigo": "2",
                               "codigoPorcentaje": "2",
                               "descuentoAdicional" : null,
                               "baseImponible": "161.79",
                               "tarifa": null,
                               "valor": "19.41",
                               "valorDevolucionIva": null
                           }
                       ],
                       "propina": "0.00",
                       "importeTotal": "181.20",
                       "moneda": "DOLAR",
                       "pagos": [
                           {
                               "formaPago": "20",
                               "total": "181.20",
                               "plazo": "30.00",
                               "unidadTiempo": "dias"
                           }
                       ],
                       "valorRetIva": "0.00",
                       "valorRetRenta": "0.00"
                   },
                   "detalles": [
                       {
                           "codigoPrincipal": "679_73",
                           "codigoAuxiliar": null,
                           "descripcion": "ABRIGO IMPERMIABLE PESADO",
                           "cantidad": "8.00",
                           "precioUnitario": "7.75",
                           "descuento": "10.46",
                           "precioTotalSinImpuesto": "51.54",
                           "detallesAdicionales":null,
                           "impuestos": [
                               {
                                   "codigo": "2",
                                   "codigoPorcentaje": "2",
                                   "tarifa": "12.00",
                                   "baseImponible": "51.54",
                                   "valor": "6.18"
                               }
                           ]
                       },
                       {
                           "codigoPrincipal": "A25-32",
                           "codigoAuxiliar": null,
                           "descripcion": "ABRAZADERAS 25/32",
                           "cantidad": "17.00",
                           "precioUnitario": "2.11",
                           "descuento": "5.80",
                           "precioTotalSinImpuesto": "30.07",
                           "detallesAdicionales":null,
                           "impuestos": [
                               {
                                   "codigo": "2",
                                   "codigoPorcentaje": "2",
                                   "tarifa": "12.00",
                                   "baseImponible": "30.07",
                                   "valor": "3.61"
                               }
                           ]
                       }
                   ],
                   "infoAdicional": [
                       {
                           "nombre": "DIRECCION",
                           "valor": "LLANO CHICO"
                       },
                       {
                           "nombre": "E-MAIL",
                           "valor": "asetemp@hotmail.com"
                       },
                       {
                           "nombre": "APELLIDO",
                           "valor": "HINOJOSA"
                       },
                       {
                           "nombre": "NOMBRE",
                           "valor": "WASHINGTON"
                       },
                       {
                           "nombre": "NOMBRECOMERCIAL",
                           "valor": "BESTSYSTEM"
                       },
                       {
                           "nombre": "CIUDAD",
                           "valor": "QUITO"
                       },
                       {
                           "nombre": "TELEFONO",
                           "valor": "0993530018"
                       },
                       {
                           "nombre": "CELULAR",
                           "valor": "0987654321"
                       },
                       {
                           "nombre": "PLAZO",
                           "valor": "1.00"
                       },
                       {
                           "nombre": "DIAS",
                           "valor": "30.00"
                       },
                       {
                           "nombre": "TARIFAIMP",
                           "valor": "12.00"
                       }
                   ]
               }
               """

   val result = Klaxon()
       .parse<Factura?>(
           facturaEstructuraString
       )

   val resultadoEnvioFactura = result?.enviarFactura(facturaEstructuraString)

   println(resultadoEnvioFactura)


} catch (e: KlaxonException) {
   println(e)
   println("ERROR")
}
*/

// Comprobante de Retencion


    try {
        val directorioGuardarXML = "/home/server/Documents/Comprobante Retencion"
        val directorioGuardarXMLFirmados = "/home/server/Documents/Comprobante Retencion"
        val nombreArchivoXML = "comprobante-retencion-01.xml"
        val nombreArchivoXMLFirmado = "comprobante-retencion-01-firmado.xml"
        val clave = "LuisPadilla2115"
        val directorioYNombreArchivoRegistroCivilP12 =
            "/home/server/Documents/Github/facturacion-electronica/documentacion/luis_alfredo_padilla_camuendo.p12"

        val comprobanteRetencionEstructuraString = """
               {
                   "directorioGuardarXML":"${directorioGuardarXML}",
                   "directorioGuardarXMLFirmados":"${directorioGuardarXMLFirmados}",
                   "nombreArchivoXML":"${nombreArchivoXML}",
                   "nombreArchivoXMLFirmado":"${nombreArchivoXMLFirmado}",
                   "clave":"${clave}",
                   "directorioYNombreArchivoRegistroCivilP12":"${directorioYNombreArchivoRegistroCivilP12}",
                   "debug": true
                   "infoTributario": {
                       "ambiente": "1",
                       "tipoEmision": "1",
                       "razonSocial": "PADILLA CAMUENDO LUIS ALFREDO",
                       "nombreComercial": "COMERCIAL BRENDA",
                       "ruc": "1710361658001",
                       "claveAcceso": null,
                       "codDoc": "01",
                       "estab": "001",
                       "ptoEmision": "001",
                       "secuencial": "000000016",
                       "dirMatriz": "PICHINCHA / QUITO / QUITO/ LLANO CHICO"
                   },
                   "infoCompRetencion": {
                       "fechaEmision": "10/12/2018",
                       "dirEstablecimiento": "GRAL. VEINTIMILLA E8-30 Y AV. 6 DE DICIEMBRE",
                       "obligadoContabilidad": "SI",
                       "contribuyenteEspecial":null,
                       "tipoIdentificacionSujetoRetenido": "04",
                       "razonSocialSujetoRetenido": "EGUEZ VASQUEZ FRANCISCO IVAN",
                       "identificacionSujetoRetenido": "1707075493001",
                       "periodoFiscal": "02/2019"
                   },
                   "impuestos": [
                       {
                           "codigo": "2",
                           "codigoRetencion": "2",
                           "baseImponible": "120.00",
                           "porcentajeRetener": "70.00",
                           "valorRetenido": "84.00"
                           "codDocSustento": "01"
                           "numDocSustento": "003005021421211"
                           "fechaEmisionDocSustento": "14/02/2019"
                       },
                       {
                           "codigo": "1",
                           "codigoRetencion": "304",
                           "baseImponible": "1000.00",
                           "porcentajeRetener": "8.00",
                           "valorRetenido": "80.00"
                           "codDocSustento": "01"
                           "numDocSustento": "003005021421211"
                           "fechaEmisionDocSustento": "14/02/2019"
                       }
                   ],
                   "infoAdicional": [
                       {
                           "nombre": "ConceptoRetencion",
                           "valor": "MANTENIMIENTO PREVENTIVO DEL CIR"
                       },
                       {
                           "nombre": "Direccion",
                           "valor": "LEONARDO MURIALDO Y HUACAMAYOS"
                       },
                       {
                           "nombre": "Telefono",
                           "valor": "2071425 0998786306"
                       }
                   ]
               }
               """

        val result = Klaxon()
            .parse<ComprobanteRetencion?>(
                comprobanteRetencionEstructuraString
            )

        val resultadoEnvioComprobanteRetencion =
            result?.enviarComprobanteRetencion(comprobanteRetencionEstructuraString)

        println(resultadoEnvioComprobanteRetencion)


    } catch (e: KlaxonException) {
        println(e)
        println("ERROR")
    }


}