package util.segmentation;

import java.util.ArrayList;

import IO.DatasetReader;
import util.comparison.TypeJudgement;
import util.rawseries.RawMap;
import util.templates.process.FeatureSelection;
import util.templates.process.Normalization;

//This class is used to pre process the raw data: including segmentation,normalization and feature selection
public class PreProcess {

	
//	private Normalization nl = new Normalization();// for normalization
	private RawMap rawMap=new RawMap();
	
	
	public PreProcess(DatasetReader dsReader){
		//For testing: get the raw data
		this.rawMap=dsReader.getDataSet();
		this.rawMap.breakpoints();
		
		System.out.println("Raw Breaking Finish!");
		this.rawMap.getBreakedSeries();
		this.rawMap.normalization();
		System.out.println("Raw Normalization Finish!");

	}
	
	public RawMap getRawMap(){
		return this.rawMap;
	}
	
	


}
