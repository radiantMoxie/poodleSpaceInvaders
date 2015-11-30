import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

/**
 * The representation and display of an Alien
 */

public class Alien extends MovingObject {
	// Size of an Alien
	public static int radius = 5;

	// Number of lives in this Alien
	// When 0, this Alien is dead
	protected int lives; // TODO Changed from private to protected to test
							// PoodleBoss, change back?

	private static Color[] colors = { Color.RED, Color.GREEN, Color.BLUE };

	/**
	 * Create an alien in the graphics window
	 * 
	 * @param window
	 *            the GWindow this Alien belongs to
	 * @param center
	 *            the center Point of this Alien
	 */
	public Alien(GWindow window, Point center) {
		super(window, center);
		this.lives = (int) (Math.random() * colors.length + 1); // Give each
																// alien a
																// random number
																// of lives to
																// start

		// Display this Alien
		// In the case of PoodleBoss, draw() in PoodleBoss is called?
		this.draw();
	}

	/**
	 * Create an alien in the graphics window
	 * 
	 * @param lives
	 *            the number of lives of the Alien
	 * 
	 * @param window
	 *            the GWindow this Alien belongs to
	 * @param center
	 *            the center Point of this Alien
	 */
	public Alien(GWindow window, Point center, int lives) {
		super(window, center);
		this.lives = lives;

		// Display this Alien
		// In the case of PoodleBoss, draw() in PoodleBoss is called?
		this.draw();
	}

	/**
	 * The alien is being shot Decrement its number of lives and erase it from
	 * the graphics window if it is dead.
	 */
	public void isShot() {
		lives--;
		if (lives == 0) {
			erase();
		}
	}

	/**
	 * Is this Alien dead?
	 */
	public boolean isDead() {
		return this.lives == 0; // If lives == 0, return true
	}

	/**
	 * Return the location of this Alien
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Move this Alien as a start make all of the aliens move downward. If an
	 * alien reaches the bottom of the screen, it reappears at the top.
	 */
	public void move() {

		// Update Location

		this.center.y += 10;

		// If alien passes bottom of window, bring it to the top
		if (center.y > window.getWindowHeight()) {
			center.y = 0; // Initially (10 * Alien.RADIUS)
		}

		// to move the alien
		erase();
		draw();

	}

	
	/**
	 * Display this Alien in the graphics window
	 */
	protected void draw() {
		// pick the color (according to the number of lives left)
		Color color = colors[lives - 1]; // access color in Array based on # of
											// lives

		// Graphics elements for the display of this Alien
		// A circle on top of an X
		this.shapes = new Shape[3];
		this.shapes[0] = new Line(this.center.x - 2 * radius, this.center.y - 2
				* radius, this.center.x + 2 * radius, this.center.y + 2
				* radius, color);
		this.shapes[1] = new Line(this.center.x + 2 * radius, this.center.y - 2
				* radius, this.center.x - 2 * radius, this.center.y + 2
				* radius, color);
		this.shapes[2] = new Oval(this.center.x - radius, this.center.y
				- radius, 2 * radius, 2 * radius, color, true);

		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);

		// Bounding box of this Alien
		this.boundingBox = new Rectangle(this.center.x - 2 * radius,
				this.center.y - 2 * radius, 4 * radius, 4 * radius);

		this.window.doRepaint(); // TODO *** What does this do? Y: I think it's
									// a safety catch.
	}
}
