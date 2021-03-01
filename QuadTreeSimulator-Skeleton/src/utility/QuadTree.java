package utility;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import quadtreesimulator.entity.GenericEntity;
import quadtreesimulator.entity.property.Sprite;

//TODO Complete
public class QuadTree extends GenericEntity {

	private Node head;
	private int maxDepth;

	// TODO Complete

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getHead() {
		return this.head;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMaxDepth() {
		return this.maxDepth;
	}

	private QuadTree() {

		sprite = new Sprite() {
			@Override
			public void draw(GraphicsContext gc) {
				gc.setLineWidth(0.5);
				drawNode(head, gc);
			}

			private void drawNode(Node n, GraphicsContext gc) {
				
				if (n.children == null || n.empty || n.depth >= maxDepth) {
					return;
				}
				gc.strokeLine(n.x + n.width / 2, n.y, n.x + n.width / 2, n.y + n.height);
				
				gc.strokeLine(n.x, n.y + n.height / 2, n.x + n.width, n.y + n.height / 2);
				
				for (Node node : n.children) {
					drawNode(node, gc);
				}
			}
		};
	}

	public QuadTree(int depth, double width, double height) {

		this();
		head=new Node(0, 0, 0, width, height);
		maxDepth = depth;
	
	}

	public void push(int[] buffer, int colSize, Color c) {

		int color = (0xff << 24) | (((int) (c.getRed() * 255) & 0xff) << 16)
				| (((int) (c.getGreen() * 255) & 0xff) << 8) | ((int) (c.getBlue() * 255) & 0xff);

		this.head.push(buffer, colSize, color);

	}

	public void clear() {
		head.clear();
	}

	private class Node {
		private Node[] children;
		private int depth;
		private double x;
		private double y;
		private double width;
		private double height;
		private boolean empty;
		

		public Node(int depth, double x, double y, double width, double height) {
	
			this.empty = true;
			this.depth = depth;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public void push(int[] buffer, int rowSize, int color) {

			for (int i = (int) y * rowSize; i < y * rowSize + height * rowSize; i += rowSize) {
				
				for (int j = (int) x; j < x + width; j += 1) {

					if (i + j >= buffer.length) {
						break;
					}
					if (buffer[i + j] == color) {
						empty = false;
						break;
					}
				
					

					}
				
				
				}
			
			if ((empty == false) && (depth < maxDepth)) {
				
				   

		             divide();
				
				
				for (Node n : children) {
					
					
					n.push(buffer, rowSize, color);

				}
				
				
				
			}
			
			
			
		
		}

		public void clear() {
			empty = true;
			
			if (children != null) {
				for (Node node : children) {
					node.clear();
				}
			}  

		}

		private void divide() {
			
		           if(children==null) {
		        	  children=new Node[4];
					children[0] = new Node(depth + 1, x , y , width / 2, height / 2);
					children[1] = new Node(depth + 1, x + width / 2, y , width / 2, height / 2);
					children[2] = new Node(depth + 1, x + width / 2, y + height / 2, width / 2, height / 2);
					children[3] = new Node(depth + 1, x , y + height / 2, width / 2, height / 2);
		           }
			
		
	}
}
}