import com.beust.klaxon.Klaxon
import documentos.*
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Validation
import com.beust.klaxon.KlaxonException

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
        arrayListOf(infoAdicional)
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

    println(factura.generarFacturaXML())


    try {
        val result = Klaxon()
            .parse<Impuesto?>(
                """
                    {
                      "codigo":"2",
                      "codigoPorcentaje":"3610",
                      "tarifa": null,
                      "baseImponible":"22.22",
                      "valor":"22.33"
                    }
                    """
            )
        println(result?.codigo)
        println(result?.codigoPorcentaje)
        println(result?.tarifa)
        println(result?.baseImponible)
        println(result?.valor)
    } catch (e: KlaxonException) {
        println("ERROR")
    }


}