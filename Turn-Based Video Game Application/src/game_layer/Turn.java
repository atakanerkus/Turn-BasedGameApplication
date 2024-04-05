package game_layer;

public abstract class Turn implements Comparable<Turn>{
	//used inheritance to specify turn owner's type and implement different attributes because of owners' classes are not same
	//used abstract class to write each non-unique methods of TurnHuman and TurnOpponent
	//used Comparable interface to use collection's sorting algorithm 
	int speed;
	double attackModifier = 1;
	boolean willSkip;
	public abstract boolean isDefeated();
	
	void setAttackModifier(double value) {
		attackModifier = value;
	}
	
	public double getAttackModifier() {
		return this.attackModifier;
	}
	//compareTo compares each turn with their speed attribute
	public int compareTo(Turn otherTurn) {
		if(this.speed < otherTurn.speed)
			return 1;
		else
			return -1;
		//if two are the speed values are equal the object that is used to calculate compareTo is bigger.
		//i made this way to handle the situation if two of the speed values are equal.
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpeed() {
		return this.speed;
	}
	public abstract Object getOwner();//will be implemented in TurnHuman and TurnOpponent
}
