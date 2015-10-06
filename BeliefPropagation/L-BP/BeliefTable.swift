//
//  BeliefTable.swift
//  L-BP-v1
//
//  Created by Irene on 03/07/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//  referance: http://www.cse.unsw.edu.au/~cs9417ml/Bayes/Pages/PearlPropagation.html#formula5



import Foundation

class BeliefTable{
    
    var nodeName : [String]
    //    var nodes:Array<Vertex>
    var columns=3
    
    var nodeList :[Vertex]
    
    //a dictionary to store each row(node)
    var array:[String:BeliefRow]
    
    //lambda list: stores lambda with target.
    // <name:tuple>
    // name: e.p, "WR" means lambda from W to R.
    var LambdaList=[ String:(Double,Double)]()
    
    //pi list: same with lambda list
    var PiList=[String:(Double,Double)]()
    
    //init
    init(graph:SwiftGraph,Nodelist:[Vertex]) {
        
        self.nodeName = graph.getNodeNames()
        self.array = [String:BeliefRow]()
        self.nodeList = Nodelist
        for name in nodeName {
            //init each node
            self.array[name]=BeliefRow()
        }
        
        initNodes(graph)
        initLists(graph)
        
    }
    //init table by nodes
    func initNodes(graph:SwiftGraph){
        var roots=graph.getRootNodes()
        var childless=graph.getChildlessNodes()
        
        for node in roots{
            var row = BeliefRow()
            row = self.array[node.key]!
            var priorProb = node.probibilities.priorProb
            row.setCol(1, probs: priorProb)
            self.array[node.key] = row
            
        }
        
        for node in childless{
            var row = BeliefRow()
            row = self.array[node.key]!
            var priorProb = node.probibilities.priorProb
            row.setInitLAMBDA()
            self.array[node.key] = row
            
        }
        
        
    }
    
    //setup pi and lambda list
    func initLists(graph:SwiftGraph){
        let edges = graph.edges
        for edge in edges {
            self.PiList[edge] = (1,1)
            self.LambdaList[edge] = (1,1)
        }
    }
    
    //search vertex with name
    func searchVertexByName(name:String) -> (Vertex){
        
        var vertex : Vertex?
        for node in self.nodeList{
            if(name == node.key){
                vertex = node
            }
        }
        //could be empty
        return vertex!
    }
    
    //  Boundary Conditions
    //  ---find the referance in the head of the file
    //  1.Root nodes: If X is a node with no parents, we set the PI value equal to the prior probabilities of P(x).
    //  2.Anticipatory nodes: If X is a childless node that has not been instantiated, we set the LAMBDA value as a vector of all 1's.
    //  3.Evidence nodes: If evidence X=xi is obtained, we set the LAMBDA value to (0,...,0,1,0,...0) with 1 at the ith position.
    
    
    
    //boundary Conditions: set an evidence Node
    //shoud be the start ****
    func setEvidenceNode(vertex:Vertex,probs:(x:Double,y:Double), col:Int ){
        setProbOfANode(col, key: vertex.key, probs:probs)
        //after setting, update the table
        propagateBelief(vertex)
    }
    
    
    //propagation rules--Lambda
    //The likelihood vector is equals to the term-by-term product of all the Lambda message passed from the node's children.
    func propagateLAMBDA(vertex:Vertex){
        var list = Array<(x:Double,y:Double)>()
        var children = vertex.children
        for node in children {
            list.append(getProbOfANode(2,key:node.key))
        }
        
        setProbOfANode(2, key: vertex.key, probs: dotProduct(list))
        
    }
   
    
    
