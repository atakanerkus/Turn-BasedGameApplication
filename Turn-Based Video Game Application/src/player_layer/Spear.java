package player_layer;

import exception.InsufficientStaminaException;
import mob_layer.Opponent;

public class Spear extends Weapon{
	
	Human<Spear> owner;
	Spear(Human<Spear> owner){
		super();
		this.owner = owner;
		action1Name = "Stab";
		action2Name = "Throw";
	}
	
	public void action1(Opponent enemy,double attackModifier) throws InsufficientStaminaException {
		stab(enemy,attackModifier);
	}

	public void action2(Opponent enemy, double attackModifier) throws InsufficientStaminaException {
		thrown(enemy,attackModifier);
	}
	
	private void stab(Opponent enemy,double attackModifier) throws InsufficientStaminaException {
		owner.changeStamina(-2);
		enemy.takeDamage((int)((additionalAttack+owner.getAttack())*1.1*attackModifier));
	}
	private void thrown(Opponent enemy,double attackModifier) throws InsufficientStaminaException {
		owner.changeStamina(-2);
		enemy.takeDamage((int)((additionalAttack+owner.getAttack())*2*attackModifier));
		owner.skip();
	}
	
	public String toString() {
		return ", Weapon: Spear with +"+getAdditionalAttack()+" attack";
	}
	
}
