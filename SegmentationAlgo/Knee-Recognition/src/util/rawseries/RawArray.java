package util.rawseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.dtw.FastDTW;

import util.comparison.FastDTWComparison;
import util.segmentation.BaselineBreak;
import util.templates.TemplateArray;
import util.templates.process.Normalization;
import util.tools.ScoredThingDsc;

public class RawArray {

	//to store all the data series in one single file
	private ArrayList<ArrayList<Double>> rawArray;
	private ArrayList<ArrayList<ArrayList<Double>>> rawBreakedArray; //store normalized results
	private ArrayList<Integer> breakArray; //to store the positions
	private ArrayList<Integer> removedBreakArray; //to store the positions(after cleaning)
	private ArrayList<Integer> flagArray;  //to store the results 0,-1,1

	private ArrayList<Integer> finalBreakArray; //to store the positions(after cleaning)
	private ArrayList<Integer> finalFlagArray;  //to store the results 0,-1,1
	
	private Normalization nl=new Normalization();
	private BaselineBreak bb=new BaselineBreak();
	private int index=0;
	private double globalHeight=1.d;
	
	public RawArray(int num){
		this.rawArray=new ArrayList<ArrayList<Double>>();
		
		//initialize data array list
		for(int j=0;j<num;j++){
			ArrayList<Double> arrayList=new ArrayList<Double>();
			this.rawArray.add(arrayList);
//			System.out.println("array added"+j);
		}
		
		this.breakArray=new ArrayList<Integer>();
		this.removedBreakArray=new ArrayList<Integer>();
		
		//initialize breakArray list
//		for(int j=0;j<num;j++){
//			ArrayList<Integer> arrayList=new ArrayList<Integer>();
//			this.breakArray.add(arrayList);
//		}
		this.flagArray=new ArrayList<Integer>();
		this.finalBreakArray=new ArrayList<Integer>();
		this.finalFlagArray=new ArrayList<Integer>();

		//initialize breakArray list
//		for(int j=0;j<num;j++){
//			ArrayList<Integer> arrayList=new ArrayList<Integer>();
//			this.flagArray.add(arrayList);
//		}
		
		this.rawBreakedArray=new ArrayList<ArrayList<ArrayList<Double>>>();
		// initialize raw breaked array list
		for (int j = 0; j < num; j++) {
			ArrayList<ArrayList<Double>> arrayList = new ArrayList<ArrayList<Double>>();
			this.rawBreakedArray.add(arrayList);
		}
	}


	//add value into arraylist
	public void add(double value, int i){
		ArrayList<Double> arrayList=this.rawArray.get(i);
		arrayList.add(value);
//		System.out.println("£ Add: "+value);

		this.rawArray.set(i, arrayList);
	}
	
	//add results into arraylist
	public void addResults(int value){
			
			this.flagArray.add(value);
	}
		
	//normalization the data series
	public void normalization(){
		for(int i=0;i<this.rawArray.size();i++){
			ArrayList<Double> arrayList=nl.normalize(this.rawArray.get(i));
			this.rawArray.set(i, arrayList);
			
		}
	}
	//find breakpoints
	public void breakpoints(){
		//the second one
//		for(int i=0;i<this.rawArray.size();i++){
//			ArrayList<Double> arrayList=this.rawArray.get(i);
//			System.out.print(","+arrayList.size());
		
		//choose the line first, then break
//		this.showResult();
		index = this.selectLine();
		System.out.print("&&Select line "+index);
		ArrayList<Integer> intList = bb.getBreakPoints(this.rawArray.get(index), 0.3);

		this.breakArray = intList;
		// }
	}
	//get  rawBreakedSeries
	public ArrayList<ArrayList<ArrayList<Double>>>  getRawBreakedSeries(){
		return this.rawBreakedArray;
	}

	
	
	
	//get breaked list
	public void getBreakedSeries(){
		for(int i=0;i<this.rawArray.size();i++){
			ArrayList<Double> arrayList=this.rawArray.get(i);
			ArrayList<Integer> intList=this.breakArray;
			ArrayList<ArrayList<Double>> breakedList=getRawDataSeries(arrayList, intList);
			System.out.println("Now " +this.rawBreakedArray.size());
			for(int j=0;j<breakedList.size();j++){
				System.out.println();
			}
			this.rawBreakedArray.set(i, breakedList);
		}
	}
	
