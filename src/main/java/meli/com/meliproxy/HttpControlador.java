package meli.com.meliproxy;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author bustosjoha
 */
public class HttpControlador {

    public static void gestionarSolicitud(HttpExchange exchange) throws IOException {

        System.out.println("meliproxy-gestionarSolicitud: incicio gestionarSolicitud");
        int codigo = 200;
        String contenido = "Respuesta desde el servidor HTTP 2";

        try {

            String path = exchange.getRequestURI().getPath();

            String origen = exchange.getRemoteAddress().getAddress().toString();
            System.out.println("meliproxy-gestionarSolicitud: la ip origen es:\" + origen");

            if (!Archivo.leerArchivo(origen, "")) {
                System.out.println("meliproxy-gestionarSolicitud: la ip origen es:\" + origen");
                System.out.println("meliproxy-gestionarSolicitud: la ip aun no existe en archivo");
            }

            if (Archivo.leerArchivo("", path)) {

                String respuesta = "No permitido. Supero el limite de accesos a este Path...";
                exchange.sendResponseHeaders(301, respuesta.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(respuesta.getBytes());
                os.close();

            } else {
                Archivo.escribirArchivo(path, true);
                if (!Archivo.leerArchivo(origen, "")) {
                    Archivo.escribirArchivo(origen, false);

                    exchange.getResponseHeaders().add("Location", "https://api.mercadolibre.com" + path);
                    exchange.getResponseHeaders().add("Cache-Control", "no-cache");
                    exchange.getResponseHeaders().add("Cache-Control", "no-store");
                    exchange.sendResponseHeaders(301, path.getBytes().length);

                    OutputStream os = exchange.getResponseBody();

                    os.write(path.getBytes());
                    os.close();

                } else {

                    String respuesta = "No permitido. Supero el limite de accesos por IP...";
                    exchange.sendResponseHeaders(codigo, respuesta.getBytes().length);

                    OutputStream os = exchange.getResponseBody();
                    os.write(respuesta.getBytes());
                    os.close();

                }
            }

        } catch (IOException e) {
            System.out.println("meliproxy-gestionarSolicitud: Error:" + e.getMessage());
        }

    }

    public static void obtenerEstadisticas(HttpExchange exchange) throws IOException {

        int codigo = 200;
        String respuesta = "{\"a\":........... \"b\"}";

        //exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.getResponseHeaders().add("Content-length", Integer.toString(respuesta.getBytes().length));
        exchange.sendResponseHeaders(codigo, respuesta.getBytes().length);

        OutputStream os = exchange.getResponseBody();

        os.write(respuesta.getBytes());

        os.close();

    }

    public static void eliminarHistorico(HttpExchange exchange) throws IOException {

        String respuesta = "respuesta";
        if (Archivo.eliminarArchivos()) {
            respuesta = "Se ha eliminado el historico del Proxy Meli";
        } else {
            respuesta = "NO se ha eliminado el historico del Proxy Meli";
        }

        int codigo = 200;

        exchange.sendResponseHeaders(codigo, respuesta.getBytes().length);
        exchange.getResponseHeaders().add("Cache-Control", "no-cache");
        exchange.getResponseHeaders().add("Cache-Control", "no-store");

        OutputStream os = exchange.getResponseBody();

        os.write(respuesta.getBytes());

        os.close();

    }

    public static void obtenerHistorico(HttpExchange exchange) throws IOException {

        String respuesta = Archivo.contarElementosArchivo().toString();

        final int codigo = 200;

        exchange.sendResponseHeaders(codigo, respuesta.getBytes().length);
        exchange.getResponseHeaders().add("Cache-Control", "no-cache");
        exchange.getResponseHeaders().add("Cache-Control", "no-store");

        OutputStream os = exchange.getResponseBody();

        os.write(respuesta.getBytes());

        os.close();
    }

}
