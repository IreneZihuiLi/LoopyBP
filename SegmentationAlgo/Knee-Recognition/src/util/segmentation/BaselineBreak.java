package util.segmentation;

import java.util.ArrayList;

import util.rawseries.RawMap;

import IO.DatasetReader;

//This class is used to find the break points, for comparison use.
//A threshold should be given first.
public class BaselineBreak {
	
//	private ArrayList<Integer> breakPoints;//store the index of the points
//	private double threshold; // threshold for different types of raw data
	private RawMap rawMap;
	
	public BaselineBreak(){}
	
	public BaselineBreak(DatasetReader ds,double threshold){
		this.rawMap=ds.getDataSet();
		
	}
	
	public ArrayList<Integer> getBreakPoints(ArrayList<Double> series, double td){
		ArrayList<Integer> breakPoints=new ArrayList<Integer>();
		double threshold=td;
		//iterator the series
		for(int i=0;i<series.size();i++){
			//if the abs value is with in threshold, then add the index to the array
			if (Math.abs(series.get(i))<=threshold){
				breakPoints.add(i);
			}
		}
	
		
		//return index ArrayList
		return breakPoints;
		
	}
	
	
	

}
