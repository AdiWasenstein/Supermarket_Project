package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.StockManagementFacade;

public class StockKeeperMenu extends AMenu{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static StockKeeperMenu stockKeeperMenu = null;
    private StockKeeperMenu(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
        this.branchId = branchId;
    }
    public static StockKeeperMenu getInstance(int branchId){
        if(stockKeeperMenu == null)
            stockKeeperMenu = new StockKeeperMenu(branchId);
        else
            stockKeeperMenu.branchId = branchId;
        return stockKeeperMenu;
    }

    public void addStockItem() {}
    public void removeStockItem() {}
    public void changeLocation() {}
    public void updateDamage() {}
}
