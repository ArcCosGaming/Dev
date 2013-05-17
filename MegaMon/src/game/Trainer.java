package game;

import geometry.Vector;
import graphics.GraphicsEngine;
import graphics.RenderObject;
import graphics.Texture;

public class Trainer extends RenderObject
{
	private Monster[] monsterList;
	private int gold;
	private String badge;
	private boolean isWalking, isStanding, startWalking;
	private Texture walkingTop, walkingBot, walkingRight, walkingLeft, currentTexture;
	private int  mode = 1, lastMode, succesion, direction, speed;
	private Vector isPos, shouldPos;
	
	public Trainer(String topPath, String leftPath, String botPath, String rightPath, int posX, int posY)
	{
		super(0, 0, 100, 100, botPath);
		this.monsterList = new Monster[6];
		this.gold = 0;
		this.isWalking = false;
		this.isStanding = true;
		this.walkingTop = new Texture(topPath, 3);
		this.walkingBot = new Texture(botPath, 3);
		this.walkingRight = new Texture(rightPath, 3);
		this.walkingLeft = new Texture(leftPath, 3);
		this.direction = 2;
		this.isPos = new Vector(this.relX, this.relY);
		this.shouldPos = this.isPos.copy();
		this.direction = 0;
		//this.updateTexture();
		this.setSuccesion();
	}
	
	private void setSuccesion()
	{
		this.succesion = GraphicsEngine.getFrameRate()/3;//this.currentTexture.getSections();
	}
	
	private void updateTexture()
	{
		if (direction == 0)
			this.currentTexture = this.walkingTop;
		else if (direction == 1)
			this.currentTexture = this.walkingLeft;
		else if (direction == 2)
			this.currentTexture = this.walkingBot;
		else if (direction == 3)
			this.currentTexture = this.walkingRight;
		super.setTexture(this.currentTexture.getID());
		this.setSuccesion();
	}
	
	@Override
	protected float[] getVertexAttributeData() {
		if (this.mode > 0) {
			return new float[]{
					//x, y, z, r, g, b, tex1, tex2
					-this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) this.mode-1)/3f, 0.0f,
					this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) this.mode)/3f, 0.0f,
					this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) this.mode)/3f, 1.0f,
					-this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) (this.mode-1))/3f, 1.0f
				};
		} else {
			return new float[]{
					//x, y, z, r, g, b, tex1, tex2
					-this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) 0)/3f, 0.0f,
					this.relWidth, this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) 1)/3f, 0.0f,
					this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) 1)/3f, 1.0f,
					-this.relWidth, -this.relHeight, 0.0f, 1.0f, 1.0f, 1.0f, ((float) 0)/3f, 1.0f
				};
		}
	}
	
	public boolean addMonster(Monster monster)
	{
		for (int i = 0; i < this.monsterList.length; i++)
			if (this.monsterList[i] == null)
			{
				this.monsterList[i] = monster;
				return true;
			}
		return false;
	}
	
	public Monster getNextMonster()
	{
		for (int i = 0; i < this.monsterList.length; i++)
			if (!monsterList[i].isDead())
				return monsterList[i];
		return null;
	}
	
	public boolean hasBadge()
	{
		return badge != null;
	}
	
	public int getPriority()
	{
		return 2;
	}
	
	private void setWalking(boolean walking)
	{
		this.isWalking = walking;
		this.isStanding = !walking;
	}
	
	public void walk(Vector shouldPos, int direction, int speed) {
		this.direction = direction;
		this.setWalking(true);
		this.speed = 2;
		this.shouldPos = shouldPos;
		if (!this.startWalking) {
			this.startWalking = true;
			this.changeMode();
		}
	}
	
	@Override
	protected void update(float dx, float dy, float angle, int frame) {
		this.updateTexture();
		if (this.isStanding) {
			if (this.mode != 1) {
				//System.out.print("not");
				this.changeMode();
				this.startWalking = false;
			}
			this.speed = 0;
		} else if (frame%this.succesion == 0 && this.isWalking){
			this.changeMode();
		}
		if (this.isPos.equals(this.shouldPos)) {
			this.setWalking(false);
		} else {
			switch (direction) {
			case 0 : this.isPos.translate(0, GraphicsEngine.getRelY(speed));break;
			case 1 : this.isPos.translate(-GraphicsEngine.getRelX(speed), 0);break;
			case 2 : this.isPos.translate(0, -GraphicsEngine.getRelY(speed));break;
			case 3 : this.isPos.translate(GraphicsEngine.getRelX(speed), 0);break;
			}
			super.relX = this.isPos.getX();
			super.relY = this.isPos.getY();
		}
		super.update(dx, dy, angle, frame);
	}
	
	private void changeMode()
	{
		if (this.mode != 1)
		{
			this.lastMode = this.mode;
			this.mode = 1;
		}
		else 
		{
			if (this.lastMode == 2)
			{
				this.lastMode = this.mode;
				this.mode = 3;
			}
			else
			{
				this.lastMode = this.mode;
				this.mode = 2;
			}
		}
		//System.out.println("changed");
		super.setupVertexAttributeBuffer();
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public int getPosX() {
		return (int) this.isPos.getX();
	}
	
	public int getPosY() {
		return (int) this.isPos.getY();
	}
	
	public void translate(float dx, float dy) {
		this.isPos.translate(dx, dy);
	}
	
	public Vector getPos() {
		return this.isPos.copy();
	}
	
	public boolean isStanding() {
		return this.isStanding;
	}
	
	public int getGold() {
		return this.gold;
	}
}
