package documentos.comprobanteretencion

import documentos.ImpuestoBase
import utils.mensajeNulo
import utils.mensajeValores
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern


class ImpuestoRetencion : ImpuestoBase {

    @NotNull(message = "codigo $mensajeNulo")
    @Pattern(
        regexp = "9|10|1|11|2|3",
        message = "codigo $mensajeValores de 9|10|1|11|2|3"
    )
    var codigo: String


    @NotNull(message = "codigoRetencion $mensajeNulo")
    @Pattern(
        regexp = "9|10|1|11|2|3|7|8|4580|303|304|304A|304B|304C|304D|304E|307|308|309|310|311|" +
                "312|312A|314A|314B|314C|314D|319|320|322|323|323A|323B1|323E|323E2|323F|323G|" +
                "323H|323I|323 M|323M|323N|323 N|323O|323 O|323P|323 P|323Q|323R|324A|324B|" +
                "325|325A|326|327|328|329|330|331|332|332A|332B|332C|332D|332E|332F|332G|332H|" +
                "332I|333|334|335|336|337|338|339|340|341|342|342A|342B|343A|343B|344|344A|" +
                "346A|347|348|349|500|501|502|503|504|504A|504B|504C|504D|504F|504G|504H|505" +
                "505A|505B|505C|505D|505E|505F|509|509A|510|511|512|513|513A|514|515|516|517|" +
                "518|519|520|520A|520B|520D|520E|520F|520G|521|522A|523A|525|525",
        message = "codigoRetencion $mensajeValores de 9|10|1|11|2|3|7|8|4580|303|304|304A|304B|304C|304D|304E|307|308|309|310|311|\" +\n" +
                "                \"312|312A|314A|314B|314C|314D|319|320|322|323|323A|323B1|323E|323E2|323F|323G|\" +\n" +
                "                \"323H|323I|323 M|323M|323N|323 N|323O|323 O|323P|323 P|323Q|323R|324A|324B|\" +\n" +
                "                \"325|325A|326|327|328|329|330|331|332|332A|332B|332C|332D|332E|332F|332G|332H|\" +\n" +
                "                \"332I|333|334|335|336|337|338|339|340|341|342|342A|342B|343A|343B|344|344A|\" +\n" +
                "                \"346A|347|348|349|500|501|502|503|504|504A|504B|504C|504D|504F|504G|504H|505\" +\n" +
                "                \"505A|505B|505C|505D|505E|505F|509|509A|510|511|512|513|513A|514|515|516|517|\" +\n" +
                "                \"518|519|520|520A|520B|520D|520E|520F|520G|521|522A|523A|525|525"
    )
    var codigoRetencion: String


    @NotNull(message = "porcentajeRetener $mensajeNulo")
    @Pattern(
        regexp = "(?:\\b|-)([1-9]{1,2}[0]?|100).[0-9]{2}\\b",
        message = "porcentajeRetener $mensajeValores de 1 a 100"
    )
    var porcentajeRetener: String

    @NotNull(message = "valorRetenido $mensajeNulo")
    @Pattern(
        regexp = "^[0-9]{1,14}(\\.[0-9]{2})?\$",
        message = "valorRetenido $mensajeValores de 1 a 14 enteros y hasta 2 decimales separados por punto"
    )
    var valorRetenido: String


    @NotNull(message = "codDocSustento $mensajeNulo")
    @Pattern(
        regexp = "([0-9]{2})",
        message = "codDocSustento $mensajeValores 2 digitos"
    )
    var codDocSustento: String

    @Pattern(
        regexp = "([0-9]{15})",
        message = "numDocSustento $mensajeValores de 15 digitos"
    )
    var numDocSustento: String?

    fun getNumDocSustento(): Optional<String> {
        return Optional.of(numDocSustento!!)
    }

    @Pattern(
        regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "fechaEmision $mensajeValores de fecha dd/mm/aaaa"
    )
    var fechaEmisionDocSustento: String

    constructor(
        codigo: String,
        codigoRetencion: String,
        baseImponible: String,
        porcentajeRetener: String,
        valorRetenido: String,
        codDocSustento: String,
        numDocSustento: String?,
        fechaEmisionDocSustento: String
    ) : super(baseImponible) {
        this.codigo = codigo
        this.codigoRetencion = codigoRetencion
        this.porcentajeRetener = porcentajeRetener
        this.valorRetenido = valorRetenido
        this.codDocSustento = codDocSustento
        this.numDocSustento = numDocSustento
        this.fechaEmisionDocSustento = fechaEmisionDocSustento
    }
}