package player_layer;

import exception.InsufficientStaminaException;
import exception.SpecialAlreadyUsedException;

public class Squire extends Human<Weapon>{
	
	public Squire(String name) {
		super(name);
		setJob();
	}
	
	private void setJob() {
		super.job = "Squire";
	}
	
	public void specialAction() throws SpecialAlreadyUsedException{
		if (this.stamina < 10)
			try {
				changeStamina(10-this.stamina);
			} catch (InsufficientStaminaException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}	//changes stamina to 10.
		
		specialActive = true;			//attackModifier = 0.5 will be in turn class after this action
	}
}
