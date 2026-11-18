import java.nio.file.*;import java.util.*;import java.util.concurrent.*;
record Outcome(String file,boolean ok,int score,String reason){}
public class ResultProcessor {
 static Outcome process(Path p){try{int n=Integer.parseInt(Files.readString(p).trim());if(n<0||n>100)throw new IllegalArgumentException("score out of range");return new Outcome(p.getFileName().toString(),true,n,"OK");}
 catch(NoSuchFileException e){return new Outcome(p.toString(),false,0,"MISSING");}
 catch(Exception e){return new Outcome(p.toString(),false,0,e.getClass().getSimpleName());}}
 static void run(List<Path> files,int size)throws Exception{
  long start=System.nanoTime();ExecutorService pool=Executors.newFixedThreadPool(size);ConcurrentLinkedQueue<Outcome> results=new ConcurrentLinkedQueue<>();
  try{List<Future<Outcome>> fs=new ArrayList<>();for(Path p:files)fs.add(pool.submit(()->process(p)));for(Future<Outcome> f:fs)results.add(f.get());}
  finally{pool.shutdown();pool.awaitTermination(1,TimeUnit.MINUTES);}
  long ok=results.stream().filter(Outcome::ok).count();int total=results.stream().filter(Outcome::ok).mapToInt(Outcome::score).sum();
  System.out.printf("pool=%d,total=%d,success=%d,failed=%d,scoreSum=%d,elapsed_ns=%d%n",size,results.size(),ok,results.size()-ok,total,System.nanoTime()-start);
 }
 public static void main(String[] a)throws Exception{
  Path d=Files.createTempDirectory("scripts");List<Path> fs=new ArrayList<>();
  for(int i=0;i<10000;i++){Path p=d.resolve("s"+i+".txt");fs.add(p);if(i==4)Files.writeString(p,"CORRUPT");else if(i!=8)Files.writeString(p,String.valueOf(i%101));}
  for(int p:new int[]{1,2,4,8})run(fs,p);
 }
}
