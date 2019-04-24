import java.awt.Color;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

public class GuiDisplay extends JFrame  {

	public static void main(String[] args) {
		GuiDisplay window1 = new GuiDisplay("test");
		window1.init();

	}


	BufferedImage juliaImage, mandelImage;
	private double MAX_ITER;
	public double shift_X, shift_Y = 0;
	JTextField readField1, readField2, readField3, readField4;
	// real and imaginary numbers for drawing julia set and converting complex number
	double cX, cY;
	MandelPanel mandel;
	// make mandel panel to draw mandelbrot set as its default picture
	String checkName = "Mandelbrot set";
	JComboBox<String> juliaPictures = new JComboBox<String>();
	JuliaPanel juliaPanel;
	HashMap<String, ComplexNumber> bookmarks = new HashMap<String, ComplexNumber>();


	
	public GuiDisplay(String title) {
		super(title);
		MAX_ITER = 335;
	}// end of constructor

	public void init(){

		this.setSize(1200, 780);
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		mandel = new MandelPanel(checkName,MAX_ITER);
		mandel.setGUI(this);
		
		JPanel topRight = new JPanel();
		topRight.setBorder(drawborder("Managing Graph"));
		JPanel middletop = new JPanel();
		middletop.setBorder(drawborder("Mouse Co-ordinates"));

		JPanel middleBottom = new JPanel();
		middleBottom.setBorder(drawborder("Selecting Formula"));

		juliaPanel = new JuliaPanel( MAX_ITER);
		juliaPanel.setBorder(drawborder("Julia set"));
		juliaPanel.setMandel(mandel);
		

		// setting panels layout to GridbagLayout
		topRight.setLayout(layout);
		middletop.setLayout(layout);
		middleBottom.setLayout(layout);
		juliaPanel.setLayout(layout);

		/*------------------------------------------------
		 * defining labels and adding them 
		 * to the panel using GridbagLayout
		 * ------------------------------------------------*/

		addLabels(topRight, "Max Iter : ", new GBC(0, 0));
		addLabels(topRight, "X-Shift : ", new GBC(0, 1));
		addLabels(topRight, "Y-Shift : ", new GBC(0, 2));

		/*
		 * adding labels to middle panel (Mouse coordinates) calling addLabel
		 * function
		 */
		addLabels(middletop, "Clicked Point : ", new GBC(0, 0));
		addLabels(middletop, "X Coordinate : ", new GBC(0, 1));
		addLabels(middletop, "Y Coordinate : ", new GBC(0, 2));
		addLabels(middletop, "Zoom Scale : ", new GBC(0, 3));
		// addLabels(middleRight,"",new GBC(0,4));

		/*------------------------------------------------
		 * defining initializing spinners and attaching 
		 * them to even handler
		 * ------------------------------------------------*/

		SpinnerModel model = new SpinnerNumberModel(MAX_ITER, 0, 1500, 5);
		JSpinner spinner1 = new JSpinner(model);
		formattingspinner(spinner1);
		spinner1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mandel.MAX_ITER = (Double) spinner1.getValue();
				mandel.repaint();

			}
		});
		SpinnerModel model1 = new SpinnerNumberModel(0, -10, 10, 0.01);
		JSpinner spinner2 = new JSpinner(model1);
		formattingspinner(spinner2);
		spinner2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				juliaPanel.x_Shift = mandel.shift_X = (Double) spinner2.getValue();
				mandel.repaint();

			}
		});
		SpinnerModel model2 = new SpinnerNumberModel(0, -10, 10, 0.01);
		JSpinner spinner3 = new JSpinner(model2);
		formattingspinner(spinner3);
		spinner3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				juliaPanel.y_shift = mandel.shift_Y = (Double) spinner3.getValue();
				mandel.repaint();
			}
		});

		/*------------------------------------------------
		 * construct the text fields components
		 *  and adding them the top right panel by using GridbagLayout
		 * ------------------------------------------------*/
		topRight.add(spinner1, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(1));
		topRight.add(spinner2, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0, 1, 0, 1));
		topRight.add(spinner3, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(1));

		/*------------------------------------------------
		 * construct the text fields components
		 *  and adding them the middle right panel by using GridbagLayout
		 * ------------------------------------------------*/
		readField1 = new JTextField(3);
		readField2 = new JTextField(3);
		readField3 = new JTextField(3);
		readField4 = new JTextField("100",3);
		readField1.setEditable(false);
		readField2.setEditable(false);
		readField3.setEditable(false);
		readField4.setEditable(false);
		middletop.add(readField1, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));
		middletop.add(readField2, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));
		middletop.add(readField3, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));
		middletop.add(readField4, new GBC(1, 3).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0, 0, 10, 0));

		/*------------------------------------------------
		 * construct button components
		 *  and adding them the top middle panel by using GridbagLayout
		 * ------------------------------------------------*/
		JButton savePicture = new JButton("Save your favorite Picture");
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonHandler.setJulia(juliaPanel);
		 savePicture.addActionListener(buttonHandler);
		 


		JButton restPicture = new JButton("Reset the Picture Size");
		restPicture.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDefault();

			}
		});
		

		topRight.add(savePicture,
				new GBC(0, 4, 2, 1).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));
		topRight.add(restPicture,
				new GBC(0, 5, 2, 1).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));
		topRight.add(juliaPictures,
				new GBC(0, 6, 2, 1).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0));

		/*------------------------------------------------
		 * construct the Check box components
		 *  and adding them the middle bottom panel by using GridbagLayout
		 * ------------------------------------------------*/
		
		JRadioButton radioButton1 = new JRadioButton("Mandelbrot set", true);
		JRadioButton radioButton2 = new JRadioButton("Burning ship", false);


		

		ButtonGroup button = new ButtonGroup();
		button.add(radioButton1);
		button.add(radioButton2);

		radioButton1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (radioButton1.isSelected()) {
					mandel.checkName = "Mandelbrot set";
					setDefault();
					mandel.repaint();
				}

			}
		});
		radioButton2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if (radioButton2.isSelected()) {
					mandel.checkName = "Burning ship";
					setDefault();
					mandel.repaint();
				}
			}
		});

		// adding Radio buttons to the panel
