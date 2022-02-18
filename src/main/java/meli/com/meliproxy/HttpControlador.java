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
        final int CODIGO_RESPUESTA = 200;
        String contenido = "Respuesta desde el servidor HTTP 2";

        String newUrl = "http://www.google.com";

        String a = "http://localhost:8080/TestWeb/index.jsp";
        URL url = new URL(newUrl);
        URLConnection conn = url.openConnection();

        try {

            String path = exchange.getRequestURI().getPath();

            String origen = exchange.getRemoteAddress().getAddress().toString();
            System.out.println("meliproxy-gestionarSolicitud: la ip origen es:\" + origen");

            Archivo archivo = new Archivo();
            if (!archivo.leerArchivo(origen, "")) {
                System.out.println("meliproxy-gestionarSolicitud: la ip origen es:\" + origen");
                System.out.println("meliproxy-gestionarSolicitud: la ip aun no existe en archivo");
            }

            if (archivo.leerArchivo("", path)) {

                String respuesta = "no puede acceder a este recurso por PATH..";
                exchange.sendResponseHeaders(CODIGO_RESPUESTA, respuesta.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(respuesta.getBytes());
                os.close();

            } else {
                archivo.escribirArchivo(path, true);
                if (!archivo.leerArchivo(origen, "")) {
                    archivo.escribirArchivo(origen, false);

                    exchange.getResponseHeaders().add("Location", "https://api.mercadolibre.com" + path);
                    exchange.sendResponseHeaders(CODIGO_RESPUESTA, path.getBytes().length);

                    OutputStream os = exchange.getResponseBody();

                    os.write(path.getBytes());
                    os.close();

                } else {

                    String respuesta = "no puede acceder a este recurso..";
                    exchange.sendResponseHeaders(CODIGO_RESPUESTA, respuesta.getBytes().length);

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
   
        final int CODIGO_RESPUESTA = 200;
        String respuesta = "{\"a\":........... \"b\"}";

        //exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.getResponseHeaders().add("Content-length", Integer.toString(respuesta.getBytes().length));
        exchange.sendResponseHeaders(CODIGO_RESPUESTA, respuesta.getBytes().length);

        OutputStream os = exchange.getResponseBody();

        os.write(respuesta.getBytes());

        os.close();

    }
}
