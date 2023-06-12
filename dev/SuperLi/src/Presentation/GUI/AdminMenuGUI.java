package SuperLi.src.Presentation.GUI;

import SuperLi.src.BusinessLogic.AReport;
import SuperLi.src.BusinessLogic.AdminController;
import SuperLi.src.BusinessLogic.StockManagementFacade;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
    public void showBranchPage(Function<LinkedList<String>, Boolean> operation){
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        for(Integer currentBranchId : stockManagementFacade.getBranchesIds()){
            labelNames.add(stockManagementFacade.getBranchAddress(currentBranchId));
            operations.add(() -> operation.apply(new LinkedList<>(List.of(currentBranchId.toString()))));
        }
        showOptionsMenu(labelNames, operations);
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
        String success = "";
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
        if(id == -1 || stockManagementFacade.getCatalogIdName(id) != null || price == -1 || minCapacity == -1 || shelfLife == -1 || name.equals("") || manufacturer.equals(""))
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
            String failureMessage = "Cannot add item to the system";
            showFillPage(labels, optionsForField, sizeOperation, successMessage, failureMessage, true, 3);
            return true;
        };
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Category name"); optionsForField.add(new LinkedList<>());
        String successMessage = "";
        String failureMessage = "Problem with categories";
        showInfiniteFillPage(labels, optionsForField, categoryOperation, successMessage, failureMessage, false, 3);
        return true;
    }
    public void addNewSupplierToSystem(){
        showMessage(false, "", "Needs To Be Completed");
        // TODO - Remove showMessage
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
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Sell price"); operations.add(this::setSellPrice);
        optionsNames.add("2. Minimum capacity required in a branch"); operations.add(this::setMinCapacity);
        optionsNames.add("3. Set costumer discount (For specific item / for category)"); operations.add(this::setCostumerDiscount);
        showOptionsMenu(optionsNames, operations);
    }
    public void setSellPrice(){
        Function<LinkedList<String>, Boolean> operation = this::setSellPrice;
        String successMessage = "";
        String failureMessage = "Invalid catalog ID";
        showCatalogPage(operation, successMessage, failureMessage, false);
    }
    public boolean setSellPrice(LinkedList<String> value){
        int id = generateInt(value.get(0));
        if(stockManagementFacade.getCatalogIdName(id) == null)
            return false;
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        String currentPrice = stockManagementFacade.getCostumerPrice(id);
        labels.add(String.format("Price (Current: %s$)", currentPrice)); optionsForField.add(new LinkedList<>());
        String successMessage = "Price changed successfully";
        String failureMessage = "Cannot change the requested item's price";
        Function<LinkedList<String>, Boolean> operation = priceLst -> {
            double price = generateDouble(priceLst.get(0));
            return price != -1 && stockManagementFacade.setSellPrice(id, price);
        };
        showFillPage(labels, optionsForField, operation, successMessage, failureMessage, true, 3);
        return true;
    }
    public void setMinCapacity(){
        Function<LinkedList<String>, Boolean> operation = this::setMinCapacity;
        String successMessage = "";
        String failureMessage = "Invalid catalog ID";
        showCatalogPage(operation, successMessage, failureMessage, false);
    }
    public boolean setMinCapacity(LinkedList<String> value){
        int id = generateInt(value.get(0));
        if(stockManagementFacade.getCatalogIdName(id) == null)
            return false;
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        String currentCapacity = stockManagementFacade.getCostumerMinCapacity(id);
        labels.add(String.format("Min Capacity (Current: %s)", currentCapacity));
        optionsForField.add(new LinkedList<>());
        String successMessage = "Min Capacity changed successfully";
        String failureMessage = "Cannot change the requested item's min capacity";
        Function<LinkedList<String>, Boolean> operation = capacityLst -> {
            int capacity = generateInt(capacityLst.get(0));
            return capacity != -1 && stockManagementFacade.setMinCapacity(id, capacity);
        };
        showFillPage(labels, optionsForField, operation, successMessage, failureMessage, true, 3);
        return true;
    }
    public void setCostumerDiscount(){
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Discount Type"); optionsForField.add(new LinkedList<>(Arrays.asList("Amount", "Percentage")));
        labels.add("Value"); optionsForField.add(new LinkedList<>());
        labels.add("Expiration Date (Format: d/M/yy)"); optionsForField.add(new LinkedList<>());
        labels.add("Minimum Amount"); optionsForField.add(new LinkedList<>());
        Function<LinkedList<String>, Boolean> operation = this::setCostumerDiscount;
        String successMessage = "";
        String failureMessage = "Invalid discount detail";
        showFillPage(labels, optionsForField, operation, successMessage, failureMessage, false, 3);
    }
    public Boolean setCostumerDiscount(LinkedList<String> values){
        String typeStr = values.get(0);
        if(typeStr.equals(""))
            return false;
        boolean isPercentage = typeStr.equals("Percentage");
        double value = generateDouble(values.get(1));
        LocalDate expirationDate = generateDate(values.get(2));
        int minCapacity = generateInt(values.get(3));
        if(value == -1 || minCapacity == -1 || expirationDate == null)
            return false;
        discountScopePage(isPercentage, value, expirationDate, minCapacity);
        return true;
    }
    public void discountScopePage(boolean isPercentage, double value, LocalDate expirationDate, int minCapacity){
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Catalog Item Discount"); operations.add(setCatalogItemDiscount(isPercentage, value, expirationDate, minCapacity));
        optionsNames.add("2. Category Discount"); operations.add(setCategoryDiscount(isPercentage, value, expirationDate, minCapacity));
        showOptionsMenu(optionsNames, operations);
    }
    public Runnable setCatalogItemDiscount(boolean isPercentage, double value, LocalDate expirationDate, int minCapacity){
        return () -> {
            Function<LinkedList<String>, Boolean> operation = values ->{
                int id = generateInt(values.get(0));
                return stockManagementFacade.setCatalogItemCostumerDiscount(id, expirationDate, value, isPercentage, minCapacity);
            };
            String successMessage = "Discount applied successfully";
            String failureMessage = "Invalid catalog ID";
            showCatalogPage(operation, successMessage, failureMessage, true);
        };
    }
    public Runnable setCategoryDiscount(boolean isPercentage, double value, LocalDate expirationDate, int minCapacity){
        return () -> {
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
                    stockManagementFacade.setCategoryDiscount(categories, size, measureNum, expirationDate, value, isPercentage, minCapacity);
                    return true;
                };
                String successMessage = "The requested discount was applied to all matching items";
                String failureMessage = "Cannot perform the discount with the inserted values";
                showFillPage(labels, optionsForField, sizeOperation, successMessage, failureMessage, true, 3);
                return true;
            };
            LinkedList<String> labels = new LinkedList<>();
            LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
            labels.add("Category Name"); optionsForField.add(new LinkedList<>());
            String successMessage = "";
            String failureMessage = "Item required to have at least one category";
            showInfiniteFillPage(labels, optionsForField, categoryOperation, successMessage, failureMessage, false, 3);
        };
    }
    public void generateReport(){
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Category Report"); operations.add(this::generateCategoryReport);
        optionsNames.add("2. Catalog Report"); operations.add(this::generateCatalogReport);
        optionsNames.add("3. Specific Branch Reports"); operations.add(this::specificBranchReport);
        showOptionsMenu(optionsNames, operations);
    }
    public void generateCategoryReport(){
        LinkedList<String> choicesLabels = new LinkedList<>(); LinkedList<Runnable> choicesOperations = new LinkedList<>();
        LinkedList<LinkedList<String>> allCategories = new LinkedList<>();
        LinkedList<String> insertCategoryLabel = new LinkedList<>(); LinkedList<LinkedList<String>> insertCategoryOptions = new LinkedList<>();
        insertCategoryLabel.add("Category Name (Leave blank and submit to end)"); insertCategoryOptions.add(new LinkedList<>());
        Function<LinkedList<LinkedList<String>>, Boolean> submitCategoryOperation =
                currentCategoryLst -> {
                    LinkedList<String> currentCategory = new LinkedList<>();
                    for (LinkedList<String> category : currentCategoryLst)
                        currentCategory.add(category.get(0));
                    allCategories.add(currentCategory);
                    showOptionsMenu(choicesLabels, choicesOperations);
                    return true;
                };
        choicesLabels.add("Continue adding categories");
        choicesOperations.add(() -> showInfiniteFillPage(insertCategoryLabel, insertCategoryOptions, submitCategoryOperation, "", "", false, 3));
        choicesLabels.add("Show me the resulted report");
        choicesOperations.add(() -> showTable(stockManagementFacade.generateCategoryReport(allCategories)));
        showInfiniteFillPage(insertCategoryLabel, insertCategoryOptions, submitCategoryOperation, "", "", false, 3);
    }
    public void generateCatalogReport(){
        showTable(stockManagementFacade.generateAllCatalogReport());
    }
    public void specificBranchReport(){
        Function<LinkedList<String>, Boolean> operation = values -> {
            LinkedList<String> reportsNames = new LinkedList<>();
            int branchId = generateInt(values.get(0));
            LinkedList<AReport> branchReports = new LinkedList<>();
            reportsNames.add("Damaged Report"); branchReports.add(stockManagementFacade.generateDamagedStockReport(branchId));
            reportsNames.add("Required Stock Report"); branchReports.add(stockManagementFacade.generateRequiredStockReport(branchId));
            reportsNames.add("All stock report"); branchReports.add(stockManagementFacade.generateStockItemsReport(branchId));
            Function<Integer, Boolean> submitOperation = chosenReport -> true;
            reportSelector(reportsNames, branchReports, submitOperation, "", "");
            return true;
        };
        showBranchPage(operation);
    }
    public void printAllSuppliersInSystem(){
        showMessage(false, "", "Needs To Be Completed");
        // TODO - Remove showMessage
    }
    public void printAllOrdersInSystem(){
        showMessage(false, "", "Needs To Be Completed");
        // TODO - Remove showMessage
    }
    public static void main(String[] args){
        AdminMenuGUI.getInstance().showMainMenu();
    }
}
