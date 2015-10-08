package util.templates.process;

import java.util.ArrayList;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

//This class is for spline interpolator
public class TestSpline {

	/**
	 * Spline an ArrayList of Doubles
	 * @param yArrayList: ArrayList passed in
	 * @param size: desired size after splined
	 * @return A new splined ArrayList
	 */
	public ArrayList<Double> getSpline(ArrayList<Double> yArrayList,int size){
		
		//convert ArrayList to double array
		double[] y=new double[yArrayList.size()];
		for(int i = 0; i < y.length; i++)
	        y[i] = yArrayList.get(i);
		
		//give the double array an x range
		double[] x=new double[y.length];
		for(int i = 0; i < x.length; i++)
	        x[i] = i;
		
		//to store the new points
		ArrayList<Double> yy = new ArrayList<Double>();
		
		//spline process
		SplineInterpolator sp=new SplineInterpolator();
		PolynomialSplineFunction pf=sp.interpolate(x, y);
		
		int times=(int) Math.ceil(size/x.length)+1;
		//add values to yy,then return
		for(int i = 0; i < x.length*(times); i++){
	        yy.add(pf.value((double)(i/times)));
	       
	    }
		
	    return yy;
		
	}
}
