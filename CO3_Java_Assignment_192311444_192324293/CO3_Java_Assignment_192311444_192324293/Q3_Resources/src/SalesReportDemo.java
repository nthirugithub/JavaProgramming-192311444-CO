import java.io.*;import java.nio.file.*;import java.util.*;
public class SalesReportDemo {
 static void manual(List<Path> inputs,Path output)throws IOException{
  BufferedWriter w=Files.newBufferedWriter(output);
  try{for(Path p:inputs){BufferedReader r=Files.newBufferedReader(p);String line;while((line=r.readLine())!=null)w.write(line+System.lineSeparator());r.close();}}
  finally{w.close();}
 }
 static void safe(List<Path> inputs,Path output)throws IOException{
  try(BufferedWriter w=Files.newBufferedWriter(output)){for(Path p:inputs)try(BufferedReader r=Files.newBufferedReader(p)){String line;while((line=r.readLine())!=null)w.write(line+System.lineSeparator());}}
 }
 public static void main(String[] a)throws Exception{
  Path d=Files.createTempDirectory("sales");Path good=d.resolve("store.txt"),missing=d.resolve("missing.txt");
  Files.writeString(good,"Store A,100\\nStore A,200\\n");List<Path> in=List.of(good,missing);
  try{manual(in,d.resolve("manual.txt"));}catch(IOException e){System.out.println("Manual failed: "+e.getMessage());}
  try{safe(in,d.resolve("safe.txt"));}catch(IOException e){System.out.println("TWR failed: "+e.getMessage());}
 }
}
