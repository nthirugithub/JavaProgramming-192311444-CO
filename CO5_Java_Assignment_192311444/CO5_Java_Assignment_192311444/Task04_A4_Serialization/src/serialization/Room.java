package serialization;
import java.io.Serializable;
import java.util.*;
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String roomNumber;
    private final List<Resident> residents = new ArrayList<>();
    public Room(String roomNumber){this.roomNumber=roomNumber;}
    public void add(Resident r){residents.add(r);}
    public List<Resident> residents(){return Collections.unmodifiableList(residents);}
}
