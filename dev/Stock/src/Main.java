package Stock.src;

public class Main {
    public static void main(String[] args) {
        UserMenu us = new UserMenu();
        initialize_Categories(us);
        initialize_Items(us);
        us.communicate();// Labohen
    }
}