package documentos.comprobanteretencion

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import documentos.*
import ec.gob.sri.comprobantes.exception.RespuestaAutorizacionException
import ec.gob.sri.comprobantes.util.ArchivoUtils
import firma.XAdESBESSignature
import utils.UtilsFacturacionElectronica
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList


class ComprobanteRetencionVersionATS(
    var infoTributario: InformacionTributaria,
    var infoCompRetencion: InformacionComprobanteRetencion,
    var docsSustento: ArrayList<DocSustento>,
    var infoAdicional: ArrayList<CampoAdicional>?,
    var directorioGuardarXML: String,
    var directorioGuardarXMLFirmados: String,
    var nombreArchivoXML: String,
    var nombreArchivoXMLFirmado: String,
    var clave: String,
    var directorioYNombreArchivoRegistroCivilP12: String,
    var debug: Boolean = true,
    versionXML: String?
) {


    var codigoNumerico = "12345678" // Codigo Quemado en guía del SRI

    var versionXML = "1.0"
    var encodingXML = "UTF-8"
    var nombreEtiquetaComprobante = "comprobanteRetencion"
    var idComprobante = "comprobante" // Codigo Quemado en guía del SRI
    var versionComprobanteRetencionXML = "2.0.0" // Codigo Quemado en guía del SRI
    var stringComprobanteRetencionXML = ""

    init {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val fecha: Date = format.parse(this.infoCompRetencion.fechaEmision)


        if (this.infoTributario.claveAcceso == null) {
            this.infoTributario.claveAcceso = GenerarDocumentos.generarClave(
                fecha,
                this.infoTributario.codDoc,
                this.infoTributario.ruc,
                this.infoTributario.ambiente,
                (this.infoTributario.estab + this.infoTributario.ptoEmision),
                this.infoTributario.secuencial,
                this.codigoNumerico,
                this.infoTributario.tipoEmision
            )
        } else {
            this.infoTributario.claveAcceso = infoTributario.claveAcceso
        }

        if (versionXML != null) {
            this.versionXML = versionXML
        }

    }

    fun getVersionXML(): Optional<String> {
        return Optional.of(versionXML)
    }

    fun getInfoAdicional(): Optional<ArrayList<CampoAdicional>> {
        return Optional.of<ArrayList<CampoAdicional>>(infoAdicional!!)
    }

    fun validar(): ArrayList<String> {
        val errores = arrayListOf<String>()

        /*val violationsInfoTributaria = validator.validate(this.infoTributario)

        for (violation in violationsInfoTributaria) {
            errores.add(violation.message)
        }

        val violationsInfoCompRetencion = validator.validate(this.infoCompRetencion)

        for (violation in violationsInfoCompRetencion) {
            errores.add(violation.message)
        }

        this.docsSustento.forEach {
            val violationsDocSustento = validator.validate(it)

            for (violation in violationsDocSustento) {
                errores.add(violation.message)
            }
        }

        this.infoAdicional?.forEach {
            val violationsInfoAdicional = validator.validate(it)

            for (violation in violationsInfoAdicional) {
                errores.add(violation.message)
            }
        }*/


        return errores
    }

    fun generarComprobanteRetencionXML(): String {
        val xmlString: String =
            "<?xml version=\"$versionXML\" encoding=\"$encodingXML\" ?>\n" +
                    "<$nombreEtiquetaComprobante id=\"${idComprobante}\" version=\"$versionComprobanteRetencionXML\">\n" +
                    generarCuerpoComprobanteRetencion() +
                    "</$nombreEtiquetaComprobante>"
        this.stringComprobanteRetencionXML = GenerarDocumentos.removerCaracteresEspeciales(xmlString)
        return xmlString
    }

    fun generarArchivoComprobanteRetencionXML(
        directorioAGuardarArchivo: String,
        nombreArchivoXMLAGuardar: String,
        stringComprobanteRetencionXML: String? = null
    ): String? {
        val xml = stringComprobanteRetencionXML ?: this.stringComprobanteRetencionXML
        var archivoPathNombre = directorioAGuardarArchivo + nombreArchivoXMLAGuardar

        try {

            val stringBuilder = StringBuilder()
            println(xml)
            stringBuilder.append(xml)

            val carpetaSalida = File(directorioAGuardarArchivo)

            if (!carpetaSalida.exists()) {
                carpetaSalida.mkdirs()
            }

            val archivoSalida = File(archivoPathNombre)

            archivoSalida.writeText(xml)

            println("Archivo generado")

            return archivoPathNombre

        } catch (ex: FileNotFoundException) {

            Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

        } catch (ex: IOException) {

            Logger.getLogger(ArchivoUtils::class.java.name).log(Level.SEVERE, null, ex)

        }
        return null
    }

    private fun generarCuerpoComprobanteRetencion(): String {

        val contenidoComprobanteRetencion = generarInformacionTributaria() +
                generarInformacionComprobanteRetencion() +
                generarDocsSustento(docsSustento) +
                generarInformacionAdicional()
        return contenidoComprobanteRetencion
    }


    private fun generarDocsSustento(docsSustento: ArrayList<DocSustento>): String {
        val nombreEtiquetaDocsSustento = "docsSustento"
        val docsSustentos = ("        <$nombreEtiquetaDocsSustento>\n"
                + generarDocSustento(docsSustento)
                + "         </$nombreEtiquetaDocsSustento>\n")
        return docsSustentos
    }

    private fun generarDocSustento(docsSustento: ArrayList<DocSustento>): String {
        val nombreEtiquetaDocSustento = "docSustento"
        var etiquetaDocSustento = ""
        docsSustento.forEach {

            var numDocSustento = ""
            if (it.numDocSustento != null) {
                numDocSustento =
                    "        <numDocSustento>${it.numDocSustento}</numDocSustento>\n"
            }

            var fechaRegistroContable = ""
            if (it.fechaRegistroContable != null) {
                fechaRegistroContable =
                    "        <fechaRegistroContable>${it.fechaRegistroContable}</fechaRegistroContable>\n"
            }

            var numAutDocSustento = ""
            if (it.numAutDocSustento != null) {
                numAutDocSustento =
                    "        <numAutDocSustento>${it.numAutDocSustento}</numAutDocSustento>\n"
            }

            var tipoRegi = ""
            if (it.tipoRegi != null) {
                tipoRegi =
                    "        <tipoRegi>${it.tipoRegi}</tipoRegi>\n"
            }

            var paisEfecPago = ""
            if (it.paisEfecPago != null) {
                paisEfecPago =
                    "        <paisEfecPago>${it.paisEfecPago}</paisEfecPago>\n"
            }

            var aplicConvDobTrib = ""
            if (it.aplicConvDobTrib != null) {
                aplicConvDobTrib =
                    "        <aplicConvDobTrib>${it.aplicConvDobTrib}</aplicConvDobTrib>\n"
            }

            var pagExtSujRetNorLeg = ""
            if (it.pagExtSujRetNorLeg != null) {
                pagExtSujRetNorLeg =
                    "        <pagExtSujRetNorLeg>${it.pagExtSujRetNorLeg}</pagExtSujRetNorLeg>\n"
            }

            var pagoRegFis = ""
            if (it.pagoRegFis != null) {
                pagoRegFis =
                    "        <pagoRegFis>${it.pagoRegFis}</pagoRegFis>\n"
            }

            var totalComprobantesReembolse = ""
            if (it.totalComprobantesReembolse != null) {
                totalComprobantesReembolse =
                    "        <totalComprobantesReembolso>${it.totalComprobantesReembolse}</totalComprobantesReembolso>\n"
            }

            var totalBaseImponibleReembolso = ""
            if (it.totalBaseImponibleReembolso != null) {
                totalBaseImponibleReembolso =
                    "        <totalBaseImponibleReembolso>${it.totalBaseImponibleReembolso}</totalBaseImponibleReembolso>\n"
            }

            var totalImpuestoReembolso = ""
            if (it.totalImpuestoReembolso != null) {
                totalImpuestoReembolso =
                    "        <totalImpuestoReembolso>${it.totalImpuestoReembolso}</totalImpuestoReembolso>\n"
            }

            var retenciones = ""
            if (it.retenciones != null) {
                retenciones =
                    "        <retenciones>${generarRetencion(it.retenciones!!)}</retenciones>\n"

            }

            var reembolsos = ""
            if (it.reembolsos != null) {
                reembolsos =
                    "      <reembolsos>${generarReembolso(it.reembolsos!!)}</reembolsos>\n"
            }

            etiquetaDocSustento += ("            <$nombreEtiquetaDocSustento>\n"
                    + "                <codSustento>${it.codSustento}</codSustento>\n"
                    + "                <codDocSustento>${it.codDocSustento}</codDocSustento>\n"
                    + numDocSustento
                    + "                <fechaEmisionDocSustento>${it.fechaEmisionDocSustento}</fechaEmisionDocSustento>\n"
                    + fechaRegistroContable
                    + numAutDocSustento
                    + "                <pagoLocExt>${it.pagoLocExt}</pagoLocExt>\n"
                    + tipoRegi
                    + paisEfecPago
                    + aplicConvDobTrib
                    + pagExtSujRetNorLeg
                    + pagoRegFis
                    + totalComprobantesReembolse
                    + totalBaseImponibleReembolso
                    + totalImpuestoReembolso
                    + "                <totalSinImpuestos>${it.totalSinImpuestos}</totalSinImpuestos>\n"
                    + "                <importeTotal>${it.importeTotal}</importeTotal>\n"
                    + "                <impuestosDocSustento>${generarImpuestoDocSustento(it.impuestosDocSustento)}</impuestosDocSustento>\n"
                    + retenciones
                    + reembolsos
                    + "                <pagos>${generarPagos(it.pagos)}</pagos>\n"
                    + "            </$nombreEtiquetaDocSustento>\n")
        }

        return etiquetaDocSustento
    }

    private fun generarImpuestoDocSustento(impuestoDocSustento: ArrayList<ImpuestoDocSustento>): String {
        val nombreEtiquetaImpuestoDocSustento = "impuestoDocSustento"
        var etiquetaImpuestoDocSustento = ""
        impuestoDocSustento.forEach {

            etiquetaImpuestoDocSustento += ("<$nombreEtiquetaImpuestoDocSustento>\n"
                    + "                <codImpuestoDocSustento>${it.codImpuestoDocSustento}</codImpuestoDocSustento>\n"
                    + "                <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <tarifa>${it.tarifa}</tarifa>\n"
                    + "                <valorImpuesto>${it.valorImpuesto}</valorImpuesto>\n"
                    + "</$nombreEtiquetaImpuestoDocSustento>\n")
        }
        return etiquetaImpuestoDocSustento
    }

    private fun generarRetencion(retenciones: ArrayList<Retencion>): String {
        val nombreEtiquetaRetencion = "retencion"
        var etiquetaRetenciones = ""
        retenciones.forEach {

            var dividendos = ""
            if (it.dividendos != null) {
                dividendos = this.generarDividendos(it.dividendos!!)
            }

            var compraCajBanano = ""
            if (it.compraCajBanano != null) {
                compraCajBanano = this.generarCompraCajBanano(it.compraCajBanano!!)
            }


            etiquetaRetenciones += ("<$nombreEtiquetaRetencion>\n"
                    + "                <codigo>${it.codigo}</codigo>\n"
                    + "                <codigoRetencion>${it.codigoRetencion}</codigoRetencion>\n"
                    + "                <baseImponible>${it.baseImponible}</baseImponible>\n"
                    + "                <porcentajeRetener>${it.porcentajeRetener}</porcentajeRetener>\n"
                    + "                <valorRetenido>${it.valorRetenido}</valorRetenido>\n"
                    + dividendos
                    + compraCajBanano
                    + "</$nombreEtiquetaRetencion>\n")
        }
        return etiquetaRetenciones
    }

    private fun generarDividendos(dividendo: Dividendos): String {
        val nombreEtiquetaDividendos = "dividendos"
        var etiquetaDividendos = ""

        var fechaPagoDiv = ""
        if (dividendo.fechaPagoDiv != null) {
            fechaPagoDiv =
                "           <fechaPagoDiv>${dividendo.fechaPagoDiv}</fechaPagoDiv>\n"
        }
        var imRentaSoc = ""
        if (dividendo.imRentaSoc != null) {
            imRentaSoc =
                "           <imRentaSoc>${dividendo.imRentaSoc}</imRentaSoc>\n"
        }
        var ejerFisUtDiv = ""
        if (dividendo.ejerFisUtDiv != null) {
            ejerFisUtDiv =
                "           <ejerFisUtDiv>${dividendo.ejerFisUtDiv}</ejerFisUtDiv>\n"
        }
        etiquetaDividendos += ("<$nombreEtiquetaDividendos>\n"
                + fechaPagoDiv
                + imRentaSoc
                + ejerFisUtDiv
                + "</$nombreEtiquetaDividendos>\n")

        return etiquetaDividendos
    }

    private fun generarCompraCajBanano(compraCajBanano: CompraCajBanano): String {
        val nombreEtiquetaCajBanano = "compraCaBanano"
        var etiquetaCompraCajBanano = ""

        var numCajBan = ""
        if (compraCajBanano.NumCajBan != null) {
            numCajBan =
                "           <NumCajBan>${compraCajBanano.NumCajBan}</NumCajBan>\n"
        }
        var precCajBan = ""
        if (compraCajBanano.PrecCajBan != null) {
            precCajBan =
                "           <PrecCajBan>${compraCajBanano.PrecCajBan}</PrecCajBan>\n"
        }

        etiquetaCompraCajBanano += ("<$nombreEtiquetaCajBanano>\n"
                + numCajBan
                + precCajBan
                + "</$nombreEtiquetaCajBanano>\n")

        return etiquetaCompraCajBanano
    }

    private fun generarReembolso(reembolsos: ArrayList<ReembolsoDetalle>): String {
        val nombreEtiquetaReembolso = "reembolsoDetalle"
        var etiquetaReembolso = ""
        reembolsos.forEach {

            var tipoIdentificacionProveedorReembolso = ""
            if (it.tipoIdentificacionProveedorReembolso != null) {
                tipoIdentificacionProveedorReembolso =
                    "        <tipoIdentificacionProveedorReembolso>${it.tipoIdentificacionProveedorReembolso}</tipoIdentificacionProveedorReembolso>\n"
            }
            var identificacionProveedorReembolso = ""
            if (it.identificacionProveedorReembolso != null) {
                identificacionProveedorReembolso =
                    "        <identificacionProveedorReembolso>${it.identificacionProveedorReembolso}</identificacionProveedorReembolso>\n"
            }
            var codPaisPagoProveedorReembolso = ""
            if (it.codPaisPagoProveedorReembolso != null) {
                codPaisPagoProveedorReembolso =
                    "        <codPaisPagoProveedorReembolso>${it.codPaisPagoProveedorReembolso}</codPaisPagoProveedorReembolso>\n"
            }
            var tipoProveedorReembolso = ""
            if (it.tipoProveedorReembolso != null) {
                tipoProveedorReembolso =
                    "        <tipoProveedorReembolso>${it.tipoProveedorReembolso}</tipoProveedorReembolso>\n"
            }
            var codDocReembolso = ""
            if (it.codDocReembolso != null) {
                codDocReembolso =
                    "        <codDocReembolso>${it.codDocReembolso}</codDocReembolso>\n"
            }
            var estabDocReembolso = ""
            if (it.estabDocReembolso != null) {
                estabDocReembolso =
                    "        <estabDocReembolso>${it.estabDocReembolso}</estabDocReembolso>\n"
            }
            var ptoEmiDocReembolso = ""
            if (it.ptoEmiDocReembolso != null) {
                ptoEmiDocReembolso =
                    "        <ptoEmiDocReembolso>${it.ptoEmiDocReembolso}</ptoEmiDocReembolso>\n"
            }
            var secuencialDocReembolso = ""
            if (it.secuencialDocReembolso != null) {
                secuencialDocReembolso =
                    "        <secuencialDocReembolso>${it.secuencialDocReembolso}</secuencialDocReembolso>\n"
            }
            var fechaEmisionDocReembolso = ""
            if (it.fechaEmisionDocReembolso != null) {
                fechaEmisionDocReembolso =
                    "        <fechaEmisionDocReembolso>${it.fechaEmisionDocReembolso}</fechaEmisionDocReembolso>\n"
            }
            var numeroAutorizacionDocReemb = ""
            if (it.numeroAutorizacionDocReemb != null) {
                numeroAutorizacionDocReemb =
                    "        <numeroAutorizacionDocReemb>${it.numeroAutorizacionDocReemb}</numeroAutorizacionDocReemb>\n"
            }
            var detalleImpuestos = ""
            if (it.detalleImpuestos != null) {
                detalleImpuestos =
                    "        <detalleImpuestos>${this.generarDetallesImpuestos(it.detalleImpuestos!!)}</detalleImpuestos>\n"
            }

            etiquetaReembolso += ("<$nombreEtiquetaReembolso>\n"
                    + tipoIdentificacionProveedorReembolso
                    + identificacionProveedorReembolso
                    + codPaisPagoProveedorReembolso
                    + tipoProveedorReembolso
                    + codDocReembolso
                    + estabDocReembolso
                    + ptoEmiDocReembolso
                    + secuencialDocReembolso
                    + fechaEmisionDocReembolso
                    + numeroAutorizacionDocReemb
                    + detalleImpuestos
                    + "</$nombreEtiquetaReembolso>\n")
        }
        return etiquetaReembolso
    }

    private fun generarDetallesImpuestos(detallesImpuesto: ArrayList<DetalleImpuestoRetencion>): String {
        val nombreEtiquetaDetalleImpuesto = "detalleImpuesto"
        var etiquetaDetalleImpuesto = ""
        detallesImpuesto.forEach {

            var codigo = ""
            if (it.codigo != null) {
                codigo =
                    "        <codigo>${it.codigo}</codigo>\n"
            }

            var codigoPorcentaje = ""
            if (it.codigoPorcentaje != null) {
                codigoPorcentaje =
                    "        <codigoPorcentaje>${it.codigoPorcentaje}</codigoPorcentaje>\n"
            }

            var tarifa = ""
            if (it.tarifa != null) {
                tarifa =
                    "        <tarifa>${it.tarifa}</tarifa>\n"
            }

            var baseImponibleReembolso = ""
            if (it.baseImponibleReembolso != null) {
                baseImponibleReembolso =
                    "        <baseImponibleReembolso>${it.baseImponibleReembolso}</baseImponibleReembolso>\n"
            }

            var impuestoReembolso = ""
            if (it.impuestoReembolso != null) {
                impuestoReembolso =
                    "        <impuestoReembolso>${it.impuestoReembolso}</impuestoReembolso>\n"
            }

            etiquetaDetalleImpuesto += ("<$nombreEtiquetaDetalleImpuesto>\n"
                    + codigo
                    + codigoPorcentaje
                    + tarifa
                    + baseImponibleReembolso
                    + impuestoReembolso
                    + "</$nombreEtiquetaDetalleImpuesto>\n")
        }
        return etiquetaDetalleImpuesto
    }

    private fun generarPagos(pagos: ArrayList<Pago>): String {
        val nombreEtiquetaPago = "pago"
        var etiquetaPagos = ""
        pagos.forEach {

//            var plazo = ""
//            if (it.plazo != null) {
//                plazo =
//                    "        <plazo>${it.plazo}</plazo>\n"
//            }
//
//            var unidadTiempo = ""
//            if (it.unidadTiempo != null) {
//                unidadTiempo =
//                    "        <unidadTiempo>${it.unidadTiempo}</unidadTiempo>\n"
//            }

            etiquetaPagos += ("<$nombreEtiquetaPago>\n"
                    + "                <formaPago>${it.formaPago}</formaPago>\n"
                    + "                <total>${it.total}</total>\n"
                    + "</$nombreEtiquetaPago>\n")
        }
        return etiquetaPagos
    }

    private fun generarInformacionTributaria(): String {
        val nombreEtiquetaInformacionTributaria = "infoTributaria"
        var nombreComercial = ""
        if (this.infoTributario.nombreComercial != null) {
            nombreComercial = "        <nombreComercial>${this.infoTributario.nombreComercial}</nombreComercial>\n"
        }
        val informacionTributaria = ("<$nombreEtiquetaInformacionTributaria>\n"
                + "        <ambiente>${this.infoTributario.ambiente}</ambiente>\n"
                + "        <tipoEmision>${this.infoTributario.tipoEmision}</tipoEmision>\n"
                + "        <razonSocial>${this.infoTributario.razonSocial}</razonSocial>\n"
                + nombreComercial
                + "        <ruc>${this.infoTributario.ruc}</ruc>\n"
                + "        <claveAcceso>${this.infoTributario.claveAcceso}</claveAcceso>\n"
                + "        <codDoc>${this.infoTributario.codDoc}</codDoc>\n"
                + "        <estab>${this.infoTributario.estab}</estab>\n"
                + "        <ptoEmi>${this.infoTributario.ptoEmision}</ptoEmi>\n"
                + "        <secuencial>${this.infoTributario.secuencial}</secuencial>\n"
                + "        <dirMatriz>${this.infoTributario.dirMatriz}</dirMatriz>\n"
                + "</$nombreEtiquetaInformacionTributaria>\n")

        return informacionTributaria
    }

    private fun generarInformacionComprobanteRetencion(): String {
        val nombreEtiquetaInformacionComprobanteretencion = "infoCompRetencion"

        var obligadoContabilidad = ""
        if (this.infoCompRetencion.obligadoContabilidad != null) {
            obligadoContabilidad =
                "        <obligadoContabilidad>${this.infoCompRetencion.obligadoContabilidad}</obligadoContabilidad>\n"
        }

        var contribuyenteEspecial = ""
        if (this.infoCompRetencion.contribuyenteEspecial != null) {
            contribuyenteEspecial =
                "        <contribuyenteEspecial>${this.infoCompRetencion.contribuyenteEspecial}</contribuyenteEspecial>\n"
        }
        var direccionEstablecimiento = ""
        if (this.infoCompRetencion.dirEstablecimiento != null) {
            direccionEstablecimiento =
                "        <dirEstablecimiento>${
                    this.infoCompRetencion.dirEstablecimiento
                        ?: ""
                }</dirEstablecimiento>\n"
        }
        var tipoIdentificacionSujetoRetenido = ""
        if (this.infoCompRetencion.tipoIdentificacionSujetoRetenido != null) {
            tipoIdentificacionSujetoRetenido =
                "        <tipoIdentificacionSujetoRetenido>${
                    this.infoCompRetencion.tipoIdentificacionSujetoRetenido
                        ?: ""
                }</tipoIdentificacionSujetoRetenido>\n"
        }

        var tipoSujetoRetenido = ""
        if (this.infoCompRetencion.tipoSujetoRetenido != null) {
            tipoSujetoRetenido =
                "        <tipoSujetoRetenido>${this.infoCompRetencion.tipoSujetoRetenido}</tipoSujetoRetenido>\n"
        }

        val informacionComprobanteRetencion = ("<$nombreEtiquetaInformacionComprobanteretencion>\n"
                + "        <fechaEmision>${this.infoCompRetencion.fechaEmision}</fechaEmision>\n"
                + direccionEstablecimiento
                + contribuyenteEspecial
                + obligadoContabilidad
                + tipoIdentificacionSujetoRetenido
                + tipoSujetoRetenido
                + "        <parteRel>${this.infoCompRetencion.parteRel}</parteRel>\n"
                + "        <razonSocialSujetoRetenido>${this.infoCompRetencion.razonSocialSujetoRetenido}</razonSocialSujetoRetenido>\n"
                + "        <identificacionSujetoRetenido>${this.infoCompRetencion.identificacionSujetoRetenido}</identificacionSujetoRetenido>\n"
                + "        <periodoFiscal>${this.infoCompRetencion.periodoFiscal}</periodoFiscal>\n"
                + "</$nombreEtiquetaInformacionComprobanteretencion>\n")
        return informacionComprobanteRetencion
    }

    private fun generarInformacionAdicional(): String {
        val nombreEtiquetaInformacionAdicional = "infoAdicional"
        if (this.infoAdicional != null && this.infoAdicional?.size ?: 0 > 0) {
            val informacionAdicional = ("<$nombreEtiquetaInformacionAdicional>\n"
                    + generarCampoAdicional(this.infoAdicional ?: arrayListOf())
                    + "</$nombreEtiquetaInformacionAdicional>\n")
            return informacionAdicional
        } else {
            return ""
        }
    }

    private fun generarCampoAdicional(informacionAdicional: ArrayList<CampoAdicional>): String {
        var totalCamposAdicionales = ""
        informacionAdicional.forEach {
            totalCamposAdicionales += ("         <campoAdicional nombre=\"${it.nombre}\">${it.valor}</campoAdicional>\n")
        }
        return totalCamposAdicionales
    }


    fun enviarComprobanteRetencion(json: String): String {

        val nombreDocumento = "Comprobante de Retencion"

        val resultado = Klaxon()
            .parse<ComprobanteRetencionVersionATS?>(
                json
            )
        try {
            val errores = resultado?.validar()
            if (errores?.size ?: 0 > 0) {
                if (debug) {
                    println("Error")
                }
                var erroresObjeto = ""
                errores?.forEachIndexed { indice, mensajeError ->
                    if (debug) {
                        println(mensajeError)
                    }
                    erroresObjeto += erroresObjeto + """
                        {
                            "mensaje": "${this.eliminarCaracteresEspeciales(mensajeError)}"
                        }${if (indice != (errores.size - 1)) "," else ""}
                    """.trimIndent()
                }
                val erroresAEnviar = "[" + erroresObjeto + "]"

                return """
                        {
                            "mensaje":"Errores en parseo de $nombreDocumento.",
                            "error": 400,
                            "data": {
                                "errores":${erroresAEnviar}
                            }
                        }
                        """.trimIndent()
            } else {
                resultado?.generarComprobanteRetencionXML()
                resultado?.generarArchivoComprobanteRetencionXML(
                    resultado.directorioGuardarXML + "/",
                    resultado.nombreArchivoXML,
                    resultado.stringComprobanteRetencionXML
                )

                val archivoGenerado = resultado?.generarArchivoComprobanteRetencionXML(
                    resultado.directorioGuardarXML + "/",
                    resultado.nombreArchivoXML
                )
                println("archivo generado: $archivoGenerado")

                if (archivoGenerado != null) {
                    val archivoFirmado = XAdESBESSignature
                        .firmar(
                            resultado.directorioGuardarXML + "/" + resultado.nombreArchivoXML,
                            resultado.nombreArchivoXMLFirmado,
                            resultado.clave,
                            resultado.directorioYNombreArchivoRegistroCivilP12,
                            resultado.directorioGuardarXMLFirmados
                        )
                    if (archivoFirmado) {
                        if (debug) {
                            println("Se firmo archivos")
                        }
                        val directorioYNombreArchivoXMLFirmado =
                            resultado.directorioGuardarXMLFirmados + File.separator + resultado.nombreArchivoXMLFirmado

                        val datos = UtilsFacturacionElectronica.convertirBytes(directorioYNombreArchivoXMLFirmado)
                        if (debug) {
                            println("Convirtiendo datos")
                        }

                        if (datos != null) {

                            if (resultado.infoTributario.ambiente == "2") {
                                AutorizarDocumentos.host = "https://cel.sri.gob.ec"
                            }

                            if (resultado.infoTributario.ambiente == "1") {
                                AutorizarDocumentos.host = "https://celcer.sri.gob.ec"
                            }


                            val respuestaSolicitud = AutorizarDocumentos.validar(datos)
                            if (debug) {
                                println("Validando Datos")
                            }
                            if (respuestaSolicitud != null && (respuestaSolicitud.comprobantes != null ?: false)) {
                                if (debug) {
                                    println("Validando si solicitud es RECIBIDA")
                                    println("Estado: ${respuestaSolicitud.estado}")
                                }
                                if (respuestaSolicitud.estado == "RECIBIDA") {
                                    try {
                                        if (debug) {
                                            println("ESTADO RECIBIDA")
                                        }
                                        val respuestaComprobante =
                                            AutorizarDocumentos.autorizarComprobante(resultado.infoTributario.claveAcceso!!)
                                        if (debug) {
                                            println("recibimos respuesta")
                                            println("numeroComprobantes ${respuestaComprobante?.numeroComprobantes}")
                                        }
                                        var autorizaciones = ""
                                        respuestaComprobante?.autorizaciones?.autorizacion?.forEachIndexed { index, it ->

                                            if (debug) {
                                                println("numeroAutorizacion ${it.numeroAutorizacion}")
                                                println("comprobante ${it.comprobante}")
                                                println("estado ${it.estado}")
                                                println("fechaAutorizacion ${it.fechaAutorizacion}")
                                                println("Mensajes:")
                                            }
                                            val comprobanteString = eliminarCaracteresEspeciales(it.comprobante)

                                            val fechaActual = LocalDateTime.now()
                                            val formatterFecha = DateTimeFormatter.ISO_LOCAL_DATE
                                            val fechaString = fechaActual.format(formatterFecha)
                                            val formatterHoraMinutoSegundo = DateTimeFormatter.ISO_TIME
                                            val horaMinutoSegundoString = fechaActual.format(formatterHoraMinutoSegundo)

                                            val xmlCompleto = """
                                            <?xml version=\"1.0\" encoding=\"UTF-8\"?>
                                            <autorizacion>
                                              <estado>${it.estado}</estado>
                                              <numeroAutorizacion>${it.numeroAutorizacion}</numeroAutorizacion>
                                              <fechaAutorizacion class=\"fechaAutorizacion\">${fechaString} ${horaMinutoSegundoString}</fechaAutorizacion>
                                              <comprobante><![CDATA[${comprobanteString}]]></comprobante>
                                              <mensajes/>
                                            </autorizacion>
                                            """.trimIndent()
                                            var autorizacion = """
                                                {
                                                    "numeroAutorizacion" : "${it.numeroAutorizacion}",
                                                    "comprobante" : "${comprobanteString}",
                                                    "estado" : "${it.estado}",
                                                    "fechaAutorizacion" : "${it.fechaAutorizacion}",
                                                    "xmlCompleto": "${eliminarEspacios(xmlCompleto)}",
                                            """.trimIndent()


                                            var mensajeString = ""
                                            it.mensajes.mensaje.forEachIndexed { indiceMensaje, mensaje ->
                                                if (debug) {
                                                    println("identificador ${mensaje.identificador}")
                                                    println("informacionAdicional ${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}")
                                                    println("mensaje ${mensaje.mensaje}")
                                                    println("tipo ${mensaje.tipo}")
                                                }
                                                mensajeString += """
                                                    {
                                                        "identificador":"${mensaje.identificador}",
                                                        "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
                                                        "mensaje":"${mensaje.mensaje}",
                                                        "tipo":"${mensaje.tipo}"
                                                    }${if (indiceMensaje != (it.mensajes.mensaje.size - 1)) "," else ""}
                                                """.trimIndent()
                                            }
                                            val mensajeArreglo = "[" + mensajeString + "]"
                                            autorizacion += """
                                                "mensajes": ${mensajeArreglo}
                                                }${
                                                if (index != ((respuestaComprobante.autorizaciones?.autorizacion?.size
                                                        ?: 1) - 1)
                                                ) "," else ""
                                            }
                                            """.trimIndent()

                                            autorizaciones += autorizacion
                                        }
                                        val autorizacionCompleta = "[" + autorizaciones + "]"
                                        return """
                                            {
                                                "mensaje":"Se recibieron autorizaciones",
                                                "error":null,
                                                "data":{
                                                    "estadoSolicitud":"RECIBIDA",
                                                    "claveAccesoConsultada":"${respuestaComprobante?.claveAccesoConsultada}",
                                                    "numeroComprobantes":"${respuestaComprobante?.numeroComprobantes}",
                                                    "autorizaciones":${autorizacionCompleta},
                                                    "claveAcceso": "${resultado.infoTributario.claveAcceso}"
                                                }
                                            }
                                        """.trimIndent()

                                    } catch (ex: RespuestaAutorizacionException) {
                                        if (debug) {
                                            println("Respuesta solicitud NO recibida")
                                        }
                                        return """
                                                {
                                                    "mensaje":"Respuesta solicitud NO recibida.",
                                                    "error": 500
                                                }
                                                """
                                    }
                                } else {


                                    var mensajes = ""
                                    respuestaSolicitud.comprobantes.comprobante.forEach {
                                        it.mensajes.mensaje.forEachIndexed { index, mensaje ->
                                            if (debug) {
                                                mensaje.informacionAdicional =
                                                    if (mensaje.informacionAdicional == null) "ninguno" else mensaje.informacionAdicional
                                                println(mensaje.tipo)
                                                println(mensaje.identificador)
                                                println(mensaje.informacionAdicional)
                                                println(mensaje.mensaje)
                                            }

                                            val fechaActual = LocalDateTime.now()
                                            val formatterFecha = DateTimeFormatter.ISO_LOCAL_DATE
                                            val fechaString = fechaActual.format(formatterFecha)
                                            val formatterHoraMinutoSegundo = DateTimeFormatter.ISO_TIME
                                            val horaMinutoSegundoString = fechaActual.format(formatterHoraMinutoSegundo)
                                            var xmlCompleto = ""
                                            if (mensaje.identificador == "45" || mensaje.identificador == "43") {
                                                xmlCompleto = """
                                           ,"xmlCompleto": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
                                            <autorizacion>
                                              <estado>AUTORIZADO</estado>
                                              <numeroAutorizacion>${resultado.infoTributario.claveAcceso}</numeroAutorizacion>
                                              <fechaAutorizacion class=\"fechaAutorizacion\">${fechaString} ${horaMinutoSegundoString}</fechaAutorizacion>
                                              <comprobante><![CDATA[${
                                                    eliminarCaracteresEspeciales(
                                                        File(
                                                            directorioYNombreArchivoXMLFirmado
                                                        ).readText()
                                                    )
                                                }]]></comprobante>
                                              <mensajes/>
                                            </autorizacion>"
                                            """.trimIndent()
                                            }

                                            mensajes += """
                                                {
                                                    "tipo":"${mensaje.tipo}",
                                                    "identificador":"${mensaje.identificador}",
                                                    "informacionAdicional":"${eliminarCaracteresEspeciales(mensaje.informacionAdicional)}",
                                                    "mensaje":"${eliminarCaracteresEspeciales(mensaje.mensaje)}"${
                                                eliminarEspacios(
                                                    xmlCompleto
                                                )
                                            }
                                                }${if (index != (it.mensajes.mensaje.size - 1)) "," else ""}
                                            """.trimIndent()

                                        }
                                    }
                                    var mensajesRespuestaSolicitudNoRecibida = "[" + mensajes + "]"
                                    return """
                                        {
                                            "mensaje": "Estado diferente a recibido",
                                            "error": 400,
                                            "data": {
                                                "comprobante": "${this.eliminarCaracteresEspeciales(resultado.stringComprobanteRetencionXML)}",
                                                "estadoSolicitud": "${respuestaSolicitud.estado}",
                                                "mensajes": ${mensajesRespuestaSolicitudNoRecibida}
                                            }
                                        }
                                    """.trimIndent()
                                }
                            } else {
                                if (debug) {
                                    println("Error en respuesta solicitud")
                                }
                                return """
                                    {
                                        "mensaje":"Error en respuesta de solicitud. Puede ser error del servidor del SRI intentelo de nuevo. Revise los logs.",
                                        "error": 500
                                    }
                                    """.trimIndent()
                            }
                        } else {
                            if (debug) {
                                println("Error convirtiendo archivo a bytes")
                            }
                            return """
                                    {
                                        "mensaje":"Error convirtiendo archivo a bytes.",
                                        "error": 500
                                    }
                                    """.trimIndent()
                        }

                    } else {
                        if (debug) {
                            println("Error firmando archivo")
                        }
                        return """
                        {
                            "mensaje":"Error firmando archivo.",
                            "error": 500
                        }
                        """.trimIndent()
                    }
                } else {
                    if (debug) {
                        println("Error firmando archivo")
                    }
                    return """
                        {
                            "mensaje":"Error creando archivo XML.",
                            "error": 500
                        }
                        """.trimIndent()
                }
            }
        } catch (e: KlaxonException) {
            if (debug) {
                println(e)
            }
            return """
            {
                "mensaje":"Errores en parseo de $nombreDocumento.",
                "error": 400,
                "data": {
                    "message":"${this.eliminarCaracteresEspeciales(e.message.toString())}",
                    "cause":"${this.eliminarCaracteresEspeciales(e.cause.toString())}"
                }
            }
            """.trimIndent()
        } catch (e: Exception) {
            if (debug) {
                println(e)
            }
            return """
            {
                "mensaje":"Error del servidor.",
                "error": "400",
                "data": {
                    "message":"${this.eliminarCaracteresEspeciales(e.message.toString())}",
                    "cause":"${this.eliminarCaracteresEspeciales(e.cause.toString())}"
                }
            }
            """.trimIndent()
        }


    }

    private fun eliminarCaracteresEspeciales(texto: String): String {
        return texto
            .replace("\"", "\\\"").replace("\n", "")
            .replace("\r", "")
    }

    private fun eliminarEspacios(texto: String): String {
        return texto
            .replace("\n", "")
            .replace("\r", "")
    }

}