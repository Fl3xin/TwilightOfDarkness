package personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import enemigos.Enemigo;
import gestorMapas.Mapa;
import utilidades.Entrada;
import utilidades.UtilHerramientas;

public class PersonajePrincipal extends Entidad {

	private float vidaMaxima = 100;
	private float vidaActual = vidaMaxima;
	private boolean corriendo = false;
	private int staminaMax = 300;
	private int stamina = staminaMax;
	private final int VELOCIDAD_NORMAL = 1, VELOCIDAD_CORRIENDO = 2;
	private final int FILAS = 4, COLUMNAS = 6;
	private float tiempoAni = 0;
	private Animation<TextureRegion> animacionDerecha, animacionIzquierda, animacionAbajo, animacionArriba;
	private Animation<TextureRegion> animacionActual;
	private boolean enMovimiento = false, enColision = false;
	private int posicionFinal = 0;
	private int direccion = 0;
	private Rectangle rectanguloJugador;
	private int correccionAlto = 72, correccionAncho = 223; //Es posible calcularlo?
//	private int posicionJugadorTileX = (int)(posicion.x/ Recursos.ANCHO_TILE);
//	private int posicionJugadorTileY = (int)(posicion.y/ Recursos.ALTO_TILE);
	private int reducciondanio = 45;
	private int danio = 20;

	public PersonajePrincipal() {
		super(UtilHerramientas.heroeAbajoSprite);
		crearAnimaciones();
		rectanguloJugador = new Rectangle(getBoundingRectangle());
	}
	
	public void dibujar(Texture textura) {
		this.setTexture(textura);
		this.draw(UtilHerramientas.batch);
		
		this.setX(this.posicion.x);
		this.setY(this.posicion.y);
		
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
		
	}
	
	public void dibujar(TextureRegion tr) {
		UtilHerramientas.batch.draw(tr, posicion.x, posicion.y, tr.getRegionWidth(), tr.getRegionHeight());
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
	}
	
	public void recibirdanio(float cantidad) {
		this.vidaActual-=(cantidad/reducciondanio);
		if(this.vidaActual < 0) {
			vidaActual = 0;
		}
	}
	
	public void hacerdanio(Enemigo enemigo) {
		enemigo.recibirdanio(danio);    //TODO Hacerlo funcionar!
	}
	
	public void crearAnimacion() {
		
		Texture heroeAnimacion = new Texture("personajes/heroe/heroeSheet.png");
		TextureRegion[][] tmp = TextureRegion.split(heroeAnimacion, (heroeAnimacion.getWidth()-correccionAncho)/COLUMNAS, (heroeAnimacion.getHeight()-correccionAlto)/FILAS);
		TextureRegion[] walkFramesDerecha = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesIzquierda = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesArriba = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesAbajo = new TextureRegion[COLUMNAS];
		
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
	public void animar(boolean animar) {
		if(animar) {
			tiempoAni += Gdx.graphics.getDeltaTime();
			frameActual = animacionActual.getKeyFrame(tiempoAni, true);
			dibujar(frameActual);
		}
	}
	
	public void crearAnimaciones() {
		crearAnimacion();
	}
	
	public void controlarMovimiento(Entrada entrada, Mapa mapa) {
		
		float oldX = posicion.x, oldY = posicion.y;
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
		
		UtilHerramientas.batch.begin();
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
				dibujar(UtilHerramientas.heroeAbajo);
			}
			if(getPosicionFinal() == 1) {
				dibujar(UtilHerramientas.heroeArriba);
			}
			if(getPosicionFinal() == 2) {
				dibujar(UtilHerramientas.heroeDerecha);
			}
			if(getPosicionFinal() == 3) {
				dibujar(UtilHerramientas.heroeIzquierda);
			}
			if(getPosicionFinal() == 4) {
				dibujar(UtilHerramientas.heroeAbajo);
			}
			
		}
		
	UtilHerramientas.batch.end();
		
	}
	
	public void mostrarColisiones() {
		UtilHerramientas.sr.setAutoShapeType(true);
		UtilHerramientas.sr.begin();
		
			UtilHerramientas.sr.set(ShapeType.Line);
			UtilHerramientas.sr.setColor(Color.GREEN);
			UtilHerramientas.sr.rect(getRectangulo().x, getRectangulo().y, getRectangulo().getWidth(), getRectangulo().getHeight());

		UtilHerramientas.sr.end();
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
	
	public void setPosicionFinal(int posicionFinal) {
		this.posicionFinal = posicionFinal;
	}
	
	public int getDireccionJugador() {
		return direccion;
	}
	
	public boolean isEnColision() {
		return enColision;
	}
	
	public void setEnColision(boolean enColision) {
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
