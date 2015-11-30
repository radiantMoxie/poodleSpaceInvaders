import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.TextShape;
import uwcse.graphics.Triangle;
import uwcse.io.Sound;

/**
 * The space ship
 */
public class SpaceShip extends MovingObject {
	/** Height of a space ship */
	public static final int HEIGHT = 40;

	/** Width of a space ship */
	public static final int WIDTH = 20;

	/** Is the space ship shooting with its laser? */
	private boolean isShooting;

	/** Define sound effect from .wav file */
	private static Sound explosion = new Sound("Laser.wav");

	/** SpaceShip initial health */
	// can be used to adjust difficulty
	protected double initHealth = 50;

	/** SpaceShip dynamic health */
	protected int health = (int) initHealth;

	/** SpaceShip Health Meter */
	private Rectangle healthMeter; // Initial "full" health meter

	/** SpaceShip Health Amount */
	private TextShape healthAmount;

	/**
	 * Construct this SpaceShip
	 */
	public SpaceShip(GWindow window, Point center) {
		super(window, center);
		this.direction = MovingObject.LEFT;

		// Draw this SpaceShip
		this.draw(); // Calls draw method in SpaceShip (this) class

		this.healthMeter = new Rectangle(window.getWindowWidth() - 189,
				window.getWindowHeight() - 40, 149, 19, Color.GREEN, true);
		this.healthAmount = new TextShape("100", window.getWindowWidth() - 62,
				window.getWindowHeight() - 59);
		window.add(healthMeter);
		window.add(healthAmount);

	}

	/**
	 * Move this SpaceShip. The space ship should be constantly moving. Select a
	 * new direction if the space ship can't move in the current direction of
	 * motion.
	 */
	public void move() {
		// A space ship moves left or right
		if (this.direction != MovingObject.LEFT
				&& this.direction != MovingObject.RIGHT)
			throw new IllegalArgumentException("Invalid space ship direction");

		// move this SpaceShip
		boolean isMoveOK;
		// Distance covered by the space ship in one step
		int step = this.boundingBox.getWidth() / 2;

		do {
			int x = this.center.x;
			switch (this.direction) {
			case LEFT:
				x -= step;
				break;
			case RIGHT:
				x += step;
				break;
			}

			// Is the new position in the window?
			if (x + this.boundingBox.getWidth() / 2 >= this.window
					.getWindowWidth() - SpaceInvader.INSTRUCTION_WIDTH)
			// past the right edge
			{
				isMoveOK = false;
				this.direction = MovingObject.LEFT;
			} else if (x - this.boundingBox.getWidth() / 2 <= 0) // past the
			// left edge
			{
				isMoveOK = false;
				this.direction = MovingObject.RIGHT;
			} else // it is in the window
			{
				isMoveOK = true;
				this.center.x = x;
			}
		} while (!isMoveOK);

		// Show the new location of this SpaceShip
		this.erase();
		this.draw();
	}

	/**
	 * Shoot at the aliens If an alien is hit, decrease its number of lives or
	 * remove it from the array list if it is dead.
	 * 
	 * @param aliens
	 *            the ArrayList of aliens
	 */
	public void shoot(ArrayList<Alien> aliens) {

		explosion.play(); // Play explosion sound

		this.isShooting = true;
		// is an alien shot?
		for (int i = 0; i < aliens.size(); i++) { // Iterate over list of aliens

			Alien a = aliens.get(i); // Get current alien from ArrayList

			Rectangle box = a.getBoundingBox(); // returns a rectangle

			System.out.println("Box Left Edge" + box.getX());
			System.out.println("Center: " + center.x);

			// Check if alien is in the path of the laser
			if (center.x >= box.getX()
					&& center.x <= box.getX() + box.getWidth()) {
				a.isShot(); // Call isShot() method on current alien (based on
							// i)
				System.out.println("IS SHOT CALLED");
			}

		}
	}

	/**
	 * Determine if a collision has occurred between a spaceship and an alien
	 */

