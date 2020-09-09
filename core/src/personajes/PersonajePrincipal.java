package personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import enemigos.Enemigo;
import mapas.gestorMapas.Mapa;
import utilidades.Entrada;
import utilidades.Utiles;

public class PersonajePrincipal extends Entidad {

	private final float vidaMaxima = 100;
	private float vidaActual = vidaMaxima;
	private boolean corriendo = false;
	private final int staminaMax = 300;
	private int stamina = staminaMax;
	private final int VELOCIDAD_NORMAL = 1, VELOCIDAD_CORRIENDO = 2;
	private final int FILAS = 4, COLUMNAS = 6;
	private float tiempoAni = 0;
	private Animation<TextureRegion> animacionDerecha, animacionIzquierda, animacionAbajo, animacionArriba;
	private Animation<TextureRegion> animacionActual;
	private boolean enMovimiento = false, enColision = false;
	private int posicionFinal = 0;
	private int direccion = 0;
	private final Rectangle rectanguloJugador;
	private final int correccionAlto = 72, correccionAncho = 223; //Es posible calcularlo?
//	private int posicionJugadorTileX = (int)(posicion.x/ Recursos.ANCHO_TILE);
//	private int posicionJugadorTileY = (int)(posicion.y/ Recursos.ALTO_TILE);
	private final int reducciondanio = 45;
	private final int danio = 20;

	public PersonajePrincipal() {
		super(Utiles.heroeAbajoSprite);
		crearAnimaciones();
		rectanguloJugador = new Rectangle(getBoundingRectangle());
	}
	
	public void dibujar(final Texture textura) {
		this.setTexture(textura);
		this.draw(Utiles.batch);
		
		this.setX(this.posicion.x);
		this.setY(this.posicion.y);
		
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
		
	}
	
	public void dibujar(final TextureRegion tr) {
		Utiles.batch.draw(tr, posicion.x, posicion.y, tr.getRegionWidth(), tr.getRegionHeight());
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
	}
	
	public void recibirdanio(final float cantidad) {
		this.vidaActual-=(cantidad/reducciondanio);
		if(this.vidaActual < 0) {
			vidaActual = 0;
		}
	}
	
	public void hacerdanio(final Enemigo enemigo) {
		enemigo.recibirdanio(danio);    //TODO Hacerlo funcionar!
	}
	
	public void crearAnimacion() {
		
		final Texture heroeAnimacion = new Texture("personajes/heroe/heroeSheet.png");
		final TextureRegion[][] tmp = TextureRegion.split(heroeAnimacion, (heroeAnimacion.getWidth()-correccionAncho)/COLUMNAS, (heroeAnimacion.getHeight()-correccionAlto)/FILAS);
		final TextureRegion[] walkFramesDerecha = new TextureRegion[COLUMNAS];
		final TextureRegion[] walkFramesIzquierda = new TextureRegion[COLUMNAS];
		final TextureRegion[] walkFramesArriba = new TextureRegion[COLUMNAS];
		final TextureRegion[] walkFramesAbajo = new TextureRegion[COLUMNAS];
		
		int index = 0;
		
		for (int i = 0; i < FILAS; i++) {
			index = 0;
			for (int j = 0; j < COLUMNAS; j++) {
				if(i == 0) {
					walkFramesDerecha[index++] = tmp[i][j];
				}
				if(i == 1) {
					walkFramesAbajo[index++] = tmp[i][j];
				}
				if(i == 2) {
					walkFramesIzquierda[index++] = tmp[i][j];
				}
				if(i == 3) {
					walkFramesArriba[index++] = tmp[i][j];
				}
			}
		}
		
		animacionDerecha = new Animation<TextureRegion>(0.2f,walkFramesDerecha);
		animacionIzquierda = new Animation<TextureRegion>(0.2f, walkFramesIzquierda);
		animacionAbajo = new Animation<TextureRegion>(0.2f, walkFramesAbajo);
		animacionArriba = new Animation<TextureRegion>(0.2f, walkFramesArriba);
		
	}
	
	TextureRegion frameActual;
	public void animar(final boolean animar) {
		if(animar) {
			tiempoAni += Gdx.graphics.getDeltaTime();
			frameActual = animacionActual.getKeyFrame(tiempoAni, true);
			dibujar(frameActual);
		}
	}
	
	public void crearAnimaciones() {
		crearAnimacion();
	}
	
