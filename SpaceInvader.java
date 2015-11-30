/* Compliance Statement
 * 
 * New Alien Description:
 * 
 * 
 * Our new alien, inspired by Francois's Critter Class lecture,
 * is a "Galactic Poodle Boss" on a quest for a new haircut. 
 * The PoodleBoss is drawn from scratch and is larger than the original aliens provided. 
 * The class that describes it, PoodleBoss, overrides several 
 * of the methods in the class it extends, Alien. The PoodleBoss moves differently. It randomly
 * reappears at a new location on the screen every 15 "ticks" of the timer. The PoodleBoss also 
 * has more lives than the original alien and spawns miniature PoodleBosses called PoodleBullets
 * of the PoodleBullet class which incur damage to the Spaceship but cannot be destroyed. 
 * All of these aspects make the PoodleBoss harder to defeat. 
 * To communicate its desires, the PoodleBoss emits a sound each time it's struck by the Spaceship.
 * 
 * We implemented the following extra features:
 * 
 * 1. Collision Ability: SpaceShip loses "life" if it collides with an alien. 
 * 2. The PoodleBoss emits different sounds when it is hit depending on remaining lives.
 * 3. A second "boss" level that begins after the original 9 aliens are destroyed. 
 * 4. An instruction window on the side of the game showing game controls and
 * 	  life counts for each type of alien.
 * 5. A Health Meter that displays the Spaceship's remaining health with both a visual 
 *    bar representation and a numerical display.
 * 6. PoodleBullets are generated from each PoodleBoss. PoodleBullets are described by their 
 *    own class that extends PoodleBoss. PoodleBullets incur damage and cannot be destroyed.  
 */




import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import uwcse.graphics.GWindow;
import uwcse.graphics.GWindowEvent;
import uwcse.graphics.GWindowEventAdapter;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.TextShape;
import uwcse.io.Sound;

/**
 * A SpaceInvader displays a fleet of alien ships and a space ship. The player
 * directs the moves of the spaceship and can shoot at the aliens.
 */

public class SpaceInvader extends GWindowEventAdapter {
	// Possible actions from the keyboard
	/** No action */
	public static final int DO_NOTHING = 0;

	/** Steer the space ship */
	public static final int SET_SPACESHIP_DIRECTION = 1;

	/** To shoot at the aliens */
	public static final int SHOOT = 2;

	// Period of the animation (in ms)
	// (the smaller the value, the faster the animation)
	private int animationPeriod = 100;

	// Current action from the keyboard
	private int action;

	// Game window
	private GWindow window;

	// The space ship
	private SpaceShip spaceShip;

	// Direction of motion given by the player
	private int dirFromKeyboard = MovingObject.LEFT;

	// The aliens
	private ArrayList<Alien> aliens;

	// Are all the aliens dead?
	private String messageGameOver = "CONGRATULATIONS! You've saved the world.";

	// Is your spaceship destroyed?
	private String messageShipDestroyed = "Your ship has been destroyed";

	// Instruction bar width
	public static final int INSTRUCTION_WIDTH = 200; // in px

	// Current level completed
	private int levelCompleted = 1;

	// Sound to begin next level
	private static Sound funnyHaircut = new Sound("Funny Haircut.wav");

	// the Boss alien
	private PoodleBoss poodleBoss;

	/**
	 * Construct a space invader game
	 */
	public SpaceInvader() {
		this.window = new GWindow("Space Invaders", 500 + INSTRUCTION_WIDTH,
				500); // ***Create window object (500 x 500 original size)
		this.window.setExitOnClose();

		/*
		 * this SpaceInvader handles all of the events fired by the graphics
		 * window
		 */

		this.window.addEventHandler(this); // TODO What does "this" keyword do
											// here?

		// Display the game rules
		String rulesOfTheGame = "Save the Earth! Destroy all of the alien ships.\n"
				+ "To move left, press '<'.\n"
				+ "To move right, press '>'.\n"
				+ "To shoot, press the space bar.\n" + "To quit, press 'Q'.";
		JOptionPane.showMessageDialog(null, rulesOfTheGame, "Space Invaders",
				JOptionPane.INFORMATION_MESSAGE);
		this.initializeGame();
	}