//		middleBottom.add(selectFormula, new GBC(0, 0, 1, 1).setWeight(100, 0).setInsets(1));
		middleBottom.add(radioButton1, new GBC(0, 0, 1, 1).setWeight(100, 0).setInsets(1));
		middleBottom.add(radioButton2, new GBC(1, 0, 1, 1).setWeight(100, 0).setInsets(1));

		// adding the panels to the main frame using GridbagLayout
		add(mandel, new GBC(0, 0, 1, 4).setFill(GBC.BOTH).setAnchor(GBC.NORTHWEST).setWeight(0.8, 1.0).setInsets(10, 10,
				10, 0));
		add(topRight, new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(0.20, 0.05).setInsets(10, 0, 0, 10));
		add(middletop, new GBC(1, 1, 1, 1).setFill(GBC.BOTH).setWeight(0.20, 0.10).setInsets(0, 0, 0, 10));
		add(middleBottom, new GBC(1, 2, 1, 1).setFill(GBC.BOTH).setWeight(0.20, 0.10).setInsets(0, 0, 0, 10));
		add(juliaPanel, new GBC(1, 3, 1, 1).setFill(GBC.BOTH).setAnchor(GBC.FIRST_LINE_START).setWeight(0.20, 0.75).setInsets(0, 0, 10, 10));
		
		// this is called run julia Combo box and make it updated all the time 
		updatebookmarkList();
		
		this.setResizable(true);
		this.setLocation(150, 150);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// update bookmark hashmap
	private void updatebookmarkList() {
		// remove all previous values to not have repetitive values in JComboBox
		juliaPictures.removeAllItems();
		
		for(String bookmarkName : bookmarks.keySet())
		{
			juliaPictures.addItem(bookmarkName);
		}
		
		// attach the item Listener
		juliaPictures.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()!= ItemEvent.SELECTED)
				{
					return;
				}
				String name  = e.getItem().toString();
				
				// if the favorite name is selected then call its
				//corresponding value from hashmap and then repaint the Julia set 
				if (bookmarks.containsKey(name)){
					ComplexNumber c = bookmarks.get(name);
					juliaPanel.cX = c.getReal();
					juliaPanel.cY = c.getImaginary();
					juliaPanel.repaint();
				}
				
			}
		});
		
	}

	// setting Border around panel to a customized view
	public Border drawborder(String titleName) {
		TitledBorder title;
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		title = BorderFactory.createTitledBorder(loweredetched, titleName);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(new Font("Verdana", Font.ITALIC, 12));
		return title;
	}

	// constructing label component to its default values
	public void addLabels(JPanel myPanel, String title, GBC gbc) {
		JLabel label1 = new JLabel(title);
		myPanel.add(label1, gbc.setAnchor(GBC.EAST));
	}

	// make each spinner's value accessible by setting formatting it's text field
	public void formattingspinner(JSpinner spinner) {
		JComponent comp = spinner.getEditor();
		JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
		DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
		formatter.setCommitsOnValidEdit(true);
	}
	
	// this functions is called to set all values in the image generating to default
	public void setDefault(){
		mandel.start_x = -2;
		mandel.start_y = -2;
		mandel.end_x = 2;
		mandel.end_y = 2;
		mandel.zoomScale = 1;
		readField4.setText("100");
		mandel.repaint();
	}

	// this function adds string name and its corresponding value to the hashmap
	public void saveBookmark(ComplexNumber c) {
		int length = bookmarks.size()+1;
		String bookmarkName = "favourite "+length;
		
		//add the new generated name to the hashmap
		bookmarks.put(bookmarkName,c);
		updatebookmarkList();
		
	}

}
