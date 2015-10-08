package util.templates;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.xml.internal.ws.Closeable;

import IO.TemplateWriter;
import util.templates.process.Normalization;
import util.templates.process.Rotate;

//To store the templates
public class TemplateArray {
	private ArrayList<Template> templateArrayList;
	
	public TemplateArray(){
		this.templateArrayList=new ArrayList<Template>();
	}
	
	// add a template object in to the ArrayList
	public void addTemplate(Template template){
		this.templateArrayList.add(template);
	}
	
	public int getSize(){
		return templateArrayList.size();
	}
	
	public Template getTemplate(int i){
		return templateArrayList.get(i);
	}
	
	public ArrayList<Double> getSeries(int i){
		return templateArrayList.get(i).getSeries();
	}
	//get all the templates
	public ArrayList<ArrayList<Double>> getAll(){
		ArrayList<ArrayList<Double>> arrayList=new ArrayList<ArrayList<Double>>();
		for(int i=0;i<getSize();i++){
			arrayList.add(getSeries(i));
		}
		return arrayList;
	}
	//get all cols
	public ArrayList<Integer> getAllCols(){
		ArrayList<Integer> cols=new ArrayList<Integer>();
		for(int i=0;i<getSize();i++){
			cols.add(getCol(i));
		}
		return cols;
	}
	
	public int getCol(int i){
		return templateArrayList.get(i).getCol();
	}
	
	public int getFlag(int i){
		return templateArrayList.get(i).getFlag();
	}
	
	//get normalized template series, write to the template files
	public ArrayList<Template> normalizationTemplates(){
		
		//Normalization and Rotate
		Normalization nl=new Normalization();
		
//		Rotate rt=new Rotate();
//		System.out.println("Templates **"+templateArrayList.size());
		
		ArrayList<Double> processedTemplate;
		// Iterator of the template ArrayList
		for (int i = 0; i < templateArrayList.size(); i++) {

			Template template = templateArrayList.get(i);
			// get normalized template series
			processedTemplate= new ArrayList<Double>();
//			processedTemplate = rt.getRotatedData(nl.normalize(template.getSeries()));
			processedTemplate=nl.normalize(template.getSeries());
			
//			System.out.println("Templates size"+template.getSeries().size());
//			System.out.println("Templates Norm size"+processedTemplate.size());
			// set the template series to the normalized one
			
			template.setSeries(processedTemplate);
			
//			System.out.println("change "+template.getSeries().size());
			// set the normalized one the ArrayList
			templateArrayList.set(i, template);
//			System.out.println("change "+templateArrayList.get(i).getSeries().size());

			// clear the ArrayList
//			processedTemplate.clear();

		}

		System.out.println("Template Normalization Done!");
		return this.templateArrayList;
		
		
	}
	
	//Write out template
	public void writeTemplate(){
		TemplateWriter tw=new TemplateWriter(this);
	}

}
