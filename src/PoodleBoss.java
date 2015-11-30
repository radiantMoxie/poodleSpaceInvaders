import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.Triangle;
import uwcse.io.Sound;

/**
 * The representation and display of a PoodleBoss Alien
 *
 */
public class PoodleBoss extends Alien {

	private static Sound bark = new Sound("DogWoof.wav");
	private static Sound beret = new Sound("Red_Beret.wav");
	private static Sound haircut = new Sound("New_Haircut.wav");
	protected int poodleRadius, moveCount;
	protected PoodleBullet[] poodleBullets = null;

	/**
	 * Constructor
	 */
	public PoodleBoss(GWindow window, Point center) {
		super(window, center, 10); // Call Alien class constructor
		this.poodleRadius = 15;
		this.draw();
	}

	/**
	 * Overrides draw in Alien
	 */

	protected void draw() {
		// Graphics elements for the display of this PoodleBoss
		this.shapes = new Shape[11];
		// face
		this.shapes[0] = new Oval(this.center.x - 2 * poodleRadius,
				this.center.y - 3 * poodleRadius, 4 * poodleRadius,
				6 * poodleRadius, Color.GRAY, true);
		// right ear
		this.shapes[1] = new Line(this.center.x + poodleRadius, this.center.y
				- 3 * poodleRadius, this.center.x + 3 * poodleRadius,
				this.center.y + 2 * poodleRadius, Color.WHITE);
		// left ear
		this.shapes[2] = new Line(this.center.x - poodleRadius, this.center.y
				- 3 * poodleRadius, this.center.x - 3 * poodleRadius,
				this.center.y + 2 * poodleRadius, Color.WHITE);
		// right ear ball
		this.shapes[3] = new Oval(this.center.x + (int) (2.5 * poodleRadius),
				this.center.y + 2 * poodleRadius, 2 * poodleRadius,
				2 * poodleRadius, Color.GRAY, true);
		// left ear ball
		this.shapes[4] = new Oval(this.center.x - 4 * poodleRadius,
				this.center.y + 2 * poodleRadius, 2 * poodleRadius,
				2 * poodleRadius, Color.GRAY, true);
		// poof atop head
		this.shapes[5] = new Oval(this.center.x - 3 * poodleRadius,
				this.center.y - 5 * poodleRadius, 6 * poodleRadius,
				4 * poodleRadius, Color.GRAY, true);
		// right eye
		this.shapes[6] = new Oval(this.center.x + (int) (.75 * poodleRadius),
				this.center.y - (int) (.25 * poodleRadius),
				(int) (.75 * poodleRadius), (int) (.75 * poodleRadius),
				Color.BLACK, true);
		// left eye
		this.shapes[7] = new Oval(this.center.x - poodleRadius, this.center.y
				- (int) (.25 * poodleRadius), (int) (.75 * poodleRadius),
				(int) (.75 * poodleRadius), Color.BLACK, true);
		// nose
		this.shapes[8] = new Triangle(
				this.center.x - (int) (.5 * poodleRadius), this.center.y
						+ poodleRadius, this.center.x
						+ (int) (.5 * poodleRadius), this.center.y
						+ poodleRadius, this.center.x, this.center.y + 2
						* poodleRadius, Color.BLACK, true);
		// red beret
		this.shapes[9] = new Oval(this.center.x - 4 * poodleRadius,
				this.center.y - 5 * poodleRadius, 8 * poodleRadius,
				2 * poodleRadius, Color.RED, true);
		this.shapes[10] = new Line(this.center.x, this.center.y - 4
				* poodleRadius, this.center.x,
				this.center.y - 6 * poodleRadius, Color.RED);

		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);

		// Bounding box of the PoodleBoss
		// Bounding box is used in SpaceShip shoot() method to determine if
		// laser hit the alien
		this.boundingBox = new Rectangle(this.center.x - 4 * poodleRadius,
				this.center.y - 5 * poodleRadius, (int) (8.5 * poodleRadius),
				9 * poodleRadius);

		this.window.doRepaint();

	}

	/**
	 * Have the PoodleBoss move to a random place
	 */
	public void move() {
		moveCount++; //
		// A PoodleBoss moves randomly across the screen
		// move this PoodleBoss
		if (poodleBullets != null) {
			for (int i = 0; i < poodleBullets.length; i++) { // iterates over poodlebullets
				poodleBullets[i].move();
			}
		}

		if (moveCount >= 15) {
			int width = this.getBoundingBox().getWidth() / 2;
			int height = this.getBoundingBox().getHeight() / 2;

			erase();
			Random rnd = new Random();
			this.center.y = height
					+ rnd.nextInt(window.getWindowHeight() - 2 * height); // Set
																			// new
																			// Y-coord
			this.center.x = width
					+ rnd.nextInt(window.getWindowWidth() - 2 * width - 200); // Set
																				// new
																				// X-coord
			moveCount = 0;
			draw();
			int y = (int) (this.center.y + (this.boundingBox.getWidth() / 2));

			this.poodleBullets = new PoodleBullet[3];
			this.poodleBullets[0] = new PoodleBullet(window, new Point(
					this.center.x - this.boundingBox.getWidth() / 2, y));
			this.poodleBullets[1] = new PoodleBullet(window, new Point(
					this.center.x, y));
			this.poodleBullets[2] = new PoodleBullet(window, new Point(
					this.center.x + this.boundingBox.getWidth() / 2, y));

		}

	}

	/**
	 * Overrides isShot in Alien The alien is being shot Decrement its number of
	 * lives and erase it from the graphics window if it is dead. The dog barks
	 * or talks depending on how many lives it has left
	 */
	public void isShot() {
		// different sounds based on how many lives dog has
		switch (this.lives) {
		case 1:
			haircut.play();
			break;
		case 3:
			beret.play();
			break;
		default:
			bark.play();
		}
		System.out.println("POODLE IS SHOT");

		super.isShot();
	}

}
