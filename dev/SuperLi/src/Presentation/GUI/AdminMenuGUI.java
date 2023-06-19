package Presentation.GUI;

import BusinessLogic.*;

import java.time.LocalDate;
import java.util.*;
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
        Runnable exitSubMenus = this::showMainMenu;
        optionsNames.add("1. Add new branch to system."); operations.add(this::addNewBranchToSystem);
        optionsNames.add("2. Add new catalog item to system."); operations.add(this::addCatalogItem);
        optionsNames.add("3. Add new supplier to system."); operations.add(this::addNewSupplierToSystem);
        optionsNames.add("4. Remove catalog item from the system."); operations.add(this::removeCatalogItem);
        optionsNames.add("5. Change catalog item's details."); operations.add(this::changeCatalogItemDetails);
        optionsNames.add("6. Print report."); operations.add(this::generateReport);
        optionsNames.add("7. Print all suppliers in the system."); operations.add(this::printAllSuppliersInSystem);
        optionsNames.add("8. Print all orders were made."); operations.add(this::printAllOrdersInSystem);
        optionsNames.add("9. Do Stock-keeper operations."); operations.add(() -> StockKeeperMenuGUI.getInstance(exitSubMenus).communicate());
        optionsNames.add("10. Do Supplier operations."); operations.add(() -> SupplierMenuGUI.getInstance(exitSubMenus).communicate());
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

    public void addNewSupplierToSystem() {
        LinkedList<String> labelNames = new LinkedList<>();
//        LinkedList<LinkedList<String>> contactDetails = new LinkedList<>();
        Function<LinkedList<String>, Boolean> operationAfterBasicDetails = basicDetailsValues -> {
            if (!checkIfSupplierValuesValid(basicDetailsValues))
                return false;
//            labelNames.add("Contact's Name");
//            contactDetails.add(new LinkedList<>());
//            labelNames.add("Contact's Phone");
//            contactDetails.add(new LinkedList<>());
//            labelNames.add("Contact's Email");
//            contactDetails.add(new LinkedList<>());
            LinkedList<LinkedList<String>> categories = new LinkedList<>();
            Function<LinkedList<String>, Boolean> operationAfterContact = contactValues -> {
                if (!checkIfContactValuesValid(contactValues))
                    return false;
//                labelNames.add("Category");
//                categories.add(new LinkedList<>());
                LinkedList<LinkedList<String>> manufacturers = new LinkedList<>();
                Function<LinkedList<LinkedList<String>>, Boolean> operationAfterCategories = categoriesValues -> {
                    if (!checkIfCategoriesAndManufacturersValuesValid(categoriesValues))
                        return false;
//                    labelNames.add("Manufacturer");
//                    manufacturers.add(new LinkedList<>());
                    Function<LinkedList<LinkedList<String>>, Boolean> operationAfterManufacturers = manufacturersValues -> {
                        if (!checkIfCategoriesAndManufacturersValuesValid(manufacturersValues))
                            return false;
                        LinkedList<LinkedList<String>> supplierKinds = new LinkedList<>();
//                        labelNames.add("Supplier Kind");
//                        supplierKinds.add(new LinkedList<>(Arrays.asList("Supplier Delivers","Supplier Not Delivers")));
                        Function<LinkedList<String>, Boolean> operationAfterSelectSupplierKind = kindValues -> {
                            if(!kindValues.get(0).equals("Supplier Delivers") && !kindValues.get(0).equals("Supplier Not Delivers"))
                                return false;
                            if(kindValues.get(0).equals("Supplier Delivers"))
                            {
                                LinkedList<LinkedList<String>> supplyKind = new LinkedList<>();
//                                labelNames.add("Supply On Regular Days");
//                                supplyKind.add(new LinkedList<>(Arrays.asList("yes","no")));
                                Function<LinkedList<String>, Boolean> operationAfterSelectSupplyKind = supplyRegularValues -> {
                                    if (!supplyRegularValues.get(0).equals("yes") && !supplyRegularValues.get(0).equals("no"))
                                        return false;
                                    if(supplyRegularValues.get(0).equals("yes"))
                                    {
                                        LinkedList<LinkedList<String>> deliveryDays = new LinkedList<>();
//                                        labelNames.add("Days Of Supply");
//                                        deliveryDays.add(new LinkedList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")));
                                        Function<LinkedList<LinkedList<String>>, Boolean> operationAfterSelectingDays = daysToSupplyValues -> {
                                            if(!checkIfDaysValid(daysToSupplyValues))
                                                return false;
                                            PaymentsWays payment = convertPayment(basicDetailsValues.get(4));
                                            LinkedList<Day> days = convertDays(daysToSupplyValues);
                                            LinkedList<String> categoriesToSend = convertCategoriesAndManufacturers(categoriesValues);
                                            LinkedList<String> manufacturersToSend = convertCategoriesAndManufacturers(manufacturersValues);
                                            adminController.addNewSupplier(basicDetailsValues.get(2), basicDetailsValues.get(3), generateInt(basicDetailsValues.get(0)), basicDetailsValues.get(1), payment, contactValues.get(0), contactValues.get(1), contactValues.get(2), categoriesToSend, manufacturersToSend, days, -1);
                                            return true; //TODO - i dont know!
                                        };
                                        labelNames.clear();
                                        labelNames.add("Days Of Supply");
                                        deliveryDays.add(new LinkedList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")));
                                        String successDays = "Days to supply was submitted successfully";
                                        String failureDays = "Days to supply wasn't submitted, inputs not valid";
                                        showInfiniteFillPage(labelNames, deliveryDays, operationAfterSelectingDays, successDays, failureDays, true, 3);
                                        return true;
                                    }
                                    else //supplyRegularValues.get(0).equals("no")
                                    {
                                        LinkedList<LinkedList<String>> daysUntilSupply = new LinkedList<>();
//                                        labelNames.add("Number Of Days To Supply After Order");
//                                        daysUntilSupply.add(new LinkedList<>());
                                        Function<LinkedList<String>, Boolean> operationAfterSelectingNumberOfDays = daysUntilSupplyValues -> {
                                            if(generateInt(daysUntilSupplyValues.get(0)) < 0)
                                                return false;
                                            PaymentsWays payment = convertPayment(basicDetailsValues.get(4));
                                            LinkedList<String> categoriesToSend = convertCategoriesAndManufacturers(categoriesValues);
                                            LinkedList<String> manufacturersToSend = convertCategoriesAndManufacturers(manufacturersValues);
                                            adminController.addNewSupplier(basicDetailsValues.get(2), basicDetailsValues.get(3), generateInt(basicDetailsValues.get(0)), basicDetailsValues.get(1), payment, contactValues.get(0), contactValues.get(1), contactValues.get(2), categoriesToSend, manufacturersToSend, null, generateInt(daysUntilSupplyValues.get(0)));
                                            return true; //TODO - i dont know!
                                        };
                                        labelNames.clear();
                                        labelNames.add("Number Of Days To Supply After Order");
                                        daysUntilSupply.add(new LinkedList<>());
                                        String successDaysUntil = "Number of days until supply was submitted successfully";
                                        String failureDaysUntil = "Number of days until supply wasn't submitted, inputs not valid";
                                        showFillPage(labelNames, daysUntilSupply, operationAfterSelectingNumberOfDays, successDaysUntil, failureDaysUntil, true, 3);
                                        return true;
                                    }
                                };
                                labelNames.clear();
                                labelNames.add("Supply On Regular Days?");
                                supplyKind.add(new LinkedList<>(Arrays.asList("yes","no")));
                                String successRegular = "Delivers supplier's kind was submitted successfully";
                                String failureRegular = "Delivers supplier's kind wasn't submitted, inputs not valid";
                                showFillPage(labelNames, supplyKind, operationAfterSelectSupplyKind, successRegular, failureRegular, false, 3);
                                return true;
                            }
                            else {
                                PaymentsWays payment = convertPayment(basicDetailsValues.get(4));
                                LinkedList<String> categoriesToSend = convertCategoriesAndManufacturers(categoriesValues);
                                LinkedList<String> manufacturersToSend = convertCategoriesAndManufacturers(manufacturersValues);
                                adminController.addNewSupplier(basicDetailsValues.get(2), basicDetailsValues.get(3), generateInt(basicDetailsValues.get(0)), basicDetailsValues.get(1), payment, contactValues.get(0), contactValues.get(1), contactValues.get(2), categoriesToSend, manufacturersToSend, null, -1);
                                return true; //TODO - i dont know!
                            }
                        };
                        labelNames.clear();
                        labelNames.add("Supplier Kind");
                        supplierKinds.add(new LinkedList<>(Arrays.asList("Supplier Delivers","Supplier Not Delivers")));
                        String successKind = "Supplier's kind was submitted successfully";
                        String failureKind = "Supplier's kind wasn't submitted, inputs not valid";
                        showFillPage(labelNames, supplierKinds, operationAfterSelectSupplierKind, successKind, failureKind, false, 3);
                        return true;
                    };
                    labelNames.clear();
                    labelNames.add("Manufacturer");
                    manufacturers.add(new LinkedList<>());
                    String successManufacturers = "Supplier's manufacturers were added successfully";
                    String failureManufacturers = "Supplier's manufacturers weren't added, inputs not valid";
                    showInfiniteFillPage(labelNames, manufacturers, operationAfterManufacturers, successManufacturers, failureManufacturers, false, 3);
                    return true;
                };
                labelNames.clear();
                labelNames.add("Category");
                categories.add(new LinkedList<>());
                String successCategories = "Supplier's categories were added successfully";
                String failureCategories = "Supplier's categories weren't added, inputs not valid";
                showInfiniteFillPage(labelNames,categories,operationAfterCategories,successCategories,failureCategories,false,3);
                return true;
            };
//            LinkedList<String> labelNames = new LinkedList<>();
            labelNames.clear();
            LinkedList<LinkedList<String>> contactDetails = new LinkedList<>();
            labelNames.add("Contact's Name");
            contactDetails.add(new LinkedList<>());
            labelNames.add("Contact's Phone");
            contactDetails.add(new LinkedList<>());
            labelNames.add("Contact's Email");
            contactDetails.add(new LinkedList<>());
            String successContact = "Supplier's contact was added successfully";
            String failureContact = "Supplier's contact wasn't added, inputs not valid";
            showFillPage(labelNames, contactDetails, operationAfterContact, successContact, failureContact, false, 3);
            return true;
        };
        LinkedList<String> labelName = new LinkedList<>();
        LinkedList<LinkedList<String>> supplierBasicDetails = new LinkedList<>();
        labelName.add("Supplier's ID");
        supplierBasicDetails.add(new LinkedList<>());
        labelName.add("Bank Account");
        supplierBasicDetails.add(new LinkedList<>());
        labelName.add("Name");
        supplierBasicDetails.add(new LinkedList<>());
        labelName.add("Address");
        supplierBasicDetails.add(new LinkedList<>());
        labelName.add("Payment way");
        supplierBasicDetails.add(new LinkedList<>(Arrays.asList("direct", "plus30", "plus60")));
        String success = "Basic details were added successfully";
        String failure = "Basic details aren't valid and weren't added";
        showFillPage(labelName, supplierBasicDetails, operationAfterBasicDetails, success, failure, false, 3);
    }

    private Day matchStringToDay(String s_day) {
        if (s_day.equalsIgnoreCase("sunday"))
            return Day.sunday;
        else if (s_day.equalsIgnoreCase("monday"))
            return Day.monday;
        else if (s_day.equalsIgnoreCase("tuesday"))
            return Day.tuesday;
        else if (s_day.equalsIgnoreCase("wednesday"))
            return Day.wednesday;
        else if (s_day.equalsIgnoreCase("thursday"))
            return Day.thursday;
        else if (s_day.equalsIgnoreCase("friday"))
            return Day.friday;
        else
            return Day.saturday;
    }

    private LinkedList<Day> convertDays(LinkedList<LinkedList<String>> days)
    {
        LinkedList<Day> daysToReturn = new LinkedList<>();
        for(LinkedList<String> day : days)
        {
            daysToReturn.add(matchStringToDay(day.get(0)));
        }
        return daysToReturn;
    }
    private PaymentsWays convertPayment(String paymentString)
    {
        if(paymentString.equals("direct"))
            return PaymentsWays.direct;
        else if (paymentString.equals("plus30"))
            return PaymentsWays.plus30;
        else
            return PaymentsWays.plus60;
    }
    private boolean checkIfDaysValid(LinkedList<LinkedList<String>> days)
    {
        for(LinkedList<String> day : days) {
            if (!(day.get(0).equalsIgnoreCase("sunday") || day.get(0).equalsIgnoreCase("monday") || day.get(0).equalsIgnoreCase("tuesday") || day.get(0).equalsIgnoreCase("wednesday") || day.get(0).equalsIgnoreCase("thursday") || day.get(0).equalsIgnoreCase("friday") || day.get(0).equalsIgnoreCase("saturday")))
                return false;
        }
        return true;
    }

    private LinkedList<String> convertCategoriesAndManufacturers(LinkedList<LinkedList<String>> values)
    {
        LinkedList<String> listToReturn = new LinkedList<>();
        for(LinkedList<String> value : values)
        {
            if(listToReturn.contains(value.get(0)))
                continue;
            listToReturn.add(value.get(0));
        }
        return listToReturn;
    }
    private boolean checkIfCategoriesAndManufacturersValuesValid(LinkedList<LinkedList<String>> values)
    {
        for(LinkedList<String> value : values)
        {
            if ((value.get(0).matches("[0-9]+")))
                return false;
        }
        return true;
    }
//    public LinkedList<String> getSupplierCategory() {
//        LinkedList<String> categories = new LinkedList<>();
//        while (true) {
//            try {
//                System.out.println("Enter category");
//                String category = input.nextLine();
//                if ((category.matches("[0-9]+")))
//                    throw new InvalidParameterException("category is not valid");
//                if (categories.contains(category))
//                    throw new InvalidParameterException("impossible to add the same category twice.");
//                categories.add(category);
//                System.out.println("if you want to stop entering categories, please enter 1. else, press anything to continue");
//                String choice = input.nextLine();
//                if (choice.equals("1"))
//                    break;
//            } catch (InvalidParameterException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        return categories;
//    }

//    public void addNewSupplierToSystem() {
//        //first page
//        LinkedList<LinkedList<String>> optionsForFieldFirstPage = new LinkedList<>();
//        LinkedList<String> labelsFirstPage = new LinkedList<>();
//        labelsFirstPage.add("Supplier's ID");
//        optionsForFieldFirstPage.add(new LinkedList<>());
//        labelsFirstPage.add("Bank Account");
//        optionsForFieldFirstPage.add(new LinkedList<>());
//        labelsFirstPage.add("Name");
//        optionsForFieldFirstPage.add(new LinkedList<>());
//        labelsFirstPage.add("Address");
//        optionsForFieldFirstPage.add(new LinkedList<>());
//        labelsFirstPage.add("Payment way");
//        optionsForFieldFirstPage.add(new LinkedList<>(Arrays.asList("direct", "plus30", "plus60")));
//
//        Function<LinkedList<String>, Boolean> operationFirstPage = this::addNewSupplierOperation;
//        String successFirstPage = "Basic details added successfully.";
//        String failureFirstPage = "Basic details were not added to system.";
//
//        //second page
//        LinkedList<LinkedList<String>> optionsForFieldSecondPage = new LinkedList<>();
//        LinkedList<String> labelsSecondPage = new LinkedList<>();
//
//        labelsSecondPage.add("Contact's Name");
//        optionsForFieldSecondPage.add(new LinkedList<>());
//        labelsSecondPage.add("Contact's Phone");
//        optionsForFieldSecondPage.add(new LinkedList<>());
//        labelsSecondPage.add("Contact's Email");
//        optionsForFieldSecondPage.add(new LinkedList<>());
//
//        Function<LinkedList<String>, Boolean> operationSecondPage = this::createSupplierContactOperation;
//        String successSecondPage = "Contact added successfully.";
//        String failureSecondPage = "Contact was not added to supplier.";
//        continuousPages(labelsFirstPage,optionsForFieldFirstPage,operationFirstPage,successFirstPage,failureFirstPage,3,
//                labelsSecondPage,optionsForFieldSecondPage,operationSecondPage,successSecondPage,failureSecondPage);
//    }




    //    public void addNewSupplierToSystem(){
//        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
//        LinkedList<String> labels = new LinkedList<>();
//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Bank Account");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Name");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Address");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Payment way");
//        optionsForField.add(new LinkedList<>(Arrays.asList("direct", "plus30", "plus60")));
//
//        Function<LinkedList<String>, Boolean> operation = this::addNewSupplierOperation;
//        String success = "Supplier added successfully.";
//        String failure = "Supplier was not added to system.";
//        showFillPage(labels, optionsForField, operation, success, failure, false, 3);
//        createSupplierContactPage();
//    }
//
    private boolean checkIfSupplierValuesValid(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        if(id==-1)
            return false;
        if(adminController.isSupplierIdExists(id))
            return false;
        String bankAccount = values.get(1);
        if (!(bankAccount.matches("[0-9]+")))
            return false;
        String name = values.get(2);
        if (!(name.matches("[a-zA-Z]+")))
            return false;
        String address = values.get(3);
        if (address.equals(""))
            return false;
        String paymentWayString = values.get(4);
        switch (paymentWayString) {
            case "direct":
                break;
            case "plus30":
                break;
            case "plus60":
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean addNewSupplierOperation(LinkedList<String> values)
    {
        if(!checkIfSupplierValuesValid(values))
            return false;
//        createSupplierContactPage();
        return true;
    }
    //
//
//    public void createSupplierContactPage() {
//        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
//        LinkedList<String> labels = new LinkedList<>();
//
//        labels.add("Contact's Name");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Contact's Phone");
//        optionsForField.add(new LinkedList<>());
//        labels.add("Contact's Email");
//        optionsForField.add(new LinkedList<>());
//
//        Function<LinkedList<String>, Boolean> operation = this::createSupplierContactOperation;
//        String success = "Contact added successfully.";
//        String failure = "Contact was not added to supplier.";
//        showFillPage(labels, optionsForField, operation, success, failure, false, 3);
//
//    }
//
    private boolean checkIfContactValuesValid(LinkedList<String> values)
    {
        String name = values.get(0);
        String phone = values.get(1);
        String email = values.get(2);
        if(!adminController.areContactDetailsValid(name,phone,email))
            return false;
        return true;
    }

    public boolean createSupplierContactOperation(LinkedList<String> values)
    {
        if(!checkIfContactValuesValid(values))
            return false;
        return true;
    }
//
//    public void createSupplierCategoryPage()
//    {
//        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
//        LinkedList<String> labels = new LinkedList<>();
//
//        labels.add("Category");
//        optionsForField.add(new LinkedList<>());
//
//        Function<LinkedList<LinkedList<String>>, Boolean> operation = this::createSupplierCategoryOperation;
//        String success = "Added all possible categories.";
//        String failure = "Failed to add- categories already exists.";
//        showInfiniteFillPage(labels, optionsForField, operation, success, failure, false, 3);
//
//    }
//
//    public boolean createSupplierCategoryOperation(LinkedList<LinkedList<String>> values)
//    {
//        LinkedList<String> categories = new LinkedList<>();
//        for (LinkedList<String> list : values)
//            categories.add(list.get(0));
//        try
//        {
//            //TODO i put in heeara
////            supplierController.addCategoriesToSupplier(curr_supplier, categories);
//            return true;
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//    }


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
        LinkedList<Supplier> allSuppliers = adminController.getAllSuppliersInSystem();
        String[] cols = new String[3];
        cols[0] = "Supplier's ID";
        cols[1] = "Supplier's name";
        cols[2] = "Supply time data";
        String[][] records = new String[allSuppliers.size()][3];
        int i = 0;
        for (Supplier curr : allSuppliers)
        {
            records[i][0] = String.valueOf(curr.getSupplierId());
            records[i][1] = curr.getSupplierCard().getSupplierName();
            records[i][2] = curr.printSupplyTimeData();
            i++;
        }
        showTable(cols, records, 1);
    }

    public void printAllOrdersInSystem()
    {

        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<Order> orders = adminController.getAllOrdersInSystem();

        for (Order curr : orders ) {
            labelNames.add("Order " + curr.getOrderNumber());
        }
        orderSelector(labelNames, orders);
    }

    public static void main(String[] args){
        AdminMenuGUI.getInstance().showMainMenu();
    }
}