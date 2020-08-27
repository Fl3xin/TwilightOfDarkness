package personajes;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import gestorMapas.Mapa;
import utilidades.Entrada;
import utilidades.Recursos;
import utilidades.Utiles;

public class PersonajePrincipal extends Entidad implements Serializable {
	
	private static final long serialVersionUID = 7304212869270622393L;
	
	private int vidaActual, vidaMaxima;
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
	private int correccionAlto = 72, correccionAncho = 223;
	private int posicionJugadorTileX = (int)(posicion.x/ Recursos.ANCHO_TILE);
	private int posicionJugadorTileY = (int)(posicion.y/ Recursos.ALTO_TILE);

	public PersonajePrincipal() {
		super(Utiles.heroeAbajoSprite);
		rectanguloJugador = new Rectangle(getBoundingRectangle());
	}
	
	public void dibujar(Texture textura) {
		this.setTexture(textura);
		this.draw(Utiles.batch);
		
		this.setX(this.posicion.x);
		this.setY(this.posicion.y);
		
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
		
	}
	
	public void dibujar(TextureRegion tr) {
		Utiles.batch.draw(tr, posicion.x, posicion.y, tr.getRegionWidth(), tr.getRegionHeight());
		rectanguloJugador.set(posicion.x, posicion.y, getWidth(), getHeight()/2);
	}
	
	public void recibirDaño() {
		
	}
	
	public void hacerDaño() {
		
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
		
		if(entrada.pressedShift && stamina > 0) {
			corriendo = true;
		}
		else if(!entrada.pressedShift || entrada.pressedShift){
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
				stamina --;
			}
			rectanguloJugador.setY(newY);
			enColision = mapa.comprobarColision(this);
			direccion = 1;
			animacionActual = animacionArriba;
			if(!enColision) {
				this.setY(newY);
				this.posicion.y = newY;
			}else {
				this.setY(oldY);
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
				stamina --;
			}
			rectanguloJugador.setX(newX);
			enColision = mapa.comprobarColision(this);
			direccion = 3;
			animacionActual = animacionIzquierda;
			if(!enColision) {
				this.setX(newX);
				this.posicion.x = newX;
			}else {
				this.setX(oldX);
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
				stamina --;
			}
			rectanguloJugador.setY(newY);
			enColision = mapa.comprobarColision(this);
			direccion = 4;
			animacionActual = animacionAbajo;
			if(!enColision) {
				this.setY(newY);
				this.posicion.y = newY;
			}else {
				this.setY(oldY);
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
				stamina --;
			}
			rectanguloJugador.setX(newX);
			enColision = mapa.comprobarColision(this);
			direccion = 2;
			animacionActual = animacionDerecha;
			if(!enColision) {
				this.setX(newX);
				this.posicion.x = newX;
			}else {
				this.setX(oldX);
				this.posicion.x = oldX;
			}
			
		}
	
//		System.out.println("Posicion x en movimiento: "+posicion.x);
//		System.out.println("Posicion y en movimiento: "+posicion.y);
//		System.out.println("Stamina: "+stamina);
		
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
			animar(false);
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
		Utiles.sr.begin(ShapeType.Line);
		
			Utiles.sr.setColor(Color.GREEN);
			Utiles.sr.rect(getRectangulo().x, getRectangulo().y, getRectangulo().getWidth(), getRectangulo().getHeight());

		Utiles.sr.end();
	}
	
	public int getVidaActual() {
		return vidaActual;
	}
	
	public int getVidaMaxima() {
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

	public int getPosicionJugadorTileX() {
		return posicionJugadorTileX;
	}

	public int getPosicionJugadorTileY() {
		return posicionJugadorTileY;
	}
	
	public int getStaminaMax() {
		return staminaMax;
	}
	
}
