package pueblo;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import gestorMapas.CargadorMapas;
import gestorMapas.Mapa;
import personajes.PersonajePrincipal;
import utilidades.Entrada;
import utilidades.Recursos;
import utilidades.Utiles;

public class Pueblo extends Mapa{
	
	private PersonajePrincipal jugador;
	private Array<Rectangle> rectColision;
	private Array<Polygon> poliColision;
	private Array<Rectangle> zonasCambioMapa;
	private Array<MapProperties> propiedadObjeto;
	private Rectangle limiteMapa;
	
	private TiledMapTileLayer capaSuelo;
	private TiledMapTileLayer capaCasas;
	private TiledMapTileLayer capa2;
	private TiledMapTileLayer capaDecoracion;
	
	private Vector2 posicionJugadorSpawn;
	private Vector2 posicionJugadorSpawnInferior;
	private Vector2 posicionJugadorSpawnSuperior;
	
	private boolean cambiarMapa;
	
	public Pueblo(PersonajePrincipal jugador) {
		posicionJugadorSpawn = new Vector2(500,200);
		posicionJugadorSpawnInferior = new Vector2(915, 210);
		posicionJugadorSpawnSuperior = new Vector2(915, 500);
		this.jugador = jugador;
		jugador.setPosicion(Recursos.posJugadorX, Recursos.posJugadorY);
	}
	
	public void crear() {
		
		cargadorMapa = new TmxMapLoader();
	
		mapa = new TiledMap();
		mapa = cargadorMapa.load("mapas/pueblo/Pueblo_1.tmx");
		
		capaSuelo = (TiledMapTileLayer) mapa.getLayers().get("suelo");
		capaCasas = (TiledMapTileLayer) mapa.getLayers().get("casa");
		capa2 = (TiledMapTileLayer) mapa.getLayers().get("capa2");
		capaDecoracion = (TiledMapTileLayer) mapa.getLayers().get("decoracion");
		
		MapObjects capaSuelo = mapa.getLayers().get("pisoObj").getObjects();
		MapObjects capaPoligonos = mapa.getLayers().get("colisionablesPoli").getObjects();
		MapObjects objetosMapa = mapa.getLayers().get("colisionables").getObjects();
		MapObjects cambioMapa = mapa.getLayers().get("cambioMapa").getObjects();
		
		RectangleMapObject rectPiso = (RectangleMapObject) capaSuelo.get(0);
		limiteMapa = rectPiso.getRectangle();
		
		rectColision = new Array<Rectangle>();
		poliColision = new Array<Polygon>();
		zonasCambioMapa = new Array<Rectangle>();
		propiedadObjeto = new Array<MapProperties>();
		
		for(int i = 0 ; i < objetosMapa.getCount() ; i++) {
			RectangleMapObject rectObj = (RectangleMapObject) objetosMapa.get(i);
			Rectangle rect = rectObj.getRectangle();
			rectColision.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		}
		
		for (int i = 0; i < capaPoligonos.getCount(); i++) {
			PolygonMapObject poliObj = (PolygonMapObject) capaPoligonos.get(i);
			Polygon poli = poliObj.getPolygon();
			poliColision.add(new Polygon(poli.getTransformedVertices()));
		}
		
		for (int i = 0; i < cambioMapa.getCount(); i++) {
			RectangleMapObject rectObj = (RectangleMapObject) cambioMapa.get(i);
			Rectangle rect = rectObj.getRectangle();
			zonasCambioMapa.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
			propiedadObjeto.add(cambioMapa.get(i).getProperties());
		}
		
//		System.out.println("Objetos mapa: "+objetosMapa.getCount());
//		System.out.println("Array size: "+rectColision.size);
		
		renderer = new OrthogonalTiledMapRenderer(mapa);
		
		camara = new OrthographicCamera(Recursos.ANCHO, Recursos.ALTO);
//		camara.position.set(camara.viewportWidth /2f, camara.viewportHeight /2f, 0);
		camara.update();
		
	}

	public void setPosicionJugador() {
		System.out.println("Mapa indicador: "+Utiles.mapaIndicadorPos);
		if(Utiles.mapaIndicadorPos == 3) {
			jugador.setPosicion(posicionJugadorSpawnInferior);
		}
		if(Utiles.mapaIndicadorPos == 4) {
			jugador.setPosicion(posicionJugadorSpawnSuperior);
		}
	}
	
	public void renderizar(Entrada entrada) {
		
		renderizarSuelo();
		renderizarDeco();
		jugador.movimiento();
		//zoom(entrada.zoom);
		renderizarCasas();
		renderizarCapa2();
		
	}
	
