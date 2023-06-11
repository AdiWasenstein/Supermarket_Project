package SuperLi.src.Presentation.GUI;

import SuperLi.src.BusinessLogic.AdminController;
import SuperLi.src.BusinessLogic.StockManagementFacade;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Function;

public class AdminMenuGUI extends AMenuGUI{
    private static AdminMenuGUI adminMenu = null;
    StockManagementFacade stockManagementFacade;
    AdminController adminController;
    static LinkedList<String> measureUnits = new LinkedList<>(Arrays.asList("UNIT", "ML", "GRAM", "CM"));
    private AdminMenuGUI() {
        stockManagementFacade = StockManagementFacade.getInstance();
        adminController = AdminController.getInstance();
    }
    public static AdminMenuGUI getInstance() {
        if (adminMenu == null)
            adminMenu = new AdminMenuGUI();
        return adminMenu;
    }
    public void showMainMenu() {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Add new branch to system."); operations.add(this::addNewBranchToSystem);
        optionsNames.add("2. Add new catalog item to system."); operations.add(this::addCatalogItem);
        optionsNames.add("3. Add new supplier to system."); operations.add(this::addNewSupplierToSystem);
        optionsNames.add("4. Remove catalog item from the system."); operations.add(this::removeCatalogItem);
        optionsNames.add("5. Change catalog item's details."); operations.add(this::changeCatalogItemDetails);
        optionsNames.add("6. Print report."); operations.add(this::generateReport);
        optionsNames.add("7. Print all suppliers in the system."); operations.add(this::printAllSuppliersInSystem);
        optionsNames.add("8. Print all orders were made."); operations.add(this::printAllOrdersInSystem);
        showOptionsMenu(optionsNames, operations);
    }
    public void showCatalogPage(Function<LinkedList<String>, Boolean> operation, String successMessage, String failureMessage, boolean returnAfterFinish){
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<LinkedList<String>> updateOptions = new LinkedList<>();
        labelNames.add("Catalog ID"); updateOptions.add(new LinkedList<>());
        showFillPage(labelNames, updateOptions, operation, successMessage, failureMessage, returnAfterFinish, 3);
    }
    public void addNewBranchToSystem(){
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Address"); optionsForField.add(new LinkedList<>());
        Function<LinkedList<String>, Boolean> operation = this::addNewBranchToSystem;
        String successMessage = "Branch added successfully";
        String failureMessage = "Cannot add a branch with the requested address to the system";
        showFillPage(labels, optionsForField, operation, successMessage, failureMessage, true, 3);
    }
    public boolean addNewBranchToSystem(LinkedList<String> values){
        String address = values.get(0);
        if(address.equals(""))
            return false;
        return stockManagementFacade.addBranch(address);
    }
    public void addCatalogItem(){
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("ID"); optionsForField.add(new LinkedList<>());
        labels.add("Name"); optionsForField.add(new LinkedList<>());
        labels.add("Manufacturer"); optionsForField.add(new LinkedList<>());
        labels.add("Costumer Price"); optionsForField.add(new LinkedList<>());
        labels.add("Minimum Capacity"); optionsForField.add(new LinkedList<>());
        labels.add("Shelf life (leave blank for default)"); optionsForField.add(new LinkedList<>());
        Function<LinkedList<String>, Boolean> operation = this::addCatalogItem;
        String success = "Got size successfully";
        String failure = "Cannot adding a item with the requested values";
        showFillPage(labels, optionsForField, operation, success, failure, false, 3);
    }
    public boolean addCatalogItem(LinkedList<String> values){
        int id = generateInt(values.get(0));
        String name = values.get(1);
        String manufacturer = values.get(2);
        double price = generateDouble(values.get(3));
        int minCapacity = generateInt(values.get(4));
        int shelfLife = values.get(5).equals("") ? 0 : generateInt(values.get(5));
        if(id == -1 || stockManagementFacade.getCatalogIdName(id) != null || price == -1 || minCapacity == -1 || shelfLife == -1)
            return false;
        Function<LinkedList<LinkedList<String>>, Boolean> categoryOperation = categoriesFilled -> {
            LinkedList<String> categories = new LinkedList<>();
            for(LinkedList<String> categoryLst : categoriesFilled)
                categories.add(categoryLst.get(0));
            if(categories.size() == 0)
                return false;
            LinkedList<String> labels = new LinkedList<>();
            LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
            labels.add("Size"); optionsForField.add(new LinkedList<>());
            labels.add("MeasureUnit"); optionsForField.add(measureUnits);
            Function<LinkedList<String>, Boolean> sizeOperation = sizeValues -> {
                double size = generateDouble(sizeValues.get(0));
                int measureNum = measureUnits.indexOf(sizeValues.get(1));
                if(size == -1 || measureNum == -1)
                    return false;
                if(shelfLife == 0)
                    return stockManagementFacade.addCatalogItem(id, name, manufacturer, price, minCapacity, categories, size, measureNum);
                return stockManagementFacade.addCatalogItem(id, name, manufacturer, price, minCapacity, categories, size, measureNum, shelfLife);
            };String successMessage = "Item added successfully";
            String failureMessage = "Can didn't add to the system";
            showFillPage(labels, optionsForField, sizeOperation, successMessage, failureMessage, true, 3);
            return true;
        };
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Category name"); optionsForField.add(new LinkedList<>());
        String successMessage = "Got categories successfully";
        String failureMessage = "Problem with categories";
        showInfiniteFillPage(labels, optionsForField, categoryOperation, successMessage, failureMessage, false, 3);
        return true;
    }
    public void addNewSupplierToSystem(){

    }
    public void removeCatalogItem(){
        Function<LinkedList<String>, Boolean> operation = this::removeCatalogItem;
        String successMessage = "Item removed successfully";
        String failureMessage = "Cannot find requested item";
        showCatalogPage(operation, successMessage, failureMessage, true);
    }
    public boolean removeCatalogItem(LinkedList<String> values){
        int id = generateInt(values.get(0));
        if(id == -1)
            return false;
        return stockManagementFacade.removeCatalogItem(id);
    }
    public void changeCatalogItemDetails(){

    }
    public void generateReport(){

    }
    public void printAllSuppliersInSystem(){

    }
    public void printAllOrdersInSystem(){

    }
    public static void main(String[] args){
        AdminMenuGUI.getInstance().showMainMenu();
    }
}
