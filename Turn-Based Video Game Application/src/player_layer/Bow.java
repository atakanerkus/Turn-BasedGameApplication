package player_layer;

import exception.InsufficientStaminaException;
import mob_layer.Opponent;

public class Bow extends Weapon{

	Human<Bow> owner;
	Bow(Human<Bow> owner){
		super();
		this.owner = owner;
		action1Name = "Single Arrow";
		action2Name = "Two Arrow";
	}
	
	public void action1(Opponent enemy, double attackModifier) throws InsufficientStaminaException {
		singleArrow(enemy,attackModifier);
	}

	public void action2(Opponent enemy, double attackModifier) throws InsufficientStaminaException {
		twoArrow(enemy,attackModifier);
	}
	private void singleArrow(Opponent enemy,double attackModifier) throws InsufficientStaminaException{
		owner.changeStamina(-1);
		enemy.takeDamage((int)((additionalAttack+owner.getAttack())*0.8*attackModifier));
	}
	
	private void twoArrow(Opponent enemy, double attackModifier) throws InsufficientStaminaException{
		owner.changeStamina(-3);
		enemy.takeDamage((int)((additionalAttack+owner.getAttack())*2.5*attackModifier));
	}
	
	public String toString() {
		return ", Weapon: Bow with +"+getAdditionalAttack()+" attack";
	}
	
}