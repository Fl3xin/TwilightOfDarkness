package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import gestorMapas.Mapa;
import interfaces.Movible;
import personajes.PersonajePrincipal;
import utilidades.Utiles;

public class Alien extends Enemigo implements Movible{

	private Rectangle rectanguloAlien;
	private Circle circuloAlien;
	private final float VELOCIDAD = 0.7f;
	private final int FILAS = 4, COLUMNAS = 3;
	private float tiempoAni = 0;
	private Animation<TextureRegion> animacionIzquierda, animacionDerecha, animacionArriba, animacionAbajo, animacionAtaqueDerecha, animacionAtaqueAbajo, animacionAtaqueIzquierda, animacionAtaqueArriba;
	private Animation<TextureRegion> animacionActual;
	private int correccionAncho = 25, correccionAlto = 120;
	private String direccionAlien = "", posicionFinal = "";
	private boolean enMovimiento, atacando, enColision;
	private int daño = 10;
	
	public Alien() {
		super(Utiles.alienAbajoSprite);
		rectanguloAlien = new Rectangle(getBoundingRectangle());
		circuloAlien = new Circle(this.posicion.x, this.posicion.y, 150);
		crearAnimacion();
		vidaMax = 100;
		vida = vidaMax;
	}

	@Override
	public void dibujar(Texture textura) {
		
		this.setTexture(textura);
		this.draw(Utiles.batch);
		
		this.setX(this.posicion.x);
		this.setY(this.posicion.y);
		
	}

	public void barraVida() {
		
		int anchoVida = (vida*100)/vidaMax;
		
		Utiles.sr.setAutoShapeType(true);
		Utiles.sr.begin();
			Utiles.sr.set(ShapeType.Line);
			Utiles.sr.setColor(Color.BLACK);
			Utiles.sr.rect(getPosicion().x - 10, getPosicion().y + getHeight(), 100*0.5f, 10);
			
			Utiles.sr.set(ShapeType.Filled);
			Utiles.sr.setColor(Color.GREEN);
			Utiles.sr.rect(getPosicion().x - 10, getPosicion().y + getHeight(), anchoVida*0.5f, 10);
		Utiles.sr.end();
	}
	
	@Override
	public void dibujar(TextureRegion tr) {
		Utiles.batch.draw(tr, posicion.x, posicion.y, tr.getRegionWidth(), tr.getRegionHeight());
	}

	@Override
	public void hacerDaño(PersonajePrincipal jugador) {
		jugador.recibirDaño(daño);
	}

	@Override
	public void recibirDaño(int cantidad) {
		this.vida -= cantidad;
	}
	
	public void crearAnimacion() {
		
		Texture alienAnimacion = new Texture("enemigos/alien/alienPack.png");
		Texture alienAtaque = new Texture("enemigos/alien/alienAtaque.png");
		TextureRegion[][] tmp = TextureRegion.split(alienAnimacion, (alienAnimacion.getWidth() - correccionAncho)/COLUMNAS, (alienAnimacion.getHeight()-correccionAlto)/FILAS);
		TextureRegion[][] tmp2 = TextureRegion.split(alienAtaque, (alienAtaque.getWidth() - correccionAncho)/COLUMNAS, (alienAtaque.getHeight() - correccionAlto)/FILAS);
		TextureRegion[] walkFramesDerecha = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesIzquierda = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesArriba = new TextureRegion[COLUMNAS];
		TextureRegion[] walkFramesAbajo = new TextureRegion[COLUMNAS];
		
		TextureRegion[] framesAtaqueDerecha = new TextureRegion[COLUMNAS];
		TextureRegion[] framesAtaqueAbajo = new TextureRegion[COLUMNAS];
		TextureRegion[] framesAtaqueIzquierda = new TextureRegion[COLUMNAS];
		TextureRegion[] framesAtaqueArriba = new TextureRegion[COLUMNAS];
		
		int index = 0;
		for (int i = 0; i < FILAS; i++) {
			index = 0;
			for (int j = 0; j < COLUMNAS; j++) {
				
				if(i == 0) {
					walkFramesArriba[index] = tmp[i][j];
					framesAtaqueDerecha[index] = tmp2[i][j];
				}
				if(i == 1) {
					walkFramesDerecha[index] = tmp[i][j];
					framesAtaqueAbajo[index] = tmp2[i][j];
				}
				if(i == 2) {
					walkFramesAbajo[index] = tmp[i][j];
					framesAtaqueIzquierda[index] = tmp2[i][j];
				}
				if(i == 3) {
					walkFramesIzquierda[index] = tmp[i][j];
					framesAtaqueArriba[index] = tmp2[i][j];
				}
				index++;
			}
		}
		
		animacionArriba = new Animation<TextureRegion>(0.2f, walkFramesArriba);
		animacionIzquierda = new Animation<TextureRegion>(0.2f, walkFramesIzquierda);
		animacionAbajo = new Animation<TextureRegion>(0.2f, walkFramesAbajo);
		animacionDerecha = new Animation<TextureRegion>(0.2f, walkFramesDerecha);
		
		animacionAtaqueDerecha = new Animation<TextureRegion>(0.2f, framesAtaqueDerecha);
		animacionAtaqueAbajo = new Animation<TextureRegion>(0.2f, framesAtaqueAbajo);
		animacionAtaqueIzquierda = new Animation<TextureRegion>(0.2f, framesAtaqueIzquierda);
		animacionAtaqueArriba = new Animation<TextureRegion>(0.2f, framesAtaqueArriba);
		
	}
	
