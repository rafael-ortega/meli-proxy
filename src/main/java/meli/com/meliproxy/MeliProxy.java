package meli.com.meliproxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http proxy server Meli
 *
 * @author bustosjoha
 */
public class MeliProxy {

    public static void main(String[] args) throws IOException {

        System.out.println("meliproxy-main: inicio el proxy");

        final int puerto = 9024;
        try {
            HttpServer httpd = HttpServer.create(new InetSocketAddress(puerto), 0);
            System.out.println("meliproxy-main: se crea puerto:" + puerto);

            //HttpContext ctx = httpd.createContext("/",Prueba::gestionarSolicitud);
            httpd.createContext("/categories", HttpControlador::gestionarSolicitud);
            httpd.createContext("/estadisticas", HttpControlador::obtenerEstadisticas);
            httpd.createContext("/eliminarHistorico", HttpControlador::eliminarHistorico);

            httpd.setExecutor(null);
            //ctx.setHandler(Prueba::gestionarSolicitud);

            httpd.start();
            System.out.println("meliproxy-main: server arriba");
        } catch (IOException e) {
            System.out.println("meliproxy-main- Erorr" + e.getMessage());
        }

    }

    

    private static void stringToJson(String jsonLine) {
        /* 
      JsonElement jelement = new JsonParser().parse(jsonLine);
    JsonObject  jobject = jelement.getAsJsonObject();
    jobject = jobject.getAsJsonObject("data");
    JsonArray jarray = jobject.getAsJsonArray("translations");
    jobject = jarray.get(0).getAsJsonObject();
    String result = jobject.get("translatedText").getAsString();
        System.out.println("El objeto es:"+result);
    return result;
         */
    }



    private static void parsePostParameters(HttpExchange exchange)
            throws IOException {

        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            @SuppressWarnings("unchecked")
            Map<String, Object> parameters
                    = (Map<String, Object>) exchange.getAttribute("parameters");
            InputStreamReader isr
                    = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseQuery(String query, Map<String, Object> parameters)
            throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

}
