import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PassStore {
    private final Path file;
    PassStore(Path file) { this.file = file; }

    public void add(String passId, String regNo, String name, int route, double amount) throws IOException {
        if (passId.contains(",") || regNo.contains(",") || name.contains(","))
            throw new IllegalArgumentException("This five-column exercise format does not support commas in fields.");
        if (!Double.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Invalid amount");
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", passId, regNo, name, String.valueOf(route), String.valueOf(amount)));
            w.newLine();
        }
    }

    /** Returns the five fields for passId, or null if no valid matching row exists. */
    public String[] find(String passId) throws IOException {
        if (!Files.exists(file)) return null;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length != 5) continue;
                try {
                    Integer.parseInt(fields[3]);
                    double amount = Double.parseDouble(fields[4]);
                    if (!Double.isFinite(amount) || amount < 0) continue;
                } catch (NumberFormatException ex) { continue; }
                if (fields[0].equals(passId)) return fields;
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> result = new ArrayList<>();
        if (!Files.exists(file)) return result;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line=reader.readLine())!=null) {
                String[] f=line.split(",",-1);
                if(f.length!=5) continue;
                try {
                    int r=Integer.parseInt(f[3]); double a=Double.parseDouble(f[4]);
                    if(r==route && Double.isFinite(a) && a>=0) result.add(f);
                } catch(NumberFormatException ignored) { }
            }
        }
        return result;
    }
}
