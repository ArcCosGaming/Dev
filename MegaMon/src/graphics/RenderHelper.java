package graphics;

import static ResourceLoader.ImageLoader.loadTexture;
import static org.lwjgl.opengl.ARBBufferObject.GL_STATIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.GL_WRITE_ONLY_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBBufferObject.glMapBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glUnmapBufferARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.glDrawRangeElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class RenderHelper implements Renderable {
	protected final static int bytesPerFloat =Float.SIZE/8, bytesPerInt =Integer.SIZE/8;
	protected final int numVertices=4, numAxisPerVertex=3, numColoursPerVertex=3, numTexCoordPerVertex = 2;
	protected int shaderProgram;
	protected boolean useShader;
	protected int coord3DAttributeIndex, vColorAttributeIndex, texCoordAttributeIndex, textureSamplerAttributeIndex, transformMatrixAttributeIndex, fadeAttributeIndex, currentFade = 1;
	protected FloatBuffer transformValues;
	protected int vertexAttributeBuffer, vertexAttributeBufferSize, vertexIndexBuffer, vertexIndexBufferSize;
	protected int stride;
	
	protected float relX, relY, relWidth, relHeight, angle = 0;
	protected int textureID, drawingMode = GL_LINES;
	protected String texturePath;
	private final String defaultTexturePath = "res/images/nothing-alpha.png";
	
	public void render(float absX, float absY, int frame, boolean isPaused) {
		float drawX = this.relX-absX;
		float drawY = this.relY-absY;
		if ((drawX + this.relWidth >= -1 && drawX - this.relWidth <= 1) && (drawY + this.relHeight >= -1 && drawY - this.relHeight <= 1)) {
			if (!isPaused) {
				this.update(drawX, drawY, this.angle, frame);
			}
			this.draw();
		}
	}
	
	protected void update(float dx, float dy, float angle, int frame) {
		Vector3f zAxis=new Vector3f(0f, 0f, 1f);
		Matrix4f translationMatrix=new Matrix4f();
		translationMatrix.setIdentity();
		translationMatrix.translate(new Vector3f(dx, dy, 0f));
		Matrix4f rotationMatrix=new Matrix4f();
		rotationMatrix.setIdentity();
		rotationMatrix.rotate(angle, zAxis);
		Matrix4f transformationMatrix=Matrix4f.mul(translationMatrix, rotationMatrix, null);
		this.transformValues.clear();
		transformationMatrix.store(this.transformValues);
		this.transformValues.flip();
	}
	
	protected void draw() {
		if (!this.useShader) {
			Sys.alert(this.getClass().getName() + "- Error", "Cannot draw without initiating Shader");
			return;
		}
		//use shader
			glUseProgramObjectARB(this.shaderProgram);
		//bind vertex attributes
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, this.vertexAttributeBuffer);
		//uniform shader attributes
			glUniform1f(this.fadeAttributeIndex, this.currentFade);
			glUniformMatrix4(this.transformMatrixAttributeIndex, false, this.transformValues);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, this.textureID);
			glUniform1i(this.textureSamplerAttributeIndex, 0);
		//pass info to shader
			glEnableVertexAttribArray(this.coord3DAttributeIndex);
			glVertexAttribPointer(this.coord3DAttributeIndex, this.numAxisPerVertex, GL_FLOAT, false, stride, 0);
			glEnableVertexAttribArray(this.vColorAttributeIndex);
			glVertexAttribPointer(this.vColorAttributeIndex, this.numColoursPerVertex, GL_FLOAT, false, stride, this.numAxisPerVertex*bytesPerFloat);
			glEnableVertexAttribArray(this.texCoordAttributeIndex);
			glVertexAttribPointer(this.texCoordAttributeIndex, this.numTexCoordPerVertex, GL_FLOAT, false, stride, (this.numAxisPerVertex+this.numColoursPerVertex)*bytesPerFloat);
		//draw vertices using indices
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, this.vertexIndexBuffer);
			glDrawRangeElements(this.drawingMode, 0, this.numVertices-1, this.numVertices, GL_UNSIGNED_INT, 0);
		//unbind buffer
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
			glDisableVertexAttribArray(this.vColorAttributeIndex);
			glDisableVertexAttribArray(this.coord3DAttributeIndex);
		//stop using shader
			glUseProgramObjectARB(0);
	}
	
	protected void initShader() {
		if (GraphicsEngine.usesShader()) {
			this.shaderProgram = GraphicsEngine.getShaderProgram();
			if (shaderProgram != 0) {
				this.useShader = true;
				this.coord3DAttributeIndex = GraphicsEngine.getCoord3DAttributeIndex();
				this.vColorAttributeIndex = GraphicsEngine.getVColorAttributeIndex();
				this.texCoordAttributeIndex = GraphicsEngine.getTexCoordAttributeIndex();
				this.textureSamplerAttributeIndex = GraphicsEngine.getTextureSamplerAttributeIndex();
				this.transformMatrixAttributeIndex = GraphicsEngine.getTransformMatrixAttributeIndex();
				this.fadeAttributeIndex = GraphicsEngine.getFadeAttributeIndex();
				this.transformValues = BufferUtils.createFloatBuffer(16);
			}
		} else {
			Sys.alert(this.getClass().getName() + " - Error", "Cannot initiate Shader if GraphicsEngine does not use Shader");
		}
	}
	protected void initVariables(float x, float y, float width, float height, String texturePath) {
		float displayHeight = GraphicsEngine.getHeight();
		float displayWidth = GraphicsEngine.getWidth();
		this.relX = x/displayWidth;
		this.relY = y/displayHeight;
		this.relWidth = width/displayWidth;
		this.relHeight = height/displayHeight;
		if (texturePath == null || texturePath.equals("")) {
			this.texturePath = this.defaultTexturePath;
		} else {
			this.texturePath = texturePath;
		}
	}
	
	protected  void initBuffer() {
		this.setupVertexAttributeBuffer();
		this.setupVertexIndexBuffer();
	}
	
	protected void setupVertexAttributeBuffer() {
		//setup
			if (this.vertexAttributeBuffer == 0) {
				this.vertexAttributeBuffer = glGenBuffersARB();
				this.vertexAttributeBufferSize = bytesPerFloat*this.numVertices*(this.numAxisPerVertex+this.numColoursPerVertex+this.numTexCoordPerVertex);
			}
		//set size of buffer
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, this.vertexAttributeBuffer);
			glBufferDataARB(GL_ARRAY_BUFFER_ARB, this.vertexAttributeBufferSize, GL_STATIC_DRAW_ARB);
		//map buffer
			ByteBuffer vertexPositionAttributes = glMapBufferARB(GL_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB, this.vertexAttributeBufferSize, null);
		//vertex position & color
			
			float[] vertexAttributeData = this.getVertexAttributeData();
		//put vertex attributes into buffer
			vertexPositionAttributes.asFloatBuffer().put(vertexAttributeData);
		//flip , unmap and unbind buffer
			vertexPositionAttributes.flip();
			glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
			this.stride = (this.numAxisPerVertex + this.numColoursPerVertex + this.numTexCoordPerVertex) * bytesPerFloat;
	}
	
	protected void setupVertexIndexBuffer() {
		//setup
			this.vertexIndexBuffer = glGenBuffersARB();
			this.vertexIndexBufferSize = bytesPerInt * this.numVertices;
		//set size of buffer
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, this.vertexIndexBuffer);
			glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, this.vertexIndexBufferSize, GL_STATIC_DRAW_ARB);
		//map buffer
			ByteBuffer vertexIndices = glMapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB, this.vertexIndexBufferSize, null);
		//vertex index data
			int[] indicesData = this.getIndicesData();
		//put data into buffer
			vertexIndices.asIntBuffer().put(indicesData);
		//flip, unmap and unbind
			vertexIndices.flip();
			glUnmapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB);
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	}
	
	protected void initTextures() {
		this.textureID = loadTexture(this.texturePath);
	}
	
	/**
	 * @param choose
	 * 0 : lines
	 * 1 : triangles
	 * 2 : rectangles
	 * 3 : triangle-stripes
	 * 4 : triangle-fans
	 * default : lines
	 */
	protected void setDrawingMode(int choose) {
		switch (choose) {
		case 0 : this.drawingMode = GL_LINES;break;
		case 1 : this.drawingMode = GL_TRIANGLES;break;
		case 2 : this.drawingMode = GL_QUADS;break;
		case 3 : this.drawingMode = GL_TRIANGLE_STRIP;break;
		case 4 : this.drawingMode = GL_TRIANGLE_FAN;break;
		default : this.drawingMode = GL_LINES;break;
		}
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	protected abstract int[] getIndicesData();
	protected abstract float[] getVertexAttributeData();
}