	public void controlarMovimiento(final Entrada entrada, final Mapa mapa) {
		
		final float oldX = posicion.x, oldY = posicion.y;
		float newX = posicion.x, newY = posicion.y;
		enMovimiento = false;
	
		//Correr
		if(entrada.pressedShift && stamina > 0) {
			corriendo = true;
		}
		else if(!entrada.pressedShift || stamina == 0){
			corriendo = false;
			if(stamina < staminaMax && !entrada.pressedShift) {
				stamina++;
			}
		}
		
		if(entrada.pressedW) {
			enMovimiento = true;
			if(!corriendo) {
				newY+= VELOCIDAD_NORMAL;
			}
			if(corriendo && stamina > 0) {
				newY+= VELOCIDAD_CORRIENDO;
				reducirStamina();
			}
			rectanguloJugador.setY(newY);
			enColision = mapa.comprobarColision(this);
			direccion = 1;
			animacionActual = animacionArriba;
			if(!enColision) {
				this.posicion.y = newY;
			}else {
				this.posicion.y = oldY;
			}
						
		}
		if(entrada.pressedA) {
			enMovimiento = true;
			if(!corriendo) {
				newX-= VELOCIDAD_NORMAL;
			}
			if(corriendo && stamina > 0) {
				newX-= VELOCIDAD_CORRIENDO;
				reducirStamina();
			}
			rectanguloJugador.setX(newX);
			enColision = mapa.comprobarColision(this);
			direccion = 3;
			animacionActual = animacionIzquierda;
			if(!enColision) {
				this.posicion.x = newX;
			}else {
				this.posicion.x = oldX;
			}
			
		}
		if(entrada.pressedS) {
			enMovimiento = true;
			if(!corriendo) {
				newY-= VELOCIDAD_NORMAL;
			}
			if(corriendo && stamina > 0) {
				newY-= VELOCIDAD_CORRIENDO;
				reducirStamina();
			}
			rectanguloJugador.setY(newY);
			enColision = mapa.comprobarColision(this);
			direccion = 4;
			animacionActual = animacionAbajo;
			if(!enColision) {
				this.posicion.y = newY;
			}else {
				this.posicion.y = oldY;
			}
			
		}
		if(entrada.pressedD) {
			enMovimiento = true;
			if(!corriendo) {
				newX+= VELOCIDAD_NORMAL;
			}
			if(corriendo && stamina > 0) {
				newX+= VELOCIDAD_CORRIENDO;
				reducirStamina();
			}
			rectanguloJugador.setX(newX);
			enColision = mapa.comprobarColision(this);
			direccion = 2;
			animacionActual = animacionDerecha;
			if(!enColision) {
				this.posicion.x = newX;
			}else {
				this.posicion.x = oldX;
			}
		}
	
//		System.out.println("Posicion x en movimiento: "+posicion.x);
//		System.out.println("Posicion y en movimiento: "+posicion.y);
//		System.out.println("Stamina: "+stamina);
		
	}
	
	public void reducirStamina() {
		stamina--;
		if(stamina < 0) {
			stamina = 0;
		}
	}
	
	public void movimiento() {
		
		Utiles.batch.begin();
		if(isEnMovimiento()) {
			
			if(getDireccionJugador() == 1) {
				animar(true);
				setPosicionFinal(1);
			}
			if(getDireccionJugador() == 2) {
				animar(true);
				setPosicionFinal(2);
			}
			if(getDireccionJugador() == 3) {
				animar(true);
				setPosicionFinal(3);
			}
			if(getDireccionJugador() == 4) {
				animar(true);
				setPosicionFinal(4);
			}
		}	
		else {
//			animar(false);
			if(getPosicionFinal() == 0) {
				dibujar(Utiles.heroeAbajo);
			}
			if(getPosicionFinal() == 1) {
				dibujar(Utiles.heroeArriba);
			}
			if(getPosicionFinal() == 2) {
				dibujar(Utiles.heroeDerecha);
			}
			if(getPosicionFinal() == 3) {
				dibujar(Utiles.heroeIzquierda);
			}
			if(getPosicionFinal() == 4) {
				dibujar(Utiles.heroeAbajo);
			}
			
		}
		
	Utiles.batch.end();
		
	}
	
	public void mostrarColisiones() {
		Utiles.sr.setAutoShapeType(true);
		Utiles.sr.begin();
		
			Utiles.sr.set(ShapeType.Line);
			Utiles.sr.setColor(Color.GREEN);
			Utiles.sr.rect(getRectangulo().x, getRectangulo().y, getRectangulo().getWidth(), getRectangulo().getHeight());

		Utiles.sr.end();
	}
	
	public float getVidaActual() {
		return vidaActual;
	}
	
	public float getVidaMaxima() {
		return vidaMaxima;
	}
	
	public boolean isEnMovimiento() {
		return enMovimiento;
	}
	
	public int getPosicionFinal() {
		return posicionFinal;
	}
	
	public void setPosicionFinal(final int posicionFinal) {
		this.posicionFinal = posicionFinal;
	}
	
	public int getDireccionJugador() {
		return direccion;
	}
	
	public boolean isEnColision() {
		return enColision;
	}
	
	public void setEnColision(final boolean enColision) {
		this.enColision = enColision;
	}
	
	public int getStamina() {
		return stamina;
	}
	
	public Rectangle getRectangulo() {
		return rectanguloJugador;
	}

//	public int getPosicionJugadorTileX() {
//		return posicionJugadorTileX;
//	}
//
//	public int getPosicionJugadorTileY() {
//		return posicionJugadorTileY;
//	}
	
	public int getStaminaMax() {
		return staminaMax;
	}
	
}
