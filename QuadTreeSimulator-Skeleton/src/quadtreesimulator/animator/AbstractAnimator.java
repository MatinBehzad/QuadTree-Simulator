/**
 * 
 */
package quadtreesimulator.animator;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import quadtreesimulator.entity.FpsCounter;
import quadtreesimulator.entity.property.Sprite;
import javafx.scene.paint.Paint;
import quadtreesimulator.scene.AbstractScene;

/**
 * @author matineh
 *
 */
public abstract class AbstractAnimator extends AnimationTimer {

	private boolean isRunning;
	protected AbstractScene scene;
	private FpsCounter fps;
	
	
	public void setScene(AbstractScene scene) {
		this.scene=scene;
	}

	private void setIsRunning(boolean isRunning) {
		
		this.isRunning=isRunning;

	}

	public void setAbstractScene(AbstractScene scene) {
         this.scene=scene;
	}

	public AbstractAnimator() {

		fps = new FpsCounter(10, 25);
		fps.getDrawable().setFill(Color.BLACK);
		fps.getDrawable().setWidth(1);
		fps.getDrawable().setStroke(Color.WHITE);
	}

	public void stop() {
		super.stop();
		setIsRunning(false);
	}

	public void start() {
		super.start();
		setIsRunning(true);

	}

	public boolean isRunning() {
		return isRunning;

	}

	public void clear() {
		clearAndFill(scene.gc(), Color.TRANSPARENT);
	}

	protected void clearAndFill(GraphicsContext gc, Color background) {
		clearAndFill(gc, background, 0, 0, scene.w(), scene.h());
	}

	protected void clearAndFill(GraphicsContext gc, Color background, double x, double y, double w, double h) {

		gc.setFill(background);
		gc.clearRect(x, y, w, h);
		gc.fillRect(x, y, w, h);

	}

	public void handle(long now) {
		
		scene.gc();
		
		 BooleanProperty  drawFPS=(BooleanProperty)scene.getOption("displayFPS");
		 
		 if(drawFPS.get()) {
			fps.calculateFPS(now); 
		 }
		
		scene.gc().save();
		
		this.handle(scene.gc(),now);
	    
	    scene.gc().restore();
	    
	    if (drawFPS.get()) {
	    	 Sprite s=fps.getDrawable();
	    	 
	    	 s.draw(scene.gc());
	    	
	    }
		 
	}

	protected abstract void handle(GraphicsContext gc, long now);

}
