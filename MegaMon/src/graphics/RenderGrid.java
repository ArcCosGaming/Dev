package graphics;


import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class RenderGrid {

	private int distance;
	
	public RenderGrid(int distance)
	{
		this.distance = distance;
	}
	
	public int getPriority() {
		return 1;
	}
	
	
	
	public void render(int absX, int absY, int frame) {
		int originX = 0;//GraphicsEngine.getOriginX();
		int originY = 0;//GraphicsEngine.getOriginY();
		int height = Display.getDisplayMode().getHeight();
		int width = Display.getDisplayMode().getWidth();
		int defX = (originX-absX)%this.distance;
		int defY = (originY-absY)%this.distance;
		glColor3f(1,1,1);
		glBegin(GL_LINES);
			for (int i = 0; i <= width; i += this.distance)
			{
				glVertex2f(i+defX-0.5f, 0);
				//glVertex2f(i+defX+0.5f, 0);
				//glVertex2f(i+defX+0.5f, height);
				glVertex2f(i+defX-0.5f, height);
			}
			for (int i = 0; i <= height; i+= this.distance)
			{
				glVertex2f(0, i+defY-0.5f);
				//glVertex2f(0, i+defY+0.5f);
				//glVertex2f(width, i+defY+0.5f);
				glVertex2f(width, i+defY-0.5f);
			}
		glEnd();
	}
	
	public void setDistance(int distance)
	{
		this.distance = distance;
	}
}
