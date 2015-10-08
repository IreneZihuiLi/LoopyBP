package util.templates.process;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;


import util.tools.*;
//This class is for feature selection. 
public class FeatureSelection {
	
	
////	FeatureSelection fs=new FeatureSelection(21);
//	double[] rest=featureSelection(double[], double);
	
	
	
	//set length： length=4 * N + 1
	private int length;
	public FeatureSelection(int length){
		this.length=length;
	}
	
	//according to the flag, calling different functions
	public ArrayList<Double> featureSelection(ArrayList<Double> series,int flag){
		if (flag==0)
			return featureSelection0(series);
		else if (flag==1) {
			return featureSelection_1(series);
		}else{
			return featureSelection1(series);
		}
	}
	//according to the flag, calling different functions
		public double[] featureSelection(double[] series,Double flag){
			ArrayList<Double> temp=new ArrayList<Double>();
			ArrayList<Double> results=new ArrayList<Double>();
			for(int i=0;i<series.length;i++){
				temp.add(series[i]);
			}
			
			int flagInt=flag.intValue();
			if (flagInt==0)
				 results=featureSelection0(temp);
			else if (flagInt==1) {
				 results=featureSelection_1(temp);
			}else{
				 results=featureSelection1(temp);
			}
			
			//change results to double[]
			double[] serie=new double[length];
			for(int i=0;i<results.size();i++){
				serie[i]=results.get(i);
			}
			return serie;
		}
	
	//for temp0
	public ArrayList<Double> featureSelection0(ArrayList<Double> series){
		//a new arraylist, after chosen the features.
		ArrayList<Double> selected=new ArrayList<Double>();
		//sorting class
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
		int[] index=new int[5];
		//add values to the ss,for comparison
		for(int i=0;i<series.size();i++){
			ss.add(new ScoredThingDsc(series.get(i).doubleValue(),i,false));
		}
		
		//find max and min
		int max=0;
		int min=0;
		int mid=0;
		ScoredThingDsc maxObj=ss.first();
		ScoredThingDsc minObj=ss.last();
		
		max=maxObj.thing;
		min=minObj.thing;
		mid=(max+min)/2;
		
		//add up 5 feature points: start,end, mid, max and min
		selected.add(maxObj.score);
		selected.add(minObj.score);
		selected.add(series.get(mid));
		selected.add(series.get(0));
		selected.add(series.get(series.size()-1));
		//add up 5 index
		index[0]=0;
		index[1]=max;
		index[2]=mid;
		index[3]=min;
		index[4]=series.size();
		//print out 
		System.out.println("Max:"+max+"; Min:"+min+";Mid "+mid);
		
		//get other points.
		int partSize=(this.length-5)/4;
		for(int i=0;i<index.length-1;i++){
			//get times
			int alpha=(index[i+1]-index[i]-1)/partSize;
			for(int j=0;j<partSize;j++){
				int insertIndex=index[i]+j*alpha;
				System.out.println(insertIndex);
				selected.add(series.get(insertIndex));
			}
			
		}
		
		for(int i=0;i<selected.size();i++){
			System.out.println(selected.get(i));
		}
		
		//Sorting 
		QuickSort qs=new QuickSort(selected);
		qs.startQuickStart(0, selected.size()-1);
//		System.out.println("****** After");
//		for(int i=0;i<selected.size();i++){
//			System.out.println(selected.get(i));
//		}

		return selected;
	}
	
	//for temp1
	public ArrayList<Double> featureSelection1(ArrayList<Double> series){
		//a new arraylist, after chosen the features.
		ArrayList<Double> selected=new ArrayList<Double>();
		//sorting class
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
		int[] index=new int[3];
		//add values to the ss,for comparison
		for(int i=0;i<series.size();i++){
			ss.add(new ScoredThingDsc(series.get(i).doubleValue(),i,false));
//			System.out.println(series.get(i));
		}
		
		//find max and min
		int max=0;

		ScoredThingDsc maxObj=ss.first();
		max=maxObj.thing;
		//add up 5 feature points: start,end, mid, max and min
		selected.add(maxObj.score);
		selected.add(series.get(0));
		selected.add(series.get(series.size()-1));
		//add up 5 index
		index[0]=0;
		index[1]=max;
		index[2]=series.size();
		//print out 
		System.out.println("Max:"+max);
		//get other points.
		int partSize=(this.length-3)/2;
		for(int i=0;i<index.length-1;i++){
			//get times
			int alpha=(index[i+1]-index[i]-1)/partSize;
			for(int j=0;j<partSize;j++){
				int insertIndex=index[i]+j*alpha;
				System.out.println(insertIndex);
				selected.add(series.get(insertIndex));
			}
			
		}
		
//		for(int i=0;i<selected.size();i++){
//			System.out.println(selected.get(i));
//		}
		System.out.println("pppuuuttt "+selected.size());

		
		//Sorting 
		QuickSort qs=new QuickSort(selected);
		qs.startQuickStart(0, selected.size()-1);
		
		//Print
//		System.out.println("****** After");
//		for(int i=0;i<selected.size();i++){
//			System.out.println(selected.get(i));
//		}
		//get index
		return selected;
	}
	
	
	public ArrayList<Double> featureSelection_1(ArrayList<Double> series){
		//a new arraylist, after chosen the features.
		ArrayList<Double> selected=new ArrayList<Double>();
	
		
		
		//add up 2 feature points: start,end
		selected.add(series.get(0));
		selected.add(series.get(series.size()-1));
		
		//select other points
		int alpha=series.size()/(this.length-1);
		int insertIndex=0;
		for(int j=0;j<(this.length-1);j++){
				insertIndex+=alpha;
				System.out.println(insertIndex);
				if(insertIndex<series.size())
					selected.add(series.get(insertIndex));
			}
			
		//printout
		for(int i=0;i<selected.size();i++){
			System.out.println(selected.get(i));
		}
		
		//Sorting 
		QuickSort qs=new QuickSort(selected);
		qs.startQuickStart(0, selected.size()-1);
		
		//Print
		System.out.println("****** After");
		for(int i=0;i<selected.size();i++){
			System.out.println(selected.get(i));
		}
		System.out.println(selected.size());

		//get index
		return selected;
	}
	
}
