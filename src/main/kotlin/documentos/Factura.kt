package documentos

import utils.mensajeNulo
import javax.validation.Validation
import javax.validation.constraints.NotNull

class Factura {


    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.getValidator()

    @NotNull(message = "infoTributario $mensajeNulo")
    var infoTributario: InformacionTributaria

    @NotNull(message = "infoFactura $mensajeNulo")
    var infoFactura: InformacionFactura

    @NotNull(message = "detalles $mensajeNulo")
    var detalles: ArrayList<Detalle>

    var infoAdicional: ArrayList<CampoAdicional>?


    constructor(
        infoTributario: InformacionTributaria,
        infoFactura: InformacionFactura,
        detalles: ArrayList<Detalle>,
        infoAdicional: ArrayList<CampoAdicional>
    ) {
        this.infoTributario = infoTributario
        this.infoFactura = infoFactura
        this.detalles = detalles
        this.infoAdicional = infoAdicional
    }

    fun validar(): ArrayList<String> {
        val errores = arrayListOf<String>()

        val violationsInfoTributaria = validator.validate(this.infoTributario)

        for (violation in violationsInfoTributaria) {
            errores.add(violation.message)
        }

        val violationsInformacionFactura = validator.validate(this.infoFactura)

        for (violation in violationsInformacionFactura) {
            errores.add(violation.message)
        }

        this.infoFactura.totalConImpuestos.forEach {
            val violationsTotalConImpuestos = validator.validate(it)

            for (violation in violationsTotalConImpuestos) {
                errores.add(violation.message)
            }
        }

        this.infoFactura.pagos.forEach {
            val violationsPagos = validator.validate(it)

            for (violation in violationsPagos) {
                errores.add(violation.message)
            }
        }

        this.detalles.forEach {
            val violationsDetalle = validator.validate(it)

            for (violation in violationsDetalle) {
                errores.add(violation.message)
            }

            it.detallesAdicionales?.forEach {
                val violationsDetallesAdicionales = validator.validate(it)

                for (violation in violationsDetallesAdicionales) {
                    errores.add(violation.message)
                }
            }

            it.impuestos?.forEach {
                val violationsImpuestos = validator.validate(it)

                for (violation in violationsImpuestos) {
                    errores.add(violation.message)
                }
            }

        }

        this.infoAdicional?.forEach {
            val violationsInfoAdicional = validator.validate(it)

            for (violation in violationsInfoAdicional) {
                errores.add(violation.message)
            }
        }


        return errores
    }
}