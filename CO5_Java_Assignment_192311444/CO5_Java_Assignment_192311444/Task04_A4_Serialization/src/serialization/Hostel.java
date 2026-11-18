package serialization;
import java.io.Serializable;
import java.util.*;
public class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String hostelName;
    private final List<Room> rooms = new ArrayList<>();
    public Hostel(String name){hostelName=name;}
    public void add(Room room){rooms.add(room);}
    public List<Room> rooms(){return Collections.unmodifiableList(rooms);}
}
