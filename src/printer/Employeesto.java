package printer;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Employeesto {
    public static void main(String[] args){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Melissa","Shami",23));
        employees.add(new Employee("Divine","Uhirwa",16));
        employees.add(new Employee("Heloise","Rugie",15));
        Collections.sort(employees, new SortEmp());
        System.out.println(employees);
        Comparator<Employee>com=new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        };

        Collections.sort(employees,com);
        System.out.println(employees);
        Collections.sort(employees);
        System.out.println(employees);


    }
}
