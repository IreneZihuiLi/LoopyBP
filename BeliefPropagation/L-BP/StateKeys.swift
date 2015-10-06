//
//  StateKeys.swift
//  L-BP-v1
//
//  Created by Irene on 27/06/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//

// To be extended...

import Foundation

class StateKeys{
    
    var node:Node //node
    var index:Int //state of this node
    
    //constructor
    init(node:Node,index:Int){
        self.node=node
        self.index=index
    }
    
    //equals
    func equals(statekeys:StateKeys) ->Bool{
        var ret=false
        if ((self.node === statekeys.node) && (self.index == statekeys.index)) {
            ret=true
        }
        return ret
    }
    
    //get a node
    func getNode() ->Node {
        return self.node
    }
    
    
}