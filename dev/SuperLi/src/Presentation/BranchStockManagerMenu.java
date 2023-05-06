package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.StockManagementFacade;

public class BranchStockManagerMenu extends AMenu{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static BranchStockManagerMenu branchStockManagerMenu = null;
    private BranchStockManagerMenu(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
        this.branchId = branchId;
    }
    public static BranchStockManagerMenu getInstance(int branchId){
        if(branchStockManagerMenu == null)
            branchStockManagerMenu = new BranchStockManagerMenu(branchId);
        else
            branchStockManagerMenu.branchId = branchId;
        return branchStockManagerMenu;
    }
    public void printMenu(){
        System.out.println("1. Add Stock Item");
        System.out.println("2. Remove Stock Item.");
        System.out.println("3. Adding Damage to Item.");
        System.out.println("4. Move stock item.");
        System.out.println("0. Actually, I would like to exit menu.");
    }
    public void communicate(){

    }
    public void generateReport() {}
    public void createShortageStockOrder() {}
    public void updatePeriodicOrder() {}
}
