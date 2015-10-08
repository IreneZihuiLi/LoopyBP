package util.tools;
import java.util.ArrayList;
import java.util.Random;
 
public class QuickSort {
private static ArrayList<Double> inputArray = new ArrayList<Double>();
         
    public QuickSort(ArrayList<Double> inputArray){
        QuickSort.inputArray = inputArray;
    }
 
    public void startQuickStart(int start,int end){
        int q;
        if(start<end){
            q = partition(start, end);
            startQuickStart(start, q);
            startQuickStart(q+1, end);
        }
    }
 
    public ArrayList<Double> getSortedArray(){
        return QuickSort.inputArray;
    }
 
    int partition(int start,int end){
//        System.out.println("\n---------Iteration Starts----------");
//        System.out.println("\nSorting Window from index number:"+start+" to "+end);
         
        int init = start;
        int length = end;
         
        Random r = new Random();
        int pivotIndex = nextIntInRange(start,end,r);
        double pivot = inputArray.get(pivotIndex);
         
//        System.out.println("Pivot Element "+pivot+" at index:"+pivotIndex);
                 
        while(true){
            while(inputArray.get(length)>pivot && length>start){
                length--;
            }
             
            while(inputArray.get(init)<pivot && init<end){
                init++;
            }
             
            if(init<length){
                double temp;
                temp = inputArray.get(init);
                inputArray.set(init,inputArray.get(length));
                inputArray.set(length,temp);
                length--;
                init++;
                 
//                System.out.println("\nAfter Swapping");
//                for(int i=start;i<=end;i++){
//                    System.out.print(inputArray.get(i)+" ");
//                }
            }else{
//                System.out.println("\n---------Iteration Ends---------");
                return length;
            }
        }
         
    }
     
    // Below method is to just find random integer from given range
    static int nextIntInRange(int min, int max, Random rng) {
           if (min > max) {
              throw new IllegalArgumentException("Cannot draw random int from invalid range [" + min + ", " + max + "].");
           }
           int diff = max - min;
           if (diff >= 0 && diff != Integer.MAX_VALUE) {
              return (min + rng.nextInt(diff + 1));
           }
           int i;
           do {
              i = rng.nextInt();
           } while (i < min || i > max);
           return i;
        }
}