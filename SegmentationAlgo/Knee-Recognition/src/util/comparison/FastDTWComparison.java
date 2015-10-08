package util.comparison;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import com.dtw.TimeWarpInfo;
import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

import util.rawseries.RawMap;
import util.segmentation.BaselineBreak;
import util.segmentation.PreProcess;
import util.templates.Template;
import util.templates.TemplateArray;
import util.templates.process.Normalization;
import util.tools.ScoredThingDsc;
import IO.DatasetReader;

public class FastDTWComparison {
	
//	private 
//	private ArrayList<ArrayList<ArrayList<Double>>> preProcesedSeries;
	private RawMap rawMap=new RawMap();
	
//	private TypeJudgement tj = new TypeJudgement();// for pre-classification
//	private int sum=0; //for matching result
//	private int total=0; //for total count
	private int total0=0;
	private int total1=0;
	private int total_1=0;
//	private ArrayList<Template> templateArray=new ArrayList<Template>();
	
	public FastDTWComparison(){
	}
	
	public FastDTWComparison(PreProcess pp,TemplateArray templates){
		this.rawMap=pp.getRawMap();
		rawMap.Compare(templates);
//		rawMap.showResults();
		System.out.println("Total -1"+total_1);
		System.out.println("Total 0 "+total0);
		System.out.println("Total 1 "+total1);
		System.out.println("Comparison Finish!");
		
		
	}

	//compare a series with all the templates, col: col number
	public int Comparison(ArrayList<Double> rawSeriesArray, TemplateArray templates,int col){
		int flag=0;
		int sum=0;
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>();  //for sorting
		ArrayList<ArrayList<Double>> templatesArrayList=templates.getAll();
		ArrayList<Integer> templatesCols=templates.getAllCols();
		for(int i=0;i<templatesArrayList.size();i++){
			
			//only match the same column templates
//			System.out.print("tem col "+templatesCols.get(i)+" \n;pass in "+ col);
			if(templatesCols.get(i) == col){
//				System.out.print("%%tem col "+templatesCols.get(i)+" \n;pass in "+ col);
				sum++;
				double dist=fastDTWCompare(rawSeriesArray, templatesArrayList.get(i));
				
				ss.add(new ScoredThingDsc(dist,i,false)); //add for sorting
			}
		}
		
//		ScoredThingDsc maxObj=ss.last(); //get the most similar one
		ScoredThingDsc maxObj=ss.first();
		int index=maxObj.thing; //get the i
		flag=templates.getFlag(index);
//		System.out.println("choose "+maxObj.score+" flag "+flag+" ; sum "+sum);
		
		return flag;
	}
	
	// use fastDTW to compare two normalized series
	public double fastDTWCompare(ArrayList<Double> rawSeries,
			ArrayList<Double> template) {

		final TimeSeries tsI = new TimeSeries(rawSeries, false, false, ',');
		final TimeSeries tsJ = new TimeSeries(template, false, false, ',');

//		System.out.println("-------------");

		final DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");

//		long start = System.currentTimeMillis();
		int searchRadius = 5;
		final TimeWarpInfo info = com.dtw.FastDTW.getWarpInfoBetween(tsI, tsJ,
				searchRadius, distFn);

//		long end = System.currentTimeMillis();
//		System.out.println("Time " + (end - start));
//		System.out.println("Warp Distance: " + info.getDistance());

		return info.getDistance();
	}
	
}
