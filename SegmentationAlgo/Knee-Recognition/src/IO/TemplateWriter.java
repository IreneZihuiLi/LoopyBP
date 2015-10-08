package IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.templates.Template;
import util.templates.TemplateArray;

public class TemplateWriter {
	
	public TemplateWriter(TemplateArray templateArrayList){

		//Write into 3 different types of templates
		String outputFile1 = "template" + File.separator + "temps0.csv";
		File file1 = new File(outputFile1);
		String outputFile2 = "template" + File.separator + "temps1.csv";
		File file2 = new File(outputFile2);
		String outputFile3 = "template" + File.separator + "temps-1.csv";
		File file3 = new File(outputFile3);
		try {
			BufferedWriter output1 = new BufferedWriter(new FileWriter(file1));
			BufferedWriter output2 = new BufferedWriter(new FileWriter(file2));
			BufferedWriter output3 = new BufferedWriter(new FileWriter(file3));
			
			//Iterator of the template ArrayList
			for(int i=0;i<templateArrayList.getSize();i++){
			
				Template template=templateArrayList.getTemplate(i);
				//get normalized template series
				ArrayList<Double> series=template.getSeries();
//				processedTemplate=rt.getRotatedData(nl.normalize(template.getSeries()));
				
				//set the template series to the normalized one
//				template.setSeries(processedTemplate);
				
				//write into different files according to the flag.
				if(template.getFlag()==0){
					
					for(int j=0;j<series.size();j++){
						output1.write(series.get(j)+",");
					}
					
					output1.write("\n");
					
				}
				if(template.getFlag()==1){
					
					for(int j=0;j<series.size();j++){
						output2.write(series.get(j)+",");
					}
					
					output2.write("\n");
				}
				if (template.getFlag() == -1) {

					for (int j = 0; j < series.size(); j++) {
						output3.write(series.get(j) + ",");
					}
					
					
	
					output3.write("\n");
				}
				
				//clear the ArrayList
				series.clear();
				
				
			}
			
			
			output1.close();
			output2.close();
			output3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\nWrite Done!");
		
		
		
	}

}
