package Stock.src;

public class Main {
    public static void main(String[] args) {
        UserMenu us = new UserMenu();
        initialize_Categories(us);
        initialize_Items(us);
        us.communicate();// Labohen
    }

    public static void initialize_Categories(UserMenu us){
//    us.branch.add_catalog_item();
    }
    public static void initialize_Items(UserMenu us){
//        us.branch.add_item();
    }
}