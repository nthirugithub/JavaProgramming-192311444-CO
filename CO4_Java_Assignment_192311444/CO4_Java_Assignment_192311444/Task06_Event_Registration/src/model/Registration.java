package app.model;
import java.time.LocalDateTime;
public record Registration(long id, String studentRegNo, String studentName, int eventId,
                           String eventName, String email, LocalDateTime registeredAt) {}
