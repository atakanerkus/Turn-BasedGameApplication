package player_layer;
import exception.*;
import mob_layer.*;
import java.util.Random;

public abstract class Human<W extends Weapon> implements Character<W>{	
	
	//we used abstract class to write some non-unique methods for all human jobs
	//we used inheritance for each human jobs because each job has an unique special action
	//because of inheritance code became more undestandable
	
	Random random = new Random();
	String name;
	int points;
	int stamina = 10;
	int attack;
	int speed;
	int armor = 1; //damage taken = damage/armor
	int damageTaken;
	Weapon weapon;
	String job;
	boolean specialUsable = true;
	boolean willSkip;
	boolean isDefeated;
	boolean specialActive; //to carry the effect of specials to other turns
	
	Human(String name){
		setPoints();
		setAttack();
		setSpeed();
		setWeapon();
		this.name = name;
	}
	
	public void punch(Opponent target,double attackModifier) throws InsufficientStaminaException  {
			changeStamina(-1);
			target.takeDamage((int)(attack*0.8*attackModifier));
	}
	
	public void skip() {
		willSkip = true;
	}
	
	public boolean isSkipping() {
		return willSkip;
	}
	
	public <W> void attackWithWeapon(Opponent target, int choice, double attackModifier) throws InsufficientStaminaException{
		switch(choice) {
		case 1:
			weapon.action1(target,attackModifier);//#TODO how can we use generics in this method
			break;
		case 2:
			weapon.action2(target,attackModifier);
			break;
			//choice can only be 1 or 2 will be checked in turn
		}
	}
	
	public void guard() throws InsufficientStaminaException {
		this.armor = 4;		//#TODO need to change armor to 1 after turn
		changeStamina(3);
	}
	
	public void checkStamina(int stamina) throws InsufficientStaminaException {
		if(stamina < 0) {
			throw new InsufficientStaminaException("Not enough stamina for this action");
			//and again human can make a choice
		}
	}
	
	public void checkSpecial() throws SpecialAlreadyUsedException{
		if(!specialUsable) {
			throw new SpecialAlreadyUsedException("You have already used your special");
			//and again human can make a choice
		}
	}
	
	public void run() {
		System.out.println("Your character(s) started running away." + "The battle ends!\nThanks for playing");
	}
	
	public int takeDamage(int dmg) {	//dmg parameter is taken in Turn from Opponent's attack and the attack modifier which is in Turn class
		this.points -= dmg/armor;
		damageTaken = dmg/armor;
		if(points<=0)
			isDefeated=true;
		return dmg/armor;			//returns damage taken
	}
	
	private void setPoints() { 
		this.points = random.nextInt(51) + 100;
	}
	
	private void setAttack() {
		this.attack = random.nextInt(21) + 20;
	}
	
	private void setSpeed() {
		this.speed = random.nextInt(90) + 10;
	}
	
	public boolean isDefeated() {
		return isDefeated;
	}
	
	private void setWeapon() {	//initialized randomly
		int weaponType = random.nextInt(3);
		switch(weaponType) {
		case 0:
			weapon = new Sword((Human<Sword>) this);
			break;
		case 1:
			weapon = new Spear((Human<Spear>) this);
			break;
		case 2:
			weapon = new Bow((Human<Bow>)this);
			break;
		}
	}
	
	//used to change stamina after lowering or increasing stamina value
	public void changeStamina(int change) throws InsufficientStaminaException {
		int temp=stamina;
		temp+=change;
		checkStamina(temp);
		stamina = temp;
	}
	
	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public int getStamina() {
		return stamina;
	}

	public int getAttack() {
		return attack;
	}

	public int getSpeed() {
		return speed;
	}

	public Weapon getWeapon() {
		return (Weapon) weapon;
	}

	public String getJob() {
		return job;
	}

	public int getDamageTaken() {
		return damageTaken;
	}
	
	public boolean isSpecialUsable() {
		return specialUsable;
	}
	
	public boolean isSpecialActive() {
		return specialActive;
	}
	
	public void specialInactive() {	//special action can only be used 1 time for humans
		this.specialActive = false;
		this.specialUsable = false;
	}
	
	public void wontSkip() {
		willSkip = false;
	}
	
	public void guardDown() {//sets guard 1 after each guard to prevent stacking guards
		this.armor = 1;
	}
	
	public String toString() {//information about a human instance
		return "Name:"+getName()+", Job:"+getJob()+", Points:"+getPoints()
				+", Attack:"+getAttack()+", Speed:"+getSpeed()+getWeapon().toString();
	}
	
}