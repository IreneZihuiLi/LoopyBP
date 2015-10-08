package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import util.rawseries.RawArray;
import util.rawseries.RawMap;

public class DatasetReader {

//	private int[] index = { 0, 1, 2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 16, 21,
//			22, 23, 24, 25, 26 };
	private int[] index = {4,5,6};
	
	//Test HF
//	private int[] index = {14,15,16};

	private RawMap rawMap;

	// Constructor
	public DatasetReader() {

		this.rawMap = new RawMap();

		getDatasetData();
	}

	// get dataset
	public RawMap getDataSet() {
		return this.rawMap;
	}

	private void getDatasetData() {
		// TODO Auto-generated method stub
		// String testFile = "smootheddata" + File.separator + filename;
		File sfolder = new File("smootheddata" + File.separator);
		File[] flist = sfolder.listFiles();
		System.out.println("num " + flist.length);
		int totalNum = 1;
		for (File f : flist) {
			String fname = f.getName();
			String source = "smootheddata/" + fname;
			System.out.println("The " + totalNum++ + " :" + source);
			RawArray rawArray=new RawArray(index.length);
			if (fname.endsWith("dat")) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							new File(source)));
					String line;
					int lines = 0;
					while ((line = br.readLine()) != null) {
						String[] lineString = line.split("\t");
						lines++;
						if (lines > 1) {

							for (int i = 0; i < index.length; i++) {
								double value = Double
										.valueOf(lineString[index[i]]);
								rawArray.add(value, i);
							}

						}
					}

					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
//			System.out.println("Map fname"+fname);
			rawMap.add(fname, rawArray);
//			rawMap.test();
		}
		
	}

}