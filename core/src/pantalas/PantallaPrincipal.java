package pantalas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import gestorMapas.Mapa;
import juego.TwilightOfDarknessPrincipal;
import personajes.PersonajePrincipal;
import utilidades.Entrada;
import utilidades.Hud;
import utilidades.Recursos;
import utilidades.Render;
import utilidades.Utiles;

public class PantallaPrincipal implements Screen {
	
	private PersonajePrincipal jugador;
	private Entrada entrada;
	@SuppressWarnings("unused")
	private TwilightOfDarknessPrincipal game;
	private Mapa mapa;
	private Mapa mapaSiguiente;
	private boolean cambiarMapa;
	private Hud hud;
	
	public PantallaPrincipal(Entrada entrada, TwilightOfDarknessPrincipal game, Mapa mapa, PersonajePrincipal jugador) {
		this.jugador = jugador;
		this.mapa = mapa;
		this.entrada = entrada;
		this.game = game;
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
		Utiles.sr.setProjectionMatrix(mapa.getCamara().combined);
		
		mapa.setearCamara(jugador.getPosicion().x, jugador.getPosicion().y);
		mapa.renderizar(entrada);
		cambiarMapa = mapa.comprobarSalidaMapa();
		
		if(cambiarMapa) {
			mapaSiguiente = mapa.cambioMapa();
			cambiarMapa(mapaSiguiente);
		}
		
		mostrarColisiones();
		
		jugador.controlarMovimiento(entrada,mapa);
//		System.out.println("X Jugador: "+jugador.getPosicion().x);
//		System.out.println("Y Jugador: "+jugador.getPosicion().y);
		controlarSalida();
		hud.mostrarBarraEnergia();
	}
	
	public void cambiarMapa(Mapa mapa) {
		this.mapa = mapa;
		this.mapa.setPosicionJugador();
	}
	
	public void controlarSalida() {
		if(entrada.pressedEsc) {
			Recursos.posJugadorX = (int) jugador.getPosicion().x;
			Recursos.posJugadorY = (int) jugador.getPosicion().y;
			
			Utiles.archivos.escribirArchivo();
			System.exit(0);
		}
	}
	
	private void mostrarColisiones() {
		Utiles.sr.begin(ShapeType.Line);
		
			Utiles.sr.setColor(Color.GREEN);
			Utiles.sr.rect(jugador.getRectanguloJugador().x, jugador.getRectanguloJugador().y, jugador.getRectanguloJugador().getWidth(), jugador.getRectanguloJugador().getHeight());
			
			Utiles.sr.setColor(Color.BLUE);
			Utiles.sr.rect(0,0,mapa.getLimiteMapa().getWidth(), mapa.getLimiteMapa().getHeight());
			
			for(int i = 0 ; i < mapa.getRectColision().size ; i++) {
				Utiles.sr.rect(mapa.getRectColision().get(i).getX(), mapa.getRectColision().get(i).getY(), mapa.getRectColision().get(i).getWidth(), mapa.getRectColision().get(i).getHeight());
			}
			for (int i = 0; i < mapa.getPoliColision().size; i++) {
				Utiles.sr.polygon(mapa.getPoliColision().get(i).getVertices());
			}
			for (int i = 0; i < mapa.getZonasCambioMapa().size; i++) {
				Utiles.sr.rect(mapa.getZonasCambioMapa().get(i).getX(), mapa.getZonasCambioMapa().get(i).getY(), mapa.getZonasCambioMapa().get(i).getWidth(), mapa.getZonasCambioMapa().get(i).getHeight());
			}
			
		Utiles.sr.end();
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
		Utiles.batch.dispose();
		mapa.dispose();
		Utiles.sr.dispose();
	}

}
