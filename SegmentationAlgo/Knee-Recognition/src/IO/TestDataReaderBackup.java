package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import util.segmentation.BaselineBreak;


public class TestDataReaderBackup {
	
	private String testFile; //File name
	private ArrayList<Double> Series; //Template series
	private File sfolder = new File("smootheddata"+File.separator); //File folder
	private File[] flist = sfolder.listFiles(); // File list to store all the files.

	public TestDataReaderBackup(String filename,int col){
		this.Series=new ArrayList<Double>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			int lines=0;
			while ((line = br.readLine()) != null) 
			{
				String[] lineString=line.split("\t");
//				System.out.println("Lines is "+ lines);
//				System.out.println("Line is "+ line);
				lines++;
				if(lines>1){
					double value=Double.valueOf(lineString[col]);
//					System.out.println("value is "+ value);
					this.Series.add(value);
				}
					
			}
			
			//Test break points
			BaselineBreak baselineBreak=new BaselineBreak();
			baselineBreak.getBreakPoints(Series, 0.005);
			
			
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

}
