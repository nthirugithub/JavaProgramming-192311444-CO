import java.nio.file.*;
import java.util.Arrays;
public class AdversarialFileDemo {
    public static void main(String[] args) throws Exception {
        Path f=Files.createTempFile("adversarial",".csv");
        Files.writeString(f,
            "P1,R1,Asha,2,100.0\n" +
            "P2,TOO,FEW\n" +
            "P3,R3,NonNumeric,3,abc\n" +
            "\n" +
            "P4,R4,Kavi,4,250.0\n");
        PassStore store=new PassStore(f);
        System.out.println("Valid P4: "+Arrays.toString(store.find("P4")));
        System.out.println("Malformed P2: "+store.find("P2"));
        System.out.println("Malformed P3: "+store.find("P3"));
        System.out.println("Empty line skipped; valid records remain searchable.");
        Files.deleteIfExists(f);
    }
}
