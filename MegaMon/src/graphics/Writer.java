package graphics;

public class Writer {
	private static Texture text;
	private final static double[] letters = {};
	public static void loadTexture() {
		text = new Texture("res/material/characters.png", 0);
	}
	
	public static RenderString drawString(String toDraw, int posX, int posY) {
		char[] letter = toDraw.toCharArray();
		RenderChar[] chars = new RenderChar[letter.length];
		for (int i = 0; i < letter.length; i++)
			chars[i] = drawCharacter(letter[i], posX, posY);
		return new RenderString(chars);
	}
	
	public static RenderChar drawCharacter(char toDraw, int posX, int posY) {
		return null;
	}
}
