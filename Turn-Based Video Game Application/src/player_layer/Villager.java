package player_layer;

public class Villager extends Human<Weapon>{

	public Villager(String name) {
		super(name);
		setJob();
	}
	
	private void setJob() {
		super.job = "Villager";
	}
	
	public void specialAction() { //does not have a special attack
		System.out.println("I am just a villager i don't have a special action");
	}

}
