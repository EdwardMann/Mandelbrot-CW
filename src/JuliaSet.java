import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;


import javax.swing.JPanel;

class JuliaPanel extends JPanel {
	// top left point of the X_Axis on the complex Number
	double xstart = -2;
	// top left point of the Y_Axis on the complex Number
	double ystart = -2;
	// bottom right point of the x_Axis on the complex Number
	double xend = 2;
	//bottom right of the Y_Axis on the complex Number
	double yend = 2;
	// thes two values below to make sure the julia set
	// is fitting in the middle of the panel 
	double x_Shift = -0.58;
	double y_shift = 0.74;
	int width, height, half_width, half_height;
	int iter_counter;
	BufferedImage juliaImage;
	// real and imaginary numbers for drawing julia set and converting complex number
	double cX, cY;
	MandelPanel mandel1;
	double MAX_ITER;

	public void setMandel(MandelPanel mandel) {
		this.mandel1 = mandel;
		// attach mouse Listener to mandelPanel class to get complexNumber
		//values in order to draw the julia image
		
		this.mandel1.addMouseMotionListener(new MouseMotionListener() {			
			public void mouseMoved(MouseEvent e) {
				mandel1.pointConvertor(e.getX(), e.getY());
				cX = mandel1.cX;
				cY = mandel1.cY;
				repaint();
			}
			
			// not used
			public void mouseDragged(MouseEvent e) {}
			
		});
	}

	public JuliaPanel(double MAX_ITER) {

		this.MAX_ITER = MAX_ITER;

	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		generatingJulia();
		g.drawImage(juliaImage, 0, 0, this);
	}

	private void generatingJulia() {
		int width = this.getWidth();
		int height = this.getHeight();
		xstart = (xstart - ystart) / width;
		ystart = (yend - ystart) / height;
		half_width = width / 2;
		half_height = height / 2;
		// the gap between each pixel
		double dx2 = 4.0 / width;
		double dy2 = 4.0 / height;
		// generate new buffered image based with the size of this panel
		juliaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// loop through each pixel to convert it to complex number and 
		// then check whether it;s escaping the the fractal set or not
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int iter_counter = 0;
				ComplexNumber c = new ComplexNumber(cX, cY);
				double cx = ((x - half_height) * dx2) + (x_Shift);
				double cy = ((y - half_width) * dy2) + (y_shift);
				ComplexNumber z = new ComplexNumber(cx, cy);
				while (z.modulusSquared() < 4 && iter_counter < MAX_ITER) {
					z = z.square();
					z = z.add(c);
					iter_counter++;
				}
					// color each pixel according to the number of iterations
					juliaImage.setRGB(x, y, iter_counter | (iter_counter <<10));
				}
			} // end of loops
		}
	}

