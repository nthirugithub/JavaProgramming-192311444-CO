import java.util.*;

final class Student {
    private final String regNo;
    private final String name;
    Student(String regNo,String name){this.regNo=Objects.requireNonNull(regNo);this.name=Objects.requireNonNull(name);}
    @Override public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof Student s))return false;
        return regNo.equals(s.regNo);
    }
    @Override public int hashCode(){return Objects.hash(regNo);}
    @Override public String toString(){return regNo+" "+name;}
}
public class Attendance {
    static void show(String label, Student... scans){
        Set<Student> set=new HashSet<>(Arrays.asList(scans));
        System.out.println(label+" -> unique count: "+set.size()+" "+set);
    }
    public static void main(String[] args){
        show("Repeated same ID",new Student("192311001","Arun"),new Student("192311002","Divya"),new Student("192311001","Arun"));
        show("Same ID different name",new Student("192311003","Kavi"),new Student("192311003","Kavitha"),new Student("192311004","Ravi"));
        TreeSet<Student> ordered=new TreeSet<>(Comparator.comparing(Student::toString));
        ordered.add(new Student("192311005","Maya")); ordered.add(new Student("192311005","Maya"));
        System.out.println("TreeSet sample size: "+ordered.size());
        System.out.println("Corrected equals/hashCode use immutable regNo as identity.");
    }
}
