package SuperLi.src;

public class Pair<FirstType, SecondType>{
    private FirstType first;
    private SecondType second;
    public Pair(FirstType first, SecondType second){
        first = first;
        second = second;
    }
    public FirstType getFirst() {return first;}
    public SecondType getSecond() {return second;}
}
