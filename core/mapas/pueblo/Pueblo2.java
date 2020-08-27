package pueblo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import gestorMapas.GestorMapas;
import gestorMapas.Mapa;
import personajes.Entidad;
import personajes.PersonajePrincipal;
import utilidades.Entrada;
import utilidades.Utiles;

public class Pueblo2 extends Mapa{

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
	
	public Pueblo2(PersonajePrincipal jugador, GestorMapas gestorMapas) {
		super(jugador, gestorMapas);
		posicionJugadorSpawnInferior = new Vector2(35,200);
		posicionJugadorSpawnSuperior = new Vector2(35,520);
	}
	
	public void crear() {
		
		mapa = cargadorMapa.load("mapas/pueblo/Pueblo_2.tmx");
		crearCapas();
		renderer = new OrthogonalTiledMapRenderer(mapa);
		camara.update();
	
	}

	public void crearCapas() {
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
	}

	public void setPosicionJugador() {
		System.out.println("Mapa indicador: "+Utiles.mapaIndicadorPos);
		if(Utiles.mapaIndicadorPos == 1) {
			jugador.setPosicion(posicionJugadorSpawnInferior.x, posicionJugadorSpawnInferior.y);
		}
		if(Utiles.mapaIndicadorPos == 2) {
			jugador.setPosicion(posicionJugadorSpawnSuperior.x, posicionJugadorSpawnSuperior.y);
		}
	}
	
	public void renderizar() {
		
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
	
	public void mostrarColisiones() {
		Utiles.sr.begin(ShapeType.Line);
		
			Utiles.sr.setColor(Color.GREEN);
			Utiles.sr.rect(jugador.getRectangulo().x, jugador.getRectangulo().y, jugador.getRectangulo().getWidth(), jugador.getRectangulo().getHeight());
			
			Utiles.sr.setColor(Color.BLUE);
			Utiles.sr.rect(0,0,getLimiteMapa().getWidth(), getLimiteMapa().getHeight());
			
			for(int i = 0 ; i < getRectColision().size ; i++) {
				Utiles.sr.rect(getRectColision().get(i).getX(), getRectColision().get(i).getY(), getRectColision().get(i).getWidth(), getRectColision().get(i).getHeight());
			}
			for (int i = 0; i < getPoliColision().size; i++) {
				Utiles.sr.polygon(getPoliColision().get(i).getVertices());
			}
			for (int i = 0; i < getZonasCambioMapa().size; i++) {
				Utiles.sr.rect(getZonasCambioMapa().get(i).getX(), getZonasCambioMapa().get(i).getY(), getZonasCambioMapa().get(i).getWidth(), getZonasCambioMapa().get(i).getHeight());
			}
			
		Utiles.sr.end();
	}
	
	public boolean comprobarColision(Entidad entidad) {
		
		boolean colision = false;
		for(int i = 0 ; i < rectColision.size ; i++) {
			
			if(Intersector.overlaps(entidad.getRectangulo(), rectColision.get(i))) {
				System.out.println("Colision");
				colision = true;
			}
			
		}
		for (int i = 0; i < poliColision.size; i++) {
			if(poliColision.get(i).contains(entidad.getRectangulo().getX(), entidad.getRectangulo().getY()) ||
			   poliColision.get(i).contains(entidad.getRectangulo().getX(), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight()) ||
			   poliColision.get(i).contains(entidad.getRectangulo().getX()+entidad.getRectangulo().getWidth(),entidad.getRectangulo().getY()) ||
			   poliColision.get(i).contains(entidad.getRectangulo().getX()+entidad.getRectangulo().getWidth(), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight()) ||
			   poliColision.get(i).contains(entidad.getRectangulo().getX()+(entidad.getRectangulo().getWidth()/2), entidad.getRectangulo().getY()) ||
			   poliColision.get(i).contains(entidad.getRectangulo().getX()+(entidad.getRectangulo().getWidth()/2), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight())) {
				System.out.println("Colision poligono");
			}
		}
		
		if(limiteMapaRect.contains(entidad.getRectangulo())) {
			//Todo bien
		}else {
			System.out.println("Fuera del mapa");
			colision = true;
		}
		return colision;
		
	}
	
	public boolean comprobarSalidaMapa() {
		cambiarMapa = false;
		
			
		if(Intersector.overlaps(jugador.getRectangulo(), zonasCambioMapa.get(1)) && propiedadObjeto.get(1).containsKey("caminoInferior") ) {
			System.out.println("Cambio mapa inferior");
			Utiles.mapaIndicadorPos = 3;
			cambiarMapa = true;
		}else if(Intersector.overlaps(jugador.getRectangulo(), zonasCambioMapa.get(0)) && propiedadObjeto.get(0).containsKey("caminoSuperior")) {
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
			return gestorMapas.getMapaPueblo();
		}else {
			return gestorMapas.getMapaPueblo2();
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
