package printer;

import java.util.Objects;

public class Employee  implements Comparable<Employee>{
    private String firstName;
    private String lastName;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return age == employee.age && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age);
    }

    public Employee(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    public String getFirstName() {
        return firstName;
    }
  public int getAge() {
        return age;
  }
  public String getLastName() {
        return lastName;
  }

   @Override
    public String toString() {
       return firstName + " " + lastName;
   }
   @Override
    public int compareTo(Employee other) {
      return Intenger.compare(this.age,other.age);
    }
}
