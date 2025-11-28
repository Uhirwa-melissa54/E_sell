
import java.util.Scanner;

public class Excepti {
    public static void main(String[] args){
        Scanner number=new Scanner(System.in);
        int a=number.nextInt();
        int b=number.nextInt();
        try{
            if(b==0){
                throw new ArithmeticException("Not zero");
            }
            int c=a/b;
        }
        catch(ArithmeticException e){
            e.printStackTrace();
        }
    }
    
}
