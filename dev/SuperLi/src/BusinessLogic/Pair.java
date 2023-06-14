package BusinessLogic;

public class Pair<T, U> {
    T first;
    U second;
    public static <T, U> Pair<T, U> create(T first, U second){
        return new Pair<>(first, second);
    }
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    public T getLeft() {
        return first;
    }
    public U getRight() {
        return second;
    }
}