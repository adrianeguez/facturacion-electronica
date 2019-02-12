import documentos.*
import javax.validation.Validation


fun main(args: Array<String>) {

    var infoTributaria = InformacionTributaria(
        "1",
        "1",
        "A",
        null,
        "1234567890123",
        "2110201101179214673900110020010000000011234567813",
        "01",
        "002",
        "001",
        "000000001",
        "Enrique Guerrero Portilla OE1-34 AV. Galo Plaza Lasso"
    )


    val impuestos = arrayOf(
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
            null,
            "64.94",
            "3.25"
        )
    )

    val pagos = arrayOf(
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

    val detalle = Detalle(
        "125BJC-01",
        "1234D56789-A",
        "CAMIONETA 4X4 DIESEL 3.7",
        "10.00",
        "300000.00",
        "5000.00",
        "295000.00",
        null
    )

    val impuesto = Impuesto(
        "2",
        "3610",
        "0.01",
        "22.22",
        "22.33"
    )

    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.getValidator()

    val violationsInfoTributaria = validator.validate(infoTributaria)
    val violationsInfoFactura = validator.validate(infoFactura)
    val violationsDetalle = validator.validate(detalle)
    val violationsImpuesto = validator.validate(impuesto)


    infoFactura.totalConImpuestos.forEach {
        val violaciones = validator.validate(it)
        for (violation in violaciones) {
            println(violation.message)
        }
    }

    infoFactura.pagos.forEach {
        val violaciones = validator.validate(it)
        for (violation in violaciones) {
            println(violation.message)
        }
    }

    for (violation in violationsInfoTributaria) {
        println(violation.message)
    }

    for (violation in violationsInfoFactura) {
        println(violation.message)
    }

    for (violation in violationsDetalle) {
        println(violation.message)
    }

    for (violation in violationsImpuesto) {
        println(violation.message)
    }


}