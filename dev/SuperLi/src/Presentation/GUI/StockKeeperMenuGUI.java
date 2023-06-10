package SuperLi.src.Presentation.GUI;
import SuperLi.src.BusinessLogic.StockManagementFacade;

import javax.swing.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
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
        optionsNames.add("2. Remove Stock Item");
        optionsNames.add("3. Adding Damage to Item");
        optionsNames.add("4. Move stock item");
        optionsNames.add("5. Create / Update order according to stock.");
        operations.add(this::addStockItem);
        operations.add(this::removeStockItem);
        operations.add(this::updateDamage);
        operations.add(this::moveStockItem);
        operations.add(this::manageOrder);
        showOptionsMenu(optionsNames, operations);
    }
    public void addStockItem(){
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Id"); optionsForField.add(new LinkedList<>());
        labels.add("Barcode"); optionsForField.add(new LinkedList<>());
        labels.add("Cost Price"); optionsForField.add(new LinkedList<>());
        labels.add("Expiration Date (Format: d/M/yy)"); optionsForField.add(new LinkedList<>());
        labels.add("Damage Type"); optionsForField.add((LinkedList<String>) damageTypes.clone());
        labels.add("Location"); optionsForField.add(new LinkedList<>(Arrays.asList("Front", "Back", "Don't care")));
        Function<LinkedList<String>, Boolean> operation = this::addStockItem;
        String success = "Item added successfully";
        String failure = "Item didn't added to the system";
        showFillMenu(labels, optionsForField, operation, success, failure, true, 3);
    }
    public boolean addStockItem(LinkedList<String> values){
        int id = generateInt(values.get(0));
        int barcode = generateInt(values.get(1));
        double costPrice = generateDouble(values.get(2));
        LocalDate expirationDate = generateDate(values.get(3));
        int damageIndex = damageTypes.indexOf(values.get(4));
        if(id == -1 || barcode == -1 || costPrice == -1 || expirationDate == null || damageIndex == -1)
            return false;
        String location = values.get(5);
        System.out.println(damageIndex);
        System.out.println((values.get(4)));
        System.out.println(damageTypes.toString());
        if(location.equals("Don't care"))
            return stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, expirationDate, damageIndex);
        return stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, expirationDate, damageIndex, location.equals("Front"));
    }
    public void removeStockItem(){

    }
    public void updateDamage(){

    }
    public void moveStockItem(){

    }
    public void manageOrder(){

    }
    public void categoryReport(){
        LinkedList<String> labels = new LinkedList<>(Arrays.asList("Prime Category", "Sub Category", "Size Value", "Size Amount"));
        String success = "Showing Report of all the asked categories";
        String failure = "No categories were given";
        Function<LinkedList<LinkedList<String>>, Boolean> operation = this::showCategories;
//        showInfiniteFillMenu(labels, operation, success, failure, false, 3);
    }
    public boolean showCategories(LinkedList<LinkedList<String>> allValues){
        int valueCount = allValues.size();
        if(valueCount == 0)
            return false;
        LinkedList<String> columns = new LinkedList<>(Arrays.asList("Prime Category", "Sub Category", "Size Value", "Size Amount"));
        showTable(columns, allValues, 3);
        return true;
    }
    public static void main(String[] args){
        StockKeeperMenuGUI.getInstance(1).showMainMenu();}
}