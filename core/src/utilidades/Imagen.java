package utilidades;

import com.badlogic.gdx.graphics.Texture;

public class Imagen {

	Texture textura;
	
	public Imagen(String ruta) {
		textura = new Texture(ruta);
	}
	
	public Texture getTexture() {
		return textura;
	}
	
}
