import documentos.InformacionTributaria
import javax.validation.Validation


fun main(args: Array<String>) {
    var a = InformacionTributaria(
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
    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.getValidator()
    val violations = validator.validate(a)
    println(a.ambiente)
    for (violation in violations) {
        println(violation.message)
    }
}