package util.rawseries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.analysis.function.Add;

import util.templates.TemplateArray;

// This class is to store raw data
public class RawMap{

	//<filename, rawArray>
	private HashMap<String, RawArray> rawMap;
	
	//constructor
	public RawMap(){
		this.rawMap=new HashMap<String, RawArray>();
	}

	//add a record
	public void add(String filename, RawArray rawArray){
		this.rawMap.put(filename, rawArray);
	}
	
	//normalization 
	public void normalization(){
		Iterator<Map.Entry<String, RawArray>> iterator=this.rawMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, RawArray> entry=iterator.next();
			RawArray rawArray=entry.getValue();
			rawArray.normalization();
		}
	}
	
	//breakpoints
	public void breakpoints(){
		Iterator<Map.Entry<String, RawArray>> iterator=this.rawMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, RawArray> entry=iterator.next();
			RawArray rawArray=entry.getValue();
			rawArray.breakpoints();
		}
	}
	
	//breakpoints
		public void getBreakedSeries(){
			Iterator<Map.Entry<String, RawArray>> iterator=this.rawMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, RawArray> entry=iterator.next();
				RawArray rawArray=entry.getValue();
				rawArray.getBreakedSeries();
			}
		}
	
	public void Compare(TemplateArray templates){
		Iterator<Map.Entry<String, RawArray>> iterator=this.rawMap.entrySet().iterator();
		int sum=0;
		while(iterator.hasNext()){
			sum++;
			System.out.println("Iterator is "+sum);
			Map.Entry<String, RawArray> entry=iterator.next();
			RawArray rawArray=entry.getValue();
//			System.out.println("size is "+rawArray.test());
			rawArray.compare(templates);
		}
	}
	
	public void showResults(){
		Iterator<Map.Entry<String, RawArray>> iterator=this.rawMap.entrySet().iterator();
		int sum=0;
		while(iterator.hasNext()){
			sum++;
			System.out.println("\nResult for  "+sum);
			Map.Entry<String, RawArray> entry=iterator.next();
			RawArray rawArray=entry.getValue();
			rawArray.showResults();
		}
	}
	
	//for plot
	public int[] getFinalBreakingPoints(String fileName){
		RawArray rawArray=this.rawMap.get(fileName);
		return rawArray.getBreakPositions();
	}
	//for check result
	public int[] showFlags(String fileName){
		RawArray rawArray=this.rawMap.get(fileName);
		return rawArray.getFlags();
	}
	
	
	
	public void test(){
		System.out.print("size "+rawMap.size());
	}
}
