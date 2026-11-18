import java.nio.file.*;
import java.util.*;

public class PassStoreBenchmark {
    public static void main(String[] args) throws Exception {
        int[] sizes={1000,10000,50000,100000};
        System.out.println("records,target_id,elapsed_ns,found");
        for(int n:sizes) {
            Path f=Files.createTempFile("pass-growth",".csv");
            StringBuilder block=new StringBuilder();
            for(int i=0;i<n;i++) block.append("P").append(i).append(",R").append(i)
                    .append(",Student").append(i).append(",").append(i%20).append(",1000.0\n");
            Files.writeString(f,block);
            PassStore store=new PassStore(f);
            String target="P"+(n-1);
            for(int w=0;w<3;w++) store.find(target);
            long total=0;
            for(int r=0;r<7;r++){long t=System.nanoTime();store.find(target);total+=System.nanoTime()-t;}
            System.out.printf(Locale.ROOT,"%d,%s,%d,%s%n",n,target,total/7,store.find(target)!=null);
            Files.deleteIfExists(f);
        }
    }
}
