import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class MandelPanel extends JPanel implements MouseListener, MouseMotionListener {

	double dX, dY;
	int x1, x2, y1, y2;
	int height, width;
	// top left point of the X_Axis on the complex Number
	double start_x = -2;
	// top left point of the Y_Axis on the complex Number
	double start_y = -2;
	// bottom right point of the x_Axis on the complex Number
	double end_x = 2;
	//bottom right of the Y_Axis on the complex Number
	double end_y = 2;
	// real and imaginary numbers for drawing julia set and converting complex number
	double cX, cY;
	double MAX_ITER;
	public double shift_X, shift_Y = 0;
	private boolean mouseDraged = false; // dragging state
	boolean zoomedIn = false; // checking zomming status
	BufferedImage juliaImage, mandelImage;
	// 
	private Rectangle zoomingRectangle = null;
	
	int zoomRectX,zoomRectY,zoomRectWidth,zoomRectHeight;
	String checkName;
	double zoomScale = 1;
	GuiDisplay myGUI;

	public MandelPanel(String checkName,double MAX_ITER ) {
		super();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		this.checkName = checkName;
		this.MAX_ITER = MAX_ITER ;
		
//		generatingMandel(checkName);
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
//		g.drawImage(mandelImage, 0, 0, null);
		if (!mouseDraged)
			generatingMandel(checkName);
		
		g.drawImage(mandelImage, 0, 0, null);
		
		if (zoomingRectangle == null) {
			return;
		}
		
		if (mouseDraged) {
			// draw a white line when the mouse is dragged then draw the line
			g.setXORMode(Color.white);
			g2.draw(zoomingRectangle);

		}
	}

	public boolean generatingMandel(String checkName) {
		Dimension size = this.getSize();
		// get this panel width and height
		height = size.width;
		width = size.height;		
		dX = (end_x - start_x) / width;
		dY = (end_y - start_y) / height;

		// generate new buffered image based with the size of this panel
		mandelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// loop through each pixel to convert it to complex number and 
		// then check whether it;s escaping the the fractal set or not
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int iter_counter = 0;
				double cx = (start_x + x * dX) + shift_X;
				double cy = (start_y + y * dY) + shift_Y;
				ComplexNumber c = new ComplexNumber(cx, cy);
				ComplexNumber z = new ComplexNumber(0.0, 0.0);
				while (z.modulusSquared() < 4 && iter_counter < MAX_ITER) {
					 if (checkName.equals("Burning ship"))
					 z = z.complexSquared();
					 else if (checkName.equals("Mandelbrot set"))
					z = z.square();
					z = z.add(c);
					iter_counter++;
				}
				// color each pixel according to the number of iterations
				if(iter_counter == MAX_ITER){
					mandelImage.setRGB(x,y,Color.BLACK.getRGB());
				}else{
					float hue =  iter_counter / (float) MAX_ITER;
					float saturation = 1.0f;
					float brightness = 1.0f;
					Color color = Color.getHSBColor(hue, saturation, brightness);
					mandelImage.setRGB(x, y, color.getRGB());
				}
			}
		}
		return false;
	}

	public void mouseClicked(MouseEvent e) {

		pointConvertor(e.getX(), e.getY());
	}

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		// mouseDraged = true;
	}

	public void mouseDragged(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		mouseDraged = true;
		zoomRectX = Math.min(x1, e.getPoint().x);
		zoomRectY = Math.min(y1, e.getPoint().y);
		zoomRectWidth = Math.abs(x1 - e.getPoint().x);
		zoomRectHeight = Math.abs(y1 - e.getPoint().y);
		zoomingRectangle = new Rectangle(zoomRectX, zoomRectY, zoomRectWidth, zoomRectHeight);
		repaint();

	}

	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		mouseDraged = false;
		// make sure the zooming is not done only when the mouse is clicked
		if (x1 != x2 && y1 != y2) {
			// make sure the x1 value is always the smallest value
			if (x2 < x1) {
				int temprory = x2;
				x2 = x1;
				x1 = temprory;
			}
			// make sure the y1 value is always the smallest value
			if (y2 < y1) {
				int temprory = y2;
				y2 = y1;
				y1 = temprory;
			}
			
			// reassigning the starting and ending points on the complex plain
			// according to the zooming part
			end_x = start_x + (dX * x2);
			end_y = start_y + (dY * y2);
			start_x = start_x + (dX * zoomRectX);
			start_y = start_y + (dY * zoomRectY);
			dX = (end_x - start_x) / width;
			dY = (end_y - start_y) / height;
			calculateZoom();
			repaint();
		}
	}
	
	// not used
	public void mouseEntered(MouseEvent e) {}
	// not used
	public void mouseExited(MouseEvent e) {}
	//not used
	public void mouseMoved(MouseEvent e) {}
	
	public void calculateZoom(){
		
		double totalArea = width * height;
		double squareArea = zoomRectWidth * zoomRectHeight;
		
		zoomScale = ( totalArea/ squareArea * zoomScale);
		myGUI.readField4.setText((int)zoomScale+"x");
	}

	public void pointConvertor(int x_point, int y_point) {

		cX = start_x + (double) x_point / width * (end_x - start_x);
		cY = start_y + (double) y_point / height * (end_y - start_y);

	}
	
	public void setGUI(GuiDisplay myGUI){
		this.myGUI = myGUI ;
		// attach mouse listener
		addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {				
				myGUI.readField1.setText("" + (float) cX + "," + (float) cY + "i");
				myGUI.readField2.setText("" + e.getX());
				myGUI.readField3.setText("" + e.getY());
				ComplexNumber c = new ComplexNumber(cX, cY);
				myGUI.saveBookmark(c);
				

			}
			
			
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			// show mouse coordinates in text fields
			public void mouseMoved(MouseEvent e) {
				myGUI.readField2.setText("" + e.getX());
				myGUI.readField3.setText("" + e.getY());
				
			}
			
			// not used
			public void mouseDragged(MouseEvent e) {
				
			}
		});
	}

}
