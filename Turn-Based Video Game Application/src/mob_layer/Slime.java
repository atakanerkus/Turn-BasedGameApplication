package mob_layer;

import player_layer.*;

public class Slime extends Opponent{

	public Slime() {
		super();
		setType();
	}

	public void special(Human target,double attackModifier) {
		absorb(target,attackModifier);
	}
	
	public void absorb(Human human,double attackModifier) {
		int absorbed = human.takeDamage((int)(attack*attackModifier));
		points += absorbed;
		if(points >= 150) {	//after absorption slime's points value cannot be higher than 150.
			System.out.println(this.getOpponentName() + " uses absorb on " +human.getName()+ 
					". Deals "+absorbed+" damage and absorbs "+(150 - (points-absorbed)) + " points");
			setPoints(150);
		}
		else {System.out.println(this.getOpponentName() + " uses absorb on " +human.getName()+ 
				". Deals "+absorbed+" damage and absorbs "+ (absorbed) + " points");}
	}
	
	private void setType() {
		super.type = "Slime";
	}
	
	private void setPoints(int points) {
		this.points = points;
	}
}