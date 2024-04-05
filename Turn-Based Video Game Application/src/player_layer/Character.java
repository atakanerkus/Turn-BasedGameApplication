package player_layer;

import exception.InsufficientStaminaException;
import exception.SpecialAlreadyUsedException;
import mob_layer.Opponent;

public interface Character <W extends Weapon>{
	void punch(Opponent target,double attackModifier) throws InsufficientStaminaException;
	public <W> void attackWithWeapon(Opponent target,int choice, double attackModifier)throws InsufficientStaminaException;
	void guard() throws InsufficientStaminaException;
	void run();
	void specialAction() throws SpecialAlreadyUsedException;
}