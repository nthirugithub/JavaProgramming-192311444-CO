import java.util.*;

record Team(String name,int points,int goalDiff) {
    @Override public String toString(){return name+"("+points+","+goalDiff+")";}
}
class Leaderboard<T> {
    private final List<T> items=new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order){this.order=Objects.requireNonNull(order);}
    void add(T item){items.add(Objects.requireNonNull(item));}
    List<T> ranking(){
        List<T> copy=new ArrayList<>(items);
        copy.sort(order);
        return List.copyOf(copy);
    }
    void addAll(List<? extends T> incoming){items.addAll(incoming);}
}
public class LeaderboardDemo {
    public static void main(String[] args){
        Comparator<Team> byRank=Comparator.comparingInt(Team::points).reversed()
            .thenComparing(Comparator.comparingInt(Team::goalDiff).reversed())
            .thenComparing(Team::name,String.CASE_INSENSITIVE_ORDER);
        Leaderboard<Team> board=new Leaderboard<>(byRank);
        board.addAll(List.of(new Team("Panthers",9,4),new Team("Eagles",9,6),
            new Team("Cobras",6,1),new Team("Bisons",9,4)));
        System.out.println(board.ranking());
        System.out.println("Bisons precedes Panthers alphabetically after points and goal difference tie.");
    }
}
