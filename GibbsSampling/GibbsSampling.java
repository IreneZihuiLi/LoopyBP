package test.algorithms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.tinkerpop.gremlin.structure.*;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONReader;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GibbsSampling {


	public static void main(String[] args) throws IOException {
		GibbsSampling test = new GibbsSampling();
		Graph g = TinkerGraph.open();
		// TinkerGraph g = TinkerFactory.createModern();

		load(g);

		test.run(g);
	}

	public static void out(ArrayList<String> arr) {
		for (String str : arr)
			System.out.printf(str + ", ");
		System.out.println();
	}

	public static void load(Graph g) throws IOException {
		InputStream in = new FileInputStream("data/graph-example-2-tp3.json");
		// GraphMLReader reader = GraphMLReader.build().create();
		GraphSONReader reader = GraphSONReader.build().create();
		reader.readGraph(in, g);
		
		
	}


	public int run(Graph g) {
		
		int vertexNumber = 0;

		Iterator<Vertex> vertexIterator = g.vertices();
		while(vertexIterator.hasNext()){
			Vertex vertex = vertexIterator.next();
//			System.out.println("Now is "+ vertex.id()+"\t");
			vertexNumber++;
		}
		
		System.out.println("Size is "+ vertexNumber);


		//Generate 100 samples, in 10 states
		GibbsSamplingTest gst = new GibbsSamplingTest(vertexNumber,1000,10);
		
		//for plot
//		test(vertexNumber);
		
		return 0;
	}
	
	//testing code
//	public void test(int vertexNumber){
//
//		for(int m=2;m<4097;m=m*2){
////			for(int i=0;i<;i++){
//				GibbsSamplingTest gst = new GibbsSamplingTest(vertexNumber,m,10);
////			}
//		}
//	}

}



class GibbsSamplingTest {
	
	
	public GibbsSamplingTest(int vertexNumber,int sampleNumber,int stateNumber) {
		// TODO Auto-generated constructor stub
		
		Query conditionQuery = new Query(1, 3);
		Query evidenceQuery = new Query(0, 5);
		Distribution distribution = new Distribution();
		
		double value = GibbsSampling(vertexNumber, sampleNumber, stateNumber, distribution, conditionQuery, evidenceQuery);

		
		
	}
	
	/**
	 * The main function of Gibbs Sampling
	 * @param vertexNumber: Graph 
	 * @param sampleNumber: sampling numbers
	 * @param stateNumber: for each vertex, how many states
	 * @param distribution: probability distribution
	 * @param conditionQuery:   P( CQ = cq | EQ = eq ), the query
	 * @param evidenceQuery:  P( CQ = cq | EQ = eq ), the evidence
	 * @return  P( CQ = cq | EQ = eq )
	 */
	public static double GibbsSampling(int vertexNumber, int sampleNumber, int stateNumber, Distribution distribution, Query conditionQuery, Query evidenceQuery) {
		double prob = 0.d;
		//the input should be a graph, but we care about the number of vertex. the distributions depend on the relations. 

		
		//Current sample list. 
		ArrayList<Object> sample = new ArrayList<Object>();
		//Number of samples with CQ = cq and EQ = eq.
		int number = 0;
		//Time
		long start =  System.currentTimeMillis();
		
		
		
		
		//Generate D1, the EQ = eq.
		for(int i = 0; i<vertexNumber; i++){
			if(i!=evidenceQuery.getVertex()){
//				sample.add(distribution.generateASample(stateNumber));
//				sample.add(distribution.generateASampleByND(stateNumber));
				sample.add(distribution.generateASampleByURD(stateNumber));

			}else{
				sample.add(evidenceQuery.state);
			}
		}
		//If CQ = cq
		if( (int)sample.get(conditionQuery.vertex) == conditionQuery.state ){
			number++;
		}
		//show
		
		
		//Get other sn-1 samples
		for(int num=0;num<sampleNumber-1;num++){
			//for each vertex(variable),non-evidence
			for(int i = 0; i<vertexNumber; i++){
				//sample
				if(i!=evidenceQuery.vertex){
//					sample.set(i,distribution.generateASample(stateNumber));
//					sample.set(i,distribution.generateASampleByND(stateNumber));
					sample.set(i,distribution.generateASampleByURD(stateNumber));

				}
			}
			
			if( (int)sample.get(conditionQuery.vertex) == conditionQuery.state ){
				number++;
			}
			System.out.println("Now getting the "+(num+2)+"th sample.");

		}
		
		
		//testing
		prob = (double)number/(double)sampleNumber;
		
		System.out.println("We want to get X"+conditionQuery.vertex + "="+ conditionQuery.state+", given the condition that X"+evidenceQuery.vertex+"="+evidenceQuery.state);

		System.out.println("Right sample number is "+ number +"/"+sampleNumber + " = "+prob);

		long time =  System.currentTimeMillis() - start;
		
		System.out.println("Samples:"+sampleNumber+"\t Computation time:"+time+"\t Probability:"+prob);
		return prob;
	}
	
	
	
}

class Query {
	int vertex;
	int state;
	
	public int getVertex() {
		return vertex;
	}


	public void setVertex(int vertex) {
		this.vertex = vertex;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	

	public Query(int vertex, int state) {
		// TODO Auto-generated constructor stub
		this.vertex = vertex;
		this.state = state;
	}

	
}

// probability distribution class
class Distribution {
	//Next contains a list of different prob distributions

	
	// build a distribution: random
	public static int generateASample(int stateNumber) {
		// Use random to test
		Random rd = new Random();
		return rd.nextInt(stateNumber);
	}
	
	//Normal distribution
	public static int generateASampleByND(int stateNumber) {
		
		NormalDistribution normalDistribution = new NormalDistribution(stateNumber/2,2.0);
				
		int prob = (int) normalDistribution.sample();
		
		// return a number
		return prob;
	}
	
	//UniformR real distribution
	public static int generateASampleByURD(int stateNumber) {
		UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(0,stateNumber);
		
		int prob = (int) uniformRealDistribution.sample();
		// return a number
		return prob;
	}
	
	//For user defined distribution: pass in the prob distribution and the number of states
	//to simulate a real sampling process, out put should be a state.
	public static int generateASample(Object userDefinedDistribution, int stateNumber){
		int prob = 0;
		//.....
		// prob = userDefinedDistribution(...);
		return prob;
	}
}
