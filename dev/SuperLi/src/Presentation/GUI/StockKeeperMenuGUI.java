package SuperLi.src.Presentation.GUI;
import SuperLi.src.BusinessLogic.StockManagementFacade;

import java.util.LinkedList;
import java.util.function.Function;

public class StockKeeperMenuGUI extends AMenuGUI{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static StockKeeperMenuGUI stockKeeperMenu = null;
    private StockKeeperMenuGUI(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
        this.branchId = branchId;
    }
    public static StockKeeperMenuGUI getInstance(int branchId){
        if(stockKeeperMenu == null)
            stockKeeperMenu = new StockKeeperMenuGUI(branchId);
        else
            stockKeeperMenu.branchId = branchId;
        return stockKeeperMenu;
    }
    public void showMainMenu(){
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Add Stock Item");
        operations.add(this::addStockItem);
        showOptionsMenu(optionsNames, operations);
    }
    public void addStockItem(){
        LinkedList<String> labels = new LinkedList<>();
        labels.add("Name");
        labels.add("Price");
        Function<LinkedList<String>, Boolean> operation = values -> {
//            return values.size() > 0;
            removeStockItem();
            return false;
        };
        String success = "Going to remove";
        String failure = "Failed to add item";
        showFillMenu(labels, operation, success, failure, false);
    }
    public void removeStockItem(){
        LinkedList<String> labels = new LinkedList<>();
        labels.add("Remove Name");
        labels.add("Remove Price");
        Function<LinkedList<String>, Boolean> operation = values -> values.size() > 0;
        String success = "Item removed successfully";
        String failure = "Failed to remove item";
        showFillMenu(labels, operation, success, failure, true);
    }
    public static void main(String[] args){
        StockKeeperMenuGUI.getInstance(1).showMainMenu();
    }
}