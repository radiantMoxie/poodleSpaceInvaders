import uwcse.graphics.*;
import java.awt.Point;

/**
 * A description of any moving object in the space invader game
 */
public abstract class MovingObject {
	// Possible directions of motion
	/** Left motion */
	public static final int LEFT = 1;

	/** Right motion */
	public static final int RIGHT = 2;

	/** Up motion */
	public static final int UP = 3;

	/** Down motion */
	public static final int DOWN = 4;

	/** The graphics window this MovingObject belongs to */
	protected GWindow window;

	/** The center of this Moving Object */
	protected Point center;

	/** The graphics elements making up the drawing of this MovingObject */
	protected Shape[] shapes;

	/** The bounding box of this MovingObject */
	protected Rectangle boundingBox;

	// the bounding box is useful to test for collisions

	/** Current direction of motion of this MovingObject */
	protected int direction;

	/**
	 * Create this MovingObject
	 * 
	 * @param window
	 *            the GWindow this MovingObject belongs to
	 * @param center
	 *            the center point of this MovingObject
	 */
	public MovingObject(GWindow window, Point center) {
		this.window = window;
		this.center = center;
	}

	/**
	 * Return the location of this MovingObject
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Get the bounding box of this MovingObject
	 */
	public Rectangle getBoundingBox() {
		return this.boundingBox;
	}

	/**
	 * Set the direction of motion of this MovingObject
	 * 
	 * @param direction
	 *            the new direction of motion (must be valid)
	 */
	public void setDirection(int direction) {
		if (direction != MovingObject.DOWN && direction != MovingObject.UP
				&& direction != MovingObject.RIGHT
				&& direction != MovingObject.LEFT) {
			// Crash the program if direction is invalid
			// (exceptions are covered in 143)
			// But there is no need to change the code here
			throw new IllegalArgumentException(
					"Bad news: Invalid direction value");
		}

		this.direction = direction;
	}
	
	/**
	 * Move this MovingObject in the graphics window
	 */
	public abstract void move();

	/**
	 * Draw this MovingObject in the graphics window
	 */
	protected abstract void draw();

	/**
	 * Erase this MovingObject from the graphics window
	 */
	protected void erase() {
		
//		System.out.println("ERASING");
		
		if (this.shapes != null) { 
			for (int i = 0; i < this.shapes.length; i++) { // Iterate over the shapes that make up an individual alien
				if (this.shapes[i] != null)
					this.window.remove(this.shapes[i]); // Remove shape from window
			}
			this.shapes = null;
		}
	}
}