package player_layer;

import exception.SpecialAlreadyUsedException;

public class Knight extends Human<Weapon>{
	
	public Knight(String name) {
		super(name);
		setJob();
	}
	
	private void setJob() {
		super.job = "Knight";
	}
	
	public void specialAction() throws SpecialAlreadyUsedException{
		specialActive = true;
	}
}
