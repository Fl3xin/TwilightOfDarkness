package utilidades;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Utiles {

	public static SpriteBatch batch = new SpriteBatch();
	
	public static ShapeRenderer sr = new ShapeRenderer();
	
	public static Archivos archivos = new Archivos("C:/ArchivoJava","PartidaGuardada.txt");
	
	public static int mapaIndicadorPos = 0;
//	public static Texture personajePrincipalTextura = new Texture("personajes/Ladron/thief.png");
//	public static Sprite personajePrincipalSprite = new Sprite(personajePrincipalTextura);
//	
//	public static Texture enemigoTextura = new Texture("personajes/personajePrincipal/dodoQuietoIzquierda.png");
//	public static Sprite enemigoSprite = new Sprite(enemigoTextura);
	
//	public static Texture ppAbajo = new Texture("personajes/personajePrincipal/personajeQuietoAbajo.png");
//	public static Sprite ppAbajoSprite = new Sprite(ppAbajo);
//	
//	public static Texture ppArriba = new Texture("personajes/personajePrincipal/personajeQuietoArriba.png");
//	public static Sprite ppArribaSprite = new Sprite(ppArriba);
//	
//	public static Texture ppDerecha = new Texture("personajes/personajePrincipal/personajeQuietoDerecha.png");
//	public static Sprite ppDerechaSprite = new Sprite(ppDerecha);
//	
//	public static Texture ppIzquierda = new Texture("personajes/personajePrincipal/personajeQuietoIzquierda.png");
//	public static Sprite ppIzquierdaSprite = new Sprite(ppIzquierda);
	
	public static Texture heroeAbajo = new Texture("personajes/heroe/heroeQuietoAbajo.png");
	public static Sprite heroeAbajoSprite = new Sprite(heroeAbajo);
	
	public static Texture heroeArriba = new Texture("personajes/heroe/heroeQuietoArriba.png");
	public static Sprite heroeArribaSprite = new Sprite(heroeArriba);
	
	public static Texture heroeDerecha = new Texture("personajes/heroe/heroeQuietoDerecha.png");
	public static Sprite heroeDerechaSprite = new Sprite(heroeDerecha);
	
	public static Texture heroeIzquierda = new Texture("personajes/heroe/heroeQuietoIzquierda.png");
	public static Sprite heroeIzquierdaSprite = new Sprite(heroeIzquierda);
	
	public static Random r = new Random();
}
