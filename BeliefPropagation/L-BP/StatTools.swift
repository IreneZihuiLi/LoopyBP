//
//  StatTools.swift
//  L-BP
//
//  Created by Irene on 04/07/2015.
//  Copyright (c) 2015 MAC. All rights reserved.
//

import Foundation

//calculate dot product of a tuple array
func dotProduct(list:Array<(x:Double,y:Double)>) -> ((x:Double,y:Double)){
    
    var sumX=1.0
    var sumY=1.0
    for tuple in list{
        sumX = sumX * tuple.0
        sumY = sumY * tuple.1
    }
    
    return (sumX,sumY )
}



//DotProduct of an array : the length of the lists must be the same
func dotProductOfArray(list:Array<[Double]>) -> (Double){
    var result = 0.0
    var sizeOfArray = countElements(list)
    var sizeOfList = countElements(list.0)
    
    var resultList = Array<Double>(count: sizeOfList, repeatedValue: 1.0) //tempary store values
    
    for elem in list{
        for (index,value) in enumerate(resultList){
            resultList[index] = value * elem[index]
        }
    }
    
    for value in resultList{
        result += value
    }
    return result
}

//calculate dot product of a tuple array
func dotProduct2(list:[(Double,Double)]) -> ((x:Double,y:Double)){
    
    var sumX=1.0
    var sumY=1.0
    for tuple in list{
        sumX = sumX * tuple.0
        sumY = sumY * tuple.1
    }
    
    return (sumX,sumY )
}

//multiplication of matrix, including ...
/**
    (0.8,0.2) x (   1,  0)   = (  yes,  no)
                ( 0.2,0.8)

     0,  1          0,  1          0, 1
**/
func multiplicationOfMatix(left:[String:Double], right:Array<[String:Double]>) -> [String:Double]{
    var result=[String:Double]()

    for (index,item) in enumerate(right){
        var sum = 0.0
        for (key,value) in item{
            
            sum += left[key]! * value
            
        }
        
        result[String(index)] = sum
        
    }
    return result
}

// (a,b) x (c,d) -->(ac,bc,ad,bd)
func multiplicationOfArrays(tuples:Array<[String:Double]>) -> ([String:Double]){
    var result=[String:Double]()
    var state = 2
    var number = tuples.count //number of tuple
    var length = state ^^ (number)
    
    for i in 0...(length-1) {
        let formatter = NSNumberFormatter()
        formatter.minimumIntegerDigits = number
        
        var key=String(i,radix:2)
        
        key=formatter.stringFromNumber(key.toInt()!)!
        
        result[key]=0.0
    }
    
    for (key,value) in result {
        let str = Array(key)
        var length = countElements(str)
        var element = 1.0

        for i in 0...(number-1){
            var tuple = tuples[i]
            element = element * tuple[String(str[i])]!

        }

        result[key]=element
        
    }
    
    return result
}

//normalization..sum up to 1
func normalizeATuple(tuple:(Double,Double)) -> ((x:Double,y:Double)){
 
    var norm = tuple.0 + tuple.1
    var newX = tuple.0/norm
    var newY = tuple.1/norm

    return (newX, newY)
}

//operator define: a^n
infix operator ^^ { }
func ^^ (radix: Int, power: Int) -> Int {
    return Int(pow(Double(radix), Double(power)))
}

