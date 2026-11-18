import java.util.*;import java.util.concurrent.*;
class StationThread extends Thread {private final int n;StationThread(String name,int n){super(name);this.n=n;}public void run(){try{Thread.sleep(20+n);}catch(InterruptedException e){interrupt();return;}System.out.println(getName()+" scanned "+n);}}
class StationTask implements Runnable {private final String name;private final int n;StationTask(String name,int n){this.name=name;this.n=n;}public void run(){try{Thread.sleep(20+n);}catch(InterruptedException e){Thread.currentThread().interrupt();return;}System.out.println(name+" scanned "+n);}}
public class ScannerStations {
 public static void main(String[] a)throws Exception{
  Thread[] t={new StationThread("Station-1",4),new Thread(new StationTask("Station-2",3)),new Thread(new StationTask("Station-3",2)),new Thread(new StationTask("Station-4",1))};
  for(Thread x:t)x.start();for(Thread x:t)System.out.println(x.getName()+" alive="+x.isAlive());for(Thread x:t)x.join();System.out.println("All stations completed.");
  ExecutorService pool=Executors.newFixedThreadPool(2);try{List<Future<Integer>> fs=new ArrayList<>();for(int i=1;i<=4;i++){final int n=i;fs.add(pool.submit(()->n));}int sum=0;for(Future<Integer> f:fs)sum+=f.get();System.out.println("Executor total="+sum);}finally{pool.shutdown();}
 }
}
