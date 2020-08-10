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

public class Pueblo2 extends Mapa{

	private PersonajePrincipal jugador;
	private Array<Rectangle> rectColision;
	private Array<Polygon> poliColision;
	private Array<Rectangle> zonasCambioMapa;
	private Array<MapProperties> propiedadObjeto;
	private Rectangle limiteMapaRect;
	
	private TiledMapTileLayer capaSuelo;
	private TiledMapTileLayer capaCasas;
	private TiledMapTileLayer capa2;
	private TiledMapTileLayer capaDecoracion;
	
	private Vector2 posicionJugadorSpawnInferior;
	private Vector2 posicionJugadorSpawnSuperior;
	
	private boolean cambiarMapa;
	
	public Pueblo2(PersonajePrincipal jugador) {
		posicionJugadorSpawnInferior = new Vector2(35,200);
		posicionJugadorSpawnSuperior = new Vector2(35,520);
		this.jugador = jugador;
	}
	
	public void crear() {
		
		cargadorMapa = new TmxMapLoader();
		mapa = new TiledMap();
		mapa = cargadorMapa.load("mapas/pueblo/Pueblo_2.tmx");
		
		capaSuelo = (TiledMapTileLayer) mapa.getLayers().get("suelo");
		capaDecoracion = (TiledMapTileLayer) mapa.getLayers().get("decoracion");
		capaCasas = (TiledMapTileLayer) mapa.getLayers().get("casas");
		capa2 = (TiledMapTileLayer) mapa.getLayers().get("capa2");
		
		MapObjects limiteMapa = mapa.getLayers().get("pisoObj").getObjects();
		MapObjects colisionables = mapa.getLayers().get("colisionables").getObjects();
		MapObjects colisionablesPoli = mapa.getLayers().get("colisionablesPoli").getObjects();
		MapObjects cambioMapa = mapa.getLayers().get("cambioMapa").getObjects();
		
		RectangleMapObject rectSuelo = (RectangleMapObject) limiteMapa.get(0);
		limiteMapaRect = rectSuelo.getRectangle();
		
		rectColision = new Array<Rectangle>();
		poliColision = new Array<Polygon>();
		zonasCambioMapa = new Array<Rectangle>();
		propiedadObjeto = new Array<MapProperties>();
		
		for(int i = 0 ; i < colisionables.getCount() ; i++) {
			RectangleMapObject rectObj = (RectangleMapObject) colisionables.get(i);
			Rectangle rect = rectObj.getRectangle();
			rectColision.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		}
		
		for (int i = 0; i < colisionablesPoli.getCount(); i++) {
			PolygonMapObject poliObj = (PolygonMapObject) colisionablesPoli.get(i);
			Polygon poli = poliObj.getPolygon();
			poliColision.add(new Polygon(poli.getTransformedVertices()));
		}
		
		for (int i = 0; i < cambioMapa.getCount(); i++) {
			RectangleMapObject rectObj = (RectangleMapObject) cambioMapa.get(i);
			Rectangle rect = rectObj.getRectangle();
			zonasCambioMapa.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
			propiedadObjeto.add(cambioMapa.get(i).getProperties());
		}
		
		renderer = new OrthogonalTiledMapRenderer(mapa);
		camara = new OrthographicCamera(Recursos.ANCHO, Recursos.ALTO);
		camara.update();
	
	}

	public void setPosicionJugador() {
		System.out.println("Mapa indicador: "+Utiles.mapaIndicadorPos);
		if(Utiles.mapaIndicadorPos == 1) {
			jugador.setPosicion(posicionJugadorSpawnInferior);
		}
		if(Utiles.mapaIndicadorPos == 2) {
			jugador.setPosicion(posicionJugadorSpawnSuperior);
		}
	}
	
	public void renderizar(Entrada entrada) {
		
		renderizarSuelo();
		renderizarDeco();
		jugador.movimiento();
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
	
	public boolean comprobarColision() {
		
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
		
		if(limiteMapaRect.contains(jugador.getRectanguloJugador())) {
			//Todo bien
		}else {
			System.out.println("Fuera del mapa");
			colision = true;
		}
		return colision;
		
	}
	
	public boolean comprobarSalidaMapa() {
		cambiarMapa = false;
		
			
		if(Intersector.overlaps(jugador.getRectanguloJugador(), zonasCambioMapa.get(1)) && propiedadObjeto.get(1).containsKey("caminoInferior") ) {
			System.out.println("Cambio mapa inferior");
			Utiles.mapaIndicadorPos = 3;
			cambiarMapa = true;
		}else if(Intersector.overlaps(jugador.getRectanguloJugador(), zonasCambioMapa.get(0)) && propiedadObjeto.get(0).containsKey("caminoSuperior")) {
			System.out.println("Cambio mapa superior");
			Utiles.mapaIndicadorPos = 4;
			cambiarMapa = true;
		}
		else {
			cambiarMapa = false;
		}
			
		
			
		return cambiarMapa;
	}
	
	public Mapa cambioMapa() {
		if(cambiarMapa) {
			return CargadorMapas.getMapaPueblo();
		}else {
			return CargadorMapas.getMapaPueblo2();
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
		return limiteMapaRect;
	}
	
}
