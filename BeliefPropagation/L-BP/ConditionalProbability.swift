//
//  ConditionalProbability.swift
//  Stores the conditionalprobability table ---2D
//
//  Created by Irene on 28/06/2015.
//  Copyright (c) 2015 MAC. All rights reserved.


//Conditional probability tabel class

class ConditionalProbability{

    var conditions: String //store condition nodes name --> order (e.p: "SR", S then R)
    var nodeStates:[String]? //states of node -> (yes,no)
    var priorProb:(x:Double,y:Double)
    
    //P(a|W), W is a set of nodes.
    //conditional probability table, first key is the state of W set, second key is the state of this node
    //["0011":["0":(0.2,0.8)]]
    var condProbTable = [ String : [ String : Double ] ]()
    
   
    //Init
    init (conditions:String){
        self.conditions = conditions
        self.priorProb = (0,0)
        self.nodeStates = ["1","0"]
        
    }
    
    //P(a)..prior prob.
    init (probs:(x:Double,y:Double)){
        self.conditions = ""
        self.priorProb = probs
        self.nodeStates = ["1","0"]

        
    }
    //add an element in CPT
    //conditions: "0110" (same order with the constructor)
    //probs: (0.1,0.9)
    //state: "0"  (when the state of node is false)
    func setPriorProbTable(conditions:String,probs:Double,state:String,conditionState:String){
        

        //same order
        if (conditionState == self.conditions){

            //check if exist
            if var col = self.condProbTable[conditions] {
                //if exist, then add
                col [state] = probs
                self.condProbTable[conditions] = col
            }else{
                //if not, create and add
                var newRow = [state:probs]
                self.condProbTable[conditions] = newRow
            }
        }
    }
    
    
    //get an element in CPT
    //conditions: "0110" (same order with the constructor)
    //state: "0"  (when the state of node is false)
    //return a tuple,state,and conditions
    func getPriorProbTable(conditions:String,state:String) -> (Double,String,String){
        var probs = 0.0
        //same order
        if (conditions == self.conditions){
            //check if exist
            if var col = self.condProbTable[conditions] {
                //if exist, then add
                probs = col[state]!
            }
        }
        return (probs,conditions,state)
    }
    
    //get an element with index keys
    //e.p when H=y, R=y,S=y, returns 1.0
    //values are: ["R":"1","S":"1"], returns a double
    func getPriorWithKeys(sourceState:String,link:[String:String])->(Double) {
        //matching key
        var searchKey = ""
        for character in conditions{
            //iterator link
            for (key,value) in link{
                if (Character(key) == character){
                
                    searchKey = searchKey + value

                }
            }
        }
        
        if let col = self.condProbTable[searchKey]{
            if let result = col[sourceState]{
                return result
            }
        }
        //if nil, then return -0.0
        return -0.0;
        
    }

   
    
    //..Series: W..
    func setPriorProbs(probilities:(x:Double,y:Double),series:String){

        self.conditions = series
        self.priorProb = probilities
    }
    
    //calculate probability
    func calculateProbability(){
        
    }
    
    //get y/n --> [state:prob]
    func getProOfAState(state:String) -> [String:Double]{
        
        var probOfAState=[String:Double]()
        
        var conditions = self.condProbTable.keys
        
        for (key,value) in self.condProbTable{
            if var col = self.condProbTable[key] {
                var prob = col[state]
                probOfAState[key] = prob
            }
        }

        return probOfAState
    }
    
    //toString
    func showProbs(){

        for (key,value) in self.condProbTable {
            print("\(key):\(value)")
        }
        
    }
    
}