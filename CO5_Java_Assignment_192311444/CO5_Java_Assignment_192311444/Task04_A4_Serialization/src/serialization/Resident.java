package serialization;
import java.io.Serializable;
public class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String registerNumber;
    private transient String password;
    private transient java.sql.Connection connection;
    private String phoneNumber;
    public Resident(String name, String registerNumber, String password, String phoneNumber) {
        this.name=name; this.registerNumber=registerNumber; this.password=password; this.phoneNumber=phoneNumber;
    }
    public String getName(){return name;}
    public String getRegisterNumber(){return registerNumber;}
    public String getPhoneNumber(){return phoneNumber;}
    public boolean hasPasswordInMemory(){return password != null;}
}
