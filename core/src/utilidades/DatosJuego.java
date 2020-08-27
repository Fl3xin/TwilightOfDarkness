package utilidades;

import java.io.Serializable;

import juego.TwilightOfDarknessPrincipal;

public class DatosJuego implements Serializable{

	private static final long serialVersionUID = 6733800243266485638L;
	
	private TwilightOfDarknessPrincipal game;
	
	public DatosJuego(TwilightOfDarknessPrincipal game) {
		this.game = game;
	}
	
	public TwilightOfDarknessPrincipal getGame() {
		return game;
	}
	
}
