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
import utilidades.Recursos;
import utilidades.Utiles;

public class Pueblo extends Mapa{
	
	private Array<Rectangle> rectColision;
	private Array<Polygon> poliColision;
	private Array<Rectangle> zonasCambioMapa;
	private Array<MapProperties> propiedadObjeto;
	private Rectangle limiteMapa;
	
	private TiledMapTileLayer capaSuelo;
	private TiledMapTileLayer capaCasas;
	private TiledMapTileLayer capa2;
	private TiledMapTileLayer capaDecoracion;
	
	private Vector2 posicionJugadorSpawnInferior;
	private Vector2 posicionJugadorSpawnSuperior;
	
	private boolean cambiarMapa;
	
	public Pueblo(PersonajePrincipal jugador, GestorMapas gestorMapas) {
		super(jugador, gestorMapas);
		posicionJugadorSpawnInferior = new Vector2(915, 210);
		posicionJugadorSpawnSuperior = new Vector2(915, 500);
		jugador.setPosicion(Recursos.posJugadorX, Recursos.posJugadorY);
	}
	
	public void crear() {
	
		mapa = cargadorMapa.load("mapas/pueblo/Pueblo_1.tmx");
		crearCapas();
		renderer = new OrthogonalTiledMapRenderer(mapa);
		camara.update(); //Probar eliminacion
		
	}

	public void crearCapas() {
		//Obtiene las capas del mapa
		capaSuelo = (TiledMapTileLayer) mapa.getLayers().get("suelo");
		capaCasas = (TiledMapTileLayer) mapa.getLayers().get("casa");
		capa2 = (TiledMapTileLayer) mapa.getLayers().get("capa2");
		capaDecoracion = (TiledMapTileLayer) mapa.getLayers().get("decoracion");
		
		//Obtiene los objetos de las distintas capas
		MapObjects capaSuelo = mapa.getLayers().get("pisoObj").getObjects();
		MapObjects capaPoligonos = mapa.getLayers().get("colisionablesPoli").getObjects();
		MapObjects objetosMapa = mapa.getLayers().get("colisionables").getObjects();
		MapObjects cambioMapa = mapa.getLayers().get("cambioMapa").getObjects();
		
		RectangleMapObject rectPiso = (RectangleMapObject) capaSuelo.get(0);
		limiteMapa = rectPiso.getRectangle();
		
		//Creacion de arrays para colisiones
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
	}

	public void setPosicionJugador() {
		System.out.println("Mapa indicador: "+Utiles.mapaIndicadorPos);
		if(Utiles.mapaIndicadorPos == 3) {
			jugador.setPosicion(posicionJugadorSpawnInferior.x, posicionJugadorSpawnInferior.y);
		}
		if(Utiles.mapaIndicadorPos == 4) {
			jugador.setPosicion(posicionJugadorSpawnSuperior.x, posicionJugadorSpawnSuperior.y);
		}
	}
	
	public void renderizar() {
		
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
	
	public void mostrarColisiones() {
		
		Utiles.sr.begin(ShapeType.Line);
			
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
	
	public boolean comprobarColision(Entidad entidad){
		boolean colision = false;
		for(int i = 0 ; i < rectColision.size ; i++) {
			
			if(Intersector.overlaps(entidad.getRectangulo(), rectColision.get(i))) {
				System.out.println("Colision");
				colision = true;
			}
			
		}
//		for (int i = 0; i < poliColision.size; i++) {
//			if(poliColision.get(i).contains(entidad.getRectangulo().getX(), entidad.getRectangulo().getY()) || //Obtiene la esquina inferior izquierda del jugador
//			   poliColision.get(i).contains(entidad.getRectangulo().getX(), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight()) || //Obtiene la esquina superior izquierda
//			   poliColision.get(i).contains(entidad.getRectangulo().getX()+entidad.getRectangulo().getWidth(),entidad.getRectangulo().getY()) || //Obtiene la esquina inferior derecha
//			   poliColision.get(i).contains(entidad.getRectangulo().getX()+entidad.getRectangulo().getWidth(), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight()) || //Obtiene la esquina superrior derecha
//			   poliColision.get(i).contains(entidad.getRectangulo().getX()+(entidad.getRectangulo().getWidth()/2), entidad.getRectangulo().getY()) || //Obtiene el punto medio de la base del rectangulo
//			   poliColision.get(i).contains(entidad.getRectangulo().getX()+(entidad.getRectangulo().getWidth()/2), entidad.getRectangulo().getY()+entidad.getRectangulo().getHeight())) {
//				System.out.println("Colision poligono");
//			}
//		}
//		
		if(!limiteMapa.contains(entidad.getRectangulo())) {
			System.out.println("Fuera del mapa");
			colision = true;
		}
		
		return colision;
	}
	
	public boolean comprobarSalidaMapa() {
		cambiarMapa = false;
		int i = 0;
		 do {
			if(Intersector.overlaps(jugador.getRectangulo(), zonasCambioMapa.get(i))) {
				if(propiedadObjeto.get(i).containsKey("caminoInferior")) {
					System.out.println("Cambio mapa inferior");
					Utiles.mapaIndicadorPos = 1; //TODO cambiar la variable a la clase padre Mapa
					cambiarMapa = true;
					break;
				}
				else if(propiedadObjeto.get(i).containsKey("caminoSuperior")) {
					System.out.println("Cambio mapa superior");
					Utiles.mapaIndicadorPos = 2; //TODO cambiar la variable a la clase padre Mapa
					cambiarMapa = true;
					break;
				}
			}
			i++;
		} while(i < zonasCambioMapa.size);
		
//		if(Intersector.overlaps(jugador.getRectangulo(), zonasCambioMapa.get(1)) && propiedadObjeto.get(1).containsKey("caminoInferior") ) {
//			System.out.println("Cambio mapa inferior");
//			Utiles.mapaIndicadorPos = 1;
//			cambiarMapa = true;
//		}else if(Intersector.overlaps(jugador.getRectangulo(), zonasCambioMapa.get(0)) && propiedadObjeto.get(0).containsKey("caminoSuperior")) {
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
			return gestorMapas.getMapaPueblo2();
		}else {
			return gestorMapas.getMapaPueblo();
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
