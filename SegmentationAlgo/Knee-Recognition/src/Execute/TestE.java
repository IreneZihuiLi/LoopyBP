package Execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.timeseries.TimeSeries;

import util.comparison.FastDTWComparison;
import util.plot.PlotData;
import util.segmentation.BaselineBreak;
import util.segmentation.PreProcess;
import util.templates.process.FeatureSelection;
import util.templates.process.Normalization;
import util.templates.process.TestSpline;
import IO.DatasetReader;
import IO.TemplateReader;
import IO.TemplateWriter;
import IO.TestDataReader;

public class TestE {

	public static void main(String[] args) throws IOException {
		
		
		//Testing template process
		String tempFile = "template" + File.separator + "template.ses";
		TemplateReader reader=new TemplateReader(tempFile);

	
		//Testing real data
		DatasetReader ds=new DatasetReader();
		PreProcess preProcess=new PreProcess(ds);
		//Comparison
		FastDTWComparison fdtw=new FastDTWComparison(preProcess,reader.getTemplateArray());

		preProcess.getRawMap().showResults();
		//plot
		PlotData.initPlot(preProcess.getRawMap());
		
		
		//		ArrayList<Double> test=reader.getTemplateArray().getTemplate(2).getSeries();
		/*
		String realFile = "smootheddata" + File.separator + "S049_SHA.dat";

		//Testing real data
		DatasetReader ds=new DatasetReader("S049_SHA.dat");
		//Test break points
		BaselineBreak baselineBreak=new BaselineBreak();
		FastDTWComparison fdc=new FastDTWComparison(ds, baselineBreak);
		fdc.preMarkPoints();
		*/
		//Testing break points
//		TestDataReader reader = new TestDataReader(realFile, 6);
		
	}

}
