package gestorMapas;

import personajes.PersonajePrincipal;
import pueblo.Pueblo;
import pueblo.Pueblo2;

public abstract class CargadorMapas {
	
	private static Mapa pueblo;
	private static Mapa pueblo2;
	
	public static void crearMapas(PersonajePrincipal jugador) {
		pueblo = new Pueblo(jugador);
		pueblo2 = new Pueblo2(jugador);
		pueblo.crear();
		pueblo2.crear();
	}
	
	public static Mapa getMapaPueblo() {
		return pueblo;
	}
	
	public static Mapa getMapaPueblo2() {
		return pueblo2;
	}
	
}
