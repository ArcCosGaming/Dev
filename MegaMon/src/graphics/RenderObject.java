package graphics;

public class RenderObject extends RenderHelper
{
	public RenderObject()
	{
		this.initVariables(0, 0, 100, 100, "res/images/crate.png");
		this.setDrawingMode(2);
		this.initTextures();
		this.initShader();
		this.initBuffer();
	}
	public RenderObject(float posX, float posY, float width, float height)
	{
		this.initVariables(relX, relY, width, height, "res/images/crate.png");
		this.initTextures();
		this.initShader();
		this.initBuffer();
	}
	public RenderObject(float posX, float posY, float width, float height, String pathToTexture)
	{
		this.initVariables(relX, relY, width, height, pathToTexture);
		this.initTextures();
		this.initShader();
		this.initBuffer();
	}
	
	protected int[] getIndicesData() {
		return new int[]{0,1,2,3};
	}
	
	protected float[] getVertexAttributeData() {
		return new float[]{
			//x, y, z, r, g, b, tex1, tex2
			-this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
			this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
			this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			-this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f
		};
	}
	
	public void setTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getPriority() {
		return 1;
	}
}
