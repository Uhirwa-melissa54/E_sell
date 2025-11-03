package printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Printing {
   public  static <K> K printTwo(K value1, K value2){
       System.out.println("value 1:"+value1);
       System.out.println("value 2:"+value2);
       return  value1;
   }
   public static <K extends Number> void listDiff(List<K> list1){
       int i= list1.size();
       for(int j=0; j < i;j++){
           System.out.println(list1.get(j));
       }
   }
    public static void main(String[] args) {


       Scanner input1=new Scanner(System.in);
        printTwo("Melissa",789);
        List<Double> list1=new ArrayList<>();
        list1.add(789.0);
        list1.add(789.1);
        listDiff(list1);
        List<Integer> list2= new ArrayList<> ();
        list2.add(67);
        list2.add(89);
        listDiff(list2);

//        Generics<Integer> printer=new Generics<>(34);
//        printer.print();

      /*  Generics<String> printer2=new Generics<>("Melissa");
    printer2.print();*/

    }
}
