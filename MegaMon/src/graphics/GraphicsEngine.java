package graphics;

import game.BugWriter;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glValidateProgramARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static ResourceLoader.LibraryLoader.loadNativeLibraries;
import static ResourceLoader.ShaderLoader.createFragmentShader;
import static ResourceLoader.ShaderLoader.createVertexShader;
import static game.GameStarter.isPaused;

public class GraphicsEngine
{
	private static ArrayList<Renderable> renderObjects = new ArrayList<Renderable>();
	private static boolean finished;
	private static boolean hasGrid = false, isCreated = false;
	private static int resX = 0, resY = 0, frame;
	private static float viewX, viewY, height, width;
	private static int FRAMERATE = 60;
	private final static RenderGrid GRID = new RenderGrid(10);
	private static int shaderProgram, vertexShader, fragmentShader;
	private static boolean shader = false;
	private static int coord3DAttributeIndex, vColorAttributeIndex, texCoordAttributeIndex, textureSamplerAttributeIndex, transformMatrixAttributeIndex, fadeAttributeIndex;
	
	public static void create() {
		if (!isCreated) {
			//loading openGL into java
				try {
				loadNativeLibraries();
				} catch (Exception e) {
					e.printStackTrace();
					Sys.alert("Error", "Natives not found!");
					BugWriter.writeBug(e);
					System.exit(0);
				}
			//setting up display
				try {
					Display.setDisplayMode(new DisplayMode(resX, resY));
					Display.setTitle("My Game");
					Display.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
					BugWriter.writeBug(e);
					System.exit(0);
				}
				initOpenGL();
				initVariables();
				initShader();
				initShaderAttributes();
				isCreated = true;
		} else {
			Sys.alert("GraphicsEngine - Error", "Cannot initiate the GraphicsEngine twice!");
		}
	}
	
	public static int getShaderProgram() {
		return shaderProgram;
	}
	
	public static void initVariables() {
		viewX = 0;
		viewY = 0;
	}
	
	private static void initShader() {
		//create shader program
			shaderProgram = glCreateProgramObjectARB();
		//create shader objects
			if (shaderProgram != 0) {
				vertexShader = createVertexShader("myShader.vert");
				fragmentShader = createFragmentShader("myShader.frag");
				if (vertexShader != 0 && fragmentShader != 0) {
					// add in sub shaders to main program
						glAttachObjectARB(shaderProgram, vertexShader);
						glAttachObjectARB(shaderProgram, fragmentShader);
						glLinkProgramARB(shaderProgram);
						glValidateProgramARB(shaderProgram);
						shader = true;
				}
			}
	}
	
	public static boolean usesShader() {
		return shader;
	}
	
	public static void initShaderAttributes() {
		if (shader) {
			//setting up attributes
				coord3DAttributeIndex = glGetAttribLocation(shaderProgram, "coord3D");
				vColorAttributeIndex = glGetAttribLocation(shaderProgram, "vColor");
				texCoordAttributeIndex = glGetAttribLocation(shaderProgram, "texCoord");
				textureSamplerAttributeIndex = glGetUniformLocation(shaderProgram, "textureSampler");
				fadeAttributeIndex = glGetUniformLocation(shaderProgram, "fade");
				transformMatrixAttributeIndex = glGetUniformLocation(shaderProgram, "transformMatrix");
		} else {
			Sys.alert("GraphicsEngine - Error", "Cannot initiate shaderattributes when there are no shaders");
		}
	}
	
	public static int getCoord3DAttributeIndex() {
		return coord3DAttributeIndex;
	}
	
	public static int getVColorAttributeIndex() {
		return vColorAttributeIndex;
	}
	
	public static int getTexCoordAttributeIndex() {
		return texCoordAttributeIndex;
	}
	
	public static int getTextureSamplerAttributeIndex() {
		return textureSamplerAttributeIndex;
	}
	
	public static int getFadeAttributeIndex() {
		return fadeAttributeIndex;
	}
	
	public static int getTransformMatrixAttributeIndex() {
		return transformMatrixAttributeIndex;
	}
	
	public static void runOnce()
	{
		frame++;
		frame %= FRAMERATE;
		Display.update();
		if (Display.isCloseRequested()) {
			finished = true;
		} 
		else if (Display.isActive()) {
			render();
	        Display.sync(FRAMERATE);
		} 
		else
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				BugWriter.writeBug(e);
			}
			if (Display.isVisible() || Display.isDirty()) {
	        	render();
	        	Display.sync(FRAMERATE);
	        }
		}
	}
	
	private static void render() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		if (hasGrid) {
			GRID.render((int) viewX, (int) viewY, frame);
		}
		for (int i = 0; i < 3; i++) {
			for (Renderable renderObject : renderObjects) {
				//render
					if (renderObject.getPriority() == i) {
						renderObject.render(viewX, viewY, frame, isPaused());
					}
			}
		}
		Display.update();
		Display.sync(60);
	}
	
	public static void updateView(float x, float y) {
		viewX = x;
		viewY = y;
	}
	
	public static float getHeight() {
		return height;
	}
	
	public static float getWidth() {
		return width;
	}
	
	public static boolean isFinished() {
		return finished;
	}
	
	public static void end() {
		finished = true;
	}
	
	public static int getFrameRate() {
		return FRAMERATE;
	}
	
	public static void cleanup() {
		Display.destroy();
	}
	
	public static void addRenderable(Renderable renderObject) {
		renderObjects.add(renderObject);
	}
	
	private static void initOpenGL() {
		// setup viewing area
			DisplayMode displayMode=Display.getDisplayMode();
			width=displayMode.getWidth();
			height=displayMode.getHeight();
			glViewport(0, 0, (int) width, (int) height);
		// setup the camera params
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, width, 0, height, -1, 1);
			//GLU.gluPerspective(45.0f, (float)width/(float)height, 1.0f, 100f);
		// setup the model (drawing) params
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glShadeModel(GL_SMOOTH);
			glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // black
			glClearDepth(1.0f);
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		// enable blending
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setResolution(int toResX, int toResY) {
		resX = toResX;
		resY = toResY;
	}
	
	public static float getRelX(float absValue) {
		return absValue/width;
	}
	
	public static float getRelY(float absValue) {
		return absValue/height;
	}
	
	public static void setGrid(boolean showGrid) {
		hasGrid = showGrid;
	}
	
	public static void setGridDistance(int distance) {
		GRID.setDistance(distance);
	}
}
