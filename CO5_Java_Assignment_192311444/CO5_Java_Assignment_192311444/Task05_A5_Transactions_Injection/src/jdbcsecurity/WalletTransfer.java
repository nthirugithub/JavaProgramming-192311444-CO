package jdbcsecurity;
import java.math.BigDecimal;
import java.sql.*;

public class WalletTransfer {
    public static void transfer(Connection con, long fromId, long toId, BigDecimal amount) throws SQLException {
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        boolean old = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            try (PreparedStatement debit = con.prepareStatement(
                    "UPDATE wallet SET balance=balance-? WHERE wallet_id=? AND balance>=?");
                 PreparedStatement credit = con.prepareStatement(
                    "UPDATE wallet SET balance=balance+? WHERE wallet_id=?")) {
                debit.setBigDecimal(1,amount); debit.setLong(2,fromId); debit.setBigDecimal(3,amount);
                if (debit.executeUpdate()!=1) throw new SQLException("Source wallet missing or insufficient funds");
                credit.setBigDecimal(1,amount); credit.setLong(2,toId);
                if (credit.executeUpdate()!=1) throw new SQLException("Destination wallet missing");
            }
            con.commit();
        } catch (SQLException | RuntimeException ex) {
            con.rollback();
            throw ex;
        } finally { con.setAutoCommit(old); }
    }
}
