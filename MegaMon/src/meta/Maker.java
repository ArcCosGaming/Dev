package meta;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.lwjgl.BufferUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * do not use !
 */
public class Maker {
	JFrame frame;
	JPanel content;
	JTextField image;
	JButton button;
	
	public Maker() {
		this.frame = new JFrame("Image to Buffer -Maker");
		this.content = new JPanel();
		this.content.setLayout(null);
		this.content.setPreferredSize(new java.awt.Dimension(300, 65));
		this.image = new JTextField();
		this.image.setBounds(15, 15, 175, 35);
		this.button = new JButton("Go!");
		this.button.addActionListener(new myActionListener());
		this.button.setBounds(190, 15, 95, 35);
		this.content.add(this.image);
		this.content.add(this.button);
		this.frame.setContentPane(this.content);
		this.frame.pack();
		this.frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Maker();
	}
	
	private static void imageToBytes(String pathname) {
		String[] strings = pathname.split("\\.");
		String byteBufferPath = strings[0] + ".buf";
		ByteBuffer buffer=imageToByteBuffer(pathname);
		buffer.rewind();
		byte[] array=new byte[buffer.remaining()];
		buffer.get(array);
		try{
			FileOutputStream file = new FileOutputStream(byteBufferPath);
			file.write(array);
			file.close();
		}
		catch ( IOException e ) { System.err.println( e ); }
	}
	
	private static ByteBuffer imageToByteBuffer(String pathname){
		try{
			File file = new File(pathname);
			BufferedImage image = ImageIO.read(file);
			long size = file.length();
			int width = image.getWidth();
			int height = image.getHeight();
			int BPP = (int) size/(width*height);
			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * BPP +3);
			
			buffer.put((byte) width);
	        buffer.put((byte) height);
	        
	        if (BPP == 4) buffer.put((byte) 1);
	        else buffer.put((byte) 0);
			
	        for(int y = 0; y < height; y++){
	            for(int x = 0; x < height; x++){
	                int pixel = pixels[y * width + x];
	                buffer.put((byte) ((pixel >> 16) & 0xFF));
	                buffer.put((byte) ((pixel >> 8) & 0xFF));
	                buffer.put((byte) (pixel & 0xFF));
	                if (BPP == 4) buffer.put((byte) ((pixel >> 24) & 0xFF));
	            }
	        }

	        return buffer;
		}
		catch(IOException e){
			System.out.println(pathname);
			throw new RuntimeException(e);
		}
	}
	
	class myActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String imagePath = image.getText();
			imageToBytes(imagePath);
		}
	}
}
