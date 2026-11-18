import java.io.*;
import java.nio.file.*;
import java.util.Locale;

public class LineCounter {
    public static long countByteWise(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(path.toFile())) {
            int value;
            while ((value = in.read()) != -1) if (value == '\n') lines++;
        }
        return lines;
    }

    public static long countBuffered(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path), 64 * 1024)) {
            byte[] buffer = new byte[64 * 1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) if (buffer[i] == '\n') lines++;
            }
        }
        return lines;
    }

    private static void measure(Path path, String label, IoTask task) throws IOException {
        long start = System.nanoTime();
        long lines = task.run(path);
        long elapsed = System.nanoTime() - start;
        System.out.printf(Locale.ROOT, "%s,%d,%d%n", label, Files.size(path), elapsed);
        if (lines < 0) throw new IllegalStateException("Invalid line count");
    }

    @FunctionalInterface interface IoTask { long run(Path path) throws IOException; }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java LineCounter <file>");
            return;
        }
        Path path = Path.of(args[0]);
        System.out.println("method,file_bytes,elapsed_ns");
        measure(path, "bytewise", LineCounter::countByteWise);
        measure(path, "buffered", LineCounter::countBuffered);
        System.out.println("line_count=" + countBuffered(path));
    }
}
