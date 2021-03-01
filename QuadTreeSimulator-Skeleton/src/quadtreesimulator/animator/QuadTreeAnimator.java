/**
 * 
 */
package quadtreesimulator.animator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import quadtreesimulator.scene.ColorDetectionScene;
import utility.QuadTree;
import javafx.event.EventHandler;

/**
 * @author matineh
 *
 */
public class QuadTreeAnimator extends AbstractAnimator {

	private int[] buffer;
	private double x;
	private double y;
	private boolean initilized;
	private Canvas drawingCanvas;
	private QuadTree qt;
	
	public QuadTree getQuadTree() {
		return qt;
	}
	
	public void setDrawingCanvas(Canvas drawingCanvas) {
		
		this.drawingCanvas=drawingCanvas;
	}
	
	public Canvas getDrawingCanvas() {
		
		return drawingCanvas;
	}

	public void init()   {
		if (initilized)
			return;//exit 
		initilized = true;
        
		qt=((ColorDetectionScene)scene).getQuadTree();
		@SuppressWarnings("unchecked")
		ObjectProperty<Color> color = (ObjectProperty<Color>) (scene.getOption("color"));

		color.addListener((v, o, n) -> qt.clear());

		drawingCanvas = new Canvas(scene.w(), scene.h());

		Canvas canvas = scene.getCanvas();

		canvas.setOnMouseDragged(e -> {
			x = e.getX();
			y = e.getY();
		});

		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub

				GraphicsContext gc = drawingCanvas.getGraphicsContext2D();

				if (event.isPrimaryButtonDown()) {

					gc.setStroke(color.get());

				}

				gc.setLineWidth(2);

				if (isRunning()) {

					gc.strokeLine(x, y, event.getX(), event.getY());

				}
				x = event.getX();
				y = event.getY();

			}
		};
		canvas.setOnMouseDragged(eventHandler);

	}

	public void clear() {

		qt.clear();
		clearAndFill(drawingCanvas.getGraphicsContext2D(), Color.TRANSPARENT);

	}

	@Override
	protected void handle(GraphicsContext gc, long now) {
		init();
		
		clearAndFill(gc, Color.TRANSPARENT);
		
		SnapshotParameters sp=new SnapshotParameters();
		
		sp.setFill(Color.TRANSPARENT);
		
		WritableImage image=getDrawingCanvas().snapshot(sp, null);
		
		if(image != null) {
			gc.drawImage(image, 0, 0);
			
		}
		
		
		 BooleanProperty displayQuadTree=(BooleanProperty)(scene.getOption("displayQuadTree"));
		 
		 if(displayQuadTree.get()==true) {
		 
		    buffer=new int[ (int) ( scene.w() * scene.h()) + 1];
		    
            image.getPixelReader().getPixels( 0, 0, (int) scene.w(), (int) scene.h(),PixelFormat.getIntArgbInstance(), 
					 buffer, 0, (int) scene.w());
	
			 
			 
			 ObjectProperty<Color> color =(ObjectProperty< Color>)(scene.getOption("color"));
			 
			 qt.push(buffer, (int)(scene.w()), color.get());
			 
			 qt.getDrawable().draw(gc);
			 
			 
		 }
		
	    // TODO Auto-generated method stub

	}

}
