package util.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.timeseries.TimeSeries;

import util.segmentation.BaselineBreak;
import util.templates.process.Normalization;

public class Template {
	
	private String FileName; //file name
	private int Col;  //column number
	private ArrayList<Double> Series; //Template series
	private int Flag; // 1 for movement, -1 for silence
	private double globalHeight; //Global height
	
	public String getFileName() {
		return FileName;
	}


	public void setFileName(String fileName) {
		FileName = fileName;
	}


	public int getCol() {
		return Col;
	}


	public void setCol(int col) {
		Col = col;
	}


	public ArrayList<Double> getSeries() {
		return this.Series;
	}


	public void setSeries(ArrayList<Double> series) {
		this.Series = series;
	}


	public int getFlag() {
		return Flag;
	}


	public void setFlag(int flag) {
		Flag = flag;
	}

	
	//Constructor
	public Template(String fileName,int start,int finish,int col,int flag,Double globalHeight) {
		this.FileName=fileName;
		this.globalHeight=globalHeight;
		this.Col=col;
		this.Series=new ArrayList<Double>();
		getTemplateData(fileName, start, finish, col);
		this.Flag=flag;
	}
	
	
	/**
	 * Read template data from the real data
	 * @param filename:real data file name
	 * @param start: start row number
	 * @param finish: finish row number
	 * @param col: column number
	 */
	public void getTemplateData(String filename,int start,int finish,int col){
		
		String testFile = "templatesData" + File.separator + filename;

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(testFile)));
			String line;
			int lines=0;
			while ((line = br.readLine()) != null) 
			{
				String[] lineString=line.split("\t");
				lines++;
				//get numbers in the range
				if((lines>=(start+1)&&(lines<=(finish+1)))){
					double value=Double.valueOf(lineString[col]);
					//add value, divided by gh
					this.Series.add(value/this.globalHeight);
				}
				
					
			}
			
			
			
			System.out.println("Time series Done! ");
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	//toString function
	public String toString(){
		System.out.println("Template is "+FileName+" "+Flag+" "+this.Series.size());
		for(int i=0;i<this.Series.size();i++){
			
			System.out.println("i is "+i+": "+Series.get(i));
			
		}
		
		return "";
	}
	
	
	
}
