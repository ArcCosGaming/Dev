package geometry;
import java.awt.Point;


public class Vector implements Cloneable {
	float x, y, length;
	public Vector(Point p) {
		this.x = (float) p.getX();
		this.y = (float) p.getY();
	}
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
		this.length();
	}
	
	public double getLength() {
		if (length == 0)
			this.length();
		return length;
	}
	
	private void length() {
		this.length = (float) Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	public void normalise() {
		if (this.length != 1)
			this.length();
		this.x /= this.length;
		this.y /= this.length;
	}
	
	public Vector times(float factor) {
		float x = this.x*factor;
		float y = this.y*factor;
		return new Vector(x, y);
	}
	
	public void multiplyWith(double factor) {
		this.x *= factor;
		this.y *= factor;
	}
	
	public Vector plus(Vector v2) {
		float x = this.x + v2.getX();
		float y = this.y + v2.getY();
		return new Vector(x, y);
	}
	
	public Vector minus(Vector v2) {
		float x = this.x - v2.getX();
		float y = this.y - v2.getY();
		return new Vector(x, y);
	}
	
	public Vector divide(Vector v2) {
		float x = this.x / v2.getX();
		float y = this.y / v2.getY();
		return new Vector(x, y);
	}
	
	public void add(Vector v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	
	public void translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public Vector copy() {
		return new Vector(this.x, this.y);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector))
			return false;
		Vector v2 = (Vector) obj;
		boolean x = this.x-0.05 <= v2.getX() && this.x+0.05 >= v2.getX();
		boolean y = this.y-0.05 <= v2.getY() && this.y+0.05 >= v2.getY();
		return x && y;
	}
	
	public String toString() {
		return "( " + this.x + " | " + this.y + " )";
	}
	
	public Vector getNormal()
	{
		float x = -this.y;
		float y = this.x;
		return new Vector(x, y);
	}
}
