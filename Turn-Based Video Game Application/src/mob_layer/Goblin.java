package mob_layer;

import player_layer.Human;

public class Goblin extends Opponent{
	
	
	public Goblin() {
		super();
		setType();
	}
	
	public void special(Human target,double attackModifier) {
		rushingAttack(target,attackModifier);
	}
	
	public void rushingAttack(Human target,double attackModifier) {
		specialActive = true;
		super.attack(target,attackModifier);
	}
	
	private void setType() {
		super.type = "Goblin";
	}
	
	public void endRush() {//need to use in Turn class after checking if rushed previous turn.
		specialActive = false;
	}
}