	public void collision(ArrayList<Alien> aliens) {
		for (int i = 0; i < aliens.size(); i++) { // Iterate over list of aliens

			Alien a = aliens.get(i); // Get current alien from ArrayList

			Rectangle alienBox = a.getBoundingBox(); // returns a rectangle
			Rectangle ssBox = this.boundingBox;
			System.out.println("Box Left Edge" + alienBox.getX());
			System.out.println("Center: " + center.x);

			// Check if spaceship is in the path of the alien
			if (alienBox.intersects(ssBox)) {
				updateHealth(); // Call updateHealth to decrement life
				System.out.println("UPDATE HEALTH CALLED");
			}

		}
	}

	/**
	 * Determine if a collision has occurred between a spaceship and an poodle
	 * bullet
	 */

	public void pBulletcollision(PoodleBullet[] poodleBullets) {
		for (int i = 0; i < poodleBullets.length; i++) { // Iterate over list of
															// poodle bullets

			PoodleBullet pb = poodleBullets[i]; // Get current poodlebullet from
												// Array

			Rectangle pbBox = pb.getBoundingBox(); // returns a rectangle
			Rectangle ssBox = this.boundingBox;
			System.out.println("Box Left Edge" + pbBox.getX());
			System.out.println("Center: " + center.x);

			// Check if spaceship is in the path of the alien
			if (pbBox.intersects(ssBox)) {
				updateHealth(); // Call updateHealth to decrement life
				System.out.println("UPDATE HEALTH CALLED");
			}

		}
	}

	/**
	 * A method to update the health meter;
	 */
	public void updateHealth() {

		// TODO Ensure health remains positive

		this.window.remove(healthMeter); // remove old health meter from window
		this.window.remove(healthAmount); // remove old health amount from window

		health--; // Decrement amount of health
		double healthDecimal = health / initHealth;

		int relWidth = (int) (148 * healthDecimal);
		if (health < initHealth / 4.0) { // draw a red health meter at 25% or
											// less
			healthMeter = new Rectangle(window.getWindowWidth() - 189,
					window.getWindowHeight() - 40, relWidth, 19, Color.RED,
					true);
		} else {
			healthMeter = new Rectangle(window.getWindowWidth() - 189,
					window.getWindowHeight() - 40, relWidth, 19, Color.GREEN,
					true);
		}
		String healthStr = Integer.toString((int) (healthDecimal * 100)); // Convert
																			// to
																			// string
																			// for
																			// TextShape
		this.healthAmount = new TextShape(healthStr,
				window.getWindowWidth() - 55, window.getWindowHeight() - 59);

		this.window.add(healthAmount);
		this.window.add(healthMeter);

	}

	/**
	 * Draw this SpaceShip in the graphics window
	 */
	protected void draw() {
		this.shapes = new Shape[6]; // Create array for 5 objects of Class:
									// Shape

		// Body of the space ship
		Rectangle body = new Rectangle(this.center.x - SpaceShip.WIDTH / 2,
				this.center.y - SpaceShip.HEIGHT / 2, SpaceShip.WIDTH,
				SpaceShip.HEIGHT, Color.cyan, true);

		this.shapes[0] = body; // Add Shape to array

		// Cone on top
		int x1 = body.getX();
		int y1 = body.getY();
		int x2 = x1 + body.getWidth();
		int y2 = y1;
		int x3 = body.getCenterX();
		int y3 = y1 - body.getWidth();
		this.shapes[1] = new Triangle(x1, y1, x2, y2, x3, y3, Color.pink, true);
		// Show the laser beam if the rocket is shooting
		if (this.isShooting) {
			this.shapes[4] = new Line(x3, y3, x3, 0, Color.yellow);
			this.isShooting = false;
		}

		// Wings on the sides
		x1 = body.getX();
		y1 = body.getY() + body.getHeight();
		x2 = body.getX() - body.getWidth() / 2;
		y2 = y1;
		x3 = x1;
		y3 = y1 - body.getWidth() / 2;
		this.shapes[2] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);
		x1 = body.getX() + body.getWidth();
		x2 = x1 + body.getWidth() / 2;
		x3 = x1;
		this.shapes[3] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);

		// The bounding box of this SpaceShip
		this.boundingBox = new Rectangle(body.getX(), body.getY()
				- body.getWidth(), body.getWidth(), body.getHeight()
				+ body.getWidth());

		// Put everything in the window
		for (int i = 0; i < this.shapes.length; i++)
			if (this.shapes[i] != null)
				this.window.add(this.shapes[i]);

		this.window.doRepaint();
	}
}
