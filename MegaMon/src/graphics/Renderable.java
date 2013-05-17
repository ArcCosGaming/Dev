package graphics;

public interface Renderable
{
	
	int getPriority(); //range 0-2
	void render(float absX, float absY, int frame, boolean isPaused);
}
