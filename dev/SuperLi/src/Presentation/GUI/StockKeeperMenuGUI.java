package SuperLi.src.Presentation.GUI;
import SuperLi.src.BusinessLogic.AReport;
import SuperLi.src.BusinessLogic.OrderController;
import SuperLi.src.BusinessLogic.PeriodicReport;
import SuperLi.src.BusinessLogic.StockManagementFacade;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Function;

public class StockKeeperMenuGUI extends AMenuGUI{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static StockKeeperMenuGUI stockKeeperMenu = null;
    static LinkedList<String> damageTypes = new LinkedList<>(Arrays.asList("COVER", "PHYSICAL", "ELECTRICAL", "ROTTEN", "NONE"));
    private StockKeeperMenuGUI(){
        stockManagementFacade = StockManagementFacade.getInstance();
        showChooseBranchPage();
    }
    public static StockKeeperMenuGUI getInstance(){
        if(stockKeeperMenu == null)
            stockKeeperMenu = new StockKeeperMenuGUI();
        return stockKeeperMenu;
    }
    public void showChooseBranchPage(){
        JPanel panel = new JPanel(); panel.setBackground(backgroundColor);
        TitledBorder title = BorderFactory.createTitledBorder("Select Branch");
        title.setTitleJustification(TitledBorder.CENTER);
        panel.setBorder(title);
        for(Integer currentBranchId : stockManagementFacade.getBranchesIds()){
            String currentBranchAddress = stockManagementFacade.getBranchAddress(currentBranchId);
            JButton currentBranchButton = new JButton(currentBranchAddress);
            currentBranchButton.addActionListener(e -> {
                branchId = currentBranchId;
                String successMessage = String.format("Welcome to %s branch", currentBranchAddress);
                showMessage(true, successMessage, "");
                showMainMenu();
            });
            panel.add(currentBranchButton);
        }
        jFrame.getContentPane().add(panel);
        jFrame.revalidate();
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
    public void showBarcodePage(Function<LinkedList<String>, Boolean> operation, String successMessage, String failureMessage, boolean returnAfterFinish){
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<LinkedList<String>> updateOptions = new LinkedList<>();
        labelNames.add("Barcode"); updateOptions.add(new LinkedList<>());
        showFillPage(labelNames, updateOptions, operation, successMessage, failureMessage, returnAfterFinish, 3);
    }
    public void addStockItem(){
        LinkedList<String> labels = new LinkedList<>();
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        labels.add("Id"); optionsForField.add(new LinkedList<>());
        labels.add("Barcode"); optionsForField.add(new LinkedList<>());
        labels.add("Cost Price"); optionsForField.add(new LinkedList<>());
        labels.add("Expiration Date (Format: d/M/yy)"); optionsForField.add(new LinkedList<>());
        labels.add("Damage Type"); optionsForField.add(damageTypes);
        labels.add("Location"); optionsForField.add(new LinkedList<>(Arrays.asList("Front", "Back", "Don't care")));
        Function<LinkedList<String>, Boolean> operation = this::addStockItem;
        String success = "Item added successfully";
        String failure = "Item didn't add to the system";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);
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
        if(location.equals("Don't care"))
            return stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, expirationDate, damageIndex);
        return stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, expirationDate, damageIndex, location.equals("Front"));
    }
    public void removeStockItem(){
        Function<LinkedList<String>, Boolean> operation = this::removeStockItem;
        String successMessage = "Item removed successfully";
        String failureMessage = "Cannot find requested item";
        showBarcodePage(operation, successMessage, failureMessage, true);
    }
    public boolean removeStockItem(LinkedList<String> values){
        int barcode = generateInt(values.get(0));
        if(barcode == -1)
            return false;
        int catalogId = stockManagementFacade.barcodeToId(barcode, branchId);
        if(!stockManagementFacade.removeStockItem(barcode, branchId))
            return false;
        String successMessage = "Item removed successfully";
        showMessage(true, successMessage, "");
        String warningMessage = "";
        if(stockManagementFacade.isRequired(catalogId, branchId))
            warningMessage += "The Item Crossed The Minimum Gap, Consider Ordering More Units Immediately. ";
        if(stockManagementFacade.needsShelvesIncrement(catalogId, branchId))
            warningMessage += String.format("\nConsider filling the shelves with %s (ID %d).", stockManagementFacade.getCatalogIdName(catalogId), catalogId);
        if(!warningMessage.equals(""))
            showMessage(false, "", warningMessage);
        return true;
    }
    public void updateDamage(){
        Function<LinkedList<String>, Boolean> operation = this::updateDamage;
        String successMessage = "Requested item found";
        String failureMessage = "Cannot find requested item";
        showBarcodePage(operation, successMessage, failureMessage, false);
    }
    public boolean updateDamage(LinkedList<String> values){
        int barcode = generateInt(values.get(0));
        String currentDamage = stockManagementFacade.getStockItemDamage(barcode, branchId);
        if(barcode == -1 || currentDamage.equals(""))
            return false;
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<LinkedList<String>> updateOptions = new LinkedList<>();
        labelNames.add(String.format("Damage Type (Current: %s)", currentDamage)); updateOptions.add(damageTypes);
        String successMessage = "Item's damage updated successfully";
        String failureMessage = "Couldn't change item's damage";
        Function<LinkedList<String>, Boolean> operation = damageValue -> {
            int damageIndex = damageTypes.indexOf(damageValue.get(0));
            if (damageIndex == -1)
                return false;
            return stockManagementFacade.setDamage(barcode, damageIndex, branchId);
        };
        showFillPage(labelNames, updateOptions, operation, successMessage, failureMessage, true, 3);
        return true;
    }
    public void moveStockItem(){
        Function<LinkedList<String>, Boolean> operation = this::moveStockItem;
        String successMessage = "Item's location changed successfully";
        String failureMessage = "Cannot find requested item";
        showBarcodePage(operation, successMessage, failureMessage, true);
    }
    public boolean moveStockItem(LinkedList<String> values){
        int barcode = generateInt(values.get(0));
        if (barcode == -1)
            return false;
        String changeLocation = "Changing item's location from " + stockManagementFacade.getLocationOfBarcode(barcode, branchId);
        boolean status = stockManagementFacade.moveStockItem(barcode, branchId);
        changeLocation += " to " + stockManagementFacade.getLocationOfBarcode(barcode, branchId);
        if(status)
            showMessage(true, changeLocation, "");
        return status;
    }
    public void manageOrder(){
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Create Shortage Order");
        optionsNames.add("2. Update Periodic Report");
        operations.add(this::createShortageStockOrder);
        operations.add(this::updatePeriodicOrder);
        showOptionsMenu(optionsNames, operations);
    }
    public void createShortageStockOrder(){
        String message = "Required stock ordered sent to the suppliers successfully";
        if (!stockManagementFacade.createShortageOrder(branchId))
            message = "No items required for the branch";
        showMessage(true, message, "");
        showMainMenu();
    }
    public void updatePeriodicOrder() {
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<PeriodicReport> reportsOfBranch = OrderController.getInstance().findReportsOfBranch(branchId);
        LinkedList<AReport> reportsToSend = new LinkedList<>();
        for (PeriodicReport report : reportsOfBranch) {
            reportsToSend.add(report);
            labelNames.add("Report " + report.getReportId());
        }
        String successMessage = "Report was updated successfully";
        String failureMessage = "Cannot update the requested reportId";
        Function<Integer, Boolean> submitOperation = reportIndex -> {
            int reportId = reportsOfBranch.get(reportIndex).getReportId();
            try {
                return stockManagementFacade.updatePeriodReport(reportId, branchId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
        reportSelector(labelNames, reportsToSend, submitOperation, successMessage, failureMessage);
    }
    public static void main(String[] args){
        StockKeeperMenuGUI.getInstance();}
}