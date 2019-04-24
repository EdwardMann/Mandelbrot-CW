import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ButtonHandler implements ActionListener{
	
	JuliaPanel julia;

	public ButtonHandler(){
//		juliaImage2 = juliaImage;
	}
	
	public void setJulia(JuliaPanel julia){
		this.julia = julia;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		try {
			File savefile = new File("Julia Set" + time + ".png");
			ImageIO.write(julia.juliaImage, "png", savefile);

		} catch (Exception e1) {
			System.err.println(e1);
		}

	}

}
