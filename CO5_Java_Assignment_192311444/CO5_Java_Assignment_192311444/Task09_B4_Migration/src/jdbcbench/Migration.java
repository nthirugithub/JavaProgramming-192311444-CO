package jdbcbench;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class Migration {
    public static void main(String[] args) throws Exception {
        Path input=Path.of(args.length>0?args[0]:"passes.csv");
        List<String[]> valid=new ArrayList<>();
        List<String> failures=new ArrayList<>();
        int lineNo=0;
        try(BufferedReader r=Files.newBufferedReader(input)) {
            String line;
            while((line=r.readLine())!=null) {
                lineNo++;
                String[] p=line.split(",",-1);
                try {
                    if(p.length!=5) throw new IllegalArgumentException("expected 5 fields");
                    Integer.parseInt(p[3]);
                    double amount=Double.parseDouble(p[4]);
                    if(!Double.isFinite(amount)||amount<0) throw new IllegalArgumentException("invalid amount");
                    valid.add(p);
                } catch(RuntimeException ex) { failures.add("line "+lineNo+": "+ex.getMessage()); }
            }
        }
        try(Connection c=Db.connect()) {
            int inserted=PassLoader.loadAll(c,valid);
            System.out.println("valid="+valid.size()+", inserted="+inserted+", rejected="+failures.size());
            for(String failure:failures) System.out.println("REJECTED "+failure);
            DatabaseMetaData md=c.getMetaData();
            System.out.println("database_product="+md.getDatabaseProductName()+" "+md.getDatabaseProductVersion());
            System.out.println("driver="+md.getDriverName()+" "+md.getDriverVersion());
            try(ResultSet tables=md.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})) {
                while(tables.next()) System.out.println("table="+tables.getString("TABLE_NAME"));
            }
        }
    }
}
