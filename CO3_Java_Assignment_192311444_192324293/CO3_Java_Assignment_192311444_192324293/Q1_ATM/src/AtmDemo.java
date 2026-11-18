import java.io.IOException;
class InvalidAccountException extends Exception { InvalidAccountException(String m){super(m);} }
class InsufficientFundsException extends Exception { InsufficientFundsException(String m){super(m);} }
class CardReaderException extends IOException { CardReaderException(String m){super(m);} }
class Atm {
 private double balance=5000;
 void withdraw(String account,String amount,boolean cardAvailable) {
  try {
   if(!cardAvailable) throw new CardReaderException("Card-reader file is missing");
   long id=Long.parseLong(account.trim()); if(id<=0) throw new InvalidAccountException("Account must be positive");
   double value=Double.parseDouble(amount.trim());
   if(!Double.isFinite(value)||value<=0) throw new IllegalArgumentException("Amount must be positive");
   if(value>balance) throw new InsufficientFundsException("Insufficient balance");
   balance-=value; System.out.printf("Withdrawal successful. Balance: %.2f%n",balance);
  } catch(NumberFormatException e){System.out.println("Input error: numeric values required");}
    catch(InvalidAccountException|InsufficientFundsException|CardReaderException e){System.out.println("ATM failed: "+e.getMessage());}
    catch(IllegalArgumentException e){System.out.println("Invalid amount: "+e.getMessage());}
    finally{System.out.println("ATM session cleanup executed.");}
 }
}
public class AtmDemo { public static void main(String[] a){Atm atm=new Atm(); if(a.length>=2)atm.withdraw(a[0],a[1],a.length<3||Boolean.parseBoolean(a[2])); else atm.withdraw("123456","500",true);} }