	public void compare(TemplateArray templates){
		
		
//		for(int i=0;i<this.rawBreakedArray.size();i++){
//			ArrayList<ArrayList<Double>> breakedList=this.rawBreakedArray.get(i);
		ArrayList<ArrayList<Double>> breakedList=this.rawBreakedArray.get(index);
		FastDTWComparison fc=new FastDTWComparison();

		// for each group
		for (int j = 0; j < breakedList.size(); j++) {
			ArrayList<Double> arrayList = breakedList.get(j);

			// set 50 as threshold
			if (arrayList.size() > 50) {

				for (int i = 0; i < arrayList.size(); i++) {
					double elem = arrayList.get(i);
//					System.out.println("Elem："+elem +";gh:"+ this.globalHeight + ";after:"+elem / this.globalHeight);
					arrayList.set(i, elem/globalHeight );

				}

				int value = fc.Comparison(arrayList, templates,(index + 4));

//				int value = fc.Comparison(arrayList, templates,(index+14));
//				if(value==1)
//				System.out.print(" OMG! "+value);
//				if(value==-1)
//					System.out.print("LOL! "+value);
				// set flag results
				addResults(value);
				this.removedBreakArray.add(this.breakArray.get(j));
			}

		}
		
		//combine 1 and 1...
		combine();
		
		
			
	}
	
	// according to the index, get the raw data series to compare
		public ArrayList<ArrayList<Double>> getRawDataSeries(ArrayList<Double> RawData,ArrayList<Integer> MarkPoints) {
			ArrayList<ArrayList<Double>> piecedRawData=new ArrayList<ArrayList<Double>>();
			//get the mark points
//			ArrayList<Integer> MarkPoints=baselineBreak.getBreakPoints(RawData, 0.03);
			
			// how to slide??
			for (int j = 0; j < MarkPoints.size() - 1; j++) {
				int start = j;
				int end = j + 1;
				ArrayList<Double> series = new ArrayList<Double>();
				for (int i = MarkPoints.get(start); i < MarkPoints
						.get(end); i++) {
					series.add(RawData.get(i));
				}
				piecedRawData.add(series);
//				//threshold: length
//				if (series.size() > 20) {
//					//get the type first
//					//normalization and feature selection
////					ArrayList<Double> normedSeries = nl.normalize(series, flag);
////					ArrayList<Double> selectedSeries = fs.featureSelection(normedSeries, flag);
//					piecedRawData.add(series);
////					this.Series.add(selectedSeries);
//					series = null;
////					selectedSeries = null;
//				}

			}

			// for(int i=0;i<this.Series.get(0).size();i++){
			// System.out.println(this.Series.get(0).get(i));
			// }
			return piecedRawData;
		}

	// add results into arraylist
	public void showResults() {
//		for(int i=0;i<this.flagArray.size();i++){
			ArrayList<Integer> arrayList=this.finalFlagArray;
			System.out.println("before breaking Size: "+ breakArray.size()+" after:"+removedBreakArray.size());
			for(int i=0;i<arrayList.size();i++){
				System.out.print(" ,"+ arrayList.get(i));
			}
			
//		}
	}
	
	//show raw data
	public void showResult() {
		
		for(int i=0;i<this.rawArray.size();i++){
			ArrayList<Double> arrayList=rawArray.get(i);
			System.out.println("() Size: "+ arrayList.size());
			for(int j=0;j<arrayList.size();j++){
				System.out.print(" ,"+ arrayList.get(j));
			}	
		}
	}
	
