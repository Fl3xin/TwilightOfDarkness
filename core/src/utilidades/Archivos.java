package utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Archivos {

	private File archivo;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	public DatosJuego datosJuego;
	
	public Archivos(DatosJuego datosJuego) {
		
		this.datosJuego = datosJuego;
		
		archivo = new File("C:\\Users\\messi\\Desktop","ttd-savegame.dat");
		try {
			input = new ObjectInputStream(new FileInputStream(archivo));
			cargarPartida();
		} catch (Exception e) {
			try {
				output = new ObjectOutputStream(new FileOutputStream(archivo));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
	}
	
	public void guardarPartida() {
		try {
			input.close();
			output.writeObject(datosJuego);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cargarPartida() {
		try {
			if(archivo!= null) {
				datosJuego = (DatosJuego) input.readObject();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
