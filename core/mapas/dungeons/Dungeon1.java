package dungeons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import enemigos.Alien;
import enemigos.Enemigo;
import gestorMapas.GestorMapas;
import gestorMapas.Mapa;
import personajes.Entidad;
import personajes.PersonajePrincipal;
import utilidades.Utiles;

public class Dungeon1 extends Mapa{

	private MapObjects aliensObj;
	private Alien[] aliens;
	private Rectangle rectanguloPiso;
	private Array<Rectangle> colisiones; 
	
	public Dungeon1(PersonajePrincipal jugador, GestorMapas gestorMapas) {
		super(jugador, gestorMapas);
		jugador.setPosicion(160, 140);
	}

	@Override
	public void setPosicionJugador() {
	}

	@Override
	public void renderizar() {
		renderer.render();
		jugador.movimiento();
		
		Utiles.sr.begin(ShapeType.Line);
		for (int i = 0; i < aliens.length; i++) {
			aliens[i].comportamiento(jugador, this);
			aliens[i].mostrarColisiones();
		}
		Utiles.sr.end();
		
		for (int j = 0; j < aliens.length; j++) {
			aliens[j].barraVida();
		}
	}

	@Override
	public void crear() {
		
		mapa = cargadorMapa.load("mapas/dungeons/dungeon1/dungeon1.tmx");
		crearEnemigos();
		crearCapas();
		renderer = new OrthogonalTiledMapRenderer(mapa);
		camara.update();
		
	}

	public void crearCapas() {
		
		colisiones = new Array<Rectangle>();
		
		MapObjects piso = mapa.getLayers().get("pisoObj").getObjects();
		MapObjects colisionables = mapa.getLayers().get("colisionables").getObjects();
		
		for (int i = 0; i < colisionables.getCount(); i++) {
			RectangleMapObject rectColiObj = (RectangleMapObject) colisionables.get(i);
			Rectangle rectColi = rectColiObj.getRectangle();
			colisiones.add(new Rectangle(rectColi.getX(), rectColi.getY(), rectColi.getWidth(), rectColi.getHeight()));
		}
		
		System.out.println(piso.getCount());
		RectangleMapObject rectPiso = (RectangleMapObject) piso.get(0);

		rectanguloPiso = rectPiso.getRectangle();
		
	}
	
	public void mostrarColisiones() {
		Utiles.sr.begin(ShapeType.Line);
		
			Utiles.sr.setColor(Color.BLUE);
			Utiles.sr.rect(0,0,getLimiteMapa().getWidth(), getLimiteMapa().getHeight());
			
			for(int i = 0 ; i < getRectColision().size ; i++) {
				Utiles.sr.rect(getRectColision().get(i).getX(), getRectColision().get(i).getY(), getRectColision().get(i).getWidth(), getRectColision().get(i).getHeight());
			}
			
		Utiles.sr.end();
	}
	
	public void crearEnemigos() {
		
		aliensObj = new MapObjects();
		aliensObj = mapa.getLayers().get("aliens").getObjects();
		aliens = new Alien[aliensObj.getCount()];
	
		for (int i = 0; i < aliensObj.getCount(); i++) {
			aliens[i] = new Alien();
			aliens[i].setPosicion((float) aliensObj.get(i).getProperties().get("x"), (float) aliensObj.get(i).getProperties().get("y"));
			aliens[i].getRectangulo().setX((float) aliensObj.get(i).getProperties().get("x"));
			aliens[i].getRectangulo().setY((float) aliensObj.get(i).getProperties().get("y"));
			aliens[i].getCirculoAlien().setX((float) aliensObj.get(i).getProperties().get("x"));
			aliens[i].getCirculoAlien().setY((float) aliensObj.get(i).getProperties().get("y"));
		}
		
	}
	
//	@Override
//	public boolean comprobarColisionEnemigos(PersonajePrincipal jugador) {
//		boolean colision = false;
//			
//		for (int i = 0; i < aliens.length; i++) {
//			if(Intersector.overlaps(jugador.getRectangulo(), aliens[i].getRectangulo())) {
//				colision = true;
//			}
//		}
//		
//		return colision;
//	}
	
	@Override
	public boolean comprobarColision(Entidad entidad) {
		boolean colision = false;
		
		for (int i = 0; i < colisiones.size; i++) {
			if(Intersector.overlaps(entidad.getRectangulo(), colisiones.get(i))) {
				colision = true;
			}
		}
		
		if(rectanguloPiso.contains(entidad.getRectangulo())) {
			//Todo bien
		}else {
			System.out.println("Fuera del mapa");
			colision = true;
		}
		return colision;
	}

	@Override
	public boolean comprobarSalidaMapa() {
		
		return false;
	}

	@Override
	public Mapa cambioMapa() {
		
		return null;
	}

	@Override
	public Array<Rectangle> getRectColision() {
		return colisiones;
	}

	@Override
	public Array<Polygon> getPoliColision() {
		
		return null;
	}

	@Override
	public Array<Rectangle> getZonasCambioMapa() {
		
		return null;
	}

	@Override
	public Rectangle getLimiteMapa() {
		return rectanguloPiso;
	}

}
