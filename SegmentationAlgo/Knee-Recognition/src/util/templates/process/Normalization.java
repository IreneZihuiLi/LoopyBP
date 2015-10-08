package util.templates.process;

import java.util.ArrayList;

//This class is for normalization the series
public class Normalization {

	private ArrayList<Double> Series;//Normalized series
	private int normalizeSize=100; //normalize size,100 or 200
	
	/**
	 * normalization function
	 * @param series: ArrayList<Double>
	 * @return normalized series: ArrayList<Double>
	 */
	public ArrayList<Double> normalize(ArrayList<Double> series){
		
		this.Series=new ArrayList<Double>();
		double alpha=0;
		
		alpha=series.size()/(double)normalizeSize; //get how many times
		//normalize points
		if(alpha>1){
			
			//If given series is larger 
			for(int i=0;i<normalizeSize;i++){
				//get points directly
				this.Series.add(series.get((int)Math.ceil(i*alpha))/alpha);
			}
			
		
		}
		if((alpha<=1)&&(alpha>0)){
			
			//If given series is smaller, use spline to make it larger
			for(int i=0;i<normalizeSize;i++){
				
				//spline
				TestSpline ts= new TestSpline();
				//get a bigger one,then normalize
				ArrayList<Double> temp=ts.getSpline(series, normalizeSize);
				
				this.Series.add(temp.get((int)Math.ceil(i*alpha))/alpha);

			}

		}
		
		if(this.Series.size()!=normalizeSize){
			System.out.println("Norm ERROR!!!!!!!!!!!!!!!");
			for (int i = 0; i < normalizeSize; i++) {
				this.Series.add(0d);
			}
		}
		
		//peak
//		double sum = 0;
//		if (flag != 0) {
//			for (int i = 0; i < normalizeSize; i++) {
//				sum += this.Series.get(i);
//			}
//		} else {
//			for (int i = 0; i < normalizeSize / 2; i++) {
//				sum += this.Series.get(i);
//			}
//		}
//
//		if(sum<0){
//			for(int i=0;i<normalizeSize;i++){
//				double temp=this.Series.get(i)*(-1);
//				this.Series.set(i, temp);
//			}
//
//		}
//		System.out.println("Normalized Done! ");

		return this.Series;
	}
}
