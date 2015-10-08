package util.comparison;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import util.tools.ScoredThingDsc;

//This class is for pre-judge the types of raw data (before normalization)
public class TypeJudgement {

	//constructor
	public TypeJudgement(){}
	
	/**
	 * Judge the types of a series of raw data
	 * @param series: serial data
	 * @return flag: 3 types
	 */
	public int TypeJudgement(ArrayList<Double> series){
		int flag=0;
		int midIndex=series.size()/2;
		
		//add up left and right separately
		double sumLeft=0;
		double sumRight=0;
		
		for(int i=0;i<series.size();i++){
			if(i<midIndex)
				sumLeft+=series.get(i);
			else 
				sumRight+=series.get(i);
		}
		
		if(sumLeft*sumRight>0){
			flag=JudgeDetail(series);
		}
		else
			flag=0;
		
//		if(flag==1){
//			flag=JudgeDescend(series);
//		}
		return flag;
	}
	
	/**
	 * Check the mixed peak
	 * @param series
	 * @return flag: a right type 
	 */
	public int JudgeDetail(ArrayList<Double> series){
		
		int flag=0;
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
		int[] index=new int[3];
		//add values to the ss,for comparison
		for(int i=0;i<series.size();i++){
			//compare with absolute value
			ss.add(new ScoredThingDsc(series.get(i).doubleValue(),i,true));
		}
		// find max and min
		int max = 0;
		int min = 0;
		int mid = 0;
		ScoredThingDsc maxObj = ss.first();
		ScoredThingDsc minObj = ss.last();

		max = maxObj.thing;
		min = minObj.thing;
		mid = (max + min) / 2;
		//get distance between min and the start or end (choose the min)
		int tMin;
		if(min>(series.size()-min))
			tMin=min;
		else 
			tMin=series.size()-min;
		//get distance between max and the start or end (choose the min)
		int tMax;
		if(max>(series.size()-max))
			tMax=max;
		else 
			tMax=series.size()-max;
		
		//a threshold is needed
		double threshold=series.size()*0.2;
		if(Math.abs(tMax-tMin)>threshold)
			flag=1;
		
		if(Math.abs(maxObj.score-minObj.score)<=5)
			flag=0;
		return flag;
	}
	
	/**
	 * Descend order-->find out template -1
	 * @param series
	 * @return
	 */
	public int JudgeDescend(ArrayList<Double> series){
		int flag=0;
		
		
		return flag;
	}
	
	
}
