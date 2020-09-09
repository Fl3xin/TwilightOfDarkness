package mapas.gestorMapas;

import mapas.dungeons.Dungeon1;
import mapas.pueblo.Pueblo;
import mapas.pueblo.Pueblo2;
import personajes.PersonajePrincipal;

public class GestorMapas {
	
	private Mapa pueblo;
	private Mapa pueblo2;
	private Mapa dungeon1;
	
	public void crearMapas(PersonajePrincipal jugador) {
		pueblo = new Pueblo(jugador, this);
		pueblo2 = new Pueblo2(jugador, this);
		dungeon1 = new Dungeon1(jugador, this);
		pueblo.crear();
		pueblo2.crear();
		dungeon1.crear();
	}
	
	public Mapa getMapaPueblo() {
		return pueblo;
	}
	
	public Mapa getMapaPueblo2() {
		return pueblo2;
	}
	
	public Mapa getMapaDungeon1() {
		return dungeon1;
	}
	
}
