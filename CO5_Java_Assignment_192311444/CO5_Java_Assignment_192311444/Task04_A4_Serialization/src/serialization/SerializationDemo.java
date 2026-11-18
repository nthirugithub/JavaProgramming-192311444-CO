package serialization;
import java.io.*;
import java.nio.file.*;
public class SerializationDemo {
    public static void main(String[] args) throws Exception {
        Hostel h = new Hostel("SIMATS Hostel");
        Room r = new Room("A-101");
        r.add(new Resident("Demo Resident","192311001","not-persisted","9000000000"));
        h.add(r);
        Path file = Path.of("hostel-state.bin");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) { out.writeObject(h); }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Hostel loaded = (Hostel) in.readObject();
            System.out.println("Deserialized hostel object graph. Rooms=" + loaded.rooms().size());
            System.out.println("Password and Connection are transient and are not restored.");
        }
        Files.deleteIfExists(file);
    }
}
