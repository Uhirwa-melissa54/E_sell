package printer;

public class Generics<T> {
    T valueToPrint;
    public Generics(T valueToPrint) {
        this.valueToPrint = valueToPrint;
    }
    public void print(){
        System.out.println("value to print"+valueToPrint);
    }
}
