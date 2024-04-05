package player_layer;

import exception.SpecialAlreadyUsedException;

public class Hunter extends Human<Weapon>{
	public Hunter(String name) {
		super(name);
		setJob();
	}
	
	private void setJob() {
		super.job = "Hunter";
	}
	
	public void specialAction() throws SpecialAlreadyUsedException{	//action needs to be implemented while game is running
		specialActive = true;
	}
}