	/**
	 * Initialize the game (draw the background, aliens, and space ship)
	 */
	private void initializeGame() {
		// Clear the window
		this.window.erase();

		// Reset level of game
		this.levelCompleted = 1;

		// Clear poodleBullet array
		if (poodleBoss != null) {
			this.poodleBoss.poodleBullets = null;
		}

		// Background (starry universe)
		// *** - 200 added for instruction sidebar
		Rectangle background = new Rectangle(0, 0, this.window.getWindowWidth()
				- INSTRUCTION_WIDTH, this.window.getWindowHeight(),
				Color.black, true);
		this.window.add(background);
		// Add 50 stars here and there (as small circles)
		Random rnd = new Random();
		for (int i = 0; i < 50; i++) {
			// Random radius between 1 and 3
			int radius = rnd.nextInt(3) + 1;
			// Random location (within the window)
			// Make sure that the full circle is visible in the window
			int x = rnd.nextInt(this.window.getWindowWidth()
					- INSTRUCTION_WIDTH - (2 * radius)); // *** - 200 added for
															// instruction
															// sidebar
			int y = rnd.nextInt(this.window.getWindowHeight() - 2 * radius);
			this.window.add(new Oval(x, y, 2 * radius, 2 * radius, Color.white,
					true));
		}

		// Add Instruction Panel

		Shape[] instructionPanel = new Shape[11];

		instructionPanel[0] = new Rectangle(window.getWindowWidth()
				- INSTRUCTION_WIDTH, 0, INSTRUCTION_WIDTH,
				window.getWindowHeight(), Color.ORANGE, true);
		instructionPanel[1] = new TextShape("Space Invader",
				window.getWindowWidth() - 190, 50);
		instructionPanel[2] = new TextShape("Move Left: <",
				window.getWindowWidth() - 190, 100);
		instructionPanel[3] = new TextShape("Move Right: >",
				window.getWindowWidth() - 190, 120);
		instructionPanel[4] = new TextShape("Shoot: Spacebar",
				window.getWindowWidth() - 190, 140);
		instructionPanel[5] = new TextShape("Quit: Q",
				window.getWindowWidth() - 190, 160);
		instructionPanel[6] = new TextShape("3 Lives",
				window.getWindowWidth() - 160, 190);
		instructionPanel[7] = new TextShape("2 Lives",
				window.getWindowWidth() - 160, 230);
		instructionPanel[8] = new TextShape("1 Life",
				window.getWindowWidth() - 160, 270);
		instructionPanel[9] = new TextShape("SpaceShip Health",
				window.getWindowWidth() - 190, window.getWindowHeight() - 59);
		instructionPanel[10] = new Rectangle(window.getWindowWidth() - 190,
				window.getWindowHeight() - 41, 150, 20, Color.WHITE, false); // Health
																				// Meter
																				// Outline

		for (int i = 0; i < instructionPanel.length; i++)
			window.add(instructionPanel[i]);

		// Add alien "key" to instruction panel

		new Alien(this.window, new Point(window.getWindowWidth() - 170, 200), 3);
		new Alien(this.window, new Point(window.getWindowWidth() - 170, 240), 2);
		new Alien(this.window, new Point(window.getWindowWidth() - 170, 280), 1);

		// ArrayList of aliens
		this.aliens = new ArrayList<Alien>();

		// Create 9 aliens
		// Initial location of the aliens
		// (Make sure that the space ship can fire at them)
		int x = 2 * SpaceShip.WIDTH;
		int y;

		/** ORIGINAL FOR LOOP TO ADD ALIENS TO ArrayList */
		for (int i = 0; i < 9; i++) {
			y = (int) (Math.random() * 20) * Alien.radius;
			this.aliens.add(new Alien(this.window, new Point(x, y))); // CALL TO
			// ALIEN CONSTRUCTOR
			x += 10 * Alien.radius;
		}

		// Create the space ship at the bottom of the window
		x = this.window.getWindowWidth() / 2;
		y = this.window.getWindowHeight() - SpaceShip.HEIGHT / 2;
		// CALL TO SPACESHIP CONSTRUCTOR
		this.spaceShip = new SpaceShip(this.window, new Point(x, y));
		// Reset health level of SpaceShip
		this.spaceShip.initHealth = 50;

		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	/**
	 * Move the objects within the graphics window every time the timer fires an
	 * event
	 */
	public void timerExpired(GWindowEvent we) {
		// Perform the action requested by the user?

		switch (this.action) {

		case SpaceInvader.SET_SPACESHIP_DIRECTION:
			this.spaceShip.setDirection(this.dirFromKeyboard);
			break;

		case SpaceInvader.SHOOT:
			this.spaceShip.shoot(this.aliens);
			break;
		}

		// Don't do the same action twice:
		this.action = SpaceInvader.DO_NOTHING; // TODO Not sure what this
												// does...

		// Show the new locations of the objects
		this.updateGame();
	}

	/**
	 * Select the action requested by the pressed key
	 */
	public void keyPressed(GWindowEvent e) {
		// Don't perform the actions (such as shoot) directly in this method.
		// Do the actions in timerExpired, so that the alien ArrayList can't be
		// modified at the same time by two methods (keyPressed and timerExpired
		// run in different threads).

		switch (Character.toLowerCase(e.getKey())) // not case sensitive
		{
		// Put here the code to move the space ship with the < and > keys

		case ' ': // shoot at the aliens
			this.action = SpaceInvader.SHOOT; // draw laser beam in graphics
												// window
			break;

		case 'q': // quit the game
			System.exit(0);

		case ',':
		case '<': // -> move left
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.LEFT;
			break;

		case '.':
		case '>': // -> move right
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			this.dirFromKeyboard = MovingObject.RIGHT;
			break;

		default: // no new action
			this.action = SpaceInvader.DO_NOTHING;
			break;
		}
	}

	/**
	 * Update the game (Move aliens + space ship)
	 */
	private void updateGame() {
		// Is the game won (or lost)?
		// whether the user chooses to continue game or not
		boolean result;

		if (this.spaceShip.health <= 0) {
			result = anotherGame(messageShipDestroyed); // ask if user wants
			// to play again
			if (result) {
				initializeGame(); // Start a new game
			} else {
				System.exit(0); // Exit Game
			}
		}

		// all aliens have been killed
		if (aliens.size() <= 0) {
			levelCompleted++;

			// first time

			if (levelCompleted == 2) {
				funnyHaircut.play();
				JOptionPane.showMessageDialog(null, "Prepare to be poodled!",
						"LEVEL 2", JOptionPane.WARNING_MESSAGE);

				new PoodleBoss(this.window, new Point(
						window.getWindowWidth() - 90, 360));
				window.add(new TextShape("10 Lives",
						window.getWindowWidth() - 180, 330));
				poodleBoss = new PoodleBoss(this.window, new Point(250, 50));
				this.aliens.add(poodleBoss);

			} else if (levelCompleted == 3) { // Second time
				// ask if user wants to play again
				result = anotherGame(messageGameOver);

				if (result) {
					initializeGame(); // Start a new game
				} else {
					System.exit(0); // Exit Game
				}
			}
		}

		this.window.suspendRepaints(); // suspend repaints to speed up the
										// drawing

		// Remove the dead aliens from the ArrayList
		Iterator<Alien> it = aliens.iterator(); // TODO Research how Iterator
												// Class works

		while (it.hasNext()) { // while aliens still exist in aliens ArrayList

			Alien a = it.next(); // select next alien, dead or alive

			if (a.isDead()) { // check if alien is dead
				it.remove(); // Remove alien from ArrayList
			}
		}

		// Move the aliens
		for (Alien a : aliens) {
			a.move();
		}

		// Move the space ship
		this.spaceShip.move();

		// Check for collisions between aliens and spaceship
		this.spaceShip.collision(aliens);

		// Check for collisions between poodlebullets and spaceship
		if (poodleBoss != null && poodleBoss.poodleBullets != null) {
			this.spaceShip.pBulletcollision(poodleBoss.poodleBullets);
		}
		// Display it all
		this.window.resumeRepaints();
	}

	/**
	 * Does the player want to play again?
	 */
	public boolean anotherGame(String s) {
		// TODO
		// this method is useful at the end of a game if you want to prompt the
		// user
		// for another game (s would be a String describing the outcome of the
		// game
		// that just ended, e.g. "Congratulations, you saved the Earth!")
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		// Returns true when Yes is selected by user, false otherwise
		return (choice == JOptionPane.YES_OPTION); // How does this work? TODO

	}

	/**
	 * Starts the application (main method)
	 */
	public static void main(String[] args) {
		new SpaceInvader(); // Instantiate a new object of the SpaceInvader
							// class
	}
}