    //propagation rules--Belief
    //Our belief of the values of X is equal to the normalised term-by-term product of the likelihood vector and the prior probabilities vector.
    func propagateBelief(vertex:Vertex){
        var list = Array<(x:Double,y:Double)>()
        list.append(getProbOfANode(1, key: vertex.key))
        list.append(getProbOfANode(2, key: vertex.key))
        
        setProbOfANode(0, key: vertex.key, probs: normalizeATuple(dotProduct(list)))
    }
    
    
    //updating rules -- Lambda
    //Hard to explain by words, see examples. NB: If (x) is an unit vector (ie. all 1's) then the output of the formula would also be an unit vector.
    func updatingLambda(vertex:Vertex){
        //list
        var list=[(Double,Double)]()
        //get children of this vertex
        let children = vertex.children
        //get value from the lambda list
        for child in children{
            var name = child.key + vertex.key
            list.append(self.LambdaList[name]!)
        }
        
        setProbOfANode(2, key: vertex.key, probs: dotProduct2(list))
//        println("...Change lambda of \(vertex.key) to \(dotProduct2(list))")
        
        
    }
    
    
    //changed
    func updatingLambdaWithTarget(sourceVertex:Vertex, targetVertex:Vertex) -> ([String:Double]){
       
        
        var sumLeft = Array<[Double]>()
        var sumRight = Array<[Double]>()
        var newLambda = [String:Double]()
        
        //get parents
        var parents = sourceVertex.parents
        
        var Pi : (x:Double,y:Double)
        Pi = (0,0)
        
        //get lambda of sourceVertex
        var lambdaOfSource = getProbOfANode(2,key:sourceVertex.key)
        //get CPT
        var cpt_conditions=sourceVertex.probibilities.conditions
        
        
        //for all states in TargetVertex(left,right)
        //for all states in combination of (parent, source)
        
        var sourceState = ["0","1"]
        var parentState = ["0","1"]
        var targetState = ["0","1"]
        
        for targetstate in targetState{
            for sourcestate in sourceState{
                for parentstate in parentState{
                    
                    
                    
                    //eliminate target
                    //get Pi from Source to target
                    for parent in parents {
                        
                        
                        // may not exist!
                        //if (parent.key != targetVertex.key){
                        
                        //println("^^^ parent \(parent.key), target \(targetVertex.key)")
                        var name = sourceVertex.key + parent.key
                        
                        
                        //get CPT value
                        var link = [String:String]()
                        link[targetVertex.key] = targetstate
                        if (parent.key != targetVertex.key){
                            link[parent.key] = parentstate
                        }
                        
                        
                        let elemFromCPT = sourceVertex.getPriorWithKeys(sourcestate, link: link)
                        if (elemFromCPT != -0.0){
//                            println("1.Link is \(link) \n \(elemFromCPT)")
                        }
                        //get lambda
                        var lambda = 0.0
                        if sourcestate == "1"{
                            lambda = lambdaOfSource.x
                        }else{
                            lambda = lambdaOfSource.y
                        }
//                        println("2.lambda is \(lambda)")
                        
                        //get pi
                        var pi = 0.0
                        
                        //check nil
                        var resultPi : (x:Double,y:Double)
                        if PiList[name] == nil{
                            pi = 1.0
                          
                            
                        }else{
                            resultPi = PiList[name]!
                            
                            if parentstate == "1"{
                                pi = resultPi.x
                            }else{
                                pi = resultPi.y
                            }
                            
                            
                        }
                        
                        
                        
                        let temp = elemFromCPT * lambda * pi
                        
                        if targetstate == "1"{
                            Pi.x = Pi.x + temp
                            
                        }else{
                            Pi.y = Pi.y + temp
                            
                        }
                        
                    }
                }
            }
//            println("Final, we get : \(Pi)")
            
            //add to lambda list
            let lambdaKey = sourceVertex.key + targetVertex.key
            self.LambdaList[lambdaKey] = Pi
        }
        
        
        
        //return
        newLambda["1"] = dotProductOfArray(sumLeft)
        newLambda["0"] = dotProductOfArray(sumRight)
        
        return newLambda
        
    }
    
    
    //updating rules -- Pi of node source -> node target
    //The message that X is going to pass onto a particular child is equals to the belief of X divide (term-by-term) by the message that child sent to X. Here, division by zero is only defined when the numerator is also equals to zero. Zero divided by zero is defined as zero in this case.
    //the result is for calculating Pi, returning a tuple
    
