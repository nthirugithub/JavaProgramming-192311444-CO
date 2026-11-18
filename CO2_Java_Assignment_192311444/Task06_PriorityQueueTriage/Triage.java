import java.util.*;

record Patient(String name,int severity,long arrival) {
    Patient { if(severity<1||severity>5)throw new IllegalArgumentException("Severity must be 1..5"); }
    @Override public String toString(){return name+"(S"+severity+")";}
}
public class Triage {
    private long counter=0;
    private final PriorityQueue<Patient> queue=new PriorityQueue<>(
        Comparator.comparingInt(Patient::severity).thenComparingLong(Patient::arrival));
    void admit(String name,int severity){queue.add(new Patient(name,severity,counter++));}
    Patient next(){return queue.poll();}
    public static void main(String[] args){
        Triage er=new Triage();
        er.admit("Ravi",3);er.admit("Meena",1);er.admit("John",3);
        er.admit("Asha",5);er.admit("Kumar",1);er.admit("Lata",2);
        StringBuilder order=new StringBuilder(); Patient p;
        while((p=er.next())!=null)order.append(p).append(' ');
        System.out.println(order.toString().trim());
        System.out.println("PriorityQueue add/poll are O(log n); peek is O(1).");
    }
}
