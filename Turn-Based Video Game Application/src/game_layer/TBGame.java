package game_layer;

import java.util.Scanner;

import exception.InsufficientStaminaException;
import exception.NotAUniqueNameException;
import exception.SpecialAlreadyUsedException;
import mob_layer.*;
import player_layer.*;

import java.util.Random;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class TBGame {
	private Scanner keyboard;
	private Random random;
	private ArrayList<String> humanNameList; // used deque because in some cases turnOwner can play 2 turns back to back
	private ArrayDeque<Turn> turnArrayDeque; // for that case we need to add the turn to front of the 'deque'
	private ArrayList<Turn> unorderedTurnList;
	private ArrayList<Human<Weapon>> humanList;
	private ArrayList<Opponent> opponentList;
	private Menu menu = new Menu();
	private int moveCounter = 1;

	public TBGame() {
		keyboard = new Scanner(System.in);
		random = new Random();
		humanNameList = new ArrayList<>();
		turnArrayDeque = new ArrayDeque<>();
		unorderedTurnList = new ArrayList<>();
		humanList = new ArrayList<>();
		opponentList = new ArrayList<>();
	}

	public static void main(String[] args) {
		TBGame game = new TBGame();
		game.gameDisplay();
	}

	private void removeIfDefeated() {// when an entity is defeated remove from all collections
		for (Turn turn : turnArrayDeque) {
			if (turn.getOwner() instanceof Human) {
				Human human = (Human) turn.getOwner();
				if (human.isDefeated()) {
					humanList.remove(human);
					turnArrayDeque.remove(turn);
					break;
				}
			}
			if (turn.getOwner() instanceof Opponent) {
				Opponent opponent = (Opponent) turn.getOwner();
				if (opponent.isDefeated()) {
					opponentList.remove(opponent);
					turnArrayDeque.remove(turn);
					break;
				}
			}
		}
	}

	private void gameDisplay() { // displays after initialization of game and the game starts running
		Menu menu = new Menu();
		Turn currentTurn;
		new Initializer();
		System.out.println("\nTurn Order:");
		for (Turn turn : turnArrayDeque) {
			System.out.println(turn.getOwner().toString());
		}
		System.out.println();
		boolean gameRunning = true;
		while (gameRunning) {
			currentTurn = turnArrayDeque.removeFirst(); // deque is moving with each entities' moves
			if (currentTurn.getOwner() instanceof Human) {
				TurnHuman humanTurn = (TurnHuman) currentTurn;
				Human human = humanTurn.getOwner();
				human.guardDown(); // resets guard
				if (!human.isSkipping()) {
					menu.moveHuman(human, humanTurn);
					moveCounter++;
				} else {
					human.wontSkip();
				} // sets willSkip variable to false means next turn this will move
				if (human.getJob().equals("Hunter") && human.isSpecialActive()) { // hunter plays 2 turns back to back
																					// after 'special used turn' is over
					menu.moveHuman(human, humanTurn);
					moveCounter++;
					human.specialInactive();
				}

				removeIfDefeated();
			} else {
				Opponent opponent = (Opponent) currentTurn.getOwner();
				opponent.guardDown(); // resets guard
				if (!opponent.isSkipping()) {
					menu.moveOpponent(opponent);
					moveCounter++;
				} else {
					opponent.wontSkip();
				} // sets willSkip variable to false means next turn this will move

				removeIfDefeated();
			}
			turnArrayDeque.addLast(currentTurn); // after a move is made the turn instance is added to end of the deque
			if (humanList.isEmpty() || opponentList.isEmpty()) { // game over case
				gameRunning = false;
				System.out.println("Game Over");
				menu.displayStats();
			}
		}
	}

	private class Initializer { // initializes game with random variables assigned
		Initializer() {
			System.out.println("Welcome to TB Game!");
			initializeOpponents();
			System.out.println();
			System.out.println("These opponents appeared in front of you:"); // prints "ahead of you" part
			for (Opponent opponent : opponentList) {
				System.out.println(opponent.toString());
			}
			int numOfChar = menu.getNumberOfCharacters();
			humanNameList = menu.setCharacterNameList(numOfChar);
			System.out.println();
			initializeHumans(numOfChar, humanNameList);
			initalizeTurns();
			System.out.println("The battle starts!");
		}

		private void initializeHumans(int charCount, ArrayList<String> humanNameList) {
			int charType;
			Human<Weapon> human = null;
			for (int i = 0; i < charCount; i++) {
				charType = random.nextInt(4);
				switch (charType) {
				case 0:
					human = new Knight(humanNameList.get(i));
					break;
				case 1:
					human = new Hunter(humanNameList.get(i));
					break;
				case 2:
					human = new Squire(humanNameList.get(i));
					break;
				case 3:
					human = new Villager(humanNameList.get(i));
					break;
				}
				humanList.add(human);
			}
		}

		private void initializeOpponents() {
			int opponentType;
			int opponentCount = random.nextInt(1, 5);
			Opponent opponent = null;
			for (int i = 0; i < opponentCount; i++) {
				opponentType = random.nextInt(4);
				switch (opponentType) {
				case 0:
					opponent = new Slime();
					break;
				case 1:
					opponent = new Goblin();
					break;
				case 2:
					opponent = new Orc();
					break;
				case 3:
					opponent = new Wolf();
					break;
				}
				opponentList.add(opponent);
			}
		}

		private void initalizeTurns() {
			TurnHuman turnHuman;
			TurnOpponent turnOpponent;
			for (Human<Weapon> human : humanList) {
				turnHuman = new TurnHuman(human);
				unorderedTurnList.add(turnHuman);
			}
			for (Opponent opponent : opponentList) {
				turnOpponent = new TurnOpponent(opponent);
				unorderedTurnList.add(turnOpponent); // first each turn added to a list unordered
			}
			Collections.sort(unorderedTurnList); // the list is sorted according to each turns' speed values
			turnArrayDeque.addAll(unorderedTurnList); // after ordering list whole list turned into a deque to operate
														// moves
		}
	}

	private class Menu { // gets human player's choices and handles exceptions
							// displays after choices made
		int characterCounter = 0;

		public int getNumberOfCharacters() {
			boolean flag = false;
			String numberOfCharacters = "0";
			while (!flag) {
				System.out.print("Please enter the number of characters to create:");
				numberOfCharacters = keyboard.next();
				switch (numberOfCharacters) {
				case "1":
					flag = true;
					break;
				case "2":
					flag = true;
					break;
				case "3":
					flag = true;
					break;
				default:
					System.out.println("At most 3 characters can be created.");
				}
			}
			return Integer.parseInt(numberOfCharacters);
		}

		private ArrayList<String> setCharacterNameList(int numberOfCharacters) { // we initialize human characters' name
																					// here
			boolean flag = true; // with player's choices and we create 'human' characters
			while (flag) { // with name parameter which are stored in humanNameList
				try {
					System.out.print("Enter the name of the " + (characterCounter + 1) + "th character: ");
					String name = keyboard.next();
					for (String previousName : humanNameList) {
						if (previousName.equals(name)) {
							throw new NotAUniqueNameException("This name is used by previous characters.");
						}
					}
					humanNameList.add(name);
					characterCounter++;
				} catch (NotAUniqueNameException e) {
					System.out.println(e.getMessage());
					System.out.println("Please write a different name. ");
				}
				if (characterCounter == numberOfCharacters)
					flag = false;
			}
			return humanNameList;
		}

		// display methods, after actions
		private void displayHuman(Human human) {
			System.out.println("Name: " + human.getName() + ", Job: " + human.getJob() + ", Points: "
					+ human.getPoints() + ", Stamina: " + human.getStamina());
		}

		private void displayOpponent(Opponent opponent) {
			System.out.println(
					opponent.getOpponentID() + ", Type: " + opponent.getType() + ", Points: " + opponent.getPoints());
		}

		private void displayOpponentAttack(Human human, Opponent opponent) {
			System.out.println("Move " + moveCounter + ": " + opponent.getOpponentName() + " attacks " + human.getName()
					+ ". Deals " + human.getDamageTaken() + " damage.");
			displayHuman(human);
		}

		private void displayOpponentGuard(Opponent opponent) {
			System.out.println(opponent.getOpponentName() + " is guarding.");
		}

		private void displayOpponentSpecial(Opponent opponent) {
			System.out.println("Move" + moveCounter + " Result: " + opponent.getType() + " "
					+ opponent.getOpponentName() + " uses his special");
		}

		private void displayPunch(Human human, Opponent opponent) {
			System.out.println("Move" + moveCounter + " Result: " + human.getName() + " punches "
					+ opponent.getOpponentName() + ". Deals " + opponent.getDamageTaken());
			displayOpponent(opponent);
		}

		private void displayAttackWithWeapon(Human human, Opponent opponent) {
			System.out.println("Move" + moveCounter + " Result: " + human.getName() + " attacks "
					+ opponent.getOpponentName() + ".Deals " + opponent.getDamageTaken());
			displayOpponent(opponent);
		}

		private void displayHumanGuard(Human human) {
			System.out.println("Player " + human.getName() + " is guarding.");
		}

		private void displayHumanSpecialAction(Human human) {
			if (!(human instanceof Villager)) {
				System.out.println("Move" + moveCounter + " Result: " + human.getJob() + " " + human.getName()
						+ " uses his special");
			}
		}

		private void displayStats() {
			for (Turn turn : turnArrayDeque) {
				if (turn.getOwner() instanceof Human) {
					displayHuman((Human) turn.getOwner());
				}
				if (turn.getOwner() instanceof Opponent) {
					displayOpponent((Opponent) turn.getOwner());
				}
			}
		}

		// move methods
		private void moveOpponent(Opponent opponent) {
			TurnOpponent thisTurn = new TurnOpponent(opponent);
			for (Turn turn : turnArrayDeque) {
				if (turn instanceof TurnOpponent) {
					TurnOpponent turnOpponent = (TurnOpponent) turn;
					if (turnOpponent.getOwner().getOpponentID() == (opponent.getOpponentID())) {
						thisTurn = (TurnOpponent) turn;
						if (thisTurn.getOwner().isSpecialActive()) { // goblin play 2 turns back to back to back if
																		// special action is performed
							if (thisTurn.getOwner() instanceof Goblin) // we need to implement special attack of the
																		// goblin while game is running
								thisTurn.setAttackModifier(0.7); // sets attack modifier to 0.7
						}
					}
				}
			}
			// each turn turn owner name and move information are displayed
			System.out.println("\nMove" + moveCounter + " - It is the turn of " + opponent.getOpponentName());

			int move = random.nextInt(3);
			int character = random.nextInt(humanList.size());
			switch (move) {
			case 0: // attack move for opponent
				opponent.attack(humanList.get(character), 1);
				displayOpponentAttack(humanList.get(character), opponent);
				break;
			case 1: // guard move for opponent
				opponent.guard();
				displayOpponentGuard(opponent);
				break;
			case 2: // special action move for opponent
					// we implement most of the special actions here to make it easier
				displayOpponentSpecial(opponent);
				if (opponent instanceof Wolf) { // opponent is wolf, clones itself
					opponent.special(humanList.get(character), thisTurn.getAttackModifier());
					if (opponent.isSpecialActive()) {
						opponent.specialInactive();
						Wolf newWolf = (Wolf) opponent; // we can't use .clone method directly
						Wolf wolfClone = newWolf.clone(); // so we created 2 wolf instances
						TurnOpponent turnObj = new TurnOpponent(wolfClone); // new cloned wolf's turn instance
						opponentList.add(wolfClone); // cloned wolf added to opponentList
						turnArrayDeque.addFirst(turnObj); // added to turnArrayDeque from front
					}
				}
				if (opponent instanceof Orc) {
					thisTurn.setAttackModifier(2);
					opponent.special(humanList.get(character), thisTurn.getAttackModifier());
					opponent.specialInactive();
					displayOpponentAttack(humanList.get(character), opponent);
				}
				if (opponent instanceof Slime) {
					opponent.special(humanList.get(character), thisTurn.getAttackModifier());
				}

				if (opponent instanceof Goblin) {
					thisTurn.setAttackModifier(0.7);
					opponent.special(humanList.get(character), thisTurn.getAttackModifier());
					displayOpponentAttack(humanList.get(character), opponent);
					moveCounter++;
					moveOpponent(opponent);
				}
				break;
			}
		}

		private void moveHuman(Human human, TurnHuman thisTurn) {
			if (thisTurn.getOwner().isSpecialActive()) {
				String job = thisTurn.getOwner().getJob();
				switch (job) {
				case "Knight":
					thisTurn.setAttackModifier(3);
					System.out.println(
							thisTurn.getOwner().getName() + "'s special active this turn and will deal 3x damage");
					thisTurn.getOwner().specialInactive(); // informs the player about special action
					break;

				default:
					thisTurn.getOwner().specialInactive();
				}
			}
			int selectOpponentId;
			int selectAttack;
			Opponent opponent;
			boolean flag = true;
			while (flag) {	//to ensure that the player's input is 1,2,3,4 or 5.

				System.out.println(); // displays moves of the player to choice
				System.out.println("Move" + moveCounter + " - It is the turn of " + human.getName());
				System.out.println("""
						[1]Punch
						[2]Attack with weapon
						[3]Guard
						[4]Special Action
						[5]Run
						""");
				System.out.print("Please select and option: ");
				String choice = keyboard.next();
				switch (choice) {
				case "1": // punch case
					selectOpponentId = selectOpponentId();
					opponent = selectOpponent(selectOpponentId);
					try {
						human.punch(opponent, thisTurn.getAttackModifier());
						displayPunch(human, opponent);
					} catch (InsufficientStaminaException e) {
						System.out.println(e.getMessage());
						System.out.println("You have " + human.getStamina() + " stamina left");
						moveHuman(human, thisTurn);
					}
					flag = false;
					break;
				case "2": // attack with weapon case
					selectAttack = selectWeaponAttackType(human.getWeapon());
					selectOpponentId = selectOpponentId();
					opponent = selectOpponent(selectOpponentId);
					try {
						human.attackWithWeapon(opponent, selectAttack, thisTurn.getAttackModifier());
						displayAttackWithWeapon(human, opponent);
					} catch (InsufficientStaminaException e) {
						System.out.println(e.getMessage());
						System.out.println("You have " + human.getStamina() + " stamina left");
						moveHuman(human, thisTurn);
					}
					flag = false;
					break;
				case "3": // guard case
					try {
						human.guard();
						displayHumanGuard(human);
					} catch (InsufficientStaminaException e) {
						System.out.println(e.getMessage());
						System.out.println("You have " + human.getStamina() + " stamina left");
						moveHuman(human, thisTurn);
					}
					flag = false;
					break;
				case "4": // special action case
					if (human instanceof Villager) {
						try {
							human.specialAction();
							displayHumanSpecialAction(human);
						} catch (SpecialAlreadyUsedException e) {
							System.out.println(e.getMessage());
							moveHuman(human, thisTurn);
						}
					}
					if (human instanceof Knight) {
						try {
							human.checkSpecial();
							human.specialAction();
							displayHumanSpecialAction(human);
						} catch (SpecialAlreadyUsedException e) {
							System.out.println(e.getMessage());
							moveHuman(human, thisTurn);
						}
					}
					if (human instanceof Squire) {
						try {
							human.checkSpecial();
							human.specialAction();
							thisTurn.setAttackModifier(0.5);
							displayHumanSpecialAction(human);
						} catch (SpecialAlreadyUsedException e) {
							System.out.println(e.getMessage());
							moveHuman(human, thisTurn);
						}
					}
					if (human instanceof Hunter) {
						try {
							human.checkSpecial();
							human.specialAction();
							thisTurn.setAttackModifier(0.5);
							displayHumanSpecialAction(human);
							moveHuman(human, thisTurn);
						} catch (SpecialAlreadyUsedException e) {
							System.out.println(e.getMessage());
							moveHuman(human, thisTurn);
						}
					}
					flag = false;
					break;
				case "5":
					human.run();
					System.exit(0);
					flag = false;
					break;
				default:
					System.out.println("Invalid move please choose again.");
				}
				thisTurn.setAttackModifier(1); // after each turn attack modifier is initialized to 1
			}
		}

		private int selectWeaponAttackType(Weapon weapon) {
			boolean flag = true;
			String choice = "0";
			while (flag) {
				System.out.print("Please select weapon attack type [1]" + weapon.getAction1Name() + " [2]"
						+ weapon.getAction2Name() + ": ");
				choice = keyboard.next();
				switch (choice) {
				case "1":
					flag = false;
					break;
				case "2":
					flag = false;
					break;
				default:
					System.out.println("Invalid input");
				}
			}
			return Integer.parseInt(choice);
		}

		private Opponent selectOpponent(int opponentId) { // after selecting opponnent id, finds opponent in
															// opponentList
			for (Opponent opponent : opponentList)
				if (opponent.getOpponentID() == opponentId)
					return opponent;
			System.out.println("Cannot select an opponent.");
			return null;
		}

		private int selectOpponentId() {
			int opponentCount = opponentList.size();
			boolean flag = true;
			String choice = "0";
			while (flag) {
				System.out.println("There are " + opponentCount + " opponents in this turn.");
				for (Opponent opponent : opponentList) {
					System.out.println(opponent.toString());
				}
				System.out.print("Please enter an opponent id: ");
				choice = keyboard.next();
				try {
					for (Opponent opponent : opponentList) {
						if (opponent.getOpponentID() == Integer.parseInt(choice)) {
							flag = false;
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Please write a number.");
				}
				if (flag)
					System.out.println("There is no such opponent.");
			}
			return (Integer.parseInt(choice));
		}
	}
}