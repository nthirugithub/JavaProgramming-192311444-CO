import java.util.concurrent.*;import java.util.concurrent.locks.*;
class TokenCounter {
 private final int[] b=new int[5];private int count=0,in=0,out=0;
 public synchronized void put(int token)throws InterruptedException{while(count==5)wait();b[in]=token;in=(in+1)%5;count++;notifyAll();}
 public synchronized int take()throws InterruptedException{while(count==0)wait();int x=b[out];out=(out+1)%5;count--;notifyAll();return x;}
 public synchronized int size(){return count;}
}
class Wallet {final int id;int balance;Wallet(int i,int b){id=i;balance=b;}}
class Transfers {
 static void safe(Wallet x,Wallet y,int amount){Wallet a=x.id<y.id?x:y,b=x.id<y.id?y:x;synchronized(a){synchronized(b){x.balance-=amount;y.balance+=amount;}}}
}
public class ConcurrencyDemo {
 public static void main(String[] a)throws Exception{
  TokenCounter c=new TokenCounter();int[] sums=new int[2];
  Thread cook=new Thread(()->{try{for(int i=1;i<=20;i++)c.put(i);}catch(InterruptedException e){Thread.currentThread().interrupt();}});
  Thread[] students=new Thread[2];for(int i=0;i<2;i++){final int id=i;students[i]=new Thread(()->{try{for(int k=0;k<10;k++)sums[id]+=c.take();}catch(InterruptedException e){Thread.currentThread().interrupt();}});}
  cook.start();for(Thread t:students)t.start();cook.join();for(Thread t:students)t.join();
  System.out.println("Sum of tokens collected: "+(sums[0]+sums[1])+" (expected 210)");System.out.println("Counter size: "+c.size());
  Wallet x=new Wallet(1,1000),y=new Wallet(2,1000);Transfers.safe(x,y,100);Transfers.safe(y,x,50);System.out.println("Wallet total: "+(x.balance+y.balance));
  ArrayBlockingQueue<Integer> q=new ArrayBlockingQueue<>(5);q.put(1);System.out.println("Queue item: "+q.take());
 }
}
