//
//  Node.swift
//  L-BP-v1
//
//  Created by Irene on 27/06/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//

import Foundation


public class Node {
    var key: String //ID of this node
    var cdp: ConditionalProbability //prior prob of this node
    var parents:[Node] //parent nodes
    var children:[Node] //child nodes
    
    var beliefs:[Double]?
    var beliefHistory:[Node]?
    
    var childValues:[Double] //lambda values
    var parentValues:[Double] //pi values
    
    
    //constructor
    init(key:String) {
        self.key = key
        //initialize then compute,with the name of ID
        self.cdp=ConditionalProbability(conditions:self.key)
        self.parents=[]
        self.children=[]
        self.childValues=[]
        self.parentValues=[]
    }
    
    //add a parent node
    func addParent(parentNode:Node){
        self.parents.append(parentNode)
    }
    
    //add a child node
    func addChild(childNode:Node){
        self.parents.append(childNode)
    }
    
    //get prior probability
    func getConditionalProbability() ->ConditionalProbability {
        return self.cdp
    }
    
    //belief for EACH node
    func setInferedBeliefs(beliefs:[Double]){
        //...
    }
    
    
}