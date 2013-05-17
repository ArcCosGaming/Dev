package graphics;
import static org.lwjgl.opengl.GL11.*;

public class RenderString {
	RenderChar[] chars;
	public RenderString(RenderChar[] chars) {
		this.chars = chars;
	}
	
	public void render(int absX, int absY, int frame) {
		for (RenderChar c : this.chars) {
			c.render(absX, absY, frame);
		}
	}
	
	public int getPriority() {
		return 2;
	}
}
