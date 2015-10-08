package util.plot;

import java.awt.Color;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

import com.util.Arrays;

import util.rawseries.RawMap;

public class PlotData {

//	public static void main(String[] argv) throws IOException {
        
	
		public static void initPlot(RawMap rawMap) throws IOException{
			
		// write results to a file
			String outputFile1 = "result" + File.separator + "final.txt";
			File result = new File(outputFile1);
			BufferedWriter output1 = new BufferedWriter(new FileWriter(result));

		// read files in the data set folder
		PlotData app = new PlotData();
		File sfolder = new File("smootheddata"+File.separator);
		File[] flist = sfolder.listFiles();
		System.out.println("Total " +  flist.length + " files");
		int totalNum = 1;
		
		
		for(File f : flist)
		{   			
			String fname =f.getName();
			if(fname.endsWith("dat")){
				String source = "smootheddata/"+fname;
				System.out.println("The " + fname + " fname");
	
				//plot the positions
				int[] indexofpoint=rawMap.getFinalBreakingPoints(fname);
				int[] flags=rawMap.showFlags(fname);
				System.out.println(java.util.Arrays.toString(flags));
				output1.write(fname+"\n");

				output1.write(java.util.Arrays.toString(flags)+"\n");
				//pass in
				app.plotdata(source,indexofpoint);
				System.out.println("The " + totalNum++ + " Time Done!");
			}
		}
	
		output1.close();
		System.out.println("Written finished!");
   
	}

