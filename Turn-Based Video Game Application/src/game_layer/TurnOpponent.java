package game_layer;

import mob_layer.Opponent;

public class TurnOpponent extends Turn{
	Opponent owner;
	String name;
	int id;
	double attackModifier = 1;
	
	TurnOpponent(Opponent owner){
		super();
		this.owner = owner;
		this.name = owner.getOpponentName();
		setSpeed(owner.getSpeed());
		this.id = owner.getOpponentID();
	}
	
	public boolean isDefeated() {
		if(owner.getPoints()<=0) {
			return true;
		}
		else 
			return false;
	}
	
	public Opponent getOwner() {
		return owner;
	}
}