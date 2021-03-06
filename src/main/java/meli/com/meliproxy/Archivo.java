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
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author bustosjoha
 */
public class Archivo {

    static final int MAXIMO_PATH = 3;
    static final int MAXIMO_IP = 5;

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
            file=null;
        } catch (Exception e) {
            System.out.println("Error creando el archivo:" + e.getMessage() + "causa:" + e.getCause() + "traza:" + Arrays.toString(e.getStackTrace()));
        }
    }
    
    
    public static HashMap contarElementosArchivo() throws FileNotFoundException, IOException {
    
        File doc = new File("proxylog_ip.txt");
        String strng;
        ArrayList lineas = new ArrayList();
        HashMap contador=new HashMap<>();
        int frecuencia;
        
        try {
            
            BufferedReader obj = new BufferedReader(new FileReader(doc));
        
          while ((strng = obj.readLine()) != null) {
                lineas.add(strng);
          
         }
          
          
          
          for (int i = 0; i < lineas.size(); i++) {
              
                if(lineas.get(i)!=null && !lineas.get(i).equals("") ){
                frecuencia=Collections.frequency(lineas, lineas.get(i));
                contador.put(lineas.get(i), frecuencia);
                }
                
          }
          
          System.out.println("Arreglo:");
          System.out.println(contador);
               
         
        } catch (Exception e) {
            System.out.println("Error en contarElementosArchivo(): Mensaje"+e.getMessage()+" Cusa:"+e.getCause()+" Trace:"+e.getStackTrace());
            e.printStackTrace();
        }
        
         return contador;    
    }
    
    public static boolean leerArchivo(String ipOrigen, String path) throws FileNotFoundException, IOException {

        Boolean existeValor = false;
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
                if (!path.isEmpty() && path.equalsIgnoreCase(lineas.get(i).toString())) {
                    System.out.println("el path ya existe.......");
                    contador++;
                    if (contador > MAXIMO_PATH) {
                        existeValor = true;
                    }
                } else {
                    if (ipOrigen.equalsIgnoreCase(lineas.get(i).toString())) {
                        System.out.println("la ip es igual al leer desde el archivo...");
                        contador++;
                        if (contador > MAXIMO_IP) {
                            existeValor = true;
                        }
                    }

                }

            }
        } catch (Exception e) {
            System.out.println("ocurrio un erro en metodo leerArchivo:" + e.getMessage());
        }

        return existeValor;
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
        
        ficheroPath=null;
        ficheroIp=null;
        return respuesta;
        
    }

}
