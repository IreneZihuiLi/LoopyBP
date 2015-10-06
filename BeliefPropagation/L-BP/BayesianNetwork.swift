//
//  BayesianNetwork.swift
//  L-BP-v1
//
//  Created by Irene on 27/06/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//

import Foundation

public class SwiftGraph
{
    private var canvas: Array<Vertex>
    public var isDirected: Bool
    var edges:[String]
    init() {
        canvas = Array<Vertex>()
        //directed model
        isDirected = true
        edges=[]
    }
    
    func addVertex(key: String) -> Vertex {
        //set the key
        var childVertex: Vertex = Vertex(key:key)
        childVertex.key = key
        //add the vertex to the graph canvas
        canvas.append(childVertex)
        
        return childVertex
    }
    
    
    func addEdge(source: Vertex, neighbor: Vertex, weight: Int)
    {
        //create a new edge
        var newEdge = Edge(name: source.key)
        //establish the default properties
        newEdge.neighbor = neighbor
        newEdge.weight = weight
        source.neighbors.append(newEdge)
        //add to children
        source.children.append(neighbor)
        //add to parent
        neighbor.parents.append(source)
        
        let edgeName = neighbor.key + source.key
        self.edges.append(edgeName)
        
        //check for undirected graph
        //undone
//        if (isDirected == false)
//        { //create a new reversed edge
//            var reverseEdge = Edge()
//            //establish the reversed properties
//            reverseEdge.neighbor = source
//            reverseEdge.weight = weight
//            neighbor.neighbors.append(reverseEdge)
//        }
    }
//    
//    func addProbabilities(source: Vertex,probs:(x:Double,y:Double)){
//        for node in canvas {
//            if(node.key == source.key){
//                node.setProbability(probs)
//                
//                node.setPriorProb(probs)
//            }
//        }
//        //printout
////        print(source.key+" ")
////        println(probs)
//    }
    
    //set post prob.
//    func setProbabilities(vertex: Vertex,probs:(x:Double,y:Double),beliefTable:BeliefTable,col:Int){
//        
//        for node in canvas {
//            if(node.key == vertex.key){
//                node.setProbability(probs)
//                //set value in the belief table
//                beliefTable.setProbOfANode(col,key:vertex.key,probs:probs)
//            }
//        }
//        
//        //printout
//        print(vertex.key+" ")
//        
//        println(probs)
//    }
    
    //get an array of node names, for the belief table
    func getNodeNames() ->[String] {
        var array=[String]()
        for vertex in canvas {
            array.append(vertex.key)
        }
        return array
    }
    
    //print out
    func showGraph(){
        for node in canvas {
            println("Key:"+node.key)
            node.showNeighbors()
            println(node.children)
            node.probibilities.showProbs()
        }
    }
    
    //find root nodes: nodes without parents
    func getRootNodes() -> Array<Vertex>{
        var roots = Array<Vertex>()
        for node in canvas {
            if (node.parents.count == 0) {
                roots.append(node)
            }
        }
        return roots
    }
    
    //find anticipatory nodes: nodes without children
    func getChildlessNodes() -> Array<Vertex>{
        var childless = Array<Vertex>()
        for node in canvas {
            if (node.children.count == 0) {
                childless.append(node)
            }
        }
        return childless
    }
}

public class Vertex
{
    var key: String
    var probibilities:ConditionalProbability
    var children: Array<Vertex>
    var parents: Array<Vertex>
    var neighbors: Array<Edge>
    init(key:String) {
        self.key = key
        self.neighbors = Array<Edge>()
        self.children = Array<Vertex>()
        self.parents = Array<Vertex>()
        self.probibilities = ConditionalProbability(conditions: "")
    }
    
    //P(a) as prior prob
    func setPriorProb(probs:(x:Double,y:Double)){
        self.probibilities.priorProb = probs
    }
    //P(a) as prior prob
    func getPriorProb(probs:(x:Double,y:Double))->(Double,Double){
        return self.probibilities.priorProb
    }
    
    
    //CPT
    //set prob Table (pre-defined)
    func setPriorProbTable(probs:[String: Double ],series:String,state:String){
        if self.probibilities.conditions == ""{
            self.probibilities = ConditionalProbability(conditions: series)
        }

        for (conditions,prob) in probs{
            self.probibilities.setPriorProbTable(conditions,probs:prob,state:state,conditionState: series)
        }
        //func setPriorProbTable(conditions:[String],probs:Double,state:String){
    }
    
    //get a dictionary of a state(y/n)
    func getConditionalProb(state:String) -> [String:Double]{
        return self.probibilities.getProOfAState(state)
    }
    
   
    
    func getPriorWithKeys(sourceState:String,link:[String:String])->(Double) {
        return self.probibilities.getPriorWithKeys(sourceState, link: link)
    }
    
    func showNeighbors(){
        print("Children:")
        for child in self.children{
            print(child.key)
            print(",")
        }
        
        print("Parents:")
        for parent in self.parents{
            print(parent.key)
            print(",")
        }
        
        
    }
    
    func showPriorProbs(){
        self.probibilities.showProbs()
    }
  
}



public class Edge
{
    var neighbor: Vertex
    var weight: Int
    init(name:String) {
        weight = 0
        self.neighbor = Vertex(key:name)
    }
}


