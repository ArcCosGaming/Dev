package graphics;

import javax.swing.JFrame;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class BugFrame extends JFrame
{
	public BugFrame()
	{
		super("BugFrame");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static void showBug(Exception e)
	{
		String output = e.getClass().getName() + " : " + e.getMessage() + "\n";
		StackTraceElement[] stack = e.getStackTrace();
		for (int i = 0; i < stack.length; i++)
			output += "    at " + stack[i].getClassName() + " in line : " + stack[i].getLineNumber() + " at method :" + stack[i].getMethodName() + "\n";
		BugFrame frame = new BugFrame();
		frame.setSize(800, 800);
		JTextArea area = new JTextArea();
		area.setText(output);
		frame.setContentPane(area);
		frame.setVisible(true);
	}
}
