package interfaces;

import mapas.gestorMapas.Mapa;
import personajes.PersonajePrincipal;

public interface Movible {

	public void comportamiento(PersonajePrincipal jugador, Mapa mapa);
	
}
