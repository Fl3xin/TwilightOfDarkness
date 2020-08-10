package personajes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Entidad extends Sprite {
	
	private char direccion;
	protected float alto, ancho;
	protected Vector2 posicion = new Vector2();
	
	public Entidad(Sprite sprite) {
		super(sprite);
	}
	
	public void mover(int desplazamientoX, int desplazamientoY) {
		
		if(desplazamientoX > 0) {
			direccion = 'e';
		}
		if(desplazamientoX < 0) {
			direccion = 'o';
		}
		if(desplazamientoY > 0) {
			direccion = 's';
		}
		if(desplazamientoY < 0) {
			direccion = 'n';
		}
		
	}
	
	public char getDireccion() {
		return direccion;
	}
	
	public Vector2 getPosicion() {
		return posicion;
	}
	
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion;
	}
	
	public void setPosicion(int x, int y) {
		this.posicion.x = x;
		this.posicion.y = y;
	}
	
}
