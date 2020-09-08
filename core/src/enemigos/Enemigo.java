package enemigos;

import com.badlogic.gdx.graphics.g2d.Sprite;

import personajes.Entidad;
import personajes.PersonajePrincipal;

public abstract class Enemigo extends Entidad {

	protected int vida, vidaMax;
	
	public Enemigo(Sprite sprite) {
		super(sprite);
	}

	public abstract void hacerdanio(PersonajePrincipal jugador);
	
	public abstract void recibirdanio(int cantidad);
	
	public int getVida() {
		return vida;
	}
	
	public int getVidaMax() {
		return vidaMax;
	}
	
}
