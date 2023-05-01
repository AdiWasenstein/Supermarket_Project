package SuperLi.src.Presentation;

public class AdminMenu extends AMenu{
    static AdminMenu adminMenu = null;
    private AdminMenu(){}
    public static AdminMenu getInstance(){
        if(adminMenu == null)
            adminMenu = new AdminMenu();
        return adminMenu;
    }
    public void addBranch(){}
    public void removeBranch(){}
    public void addCatalogItem(){}
    public void removeCatalogItem(){}
    public void changeCostumerPrice(){}
    public void changeMinCapacity(){}
    public void setCostumerDiscount(){}

}
