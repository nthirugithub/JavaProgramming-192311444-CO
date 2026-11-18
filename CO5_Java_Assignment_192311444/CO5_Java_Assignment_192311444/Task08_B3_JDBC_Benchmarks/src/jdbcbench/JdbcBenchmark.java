package jdbcbench;
import java.sql.*;
import java.util.*;

public class JdbcBenchmark {
    private static List<String[]> generate(int n) {
        List<String[]> rows=new ArrayList<>(n);
        for(int i=0;i<n;i++) rows.add(new String[]{"P"+i,"R"+i,"Student"+i,
                String.valueOf(i%30),String.valueOf(1000.0+(i%500))});
        return rows;
    }
    private static void clear(Connection c) throws SQLException {
        try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}
    }
    private static long statementInsert(Connection c,List<String[]> rows) throws SQLException {
        clear(c); boolean old=c.getAutoCommit(); c.setAutoCommit(false);
        long t=System.nanoTime();
        try(Statement st=c.createStatement()) {
            for(String[] p:rows) {
                String sql="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES('"+
                    p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")";
                st.executeUpdate(sql);
            }
            c.commit();
        } catch(SQLException ex){c.rollback();throw ex;} finally{c.setAutoCommit(old);}
        return System.nanoTime()-t;
    }
    private static long preparedInsert(Connection c,List<String[]> rows) throws SQLException {
        clear(c); boolean old=c.getAutoCommit(); c.setAutoCommit(false);
        long t=System.nanoTime();
        try(PreparedStatement ps=c.prepareStatement(
            "INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)")) {
            for(String[] p:rows) {
                ps.setString(1,p[0]);ps.setString(2,p[1]);ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3]));ps.setDouble(5,Double.parseDouble(p[4]));ps.executeUpdate();
            }
            c.commit();
        } catch(SQLException ex){c.rollback();throw ex;} finally{c.setAutoCommit(old);}
        return System.nanoTime()-t;
    }
    private static long batchInsert(Connection c,List<String[]> rows) throws SQLException {
        clear(c); boolean old=c.getAutoCommit(); c.setAutoCommit(false);
        long t=System.nanoTime();
        try(PreparedStatement ps=c.prepareStatement(
            "INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)")) {
            int count=0;
            for(String[] p:rows) {
                ps.setString(1,p[0]);ps.setString(2,p[1]);ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3]));ps.setDouble(5,Double.parseDouble(p[4]));
                ps.addBatch(); if(++count%1000==0) ps.executeBatch();
            }
            if(count%1000!=0) ps.executeBatch();
            c.commit();
        } catch(SQLException ex){c.rollback();throw ex;} finally{c.setAutoCommit(old);}
        return System.nanoTime()-t;
    }
    public static void main(String[] args) throws Exception {
        try(Connection c=Db.connect()) {
            System.out.println("method,rows,elapsed_ns");
            for(int n:new int[]{10,1000,10000,100000}) {
                List<String[]> rows=generate(n);
                System.out.printf("Statement,%d,%d%n",n,statementInsert(c,rows));
                System.out.printf("PreparedStatement,%d,%d%n",n,preparedInsert(c,rows));
                System.out.printf("Batch1000,%d,%d%n",n,batchInsert(c,rows));
            }
            try(Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT * FROM bus_pass LIMIT 10")) {
                ResultSetMetaData md=rs.getMetaData();
                for(int i=1;i<=md.getColumnCount();i++)
                    System.out.printf("column=%s,type=%s%n",md.getColumnName(i),md.getColumnTypeName(i));
            }
        }
    }
}
