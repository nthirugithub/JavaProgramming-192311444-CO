import java.util.*;

public class PharmacyMaps {
    static Map<String,Integer> sample(int n, SupplierLike<Map<String,Integer>> factory){
        Map<String,Integer> map=factory.get();
        for(int i=0;i<n;i++) map.put(String.format("MED%06d",i),i%500);
        return map;
    }
    interface SupplierLike<T>{T get();}
    static long lookupTime(Map<String,Integer> map){
        long sum=0,start=System.nanoTime();
        for(int i=0;i<100_000;i++) sum+=map.get(String.format("MED%06d",i%map.size()));
        long elapsed=System.nanoTime()-start;
        if(sum<0)System.out.println(sum);
        return elapsed;
    }
    public static void main(String[] args){
        System.out.println("count,hash_lookup_100k_ns,tree_lookup_100k_ns,linked_lookup_100k_ns");
        for(int n:new int[]{1_000,10_000,50_000}){
            Map<String,Integer> h=sample(n,HashMap::new), t=sample(n,TreeMap::new), l=sample(n,LinkedHashMap::new);
            System.out.printf("%d,%d,%d,%d%n",n,lookupTime(h),lookupTime(t),lookupTime(l));
            System.out.println("Code order first 3: "+new TreeMap<>(h).keySet().stream().limit(3).toList());
            System.out.println("Insertion order first 3: "+l.keySet().stream().limit(3).toList());
        }
    }
}