	//select a line for the breaking 
	public int selectLine(){
		int index=0;
		double[] deviation=new double[rawArray.size()];
		
		for(int i=0;i<rawArray.size();i++){
			ArrayList<Double> dataArrayList=rawArray.get(i);
			SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
			for(int j=0;j<dataArrayList.size();j++){
				//add all the values for comparision
				ss.add(new ScoredThingDsc(dataArrayList.get(j).doubleValue(),j,false));
//				System.out.println("£ Now: "+dataArrayList.get(j));

			}
			//get max 10% and min 19%
			int numberSize=(int) Math.ceil(dataArrayList.size() * 0.1);
//			ScoredThingDsc maxObj=ss.first();
			int itor=0;
			double sumMax=0,sumMin=0;
			for(Iterator<ScoredThingDsc> iterator=ss.iterator();iterator.hasNext();){
				itor++;
				ScoredThingDsc sThingDsc=iterator.next();
				if(itor<numberSize)
					sumMax+=sThingDsc.score;
				if((itor<dataArrayList.size())&&(itor>(dataArrayList.size()-numberSize)))
					sumMin+=sThingDsc.score;
//				System.out.println("£ Now: "+sThingDsc.score);

			}

			deviation[i]=(sumMax-sumMin)/(double)numberSize;
			
//			System.out.println("& We get: "+sumMax+","+sumMin+", size "+numberSize);

			
			
		}
		
		//find out the max deviation
		double max=deviation[0];
		for(int i=0;i<deviation.length;i++){
//			System.out.print("added:"+deviation[i]);
			if (deviation[i] > max){
				index=i;
				max=deviation[i];
			}
		}
		//set global height
		this.globalHeight=max;
//		this.globalHeight=1;
		
		System.out.println("% We choose the "+index);
		return index;
	}
	
	//for plot
	public int[] getBreakPositions() {
		ArrayList<Integer> arrayList = this.removedBreakArray;
		int[] positions = new int[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			positions[i]=arrayList.get(i);
		}
		
		return positions;

	}

	//combine: 1 ... 1 -> 0  , 0 and 0  -> 0 and nil
	public void combine(){

		

		if (this.flagArray.size() > 0) {
			
			System.out.println("FlagArray " + this.flagArray.size());
			System.out.println("Before "
					+ Arrays.toString(this.flagArray.toArray()));
			
			// check 1,1
			for (int i = 0; i < (this.flagArray.size() - 1); i++) {
				// System.out.println("&&&&&--------------"+this.flagArray.get(i)+" "+this.flagArray.get(i+1));

				if ((this.flagArray.get(i) == this.flagArray.get(i + 1))
						&& (this.flagArray.get(i) == 1)) {
					this.finalFlagArray.add(0);

					this.finalBreakArray.add(this.removedBreakArray.get(i + 1));
					i++;
				} else {
					this.finalFlagArray.add(this.flagArray.get(i));
					this.finalBreakArray.add(this.removedBreakArray.get(i));
				}

			}
			// add last element
			this.finalBreakArray.add(this.removedBreakArray
					.get(this.removedBreakArray.size() - 1));
			this.finalFlagArray
					.add(this.flagArray.get(this.flagArray.size() - 1));

			this.removedBreakArray = this.finalBreakArray;
			this.flagArray = this.finalFlagArray;

			ArrayList<Integer> tempBreak = new ArrayList<Integer>();
			ArrayList<Integer> tempFinal = new ArrayList<Integer>();

			// check -1,-1
			for (int i = 0; i < (this.flagArray.size() - 1); i++) {
				// System.out.println("&&&&&--------------"+this.flagArray.get(i)+" "+this.flagArray.get(i+1));

				if ((this.flagArray.get(i) == this.flagArray.get(i + 1))
						&& (this.flagArray.get(i) == -1)) {
					tempFinal.add(-1);

					tempBreak.add(this.removedBreakArray.get(i + 1));
					i++;
				} else {
					tempFinal.add(this.flagArray.get(i));
					tempBreak.add(this.removedBreakArray.get(i));
				}

			}

			tempFinal.add(this.flagArray.get(this.flagArray.size() - 1));
			tempBreak.add(this.removedBreakArray.get(this.removedBreakArray
					.size() - 1));

			this.removedBreakArray = tempBreak;
			this.flagArray = tempFinal;

			System.out.println("FlagArray " + this.flagArray.size());
			System.out.println("After "
					+ Arrays.toString(this.flagArray.toArray()));
		}

	}
	
	//show flags
	public int[] getFlags() {
		ArrayList<Integer> arrayList = this.flagArray;
		int[] positions = new int[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			positions[i]=arrayList.get(i);
		}

		return positions;

	}
	public String test(){
		
		return rawBreakedArray.size()+"";
	}
}
