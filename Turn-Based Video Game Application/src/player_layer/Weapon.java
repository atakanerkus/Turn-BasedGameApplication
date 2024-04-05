package player_layer;
import java.util.Random;

import exception.InsufficientStaminaException;
import mob_layer.Opponent;

public abstract class Weapon {
	String action1Name;
	String action2Name;
	Random random = new Random();
	int additionalAttack;
	
	public String getAction1Name() {
		return action1Name;
	}
	
	public String getAction2Name() {
		return action2Name;
	}
	
	Weapon(){	//in turn first we need to construct human null then we will create weapon
		additionalAttack = random.nextInt(11)+10;
	}
	
	public int getAdditionalAttack() {
		return additionalAttack;
	}
	
	//action1 and action2 decreases stamina of the character so it throws throws an exception
	//which will be handled in turn class
	public abstract void action1(Opponent enemy,double attackModifier) throws InsufficientStaminaException;
	public abstract void action2(Opponent enemy, double attackModifier) throws InsufficientStaminaException;
}