	public void plotdata(String source,int [] indexofpiont) throws IOException {
		String[] str = source.split("\\.");
		String[] str2 = str[0].split("/");// get the file name which is str2[1]
		String fileName = str2[1];
		for(int i=0;i<indexofpiont.length;i++){
			System.out.print(","+indexofpiont[i]);

		}
		System.out.print("file name "+fileName);
		ArrayList<double[]> rawdata = readFile(source);
		ArrayList<String> titles = readFirstLine(source);
		int len = rawdata.get(0).length;
		
		int tlen = titles.size();
		String [] t = new String[tlen];
		for(int a=0;a<tlen;a++)
		{
			t[a]=titles.get(a);
		}
		
		int[] indexs = new int[len];
		for(int i=0;i<len;i++)
		{
			indexs[i] = i;
		}
		//set the name y axis
		String [] ynames = {"Magn. of Acceleration","Angular Velocity","Magnetic strength","Magn. of Acceleration","Angular Velocity","Magnetic strength",
        		"Magn. of Acceleration","Angular Velocity","Magnetic strength"};
		int ord =0;
		
		// double loop is used to identify different columns
		for(int m=0;m<len;m+=10)
		{
			for(int n=0;n<9;n+=3)
			{   
				String yname=ynames[ord];
				int[] index = {0,m+n+1,m+n+2,m+n+3};
				//passing in the array of index
//				indexofpiont = {33,300,1000,2000,3000,4000,4003,4005};
				
				XYDataset dataset = getXYdata(rawdata,t,index,indexofpiont);
				JFreeChart chart = createXYPlotChart(dataset,yname);
				ChartFrame frame = new ChartFrame("name",chart);
				frame.pack();
				String PNGfile="result/"+fileName+ord+".png";
			    ChartUtilities.saveChartAsPNG(new File(PNGfile), chart, 900,350,null);
			    ord++;
			}	     
		
		}
		
		// write the pictures into HTML file
		String outputFile1 = "result" + File.separator + fileName+".html";
		File file1 = new File(outputFile1);
		try {
			BufferedWriter output1 = new BufferedWriter(new FileWriter(file1));			
			//write the start
			output1.write("<html><head><body>");		
			File folder = new File("result"+File.separator);
			File[] listOfFiles = folder.listFiles();
			//get all the files under the folder
			for (File file : listOfFiles) {
				int i=0;
			    if (file.isFile()) {
			        String pic=file.getName();
			        //if it is a png file
			        if(pic.endsWith(".png") && pic.startsWith(fileName)){
				        i++;
				        String png="<img src=\""+pic+"\" alt=\""+i+"\">";
				        output1.write(png);
			        }
			        
			    }
			}	
			//write the end
			output1.write("</body></head></html>");			
			output1.close();	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
			
	}
    /* This method is used to get data draw picture without points
     * param
     * dataVector  the whole data set
     * seriesnames the names of series, the names are used for legend
     * indexis  the index of column of dataset, indexis[0] is time series(x axis), and others are y value
     * */
	public XYDataset getXYdata(ArrayList<double[]> dataVector,String seriesnames[], int indexis[]) {
        
		XYSeries xyData[] = new XYSeries[3];
		for (int i = 0; i < xyData.length; i++)
			xyData[i] = new XYSeries(seriesnames[i]);
		
		for (int j = 0; j < xyData.length; j++) {
			for (int i = 0; i < dataVector.size(); i++) {
				double values[] = dataVector.get(i);
				// get the values in same column
				xyData[j].add(values[indexis[0]], values[indexis[j + 1]]);
			}

		}

		XYSeriesCollection datasetSeries = new XYSeriesCollection();
		for (int i = 0; i < xyData.length; i++)
			datasetSeries.addSeries(xyData[i]);

		return datasetSeries;
	}

	

	/**
	 * 
	 * @param index1
	 * @param index2
	 * @return
	 */
	 /* The function is used to draw picture with points
	  * dataVector the whole data set
      * seriesName the number of seriesName = (indexs.length -1)
      * seriesnames the names of series, the names are used for legend
      * indexis  the index of column of dataset, indexis[0] is time series(x axis), and others are y value
      * indexkeyPoint the index of points
      * draw picture with points
      * */
	public XYDataset getXYdata(ArrayList<double[]> dataVector,String seriesnames[], int indexis[], int indexkeyPoint[]) {

		XYSeries xyData[] = new XYSeries[(indexis.length - 1)*2];

		for (int i = 0; i < xyData.length; i++)
			xyData[i] = new XYSeries(seriesnames[i]);

		for (int j = 0; j < xyData.length; j = j + 2) {
			for (int i = 0; i < dataVector.size(); i++) {
				double values[] = dataVector.get(i);
				xyData[j].add(values[indexis[0]], values[indexis[j/2 + 1]]);
			}

			for (int i = 0; i < indexkeyPoint.length; i++) {
				double values[] = dataVector.get(indexkeyPoint[i]);
				xyData[j + 1].add(values[indexis[0]], values[indexis[j/2 + 1]]);
			}

		}
		XYSeriesCollection datasetSeries = new XYSeriesCollection();
		for (int i = 0; i < xyData.length; i++)
			datasetSeries.addSeries(xyData[i]);

		return datasetSeries;
	}
	

	// The function is used to plot picture
	public JFreeChart createXYPlotChart(XYDataset dataset, String seriesName) {

		JFreeChart chart = ChartFactory.createXYLineChart("", "TimeStamp", seriesName,
				dataset, PlotOrientation.VERTICAL, true, true, false);

		int num_series = dataset.getSeriesCount();
		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setVerticalTickLabels(true);
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for (int i = 0; i < num_series; i++) {
 
	        if (i % 2 == 0)
	        renderer.setSeriesShapesVisible(i, false);
	        else {
			renderer.setSeriesLinesVisible(i, false);
			renderer.setSeriesShapesVisible(i, true);
			renderer.setSeriesShapesFilled(i, false);
			renderer.setSeriesShape(i, ShapeUtilities.createDownTriangle(3));
			//renderer.setBaseLinesVisible(false);
	        }
		}
		plot.setRenderer(renderer);

		return chart;

	}
	
	// read file and skip the first line
	public ArrayList<double[]> readFile(String filename) throws IOException {
		File f = new File(filename);
		int fleng = (int) f.length();// file length
		ArrayList<double[]> dataset = new ArrayList<double[]>();

		if (fleng > 0) {
			BufferedReader bufReader = new BufferedReader(new FileReader(f));
			String line;
			while ((line = bufReader.readLine()) != null && line.length() != 0) {
				String str = line.trim();// 
				int numTokens = 0;
				int tcmp = str.charAt(0);
				if (48 > tcmp || tcmp > 57)
					continue; // if character or symbol will skip;
				StringTokenizer strToken = new StringTokenizer(str, "\t");
				numTokens = strToken.countTokens();
				double values[] = new double[numTokens];

				for (int i = 0; i < numTokens; i++) {
					String tmpval = strToken.nextElement().toString();
					values[i] = Double.parseDouble(tmpval);
				}
				dataset.add(values);
			}
			bufReader.close();
			return dataset;
		}
		return null;
	}

	/* get the title of each column, and remove the title of time such as
	 S1_T,S2_T,S3_T
	 */
	public ArrayList<String> readFirstLine(String filename) throws IOException {
		File f = new File(filename);
		int fleng = (int) f.length();// file length
		ArrayList<String> title = new ArrayList<String>();
		if (fleng > 0) {
			BufferedReader bufReader = new BufferedReader(new FileReader(f));
			String line;
			int num_timestamp_steps = 0;
			while ((line = bufReader.readLine()) != null && line.length() != 0) {
				String str = line.trim();
				int numTokens = 0;
				if (num_timestamp_steps == 1)
					break;
				StringTokenizer strToken = new StringTokenizer(str, "\t");
				numTokens = strToken.countTokens();
				for (Integer i = 0; i < numTokens; i++) {
					String tmpval = strToken.nextElement().toString();
					if(i%10==0)
						continue;
					title.add(tmpval);		
				}
				num_timestamp_steps++;
			}
			bufReader.close();
			return title;
		}
		return null;
	}

}