package pantallas;

import com.badlogic.gdx.Screen;
import gestorMapas.Mapa;
import juego.TwilightOfDarknessPrincipal;
import personajes.PersonajePrincipal;
import utilidades.Entrada;
import utilidades.Hud;
import utilidades.Recursos;
import utilidades.Render;
import utilidades.UtilHerramientas;

public class PantallaPrincipal implements Screen {
	
	private PersonajePrincipal jugador;
	private Entrada entrada;
	private Mapa mapa;
	private Mapa mapaSiguiente;
	private boolean cambiarMapa;
	private Hud hud;
	
	public PantallaPrincipal(Entrada entrada, TwilightOfDarknessPrincipal game, Mapa mapa, PersonajePrincipal jugador) {
		this.jugador = jugador;
		this.mapa = mapa;
		this.entrada = entrada;
	}
	
	@Override
	public void show() {
//		mapa.crear();
		hud = new Hud(jugador);
	}

	@Override
	public void render(float delta) {
		
		actualizar();
		
		Render.limpiarPantalla();
		UtilHerramientas.sr.setProjectionMatrix(mapa.getCamara().combined);
		
		mapa.setearCamara(jugador.getPosicion().x, jugador.getPosicion().y);
		mapa.renderizar();
		cambiarMapa = mapa.comprobarSalidaMapa();
		
		if(cambiarMapa) {
			mapaSiguiente = mapa.cambioMapa();
			cambiarMapa(mapaSiguiente);
		}
		
		mapa.mostrarColisiones();
		jugador.mostrarColisiones();
		
		jugador.controlarMovimiento(entrada,mapa);
//		System.out.println("X Jugador: "+jugador.getPosicion().x);
//		System.out.println("Y Jugador: "+jugador.getPosicion().y);
		controlarSalida();
		hud.mostrarStats();
	}
	
	public void cambiarMapa(Mapa mapa) {
		this.mapa = mapa;
		this.mapa.setPosicionJugador();
	}
	
	public void controlarSalida() {
		if(entrada.pressedEsc) {
			Recursos.posJugadorX = (int) jugador.getPosicion().x;
			Recursos.posJugadorY = (int) jugador.getPosicion().y;
			
			System.exit(0);
		}
	}
	
	private void actualizar() {
		
	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void pause() {
		

	}

	@Override
	public void resume() {
		

	}
	
	@Override
	public void hide() {
		

	}

	@Override
	public void dispose() {
		UtilHerramientas.batch.dispose();
		mapa.dispose();
		UtilHerramientas.sr.dispose();
	}

}
