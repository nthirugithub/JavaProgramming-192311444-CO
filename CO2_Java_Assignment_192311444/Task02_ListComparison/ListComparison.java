import java.util.*;

public class ListComparison {
    static volatile long sink;
    static long time(Runnable task) {
        long start = System.nanoTime(); task.run(); return System.nanoTime() - start;
    }
    static void test(int n) {
        ArrayList<Integer> array = new ArrayList<>(n);
        LinkedList<Integer> linked = new LinkedList<>();
        for (int i=0;i<n;i++){ array.add(i); linked.add(i); }
        int repeats = Math.max(100, 1_000_000 / n);
        long arrayGet = time(() -> { long s=0; for(int i=0;i<repeats;i++) s+=array.get((i*31)%n); sink=s; });
        long linkedGet = time(() -> { long s=0; for(int i=0;i<repeats;i++) s+=linked.get((i*31)%n); sink=s; });
        long arrayIter = time(() -> { long s=0; for(int x:array)s+=x; sink=s; });
        long linkedIter = time(() -> { long s=0; for(int x:linked)s+=x; sink=s; });
        long arrayFront = time(() -> { ArrayList<Integer> x=new ArrayList<>(array); for(int i=0;i<Math.min(1000,n);i++) x.add(0,-i); });
        long linkedFront = time(() -> { LinkedList<Integer> x=new LinkedList<>(linked); for(int i=0;i<Math.min(1000,n);i++) x.addFirst(-i); });
        System.out.printf(Locale.ROOT,"%d,%d,%d,%d,%d,%d,%d%n",n,arrayGet,linkedGet,arrayIter,linkedIter,arrayFront,linkedFront);
    }
    public static void main(String[] args) {
        System.out.println("size,array_get_ns,linked_get_ns,array_iter_ns,linked_iter_ns,array_front_batch_ns,linked_front_batch_ns");
        for(int n:new int[]{1_000,10_000,50_000}) test(n);
        ArrayDeque<Integer> upNext=new ArrayDeque<>();
        upNext.addFirst(2); upNext.addLast(3); upNext.addFirst(1);
        System.out.println("Up Next dequeue order: "+upNext.removeFirst()+", "+upNext.removeFirst()+", "+upNext.removeFirst());
    }
}
