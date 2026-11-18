package app.model;
public record Event(int id, String name) {
    @Override public String toString() { return name; }
}
