package player_layer;

import exception.InsufficientStaminaException;
import mob_layer.Opponent;

public class Sword extends Weapon{
	
	Human<Sword> owner;
	Sword(Human<Sword> owner){
		super();
		this.owner = owner;
		action1Name = "Slash";
		action2Name = "Stab";
	}
	
	public void action1(Opponent enemy,double attackModifier) throws InsufficientStaminaException {	//slash
		slash(enemy,attackModifier);
	}

	public void action2(Opponent enemy,double attackModifier) throws InsufficientStaminaException {	//stab
		stab(enemy,attackModifier);
	}
	
	public void slash(Opponent enemy,double attackModifier) throws InsufficientStaminaException {
		owner.changeStamina(-2);
		enemy.takeDamage((int) (additionalAttack+owner.getAttack()*attackModifier));
	}
	
	public void stab(Opponent enemy,double attackModifier) throws InsufficientStaminaException {
		owner.changeStamina(-2);	//can hit 0 with %25 chance
		int chance = random.nextInt(4);
		if(chance == 1) {
			enemy.takeDamage(0);
		}
		else {
			enemy.takeDamage((int) ((additionalAttack+owner.getAttack())*2*attackModifier));
		}
	}
	

	public String toString() {
		return ", Weapon: Sword with +"+getAdditionalAttack()+" attack";
	}
	
}