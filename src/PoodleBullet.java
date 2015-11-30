import java.awt.Point;
import uwcse.graphics.GWindow;

/**
 * The display and movement of Poodle Bullets
 */

public class PoodleBullet extends PoodleBoss {
	int y = (int) (this.center.y + (this.boundingBox.getWidth() / 2));

	/**
	 * The display of the Mini poodle bullets
	 */
	public PoodleBullet(GWindow window, Point center) {
		super(window, center);
		this.poodleRadius = 5;
	}
 /**
  * Method to move PooodleBullets down screen
  */
	public void move() {
		this.center.y += 30;		
		erase();
		draw();
		
		// If bullet passes bottom of window, erase it
		if (center.y > window.getWindowHeight()) {
			erase();
		}
	}
}
