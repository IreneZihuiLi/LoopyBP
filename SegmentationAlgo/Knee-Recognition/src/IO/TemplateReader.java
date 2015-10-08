package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.templates.Template;
import util.templates.TemplateArray;

//This class is used to store the Template objects
public class TemplateReader {
	
	private TemplateArray templateArray=new TemplateArray();
	
	// Read in template files files
	public TemplateReader(String filename){
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f");
				if(st.countTokens() != 7)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}
				String fileName = String.valueOf(st.nextToken());
				Integer columNumber = Integer.valueOf(st.nextToken());
				Integer start = Integer.valueOf(st.nextToken());
				Integer finish = Integer.valueOf(st.nextToken());
				//??
				Double globalHeight=Double.valueOf(st.nextToken());
				Double b=Double.valueOf(st.nextToken());
				Integer flag=Integer.valueOf(st.nextToken());
				
				columNumber +=3;
				//create template object
				Template template=new Template(fileName, start, finish, columNumber, flag, globalHeight);
				templateArray.addTemplate(template);
				
			}
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		//normalization
		ArrayList<Template> tA=templateArray.normalizationTemplates();
	}
	
	
	public TemplateArray getTemplateArray(){
		return this.templateArray;
	}
	
	
	
}
