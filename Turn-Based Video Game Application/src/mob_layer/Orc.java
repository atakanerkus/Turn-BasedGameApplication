package mob_layer;

import player_layer.Human;

public class Orc extends Opponent{	

	
	public Orc() {
		super();
		setType();
	}
	
	public void special(Human target, double attackModifier) {
		heavyHit(target,attackModifier);
	}
	
	public void heavyHit(Human target,double attackModifier) {
		specialActive = true;
		target.takeDamage((int)(attack*attackModifier));
		willSkip = true;
	}
	
	private void setType() {
		super.type = "Orc";
	}
}