    //fomular 2 **Important
    func updatingPi (vertex:Vertex){
        var newPi : (x:Double,y:Double)
        var parents = vertex.parents
        
        var Pi = Array<[String:Double]>()
        for parent in parents{
            //...
            var pi = updatingPiWithTarget(parent, targetVertex:vertex)
            Pi.append(pi)
        }
        
        var left = vertex.getConditionalProb("0")
        var right = vertex.getConditionalProb("1")
        var matrix = Array<[String:Double]>()
        matrix.append(left)
        matrix.append(right)
        var result = multiplicationOfMatix(multiplicationOfArrays(Pi), matrix)
        
        //dict to tuple
        newPi.x = result["1"]!
        newPi.y = result["0"]!
        
        setProbOfANode(1, key: vertex.key, probs: newPi)
        
        
    }
    //fomular 2 **Important
    func updatingPi2 (vertex:Vertex){
        var newPi : (x:Double,y:Double)
        var parents = vertex.parents
        
        var Pi = Array<[String:Double]>()
        for parent in parents{
            //...
            var pi = updatingPiWithTarget2(parent, targetVertex:vertex)
            Pi.append(pi)
        }
        
        var left = vertex.getConditionalProb("0")
        var right = vertex.getConditionalProb("1")
        var matrix = Array<[String:Double]>()
        matrix.append(left)
        matrix.append(right)
        var result = multiplicationOfMatix(multiplicationOfArrays(Pi), matrix)
        
        //dict to tuple
        newPi.x = result["1"]!
        newPi.y = result["0"]!
        
        setProbOfANode(1, key: vertex.key, probs: newPi)
        
        
    }
    
    //formula 5
    func updatingPiWithTarget(sourceVertex:Vertex,targetVertex:Vertex) -> ([String:Double]){
        
        //get belief of the source vertex
        var Belief = getProbOfANode(0, key: sourceVertex.key)
        
        //for normalization
        var alpha = 1.0
        
        //get lambda of the target
        var lambda = getProbOfANode(2, key: targetVertex.key)
        
        
        
        
        //updating Pi
        var newPi = [String:Double]()
        newPi["1"] = alpha * (Belief.x / lambda.x)
        newPi["0"] = alpha * (Belief.y / lambda.y)
        
        //dealing with 0
        if (lambda.x == 0){
            newPi["1"] = 0
        }
        if (lambda.y == 0){
            newPi["0"] = 0
            
        }
        //add to the pi list
        var name = targetVertex.key + sourceVertex.key
        self.PiList[name] = (newPi["1"]!,newPi["0"]!)
        
        
        return newPi
    }
    
    
    func updatingPiWithTarget2(sourceVertex:Vertex,targetVertex:Vertex) -> ([String:Double]){
        
        var result : (x:Double,y:Double)
        result = (1,1)
        //get pi of the source vertex
        var Pi = getProbOfANode(1,
            key: sourceVertex.key)
        //get children of the source vertex
        var children = sourceVertex.children
        
        var lambdaArray = [(Double,Double)] ()
        var resultArray = [(Double,Double)] ()
        for child in children{
            if(child.key != targetVertex.key){
                var lambda = self.LambdaList[(child.key + sourceVertex.key)]
                lambdaArray.append(lambda!)
                lambdaArray.append(Pi)
                result = dotProduct2(lambdaArray)
                resultArray.append(result)
            }
        }
        
        result = dotProduct2(resultArray)
        result = normalizeATuple(result)
        
        //updating pi
        var newPi = [String:Double]()
        newPi["1"] = result.x
        newPi["0"] = result.y
        
        
        //add to the pi list
        var name = targetVertex.key + sourceVertex.key
        self.PiList[name] = (newPi["1"]!,newPi["0"]!)
        
        
        return newPi
        
        
    }
    
    
    
    //get the column
    func getCol(col:Int) -> Array<(x:Double,y:Double)>{
        var lambda = Array<(x:Double,y:Double)>()
        for (nodename,row) in array{
            lambda.append(row.getCol(col))
        }
        
        return lambda
    }
    
    //get probs by node and colum
    func getProbOfANode(col:Int,key:String) -> (x:Double,y:Double){
        var row = array[key]
        var tuple = row?.getCol(col)
        return tuple!
        
    }
    
    //set probs by node and colum
    func setProbOfANode(col:Int,key:String,probs:(x:Double,y:Double)) {
        var row = array[key]
        row?.setCol(col, probs: probs)
    }
    
    
    
