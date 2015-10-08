package util.templates.process;

import java.util.ArrayList;

import util.tools.QuickSort;

public class Rotate {
	private ArrayList<Double> originalData;
	private ArrayList<Double> rotatedData;
	
	private QuickSort quickSort;
	//constructor
	public Rotate(){
		
	}

	public ArrayList<Double> getRotatedData(ArrayList<Double> originalData){
		this.originalData=originalData;
		this.rotatedData=new ArrayList<Double>();
		
		this.quickSort=new QuickSort(originalData);

		int length=originalData.size();
		//add the 20% of the largest values
		int start1=(int)Math.ceil(length*0.8);
		int start2=(int)Math.ceil(length*0.2);
		
		//add up
		double sumMax=0;
		double sumMin=0;
		for(int i=start1;i<originalData.size();i++){
			sumMax+=originalData.get(i);
		}
		for(int i=0;i<start2;i++){
			sumMin+=originalData.get(i);
		}
		
		//if it's a lower occasion
		if((sumMax+sumMin)<0){
			//multiply by -1
			for(int i=0;i<length;i++){
				double elem=originalData.get(i);
				originalData.set(i, elem*(-1));
			}
			
			//Transpose
			for(int i=0;i<length/2;i++){
				double temp=0;
				temp=originalData.get(i);
				originalData.set(i,originalData.get(length-i-1));
				originalData.set(length-i-1,temp);
				
			}
		}
			return originalData;
		
		
	}
	
	

}
