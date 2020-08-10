package utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Archivos {

	File archivo;
	String ruta, rutaCompleta;
	String nombreArchivo;
	
	public Archivos(String ruta, String nombreArchivo) {
		
		archivo = new File(ruta,nombreArchivo);
		
		this.ruta = ruta;
		this.nombreArchivo = nombreArchivo;
		
		try {
			if(archivo.createNewFile()) {
				System.out.println("Archivo creado correctamente");
			}else {
				System.out.println("Archivo ya creado");
			}
		} catch (IOException e) {
			System.out.println("No se ha podido crear el archivo");
			e.printStackTrace();
		}
			
	}
	
	public void escribirArchivo() {
		FileWriter fichero = null;
		PrintWriter pw = null;
		
		try {
			fichero = new FileWriter("C:/ArchivoJava/PartidaGuardada.txt");
			pw = new PrintWriter(fichero);
			
			for (int i = 0; i < 1; i++) {
				pw.println(Recursos.horasJugadas);
				pw.println(Recursos.minutosJugados);
				pw.println(Recursos.posJugadorX);
				pw.println(Recursos.posJugadorY);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fichero != null) {
					fichero.close();
					pw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	public void leerArchivo() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		int cont = 0;
		int[] tiempo = new int[2];
		int[] posJugador = new int[2];
		try {
			archivo = new File(ruta,nombreArchivo);
			fileReader = new FileReader(archivo);
			bufferedReader = new BufferedReader(fileReader);
			
			String linea;
			while((linea = bufferedReader.readLine())!= null) {
				System.out.println(linea);
				
				if(cont == 0) {
					tiempo[cont] = Integer.parseInt(linea);
					Recursos.horasJugadas = tiempo[cont];
				}
				if(cont == 1) {
					tiempo[cont] = Integer.parseInt(linea);
					Recursos.minutosJugados = tiempo[cont];
				}
				if(cont == 2) {
					posJugador[cont-2] = Integer.parseInt(linea);
					Recursos.posJugadorX = posJugador[cont-2];
				}
				if(cont == 3) {
					posJugador[cont-2] = Integer.parseInt(linea);
					Recursos.posJugadorY = posJugador[cont-2];
				}
				
				cont++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileReader != null) {
					fileReader.close();
					bufferedReader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
}
