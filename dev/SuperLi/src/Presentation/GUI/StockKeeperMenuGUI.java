package SuperLi.src.Presentation.GUI;
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
        String failure = "Item didn't added to the system";
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
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<LinkedList<String>> closeOptions = new LinkedList<>();
        labelNames.add("Barcode"); closeOptions.add(new LinkedList<>());
        Function<LinkedList<String>, Boolean> operation = this::removeStockItem;
        String successMessage = "Item removed successfully";
        String failureMessage = "Cannot remove requested stock item";
        showFillPage(labelNames, closeOptions, operation, successMessage, failureMessage, true, 3);
    }
    public boolean removeStockItem(LinkedList<String> values){
        int barcode = generateInt(values.get(0));
        if(barcode == -1)
            return false;
        int catalogId = stockManagementFacade.barcodeToId(barcode, branchId);
        if(!stockManagementFacade.removeStockItem(barcode, branchId))
            return false;
        System.out.println("GOT HERE");
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

    }
    public void moveStockItem(){

    }
    public void manageOrder(){

    }
    public static void main(String[] args){
        StockKeeperMenuGUI.getInstance();}
}