package mob_layer;
import player_layer.Human;
import java.util.Random;

public abstract class Opponent{		//we used abstract class to write some non-unique methods for all opponent types
	Random random = new Random();
	int damageTaken;		 //to create new wolf element while game running we need to start objCount from 0
	static int objCount = 0; //in order to give the correct ID's to the opponents
	int opponentID;
	int points;
	int attack;
	int speed;
	int armor = 1; //damage taken = damage/armor
	boolean willSkip;
	boolean isDefeated;
	boolean specialActive;
	String opponentName;//to print opponents in menu
	String type;
	
	Opponent(){
		objCount++;
		opponentID = objCount;
		opponentName = "Opponent"+opponentID;
		setPoints();
		setAttack();
		setSpeed();	
	}
	
	public void attack(Human target, double attackModifier) {	//attack modifier may be needed to be written here
		target.takeDamage((int)(attack*attackModifier));
	}
	
	public void guard() {
		setArmor(2);
	}
	
	public abstract void special(Human target, double AttackModifier);
	
	public int takeDamage(int dmg) {//dmg parameter is from Human type object
		this.points -= dmg/armor;
		damageTaken = dmg/armor;
		if(points<=0) 
			isDefeated=true;
		return dmg/armor;			//return damage taken
	}
	
	public String getOpponentName() {
		return opponentName;
	}
	
	public int getOpponentID() {
		return opponentID;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getAttack() {
		return attack;
	}

	public int getSpeed() {
		return speed;
	}
	
	public boolean isSkipping() {
		return willSkip;
	}
	
	public void skip() {// opponents can skip turn
		willSkip = true;
	}
	
	public double getArmor() {
		return armor;
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}
	
	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	private void setPoints() { 
		this.points = random.nextInt(101) + 50;
	}
	
	private void setAttack() {
		this.attack = random.nextInt(21) + 5;
	}
	
	private void setSpeed() {
		this.speed = random.nextInt(90) + 1;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDamageTaken() {
		return damageTaken;
	}
	
	public boolean isSpecialActive() {
		return specialActive;
	}
	
	public void specialInactive() {
		this.specialActive = false;
	}
	
	public void wontSkip() {//after skipping willSkip variable needs to set false
		willSkip = false;
	}
	
	public void guardDown() {
		this.armor = 1;
	}
	
	public String toString() {	//used to display stats of an opponent
		return "Id:"+getOpponentID()+", Type:"+getType()+", Points:"
				+getPoints()+", Attack:"+getAttack()
				+", Speed:"+getSpeed();
	}
}