    //execute function
    func propagate(){
        
        //start from lambda: when lambda is zero
        for (nodename,row) in array{
            if ( !row.getColFlag(2) ){
                beliefTable.propagateLAMBDA(searchVertexByName(nodename))
            }
        }
        
        //then propagate belief: when pi and lambda are both exist
        for (nodename,row) in array{
            if ( (row.getColFlag(2) )&&(row.getColFlag(1) )  ){
                beliefTable.propagateBelief(searchVertexByName(nodename))
                
            }
        }
        
        
        
        //updating Pi:when pi is not exist
        for (nodename,row) in array{
            if ( !row.getColFlag(1)  ){
                beliefTable.updatingPi(searchVertexByName(nodename))
                beliefTable.propagateBelief(searchVertexByName(nodename))
            }
        }
        
        //show the result of Table
        println("------------ Finish Propagating -----------")
        println("--+- Belief -+--- Pi --+-- Lambda ---")
        beliefTable.show()
        println("------------------------------------------------------")
    }
    
    
    
    //change nodes
    func change(nodeName:String,probs:(x:Double,y:Double), col:Int){
        
        beliefTable.setProbOfANode(col, key:nodeName, probs:probs)
        //update lambda with target
        let parents = searchVertexByName(nodeName).parents
        for parent in parents{
            
            beliefTable.updatingLambdaWithTarget(searchVertexByName(nodeName), targetVertex: parent)
            
        }
        
        var children = [Vertex]()
        //change parents Lambda, Belief
        for parent in parents{
            //recursive (until nil)
            beliefTable.updatingLambda(parent)
            beliefTable.propagateBelief(parent)
            
            children += parent.children
            
            
        }
        
        //avoid duplicates and the changing node
        var filter = Dictionary<String,Int>()
        var len = children.count
        for var index = 0; index < len  ;++index {
            var value = children[index].key
            if ((filter[value] != nil)||(value == nodeName)) {
                children.removeAtIndex(index--)
                len--
            }else{
                filter[value] = 1
            }
        }
        
        //change parents' Children(except the changing node)
        for child in children{
            beliefTable.updatingPi2(child)
            beliefTable.propagateBelief(child)
        }
        
        println("Changed a condition successfully!")

        
    }
    
    //show belief of a node
    func showResult(vertex:Vertex...){
        for node in vertex {
            let resultBEL = beliefTable.getProbOfANode(0, key: node.key)
            print("Result for node \(node.key): \(resultBEL)")
            if (resultBEL.x > resultBEL.y){
                println("TRUE")
            }else{
                println("FALSE")
            }
        }
    }
    
    //show the table
    func show(){
        for (nodename,row) in array{
            print(nodename+":")
            row.show()
            println()
        }
        showList()
    }
    
    //show the pi and lambda list
    func showList(){
        println("Pi list: \(PiList)")
        println("Lambda list:\(LambdaList)")
    }
    
    
    
}


class BeliefRow{
    
    //an array of tuples: 0-> Belief, 1->pi, 2-> lambda
    var row: Array<(x:Double,y:Double)>
    var changeFlag: Array<Bool> // tag if changed or not
    let UNIT_VECTOR=(x:1.0,y:1.0) //unit vector: tupel
    let ZERO_VECTOR=(x:0.0,y:0.0) //zero vector: tupel
    
    init(){
        row = Array(count: 3, repeatedValue: ZERO_VECTOR);
        changeFlag = Array(count:3, repeatedValue: false)
    }
    
    func setInitLAMBDA(){
        row[2] = UNIT_VECTOR
        changeFlag[2] = true
    }
    
    //get and set an element in the row
    func getCol(index:Int)-> ((x:Double,y:Double)) {
        return row[index]
    }
    func setCol(index:Int, probs:(x:Double,y:Double)) {
        row[index] = probs
        changeFlag[index] = true
    }
    //get and set an element in the row
    func getColFlag(index:Int)-> (Bool) {
        return changeFlag[index]
    }
    //get and set an element in the row
    func checkCol(index:Int)-> (Bool) {
        if((row[index].x == 1.0)&&(row[index].y == 1.0)){
            return true
        }else{
            return false
        }
    }
    
    
    
    func show(){
        for tuple in row{
            print("\t\(tuple)")
            print(";")
        }
    }
    
}


