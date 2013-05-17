package game;

import javax.swing.ImageIcon;

import ResourceLoader.ResourceLoader;

public class Attack
{
	String name, effect, target, type, attack;
	short damage, hitChance, effectChance;
	short[] attkCount = new short[2];
	short[] powerPoints = new short[2];
	ImageIcon animation;
	boolean isFlee, isFirst;
	
	public Attack(String name)
	{
		try
		{
			//this.image = new ImageIcon(ImageIO.read(ResourceLoader.load(name + ".png")));
			this.isFlee = false;
			String string = ResourceLoader.convertStreamToString(ResourceLoader.load("Attack"), name);
			this.builtAttack(string);

		}
		catch (Exception e)
		{
			if (e instanceof ResourceLoader.LoadException)
				((ResourceLoader.LoadException) e).printReason();
			e.printStackTrace();
		}
	}
	public Attack() {}
	
	public void builtAttack(String string)
	{
		String[] strings = string.split("\\n");
		for (int i = 0; i < strings.length; i++)
			strings[i] = this.getData(strings[i]);
		this.name = strings[1];
		this.damage = Short.parseShort(strings[2]);
		this.hitChance = Short.parseShort(strings[3]);
		this.effect = strings[4];
		this.effectChance = Short.parseShort(strings[5]);
		if (strings[6].contains(","))
		{
			String[] counts = strings[6].split(", ");
			this.attkCount[0] = Short.parseShort(counts[0]);
			this.attkCount[1] = Short.parseShort(counts[1]);
		}
		else
		{
			this.attkCount[0] = Short.parseShort(strings[6]);
			this.attkCount[1] = this.attkCount[0];
		}
		this.powerPoints[0] = Short.parseShort(strings[7]);
		this.powerPoints[1] = this.powerPoints[0];
		this.target = strings[8];
		this.type = strings[9];
		this.attack = strings[10];
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
	
	public boolean use()
	{
		if (this.powerPoints[0] > 0)
		{
			this.powerPoints[0]--;
			return false;
		}
		else
			return true;
			
	}
	
	public ImageIcon getAnimation()
	{
		return this.animation;
	}
	
	public boolean isFlee()
	{
		return this.isFlee;
	}
	
	public static Attack createFLEE()
	{
		Attack attack = new Attack();
		attack.isFlee();
		return attack;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public static void main(String[] args)
	{
		new Attack("Ice_Beam");
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public short getDamage() {
		return damage;
	}
	public void setDamage(short damage) {
		this.damage = damage;
	}
	public short getHitChance() {
		return hitChance;
	}
	public void setHitChance(short hitChance) {
		this.hitChance = hitChance;
	}
	public short getEffectChance() {
		return effectChance;
	}
	public void setEffectChance(short effectChance) {
		this.effectChance = effectChance;
	}
	public short[] getAttkCount() {
		return attkCount;
	}
	public void setAttkCount(short[] attkCount) {
		this.attkCount = attkCount;
	}
}
