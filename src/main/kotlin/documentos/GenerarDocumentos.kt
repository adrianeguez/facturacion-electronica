package documentos

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Date
import java.util.logging.Level
import java.util.logging.Logger
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat

class GenerarDocumentos {

    fun instalarCertificado() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })


        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        } catch (e: KeyManagementException) {

        } catch (ex: NoSuchAlgorithmException) {
            Logger.getLogger(AutorizarDocumentos::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    companion object {
        fun removerCaracteresEspeciales(caracteresARemover: String): String {
            val cadenaDeCaracteresASustituir = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ$&¨"
            val cadenaDeCaracteresAReemplazar = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcCxy'"
            var caracteresADevolver: String = caracteresARemover
            println(caracteresARemover)
            for (i in 0 until cadenaDeCaracteresASustituir.length) {
                caracteresADevolver =
                        caracteresADevolver.replace(cadenaDeCaracteresASustituir[i], cadenaDeCaracteresAReemplazar[i])
            }
            println(caracteresADevolver)
            return caracteresADevolver
        }

        fun generarClave(
            fechaEmision: Date,
            tipoComprobante: String,
            ruc: String?,
            ambiente: String,
            serie: String,
            numeroComprobante: String,
            codigoNumerico: String,
            tipoEmision: String
        ): String? {
            var ruc = ruc
            var claveGenerada: String?
            var verificador = 0

            if (ruc != null && ruc.length < 13) {
                ruc = String.format("%013d", arrayOf<Any>(ruc))
            }
            val dateFormat = SimpleDateFormat("ddMMyyyy")
            val fecha = dateFormat.format(fechaEmision)
            val clave = StringBuilder(fecha)
            clave.append(tipoComprobante)
            clave.append(ruc)
            clave.append(ambiente)
            clave.append(serie)
            clave.append(numeroComprobante)
            clave.append(codigoNumerico)
            clave.append(tipoEmision)
            verificador = generarDigitoModulo11(clave.toString())
            clave.append(Integer.valueOf(verificador))
            claveGenerada = clave.toString()
            if (clave.toString().length != 49) {
                claveGenerada = null
            }
            return claveGenerada
        }

        fun generarDigitoModulo11(cadena: String): Int {
            val baseMultiplicador = 7
            println("CADENA-->$cadena")
            val aux = IntArray(cadena.length)
            var multiplicador = 2
            var total = 0
            var verificador = 0
            for (i in aux.indices.reversed()) {
                aux[i] = Integer.parseInt("" + cadena[i])
                aux[i] *= multiplicador
                multiplicador++
                if (multiplicador > baseMultiplicador) {
                    multiplicador = 2
                }
                total += aux[i]
            }
            if (total == 0 || total == 1) {
                verificador = 0
            } else {
                verificador = if (11 - total % 11 == 11) 0 else 11 - total % 11
            }
            if (verificador == 10) {
                verificador = 1
            }
            return verificador
        }

        //<editor-fold defaultstate="collapsed" desc=" ARMAR FACTURA">
        /*
    public String generaXMLFactura(Factura valor, Tipoambiente amb, String folderDestino, String nombreArchivoXML, Boolean autorizada, Date fechaAutorizacion) {
        try {
            FileOutputStream out = null;
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

            StringBuilder build = new StringBuilder();
            String linea = "";
            DecimalFormat df = new DecimalFormat("#.##");

            //            String claveAcceso = generaClave(new Date(), "01", empresa.getRucempresa(), "1", serie, cabdoc.getSecuencialcar(), "12345678", "1");
            //fecha de emision, tipo comprobante, RUC,tipo ambiente, serie(001001)Estabecimiento 002 emision001,tipo de emision comprobante
            String claveAcceso = generaClave(valor.getFacFecha(), "01", amb.getAmRuc(), amb.getAmCodigo(), "002001", valor.getFacNumeroText(), "12345678", "1");
            String tipoAmbiente = "";
            if (amb.getAmCodigo().equals("1")) {
                tipoAmbiente = "PRUEBAS";

            } else {
                tipoAmbiente = "PRODUCCION";
            }
            linea = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                    + "<factura id=\"comprobante\" version=\"1.1.0\">\n");
            build.append(linea);
            linea = "";
            if (autorizada) {
                linea = (" <estado>AUTORIZADO</estado>\n"
                        + " <numeroAutorizacion>" + claveAcceso + "</numeroAutorizacion>\n"
                        + " <fechaAutorizacion>" + formato.format(fechaAutorizacion) + "</fechaAutorizacion>\n"
                        + " <ambiente>" + tipoAmbiente + "</ambiente>\n");
            }
            build.append(linea);
            linea = ("<infoTributaria>\n"
                    + "        <ambiente>" + amb.getAmCodigo() + "</ambiente>\n"
                    + "        <tipoEmision>" + amb.getAmCodigo() + "</tipoEmision>\n"
                    + "        <razonSocial>" + removeCaracteres(amb.getAmRazonSocial()) + "</razonSocial>\n"
                    + "        <nombreComercial>" + removeCaracteres(amb.getAmNombreComercial()) + "</nombreComercial>\n"
                    + "        <ruc>" + amb.getAmRuc() + "</ruc>\n"
                    + "        <claveAcceso>" + claveAcceso + "</claveAcceso>\n"
                    + "        <codDoc>01</codDoc>\n"
                    //001 estab y punto emision/
                    + "        <estab>" + valor.getCodestablecimiento() + "</estab>\n"
                    + "        <ptoEmi>" + valor.getPuntoemision() + "</ptoEmi>\n"
                    + "        <secuencial>" + valor.getFacNumeroText() + "</secuencial>\n"
                    + "        <dirMatriz>" + removeCaracteres(amb.getAmDireccionMatriz()) + "</dirMatriz>\n"
                    + "</infoTributaria>\n"
                    + "<infoFactura>\n"
                    + "        <fechaEmision>" + formato.format(valor.getFacFecha()) + "</fechaEmision>\n"
                    + "        <dirEstablecimiento>" + removeCaracteres(amb.getAmDireccionMatriz()) + "</dirEstablecimiento>\n"
                    + "        <contribuyenteEspecial>0047</contribuyenteEspecial>\n"
                    + "        <obligadoContabilidad>" + amb.getLlevarContabilidad() + "</obligadoContabilidad>\n"
                    + "        <tipoIdentificacionComprador>" + valor.getIdCliente().getIdTipoIdentificacion().getTidCodigo() + "</tipoIdentificacionComprador>\n"
                    + "        <razonSocialComprador>" + removeCaracteres(valor.getIdCliente().getCliNombre()) + "</razonSocialComprador>\n"
                    + "        <identificacionComprador>" + valor.getIdCliente().getCliCedula() + "</identificacionComprador>\n"
                    + "        <totalSinImpuestos>" + valor.getFacSubtotal().setScale(2, RoundingMode.FLOOR) + "</totalSinImpuestos>\n"
                    + "        <totalDescuento>" + valor.getFacDescuento().setScale(2, RoundingMode.FLOOR) + "</totalDescuento>\n"
                    + "        <totalConImpuestos>\n"
                    + "            <totalImpuesto>\n"
                    + "                <codigo>" + valor.getFacCodIce() + "</codigo>\n"
                    + "                <codigoPorcentaje>0</codigoPorcentaje>\n"
                    + "                <baseImponible>" + valor.getFacTotalBaseCero().setScale(2, RoundingMode.FLOOR) + "</baseImponible>\n"
                    + "                <tarifa>0</tarifa>\n"
                    + "                <valor>0.00</valor>\n"
                    + "             </totalImpuesto>\n"
                    + "             <totalImpuesto>\n"
                    //CODIGO DEL IVA 2, ICE 3 IRBPNR 6
                    + "             <codigo>" + valor.getFacCodIva() + "</codigo>\n"
                    //CODIGO VALOR DEL IVA SI ES IVA
                    //0 --> 0
                    //SI 12-->2
                    //SI 14-->3
                    //No Objeto de Impuesto -->6
                    //EXENTO DE IVA 7
                    + "                 <codigoPorcentaje>" + valor.getCodigoPorcentaje() + "</codigoPorcentaje>\n"
                    + "                 <baseImponible>" + valor.getFacTotalBaseGravaba().setScale(2, RoundingMode.FLOOR) + "</baseImponible>\n"
                    + "                 <tarifa>" + valor.getFacPorcentajeIva() + "</tarifa>\n"
                    + "                 <valor>" + valor.getFacIva().setScale(2, RoundingMode.FLOOR) + "</valor>\n"
                    + "              </totalImpuesto>\n"
                    + "         </totalConImpuestos>\n"
                    + "                 <propina>0</propina>\n"
                    + "                 <importeTotal>" + valor.getFacTotal().setScale(2, RoundingMode.FLOOR) + "</importeTotal>\n"
                    + "                 <moneda>" + valor.getFacMoneda() + "</moneda>\n"
                    + "         <pagos>\n"
                    + "                 <pago>\n"
                    + "                     <formaPago>" + valor.getIdFormaPago().getForCodigo() + "</formaPago>\n"
                    + "                     <total>" + valor.getFacTotal().setScale(2, RoundingMode.FLOOR) + "</total>\n"
                    + "                     <plazo>" + valor.getFacPlazo() + "</plazo>\n"
                    + "                     <unidadTiempo>" + valor.getFacUnidadTiempo() + "</unidadTiempo>\n"
                    + "                 </pago>\n"
                    + "         </pagos>\n"
                    + "         <valorRetIva>" + 0.00 + "</valorRetIva>\n"
                    + "         <valorRetRenta>" + 0.00 + "</valorRetRenta>\n"
                    + "    </infoFactura>");
            build.append(linea);
            linea = ("     <detalles>\n");
            build.append(linea);

            List<DetalleFactura> listaDetalle = servicioDetalleFactura.findDetalleForIdFactuta(valor);
            for (DetalleFactura item : listaDetalle) {

                linea = ("        <detalle>\n"
                        + "            <codigoPrincipal>" + removeCaracteres(item.getIdProducto().getProdCodigo()) + "</codigoPrincipal>\n"
                        + "            <descripcion>" + removeCaracteres(item.getIdProducto().getProdNombre()) + "</descripcion>\n"
                        + "            <cantidad>" + item.getDetCantidad().setScale(2, RoundingMode.FLOOR) + "</cantidad>\n"
                        + "            <precioUnitario>" + item.getDetSubtotal() + "</precioUnitario>\n"
                        + "            <descuento>" + item.getDetCantpordescuento().setScale(2, RoundingMode.FLOOR) + "</descuento>\n"
                        + "            <precioTotalSinImpuesto>" + (item.getDetSubtotaldescuento().multiply(item.getDetCantidad())).setScale(2, RoundingMode.FLOOR) + "</precioTotalSinImpuesto>\n"
                        + "            <impuestos>\n"
                        + "                <impuesto>\n"
                        + "                    <codigo>" + valor.getFacCodIva() + "</codigo>\n"
                        + "                    <codigoPorcentaje>" + valor.getCodigoPorcentaje() + "</codigoPorcentaje>\n"
                        + "                    <tarifa>" + valor.getFacPorcentajeIva() + "</tarifa>\n"
                        + "                    <baseImponible>" + (item.getDetSubtotaldescuento().multiply(item.getDetCantidad())).setScale(2, RoundingMode.FLOOR) + "</baseImponible>\n"
                        + "                    <valor>" + (item.getDetTotaldescuentoiva().subtract(item.getDetIva())).setScale(2, RoundingMode.FLOOR) + "</valor>\n"
                        + "                </impuesto>\n"
                        + "            </impuestos>\n"
                        + "        </detalle>\n");
                build.append(linea);
            }

//            build.append(linea);
            linea = ("    </detalles>\n");
            build.append(linea);
            linea = ("    <infoAdicional>\n"
                    + (valor.getIdCliente().getCliDireccion().length() > 0 ? "<campoAdicional nombre=\"DIRECCION\">" + removeCaracteres(valor.getIdCliente().getCliDireccion()) + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliCorreo().length() > 0 ? "<campoAdicional nombre=\"E-MAIL\">" + valor.getIdCliente().getCliCorreo() + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliApellidos().length() > 0 ? "<campoAdicional nombre=\"APELLIDO\">" + removeCaracteres(valor.getIdCliente().getCliApellidos()) + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliNombres().length() > 0 ? "<campoAdicional nombre=\"NOMBRE\">" + removeCaracteres(valor.getIdCliente().getCliNombres()) + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliNombre().length() > 0 ? "<campoAdicional nombre=\"NOMBRECOMERCIAL\">" + removeCaracteres(valor.getIdCliente().getCliNombre()) + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCiudad().length() > 0 ? "<campoAdicional nombre=\"CIUDAD\">" + removeCaracteres(valor.getIdCliente().getCiudad()) + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliTelefono().length() > 0 ? "<campoAdicional nombre=\"TELEFONO\">" + valor.getIdCliente().getCliTelefono() + "</campoAdicional>\n" : " ")
                    + (valor.getIdCliente().getCliMovil().length() > 0 ? "<campoAdicional nombre=\"CELULAR\">" + valor.getIdCliente().getCliMovil() + " </campoAdicional>\n" : " ")
                    + "<campoAdicional nombre=\"PLAZO\"> </campoAdicional>\n"
                    + (valor.getFacPlazo().toString().length() > 0 ? "<campoAdicional nombre=\"DIAS\">" + valor.getFacPlazo() + "</campoAdicional>\n" : " ")
                    + (valor.getFacPorcentajeIva().length() > 0 ? "<campoAdicional nombre=\"TARIFAIMP\">" + valor.getFacPorcentajeIva() + "</campoAdicional>\n" : " ")
                    + "   </infoAdicional>\n"
                    + "</factura>\n");
            build.append(linea);
            //IMPRIME EL XML DE LA FACTURA
            System.out.println("XML " + build);
            String pathArchivoSalida = "";

            //ruta de salida del archivo XML
            //generados o autorizados para enviar al cliente
            //dependiendo la ruta enviada en el parametro del metodo
            pathArchivoSalida = folderDestino
                    + nombreArchivoXML;

            //String pathArchivoSalida = "D:\\";
            out = new FileOutputStream(pathArchivoSalida);
            out.write(build.toString().getBytes());
            //GRABA DATOS EN FACTURA//
            return pathArchivoSalida;
            //return Utilidades.DirXMLPrincipal + Utilidades.DirSinFirmas + "FACT-" + cabdoc.getEstablecimientodocumento() + "-" + cabdoc.getPuntoemisiondocumento() + "-" + cabdoc.getSecuencialcar() + ".xml";
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR EN LA GENERACION DE XML FACTURA  FileNotFoundException" + ex);
        } catch (IOException ex) {
            System.out.println("ERROR EN LA GENERACION DE XML FACTURA IOException " + ex);
        }
        return null;
    }


     */
        //</editor-fold >
        //<editor-fold defaultstate="collapsed" desc=" ARMAR FACTURA">

        fun generarFacturaXML(claveAcceso: String): String? {

            try {
                var out: FileOutputStream? = null

                val build = StringBuilder()
                var linea = ""
                val formato = SimpleDateFormat("dd/MM/yyyy")
                //String claveAcceso = generaClave(valor.getFacFecha(), "01", amb.getAmRuc(), amb.getAmCodigo(), "002001", valor.getFacNumeroText(), "12345678", "1");
                //fecha de emision, tipo comprobante, RUC,tipo ambiente, serie(001001)Estabecimiento 002 emision001,tipo de emision comprobante
                linea = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                        + "<factura id=\"comprobante\" version=\"1.1.0\">\n"
                        + "<infoTributaria>\n"
                        + "        <ambiente>1</ambiente>\n"
                        + "        <tipoEmision>1</tipoEmision>\n"
                        + "        <razonSocial>PADILLA CAMUENDO LUIS ALFREDO</razonSocial>\n"
                        + "        <nombreComercial>COMERCIAL BRENDA</nombreComercial>\n"
                        + "        <ruc>1710361658001</ruc>\n"
                        + "        <claveAcceso>" + claveAcceso + "</claveAcceso>\n"
                        + "        <codDoc>01</codDoc>\n"
                        + "        <estab>001</estab>\n"
                        + "        <ptoEmi>001</ptoEmi>\n"
                        + "        <secuencial>000000010</secuencial>\n"
                        + "        <dirMatriz>PICHINCHA / QUITO / QUITO/ LLANO CHICO</dirMatriz>\n"
                        + "</infoTributaria>\n"
                        + "<infoFactura>\n"
                        + "        <fechaEmision>" + formato.format(Date()) + "</fechaEmision>\n"
                        + "        <dirEstablecimiento>PICHINCHA / CAYAMBE / CAYAMBE / ROCAFUERTE N0-45 Y LIBERTAD</dirEstablecimiento>\n"
                        + "        <contribuyenteEspecial>0047</contribuyenteEspecial>\n"
                        + "        <obligadoContabilidad>SI</obligadoContabilidad>\n"
                        + "        <tipoIdentificacionComprador>05</tipoIdentificacionComprador>\n"
                        + "        <razonSocialComprador>BESTSYSTEM</razonSocialComprador>\n"
                        + "        <identificacionComprador>1710372705</identificacionComprador>\n"
                        + "        <totalSinImpuestos>1098.90</totalSinImpuestos>\n"
                        + "        <totalDescuento>0.00</totalDescuento>\n"
                        + "        <totalConImpuestos>\n"
                        + "            <totalImpuesto>\n"
                        + "                <codigo>3</codigo>\n"
                        + "                <codigoPorcentaje>0</codigoPorcentaje>\n"
                        + "                <baseImponible>0.00</baseImponible>\n"
                        + "                <tarifa>0</tarifa>\n"
                        + "                <valor>0.00</valor>\n"
                        + "             </totalImpuesto>\n"
                        + "             <totalImpuesto>\n"
                        + "             <codigo>2</codigo>\n"
                        + "                 <codigoPorcentaje>2</codigoPorcentaje>\n"
                        + "                 <baseImponible>1098.90</baseImponible>\n"
                        + "                 <tarifa>12</tarifa>\n"
                        + "                 <valor>131.86</valor>\n"
                        + "              </totalImpuesto>\n"
                        + "         </totalConImpuestos>\n"
                        + "                 <propina>0</propina>\n"
                        + "                 <importeTotal>1230.76</importeTotal>\n"
                        + "                 <moneda>DOLAR</moneda>\n"
                        + "         <pagos>\n"
                        + "                 <pago>\n"
                        + "                     <formaPago>20</formaPago>\n"
                        + "                     <total>1230.76</total>\n"
                        + "                     <plazo>30.0000</plazo>\n"
                        + "                     <unidadTiempo>dias</unidadTiempo>\n"
                        + "                 </pago>\n"
                        + "         </pagos>\n"
                        + "         <valorRetIva>0.0</valorRetIva>\n"
                        + "         <valorRetRenta>0.0</valorRetRenta>\n"
                        + "    </infoFactura>     <detalles>\n"
                        + "        <detalle>\n"
                        + "            <codigoPrincipal>679_73</codigoPrincipal>\n"
                        + "            <descripcion>ABRIGO IMPERMIABLE PESADO </descripcion>\n"
                        + "            <cantidad>94.00</cantidad>\n"
                        + "            <precioUnitario>11.2500</precioUnitario>\n"
                        + "            <descuento>0.00</descuento>\n"
                        + "            <precioTotalSinImpuesto>1057.50</precioTotalSinImpuesto>\n"
                        + "            <impuestos>\n"
                        + "                <impuesto>\n"
                        + "                    <codigo>2</codigo>\n"
                        + "                    <codigoPorcentaje>2</codigoPorcentaje>\n"
                        + "                    <tarifa>12</tarifa>\n"
                        + "                    <baseImponible>1057.50</baseImponible>\n"
                        + "                    <valor>1057.50</valor>\n"
                        + "                </impuesto>\n"
                        + "            </impuestos>\n"
                        + "        </detalle>\n"
                        + "        <detalle>\n"
                        + "            <codigoPrincipal>A25-32</codigoPrincipal>\n"
                        + "            <descripcion>ABRAZADERAS 25/32</descripcion>\n"
                        + "            <cantidad>1.00</cantidad>\n"
                        + "            <precioUnitario>0.4500</precioUnitario>\n"
                        + "            <descuento>0.00</descuento>\n"
                        + "            <precioTotalSinImpuesto>0.45</precioTotalSinImpuesto>\n"
                        + "            <impuestos>\n"
                        + "                <impuesto>\n"
                        + "                    <codigo>2</codigo>\n"
                        + "                    <codigoPorcentaje>2</codigoPorcentaje>\n"
                        + "                    <tarifa>12</tarifa>\n"
                        + "                    <baseImponible>0.45</baseImponible>\n"
                        + "                    <valor>0.45</valor>\n"
                        + "                </impuesto>\n"
                        + "            </impuestos>\n"
                        + "        </detalle>\n"
                        + "        <detalle>\n"
                        + "            <codigoPrincipal>A3-4</codigoPrincipal>\n"
                        + "            <descripcion>ABRAZADERAS 3/4</descripcion>\n"
                        + "            <cantidad>91.00</cantidad>\n"
                        + "            <precioUnitario>0.4500</precioUnitario>\n"
                        + "            <descuento>0.00</descuento>\n"
                        + "            <precioTotalSinImpuesto>40.95</precioTotalSinImpuesto>\n"
                        + "            <impuestos>\n"
                        + "                <impuesto>\n"
                        + "                    <codigo>2</codigo>\n"
                        + "                    <codigoPorcentaje>2</codigoPorcentaje>\n"
                        + "                    <tarifa>12</tarifa>\n"
                        + "                    <baseImponible>40.95</baseImponible>\n"
                        + "                    <valor>40.95</valor>\n"
                        + "                </impuesto>\n"
                        + "            </impuestos>\n"
                        + "        </detalle>\n"
                        + "    </detalles>\n"
                        + "    <infoAdicional>\n"
                        + "<campoAdicional nombre=\"DIRECCION\">LLANO CHICO</campoAdicional>\n"
                        + "<campoAdicional nombre=\"E-MAIL\">asetemp@hotmail.com</campoAdicional>\n"
                        + "<campoAdicional nombre=\"APELLIDO\">HINOJOSA</campoAdicional>\n"
                        + "<campoAdicional nombre=\"NOMBRE\">WASHINGTON</campoAdicional>\n"
                        + "<campoAdicional nombre=\"NOMBRECOMERCIAL\">BESTSYSTEM</campoAdicional>\n"
                        + "<campoAdicional nombre=\"CIUDAD\">QUITO</campoAdicional>\n"
                        + "<campoAdicional nombre=\"TELEFONO\">0993530018</campoAdicional>\n"
                        + "<campoAdicional nombre=\"CELULAR\">0987654321 </campoAdicional>\n"
                        + "<campoAdicional nombre=\"PLAZO\"> </campoAdicional>\n"
                        + "<campoAdicional nombre=\"DIAS\">30.0000</campoAdicional>\n"
                        + "<campoAdicional nombre=\"TARIFAIMP\">12</campoAdicional>\n"
                        + "   </infoAdicional>\n"
                        + "</factura>")
                build.append(linea)
                /*IMPRIME EL XML DE LA FACTURA*/
                println("XML $build")
                var pathArchivoSalida = "D:\\GENERADOS\\"
                /*EN EL CASO DE NO EXISTIR LOS DIRECTORIOS LOS CREA*/
                val folderGen = File(pathArchivoSalida)
                if (!folderGen.exists()) {
                    folderGen.mkdirs()
                }
                /*ruta de salida del archivo XML
            generados o autorizados para enviar al cliente
            dependiendo la ruta enviada en el parametro del metodo */
                pathArchivoSalida = pathArchivoSalida + "FACT-00018171.xml"

                //String pathArchivoSalida = "D:\\";
                out = FileOutputStream(pathArchivoSalida)
                out.write(build.toString().toByteArray())
                //GRABA DATOS EN FACTURA//
                return pathArchivoSalida
                //return Utilidades.DirXMLPrincipal + Utilidades.DirSinFirmas + "FACT-" + cabdoc.getEstablecimientodocumento() + "-" + cabdoc.getPuntoemisiondocumento() + "-" + cabdoc.getSecuencialcar() + ".xml";
            } catch (ex: FileNotFoundException) {
                println("ERROR EN LA GENERACION DE XML FACTURA  FileNotFoundException$ex")
            } catch (ex: IOException) {
                println("ERROR EN LA GENERACION DE XML FACTURA IOException $ex")
            }

            return null
        }
    }
    //</editor-fold >

}
