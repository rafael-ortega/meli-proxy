/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meli.com.meliproxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author bustosjoha
 */
public class Archivo {

    static final int MAXIMO_PATH = 3;
    static final int MAXIMO_IP = 3;

    public static void escribirArchivo(String texto, Boolean isPath) {

        try {
            String ruta = "proxylog_ip.txt";
            if (isPath) {
                ruta = "proxylog_path.txt";
            }

            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("\n" + texto);
            bw.close();
        } catch (Exception e) {
            System.out.println("Error creando el archivo:" + e.getMessage() + "causa:" + e.getCause() + "traza:" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static boolean leerArchivo(String ipOrigen, String path) throws FileNotFoundException, IOException {

        Boolean exiteValor = false;
        try {

            System.out.println("la ip que esta llegando es:" + ipOrigen);

            File doc = new File("proxylog_ip.txt");
            if (!path.isEmpty()) {
                doc = new File("proxylog_path.txt");
            }

            BufferedReader obj = new BufferedReader(new FileReader(doc));

            String strng;
            ArrayList lineas = new ArrayList();
            while ((strng = obj.readLine()) != null) {
                lineas.add(strng);
                System.out.println(strng + ":desde el archivo");
            }

            int contador = 0;
            for (int i = 0; i <= lineas.size(); i++) {

                System.out.println("pos 1:" + lineas.get(i));
                if (!path.isEmpty()) {
                    System.out.println("el path ya existe.......");
                    contador++;
                    if (contador > MAXIMO_PATH) {
                        exiteValor = true;
                    }
                } else {
                    if (ipOrigen.equalsIgnoreCase(lineas.get(i).toString())) {
                        System.out.println("la ip es igual al leer desde el archivo...");
                        contador++;
                        if (contador > MAXIMO_IP) {
                            exiteValor = true;
                        }
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("ocurrio un erro en metodo leerArchivo:" + e.getMessage());
        }

        return exiteValor;
    }

    public static boolean eliminarArchivos() {

       Boolean respuesta=false;
        File ficheroPath = new File("proxylog_ip.txt");
        File ficheroIp = new File("proxylog_path.txt");
        
        try {
            if (ficheroIp.delete() && ficheroPath.delete())
            respuesta=true;
        } catch (Exception e) {
            System.err.println("Ocurrio un error eliminando los archivos logs:"+e.getMessage());
        }
        
        
        return respuesta;
        
    }

}
