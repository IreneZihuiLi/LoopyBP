//
//  main.swift
//  L-BP
//
//  Created by Irene on 03/07/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//

import Foundation



println("Hello, L-BP! Init....")

//init
var graph=SwiftGraph()
var R=graph.addVertex("R")
var S=graph.addVertex("S")
var W=graph.addVertex("W")
var H=graph.addVertex("H")


graph.addEdge(R, neighbor: W, weight: 0)
graph.addEdge(R, neighbor: H, weight: 0)
graph.addEdge(S, neighbor: H, weight: 0)


R.setPriorProb((0.2,0.8))
S.setPriorProb((0.1,0.9))
//add prob table
W.setPriorProbTable(["1": 1.0,"0": 0.2,], series: "R", state: "1")
W.setPriorProbTable(["1": 0.0,"0": 0.8,], series: "R", state: "0")
H.setPriorProbTable(["11": 0.0,"01": 0.1,"10": 0.0,"00": 1.0], series: "RS", state: "0")
H.setPriorProbTable(["11": 1.0,"01": 0.9,"10": 1.0,"00": 0.0], series: "RS", state: "1")


println("Init Finished! Now start computing...")

var beliefTable=BeliefTable(graph: graph,Nodelist:[R,S,W,H])

//auto propagate...
beliefTable.propagate()

//change a condition
beliefTable.change(H.key, probs:(1.0,0.0), col: 2)
beliefTable.propagate()
beliefTable.showResult(S,R)

