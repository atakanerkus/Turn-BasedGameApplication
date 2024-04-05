package mob_layer;

import player_layer.Human;
import java.util.Random;

public class Wolf extends Opponent{
	public Wolf() {
		super();
		setType();
	}
	
	public Wolf(Wolf wolf) {
		super();
		this.armor = wolf.armor;
		this.attack = wolf.attack;
		this.speed = wolf.speed;
		this.points = wolf.points;
		setType();
	}
	
	public void special(Human target,double attackModifier) {
		Random random = new Random();
		int chance = random.nextInt(5);
		
		if (chance == 1) {
			callFriend();
			System.out.println("New wolf added to the game");
		}
		else {
			System.out.println("Opponent"+ opponentID + " tried to use special but failed");
		}
	}
	
	private void setType() {
		super.type = "Wolf";
	}
	
	public void callFriend() {//new cloned wolf will be addded to turnArrayDeque while game is running
		specialActive = true;
	}

	public Wolf clone() {
		Wolf newWolf = new Wolf(this);
		return newWolf;
	}
}