package game;

import javax.swing.ImageIcon;

import ResourceLoader.ResourceLoader;

public class Monster
{
	String[] attackList, types, preEvolution, postEvolution;
	Attack[] moveList;
	ImageIcon image;
	String defaultName, customName, effect;
	float currentHP;
	float[] stats = new float[5];
	float[][] statsPL = new float[5][2];
	boolean isDead;
	
	public Monster(String name)
	{
		try
		{
			//this.image = new ImageIcon(ImageIO.read(ResourceLoader.load(name + ".png")));
			String string = ResourceLoader.convertStreamToString(ResourceLoader.load("Monster"), name);
			this.builtMonster(string);
			this.isDead = currentHP == 0;
		}
		catch (Exception e)
		{
			if (e instanceof ResourceLoader.LoadException)
				((ResourceLoader.LoadException) e).printReason();
			e.printStackTrace();
		}
	}
	
	public boolean getAttacked(float damage)
	{
		this.currentHP -= damage;
		if (this.currentHP == 0)
		{
			this.isDead = true;
			return this.isDead;
		}
		return false;
	}
	
	public void setName(String name)
	{
		customName = name;
	}
	public void deleteName()
	{
		customName = null;
	}
	
	public String getName()
	{
		if (customName == null)
			return defaultName;
		return customName;
	}
	
	public void builtMonster(String str)
	{
		String[] strings = str.split("\\n");
		for (int i = 0; i < strings.length; i++)
			strings[i] = this.getData(strings[i]);
		this.defaultName = strings[1];
		if (strings[2].contains(","))
			this.types = strings[2].split(", ");
		else
			this.types = new String[]{strings[2]};
		int current = 3;
		int goal = current+5;
		for (int i = current; i < goal; i++)
		{
			this.stats[i-current] = Float.parseFloat(strings[i]);
		}
		this.currentHP = stats[0];
		current = goal;
		goal += 5;
		for (int i = current; i < goal; i++)
		{
			if (strings[i].contains(","))
			{
				String[] extreme = strings[i].split(", ");
				this.statsPL[i-current][0] = Float.parseFloat(extreme[0]);
				this.statsPL[i-current][1] = Float.parseFloat(extreme[1]);
			}
			else
			{
				this.statsPL[i-current][0] = Float.parseFloat(strings[i]);
				this.statsPL[i-current][1] = statsPL[i-current][0];
			}
		}
		current = goal;
		if (strings[current] != null)
			this.preEvolution = strings[current].split(", ");
		if (strings[current+1] != null)
			this.postEvolution = strings[current+1].split(", ");
	}
	
	public String getData(String string)
	{
		if (string.contains(":"))
		{
			String[] strings = string.split(": ");
			if (strings[1].contains("-"))
				return null;
			return strings[1];
		}
		return string;
	}
	
	public static void main(String[] args)
	{
		new Monster("Mewtu");
	}
	
	public boolean learn(Attack attack)
	{
		for (int i = 0; i < moveList.length; i++)
			if (moveList[i] == null)
			{
				moveList[i] = attack;
				return false;
			}
		return true;
	}
	
	public Attack getAttack(int i)
	{
		if (moveList[i] != null)
			return moveList[i];
		else
			return null;
	}
	
	public Attack useAttack(int i)
	{
		if (moveList[i] != null)
			if (moveList[i].use())
				return moveList[i];
			else
				return null;
		else
			return null;
	}

	public String[] getAttackList() {
		return attackList;
	}

	public void setAttackList(String[] attackList) {
		this.attackList = attackList;
	}

	public String[] getPreEvolution() {
		return preEvolution;
	}

	public void setPreEvolution(String[] preEvolution) {
		this.preEvolution = preEvolution;
	}

	public String[] getPostEvolution() {
		return postEvolution;
	}

	public void setPostEvolution(String[] postEvolution) {
		this.postEvolution = postEvolution;
	}

	public Attack[] getMoveList() {
		return moveList;
	}

	public void setMoveList(Attack[] moveList) {
		this.moveList = moveList;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public float getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(float currentHP) {
		this.currentHP = currentHP;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public float[] getStats() {
		return stats;
	}

	public void setStats(float[] stats) {
		this.stats = stats;
	}
	
	public short getLevel()
	{
		return 0;
	}
	
	public boolean isDead()
	{
		return this.isDead;
	}
}
