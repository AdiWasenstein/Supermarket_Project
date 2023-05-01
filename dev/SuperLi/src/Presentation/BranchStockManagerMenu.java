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

    public void generateReport() {}
    public void createShortageStockOrder() {}
    public void updatePeriodicOrder() {}
}
