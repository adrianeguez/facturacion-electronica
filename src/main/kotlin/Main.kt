import documentos.InformacionFactura
import documentos.InformacionTributaria
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

    val infoFactura = InformacionFactura(
        "11/06/1111"
    )
    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.getValidator()
    val violationsInfoTributaria = validator.validate(infoTributaria)
    val violationsInfoFactura = validator.validate(infoFactura)
    for (violation in violationsInfoTributaria) {
        println(violation.message)
    }
    for (violation in violationsInfoFactura) {
        println(violation.message)
    }
}