package game;

public class Player extends Trainer
{
	private String[] badges;
	public Player()
	{
		super("res/images/player/walkingTop-alpha.png", "res/images/player/walkingLeft-alpha.png", "res/images/player/walkingBot-alpha.png", "res/images/player/walkingRight-alpha.png", 0, 0);
		this.badges = new String[8];
	}
	
	public String[] getBadges()
	{
		return badges;
	}
	
	public String getBadge(int i)
	{
		if (i < 8)
			return badges[i];
		return null;
	}
	
	@Override
	protected void update(float dx, float dy, float angle, int frame) {
		super.update(dx, dy, angle, frame);
		GameStarter.setView(this.relX, this.relY);
	}
	
	public void winBadge(String badge)
	{
		for (int i = 0; i < 8; i++)
		{
			if (badges[i].equals("") || badges[i] == null)
				badges[i] = badge;
		}
	}
}