	public void renderizarSuelo() {
		renderer.getBatch().begin();
		renderer.renderTileLayer(capaSuelo);
		renderer.getBatch().end();
	}
	
	public void renderizarCasas() {
		renderer.getBatch().begin();
		renderer.renderTileLayer(capaCasas);
		renderer.getBatch().end();
	}
	
	public void renderizarCapa2() {
		renderer.getBatch().begin();
		renderer.renderTileLayer(capa2);
		renderer.getBatch().end();
	}
	
	public void renderizarDeco() {
		renderer.getBatch().begin();
		renderer.renderTileLayer(capaDecoracion);
		renderer.getBatch().end();
	}
	
	public boolean comprobarColision(){
		boolean colision = false;
		for(int i = 0 ; i < rectColision.size ; i++) {
			
			if(Intersector.overlaps(jugador.getRectanguloJugador(), rectColision.get(i))) {
				System.out.println("Colision");
				colision = true;
			}
			
		}
		for (int i = 0; i < poliColision.size; i++) {
			if(poliColision.get(i).contains(jugador.getRectanguloJugador().getX(), jugador.getRectanguloJugador().getY()) ||
			   poliColision.get(i).contains(jugador.getRectanguloJugador().getX(), jugador.getRectanguloJugador().getY()+jugador.getRectanguloJugador().getHeight()) ||
			   poliColision.get(i).contains(jugador.getRectanguloJugador().getX()+jugador.getRectanguloJugador().getWidth(),jugador.getRectanguloJugador().getY()) ||
			   poliColision.get(i).contains(jugador.getRectanguloJugador().getX()+jugador.getRectanguloJugador().getWidth(), jugador.getRectanguloJugador().getY()+jugador.getRectanguloJugador().getHeight()) ||
			   poliColision.get(i).contains(jugador.getRectanguloJugador().getX()+(jugador.getRectanguloJugador().getWidth()/2), jugador.getRectanguloJugador().getY()) ||
			   poliColision.get(i).contains(jugador.getRectanguloJugador().getX()+(jugador.getRectanguloJugador().getWidth()/2), jugador.getRectanguloJugador().getY()+jugador.getRectanguloJugador().getHeight())) {
				System.out.println("Colision poligono");
			}
		}
		
		if(limiteMapa.contains(jugador.getRectanguloJugador())) {
			//Todo bien
		}else {
			System.out.println("Fuera del mapa");
			colision = true;
		}
		return colision;
	}
	
	public boolean comprobarSalidaMapa() {
		cambiarMapa = false;
		int i = 0;
		while(i < zonasCambioMapa.size) {
			if(Intersector.overlaps(jugador.getRectanguloJugador(), zonasCambioMapa.get(i))) {
				if(propiedadObjeto.get(i).containsKey("caminoInferior")) {
					System.out.println("Cambio mapa inferior");
					Utiles.mapaIndicadorPos = 1;
					cambiarMapa = true;
					break;
				}
				else if(propiedadObjeto.get(i).containsKey("caminoSuperior")) {
					System.out.println("Cambio mapa superior");
					Utiles.mapaIndicadorPos = 2;
					cambiarMapa = true;
					break;
				}else {
					cambiarMapa = false;
					break;
				}
			}
			i++;
		}
		
//		if(Intersector.overlaps(jugador.getRectanguloJugador(), zonasCambioMapa.get(1)) && propiedadObjeto.get(1).containsKey("caminoInferior") ) {
//			System.out.println("Cambio mapa inferior");
//			Utiles.mapaIndicadorPos = 1;
//			cambiarMapa = true;
//		}else if(Intersector.overlaps(jugador.getRectanguloJugador(), zonasCambioMapa.get(0)) && propiedadObjeto.get(0).containsKey("caminoSuperior")) {
//			System.out.println("Cambio mapa superior");
//			Utiles.mapaIndicadorPos = 2;
//			cambiarMapa = true;
//		}
//		else {
//			cambiarMapa = false;
//		}
			
		
		
		return cambiarMapa;
	}
	
	public Mapa cambioMapa() {
		if(cambiarMapa) {
			return CargadorMapas.getMapaPueblo2();
		}else {
			return CargadorMapas.getMapaPueblo();
		}
	}
	
	public Array<Rectangle> getRectColision() {
		return rectColision;
	}
	
	public Array<Polygon> getPoliColision() {
		return poliColision;
	}
	
	public Array<Rectangle> getZonasCambioMapa() {
		return zonasCambioMapa;
	}
	
	public Rectangle getLimiteMapa() {
		return limiteMapa;
	}
	
}
