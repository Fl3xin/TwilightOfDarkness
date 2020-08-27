package gestorMapas;

import java.io.Serializable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import personajes.Entidad;
import personajes.PersonajePrincipal;
import utilidades.Recursos;
import utilidades.Utiles;

public abstract class Mapa implements Serializable {

	private static final long serialVersionUID = 4589632894337340154L;
	
	protected TiledMap mapa;
	protected OrthogonalTiledMapRenderer renderer;
	protected OrthographicCamera camara;
	protected TmxMapLoader cargadorMapa;
	protected PersonajePrincipal jugador;
	protected GestorMapas gestorMapas;
	
	public Mapa(PersonajePrincipal jugador, GestorMapas gestorMapas) {
		this.jugador = jugador;
		this.gestorMapas = gestorMapas;
		cargadorMapa = new TmxMapLoader();
		mapa = new TiledMap();
		camara = new OrthographicCamera(Recursos.ANCHO, Recursos.ALTO);
	}
	
	public void setearCamara(float xJugador, float yJugador) {
		
		Utiles.batch.setProjectionMatrix(camara.combined);
		camara.position.set(xJugador, yJugador, 0); //Centrar camara en el jugador
		camara.update();
		renderer.setView(camara);
		
	}
	
	public abstract void setPosicionJugador();
	
	public abstract void renderizar(); 
	
	public abstract void crear();
	
	public abstract boolean comprobarColision(Entidad entidad);
	
	public abstract boolean comprobarSalidaMapa();
	
	public abstract Mapa cambioMapa();
	
	public abstract void mostrarColisiones();
	
	public void zoom(boolean acercar) {
		if(acercar) {
			camara.zoom += 0.1f;
		}else {
			if(camara.zoom > 0.5f) {
				camara.zoom -= 0.1f;
			}
		}
	}

	public TiledMap getMapa() {
		return mapa;
	}
	
	public OrthographicCamera getCamara() {
		return camara;
	}
	
	public abstract Array<Rectangle> getRectColision();
	
	public abstract Array<Polygon> getPoliColision();
	
	public abstract Array<Rectangle> getZonasCambioMapa();
	
	public abstract Rectangle getLimiteMapa();
	
	public void dispose() {
		renderer.dispose();
		mapa.dispose();
	}
	
}