	TextureRegion frameActual;
	public void animar(boolean animar) {
		if(animar) {
			tiempoAni += Gdx.graphics.getDeltaTime();
			frameActual = animacionActual.getKeyFrame(tiempoAni, true);
			dibujar(frameActual);
		}
	}
	
	public void movimiento() {
		Utiles.batch.begin();
		if(enMovimiento || atacando) {
			animar(true);
			System.out.println("Direccion alien: "+direccionAlien);
			if(direccionAlien.equals("izquierda")) {
				posicionFinal = "izquierda";
			}
			if(direccionAlien.equals("derecha")) {
				posicionFinal = "derecha";
			}
			if(direccionAlien.equals("abajo")) {
				posicionFinal = "abajo";
			}
			if(direccionAlien.equals("arriba")) {
				posicionFinal = "arriba";
			}
		}else if(!enMovimiento && !atacando){
			animar(false);
			if(posicionFinal.equals("")) {
				dibujar(Utiles.alienAbajo);
			}
			if(posicionFinal.equals("izquierda")) {
				dibujar(Utiles.alienIzquierda);
			}
			if(posicionFinal.equals("abajo")) {
				dibujar(Utiles.alienAbajo);
			}
			if(posicionFinal.equals("derecha")) {
				dibujar(Utiles.alienDerecha);
			}
			if(posicionFinal.equals("arriba")) {
				dibujar(Utiles.alienArriba);
			}
		}
		Utiles.batch.end();
	}
	
	public void comportamiento(PersonajePrincipal jugador, Mapa mapa) {
		//TODO comportamiento del enemigo
		
		float newX = posicion.x, newY = posicion.y;
		float oldX = posicion.x, oldY = posicion.y;
		
		if(circuloAlien.contains(jugador.getPosicion().x, jugador.getPosicion().y)) {
			enMovimiento = true;
			
			if(rectanguloAlien.overlaps(jugador.getRectangulo())) {
				
				atacando = true;
				hacerDaño(jugador);
				enMovimiento = false;
				if(direccionAlien.equals("izquierda")) {
					animacionActual = animacionAtaqueIzquierda;
				}
				if(direccionAlien.equals("abajo")) {
					animacionActual = animacionAtaqueAbajo;
				}
				if(direccionAlien.equals("derecha")) {
					animacionActual = animacionAtaqueDerecha;
				}
				if(direccionAlien.equals("arriba")) {
					animacionActual = animacionAtaqueArriba;
				}
			}else {
				enMovimiento = true;
				atacando = false;
				if(jugador.getPosicion().x < this.posicion.x) {
					animacionActual = animacionIzquierda;
					direccionAlien = "izquierda";
					newX -=VELOCIDAD;
					rectanguloAlien.setX(newX);
					
					enColision = mapa.comprobarColision(this);
					
					if(!enColision) {
						posicion.x = newX;
						setX(posicion.x);
					}else {
						posicion.x = oldX;
						setX(posicion.x);
					}
				}
				if(jugador.getPosicion().y < this.posicion.y) {
					animacionActual = animacionAbajo;
					direccionAlien = "abajo";
					newY -=VELOCIDAD;
					rectanguloAlien.setY(newY);
					
					enColision = mapa.comprobarColision(this);
					
					if(!enColision) {
						posicion.y = newY;
						setX(posicion.y);
					}else {
						posicion.y = oldY;
						setX(posicion.y);
					}
				}
				if(jugador.getPosicion().x > this.posicion.x) {
					animacionActual = animacionDerecha;
					direccionAlien = "derecha";
					newX +=VELOCIDAD;
					rectanguloAlien.setX(newX);
					
					enColision = mapa.comprobarColision(this);
					
					if(!enColision) {
						posicion.x = newX;
						setX(posicion.x);
					}else {
						posicion.x = oldX;
						setX(posicion.x);
					}
				}
				if(jugador.getPosicion().y > this.posicion.y) {
					animacionActual = animacionArriba;
					direccionAlien = "arriba";
					newY +=VELOCIDAD;
					rectanguloAlien.setY(newY);
					
					enColision = mapa.comprobarColision(this);
					
					if(!enColision) {
						posicion.y = newY;
						setX(posicion.y);
					}else {
						posicion.y = oldY;
						setX(posicion.y);
					}
				}
				circuloAlien.setX(posicion.x);
				circuloAlien.setY(posicion.y);	

			}
			
		}else {
			enMovimiento = false;
		}
		movimiento();
	}

	public void mostrarColisiones() {
		Utiles.sr.circle(getCirculoAlien().x + (getWidth()/2), getCirculoAlien().y + (getHeight()/2), getCirculoAlien().radius);
		Utiles.sr.rect(getRectangulo().x, getRectangulo().y, getRectangulo().width, getRectangulo().height);
	}
	
	public Circle getCirculoAlien() {
		return circuloAlien;
	}
	
	public Rectangle getRectangulo() {
		return rectanguloAlien;
	}
	
}
