package graphics;

import static org.lwjgl.opengl.GL11.*;

public class RenderChar {
	int textureID;
	double[][] texCoord;
	int[][] coord;
	int pt;
	
	public RenderChar(int textureID, double[][] texCoord, int[][] coord, int pt) {
		this.textureID = textureID;
		this.texCoord = texCoord;
		this.coord = coord;
		this.pt = pt;
	}
	
	public void render(int absX, int absY, int frame) {
		glBegin(GL_QUADS);
			for (int i = 0; i < 4; i++) {
				glTexCoord2d(texCoord[i][0], texCoord[i][2]);
				glVertex2i(coord[i][0], coord[i][1]);
			}
		glEnd();
	}
	
	public int getPriority() {
		return 2;
	}
}
