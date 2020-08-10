package gestorMapas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import utilidades.Entrada;
import utilidades.Utiles;

public abstract class Mapa {

	protected TiledMap mapa;
	protected OrthogonalTiledMapRenderer renderer;
	protected OrthographicCamera camara;
	protected TmxMapLoader cargadorMapa;
	
	public void setearCamara(float xJugador, float yJugador) {
		
		Utiles.batch.setProjectionMatrix(camara.combined);
		camara.position.set(xJugador, yJugador, 0); //Centrar camara en el jugador
		camara.update();
		renderer.setView(camara);
		
	}
	
	public abstract void setPosicionJugador();
	
	public abstract void renderizar(Entrada entrada); 
	
	public abstract void crear();
	
	public abstract boolean comprobarColision();
	
	public abstract boolean comprobarSalidaMapa();
	
	public abstract Mapa cambioMapa();
	
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
