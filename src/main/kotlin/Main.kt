import com.beust.klaxon.Klaxon
import documentos.*
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Validation
import com.beust.klaxon.KlaxonException
import java.io.File

fun main(args: Array<String>) {

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
            "5.00",
            "68.19",
            "7.58"
        ),
        TotalImpuesto(
            "3",
            "3072",
            "1.00",
            "64.94",
            "3.25"
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

    val factura = Factura(
        infoTributaria,
        infoFactura,
        arrayListOf(detalle),
        null
    )

    val errores = factura.validar()

    errores.forEach {
        println(it)
    }
    if (errores.size > 0) {
        println("Hay Errores")
    } else {
        println("No hay errores")
    }

    // println(factura.generarFacturaXML())


    try {
        val result = Klaxon()
            .parse<Factura?>(
                """
                    {
                        "infoTributario": {
                            "ambiente": "1",
                            "tipoEmision": "1",
                            "razonSocial": "Distribuidora de Suministros Nacional S.A.",
                            "nombreComercial": null,
                            "ruc": "1234567890123",
                            "claveAcceso": null,
                            "codDoc": "01",
                            "estab": "002",
                            "ptoEmision": "001",
                            "secuencial": "000000001",
                            "dirMatriz": "Enrique Guerrero Portilla OE1-34 AV. Galo Plaza Lasso"
                        },
                        "infoFactura": {
                            "fechaEmision": "21/10/2012",
                            "dirEstablecimiento": "Sebastián Moreno S/N Francisco García",
                            "contribuyenteEspecial": "5368",
                            "obligadoContabilidad": "SI",
                            "tipoIdentificacionComprador": "04",
                            "guiaRemision": "001-001-000000001",
                            "razonSocialComprador": "PRUEBAS SERVICIO DE RENTAS INTERNAS",
                            "identificacionComprador": "1713328506001",
                            "direccionComprador": "salinas y santiago",
                            "totalSinImpuestos": "295000.00",
                            "totalDescuento": "5005.00",
                            "totalConImpuestos": [
                                {
                                    "codigo": "3",
                                    "codigoPorcentaje": "3072",
                                    "descuentoAdicional": null,
                                    "baseImponible": "295000.00",
                                    "valor": "14750.00"
                                },
                                {
                                    "codigo": "2",
                                    "codigoPorcentaje": "2",
                                    "descuentoAdicional": "5.00",
                                    "baseImponible": "309750.00",
                                    "valor": "37169.40"
                                },
                                {
                                    "codigo": "5",
                                    "codigoPorcentaje": "3053",
                                    "descuentoAdicional": null,
                                    "baseImponible": "12000.00",
                                    "valor": "240.00"
                                }
                            ],
                            "propina": "0.00",
                            "importeTotal": "347159.40",
                            "moneda": "DOLAR",
                            "pagos": [
                                {
                                    "formaPago": "01",
                                    "total": "347159.40",
                                    "plazo": "30",
                                    "unidadTiempo": "dias"
                                }
                            ],
                            "valorRetIva": "10620.00",
                            "valorRetRenta": "2950.00"
                        },
                        "detalles": [
                            {
                                "codigoPrincipal": "125BJC-01",
                                "codigoAuxiliar": "1234D56789-A",
                                "descripcion": "CAMIONETA 4X4 DIESEL 3.7",
                                "cantidad": "10.00",
                                "precioUnitario": "300000.00",
                                "descuento": "5000.00",
                                "precioTotalSinImpuesto": "295000.00",
                                "detallesAdicionales": [
                                    {
                                        "nombre": "Marca Chevrolet",
                                        "valor": "Chevrolet"
                                    },
                                    {
                                        "nombre": "Modelo",
                                        "valor": "2012"
                                    },
                                    {
                                        "nombre": "Chasis",
                                        "valor": "8LDETA03V20003289"
                                    }
                                ],
                                "impuestos": [
                                    {
                                        "codigo": "2",
                                        "codigoPorcentaje": "2",
                                        "tarifa": "12.00",
                                        "baseImponible": "68.19",
                                        "valor": "8.18"
                                    },
                                    {
                                        "codigo": "3",
                                        "codigoPorcentaje": "3072",
                                        "tarifa": "5.00",
                                        "baseImponible": "64.94",
                                        "valor": "3.25"
                                    },
                                    {
                                        "codigo": "5",
                                        "codigoPorcentaje": "3630",
                                        "tarifa": "0.02",
                                        "baseImponible": "12000.00",
                                        "valor": "240.00"
                                    }
                                ]
                            }
                        ],
                        "infoAdicional": [
                            {
                                "nombre": "Codigo Impuesto ISD",
                                "valor": "4580"
                            },
                            {
                                "nombre": "Impuesto ISD",
                                "valor": "15.42x"
                            }
                        ]
                    }
                    """
            )
        val errores = result?.validar()
        if (errores?.size ?: 0 > 0) {
            println("Error")
            errores?.forEach {
                println(it)
            }
        } else {
            result?.generarFacturaXML()
            result?.generarArchivoFacturaXML("/home/dev-08/Documents/factura/", "factura-prueba-01.xml", null)
        }
    } catch (e: KlaxonException) {
        println(e)
        println("ERROR")
    }

}