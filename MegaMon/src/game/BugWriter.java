package game;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BugWriter {
	
	public static void writeBug(Exception e)
	{
		try {
		File file = mKdir();
		FileWriter writer = new FileWriter(file);
		writer.append(new SimpleDateFormat().format(Calendar.getInstance().getTime()) + System.getProperty("line.separator"));
		writer.append(e.getClass().getName() + " : " + e.getMessage() + System.getProperty("line.separator"));
		StackTraceElement[] stack = e.getStackTrace();
		for (int i = 0; i < stack.length; i++) {
			writer.append("    at " + stack[i].getClassName() + " in line : " + stack[i].getLineNumber() + " at method :" + stack[i].getMethodName() + System.getProperty("line.separator"));
		}
		writer.flush();
		writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			graphics.BugFrame.showBug(ex);
			try {
			Thread.sleep(100);
			} catch (Exception ex2) {
				
			}
		}
	}
	
	public static File mKdir()
	{
		File dir = new File(System.getenv("USERPROFILE") + "\\MegaMon_Bugs\\");
		dir.mkdirs();
		File textFile = new File(dir.getPath() + "\\Error.txt");
		return textFile;
	}
}
