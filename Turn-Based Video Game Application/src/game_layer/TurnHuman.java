package game_layer;

import player_layer.Human;
import player_layer.Weapon;

public class TurnHuman extends Turn{
	double attackModifier = 1;//initialized to 1
	Human<Weapon> owner;
	String name; 
	
	TurnHuman(Human<Weapon> owner){
		super();
		this.owner = owner;
		setSpeed(owner.getSpeed());
		this.name = owner.getName();
	}
	
	public boolean isDefeated() {
		if(owner.getPoints()<=0) {
			return true;
		}
		else 
			return false;
	}

	public Human getOwner() {
		return owner;
	}
}