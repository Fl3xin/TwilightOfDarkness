package juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import gestorMapas.CargadorMapas;
import gestorMapas.Mapa;
import pantalas.CargadorPantallas;
import pantalas.MenuOpciones;
import pantalas.MenuPrincipal;
import pantalas.PantallaPrincipal;
import personajes.PersonajePrincipal;
import utilidades.Entrada;
import utilidades.Hilo;
import utilidades.Utiles;

public class TwilightOfDarknessPrincipal extends Game {
	
	private Entrada entrada;
	private PersonajePrincipal jugador;
	Hilo hilo;
	
	@Override
	public void create() {
		Utiles.archivos.leerArchivo();
		
		jugador = new PersonajePrincipal(Utiles.heroeAbajoSprite);
		jugador.crearAnimaciones();
		entrada = new Entrada();
		Gdx.input.setInputProcessor(entrada);
		CargadorMapas.crearMapas(jugador);
		setMenuPrincipal();
		hilo = new Hilo();
		hilo.startHilo();
		
	}
	
	public void setPantallaPrincipal(Mapa mapa) {
		CargadorPantallas.pantallaPrincipal = new PantallaPrincipal(entrada, this, mapa, jugador);
		setScreen(CargadorPantallas.pantallaPrincipal);
	}
	
	public void setMenuOpciones() {
		CargadorPantallas.menuOpciones = new MenuOpciones(entrada, this);
		setScreen(CargadorPantallas.menuOpciones);
	}
	
	
	public void setMenuPrincipal() {
		CargadorPantallas.menuPrincipal = new MenuPrincipal(entrada, this);
		setScreen(CargadorPantallas.menuPrincipal);
	}
	
}
