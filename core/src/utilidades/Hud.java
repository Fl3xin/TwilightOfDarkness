package utilidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import personajes.PersonajePrincipal;

public class Hud {
	
	private OrthographicCamera camaraHud;
	private ShapeRenderer shapeRendererHud;
	private PersonajePrincipal jugador;
	
	public Hud(PersonajePrincipal jugador) {
		this.jugador = jugador;
		shapeRendererHud = new ShapeRenderer();
		camaraHud = new OrthographicCamera(Recursos.ANCHO, Recursos.ALTO);
		camaraHud.update();
	}

	public void mostrarBarraEnergia(){
		camaraHud.position.set(0, 0, 0);
		camaraHud.update();

		shapeRendererHud.setProjectionMatrix(camaraHud.combined);

		int anchoBarra = (100*jugador.getStamina())/jugador.getStaminaMax();
		shapeRendererHud.setAutoShapeType(true);
		shapeRendererHud.begin();
			shapeRendererHud.set(ShapeRenderer.ShapeType.Line);
			shapeRendererHud.setColor(Color.BLACK);
			shapeRendererHud.rect(-350,200,100, 20);

			shapeRendererHud.set(ShapeRenderer.ShapeType.Filled);
			shapeRendererHud.setColor(Color.BLUE);
			shapeRendererHud.rect(-351,201,anchoBarra,19);
		shapeRendererHud.end();
	}
	
}
