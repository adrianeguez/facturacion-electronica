import com.beust.klaxon.Klaxon
import documentos.*
import com.beust.klaxon.KlaxonException
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import firma.XAdESBESSignature
import org.apache.log4j.BasicConfigurator
import java.io.File
import utils.UtilsFacturacionElectronica
import javax.xml.ws.WebServiceException


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
        val directorioXML = "/home/dev-08/Documents/factura/"
        val directorioGuardarXMLFirmados = "/home/dev-08/Documents/factura/firmados"
        val nombreArchivoXML = "factura-prueba-01.xml"
        val nombreArchivoXMLFirmado = "factura-prueba-01-firmado.xml"
        val clave = "LuisPadilla2115"
        val directorioYNombreArchivoRegistroCivilP12 =
            "/home/dev-08/Documents/Github/adrianeguez/netbeans/luis_alfredo_padilla_camuendo.p12"

        val facturaEstructuraString = """
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
        val result = Klaxon()
            .parse<Factura?>(
                """
                    {
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
                            "secuencial": "000000013",
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
            )

        val errores = result?.validar()
        if (errores?.size ?: 0 > 0) {
            println("Error")
            errores?.forEach {
                println(it)
            }
        } else {
            result?.generarFacturaXML()

            val resultado = result?.generarArchivoFacturaXML(
                directorioXML,
                nombreArchivoXML
            )

            if (resultado != null) {
                val archivoFirmado = XAdESBESSignature
                    .firmar(
                        directorioXML + nombreArchivoXML,
                        nombreArchivoXMLFirmado,
                        clave,
                        directorioYNombreArchivoRegistroCivilP12,
                        directorioGuardarXMLFirmados
                    )
                if (archivoFirmado) {
                    println("Se firmo archivos")

                    val directorioYNombreArchivoXMLFirmado =
                        directorioGuardarXMLFirmados + File.separator + nombreArchivoXMLFirmado

                    val datos = UtilsFacturacionElectronica.convertirBytes(directorioYNombreArchivoXMLFirmado)
                    println("Convirtiendo datos")
                    if (datos != null) {
                        val respuestaSolicitud = AutorizarDocumentos.validar(datos)
                        println("Validando Datos")
                        if (respuestaSolicitud != null && (respuestaSolicitud.comprobantes != null ?: false)) {
                            println("Validando si solicitud es RECIBIDA")
                            println(respuestaSolicitud.toString())
                            println(respuestaSolicitud.estado)
                            respuestaSolicitud.comprobantes.comprobante.forEach {
                                it.mensajes.mensaje.forEach {
                                    println(it.tipo)
                                    println(it.identificador)
                                    println(it.informacionAdicional)
                                    println(it.mensaje)
                                }
                            }
                            if (respuestaSolicitud.estado == "RECIBIDA") {
                                try {
                                    println("ESTADO EFECTIVAMENTE ES RECIBIDO")
                                    val respuestaComprobante =
                                        AutorizarDocumentos.autorizarComprobante(factura.infoTributario.claveAcceso!!)
                                    println("Recibimos respuesta")
                                    if (respuestaComprobante != null) {
                                        respuestaComprobante.autorizaciones.autorizacion.forEach {
                                            println("Autorizado")
                                            println(it.comprobante)
                                            println(it.estado)
                                            println(it.fechaAutorizacion)
                                            println(it.mensajes)
                                            println(it.numeroAutorizacion)
                                        }
                                    }else{
                                        println("Errores en respuesta de comprobante")
                                    }


                                } catch (ex: RespuestaAutorizacionException) {
                                    println("Respuesta solicitud NO recibida")
                                }
                            }
                        } else {
                            println("Error en respuesta")
                        }
                    } else {
                        println("Error convirtiendo archivo a bytes")
                    }

                } else {
                    println("Error firmando archivo")
                }
            } else {
                println("Error creando archivo XML")
            }
        }


    } catch (e: KlaxonException) {
        println(e)
        println("ERROR")
    }

}