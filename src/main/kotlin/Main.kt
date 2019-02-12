import documentos.InformacionFactura
import documentos.InformacionTributaria
import documentos.TotalImpuesto
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
        impuestos
    )

    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.getValidator()
    val violationsInfoTributaria = validator.validate(infoTributaria)
    val violationsInfoFactura = validator.validate(infoFactura)

    infoFactura.totalConImpuestos.forEach {
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
}