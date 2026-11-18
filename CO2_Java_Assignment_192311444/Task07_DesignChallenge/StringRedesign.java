public class StringRedesign {
    private static volatile int sink;
    static String original(int fields){
        String s="";
        for(int i=0;i<fields;i++)s=s+"FIELD-"+i+"|";
        return s;
    }
    static String redesigned(int fields){
        StringBuilder s=new StringBuilder(fields*10);
        for(int i=0;i<fields;i++)s.append("FIELD-").append(i).append('|');
        return s.toString();
    }
    static long measure(boolean useBuilder,int fields,int rounds){
        for(int i=0;i<2;i++) sink=(useBuilder?redesigned(fields):original(fields)).length();
        long start=System.nanoTime();
        for(int i=0;i<rounds;i++)sink=(useBuilder?redesigned(fields):original(fields)).length();
        return System.nanoTime()-start;
    }
    public static void main(String[] args){
        System.out.println("fields,rounds,original_concat_ns,redesigned_builder_ns");
        for(int fields:new int[]{10,50,100,250}){
            int rounds=Math.max(20,5000/fields);
            long a=measure(false,fields,rounds),b=measure(true,fields,rounds);
            System.out.printf("%d,%d,%d,%d%n",fields,rounds,a,b);
        }
    }
}
