package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.BranchDataMapper;
import SuperLi.src.DataAccess.CatalogItemDataMapper;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

public class StockManagementFacade {
    CatalogItemDataMapper catalogItemDataMapper;
    BranchDataMapper branchDataMapper;
    static StockManagementFacade stockManagementFacade = null;
    private StockManagementFacade() {
        catalogItemDataMapper = CatalogItemDataMapper.getInstance();
        branchDataMapper = BranchDataMapper.getInstance();
    }
    public static StockManagementFacade getInstance() {
        if (stockManagementFacade == null)
            stockManagementFacade = new StockManagementFacade();
        return stockManagementFacade;
    }
    public int getBranchCount(){
        return branchDataMapper.findAll().size();
    }
    public LinkedList<Integer> getBranchesIds(){
        LinkedList<Integer> branchesId = new LinkedList<>();
        for(Branch branch : branchDataMapper.findAll())
            branchesId.add(branch.getId());
        return branchesId;
    }
    public boolean addBranch(String address){
        branchDataMapper.insert(new Branch(address, branchDataMapper.findAll().size() + 1));
        return true;
    }
    public CatalogItem getCatalogItem(int id) {
        Optional<CatalogItem> catalogItem = catalogItemDataMapper.find(Integer.toString(id));
        return catalogItem.orElse(null);
    }
    public Branch getBranch(int branchId) {
        Optional<Branch> branch = branchDataMapper.find(Integer.toString(branchId));
        return branch.orElse(null);
    }
    public String getBranchAddress(int branchId) {
        Branch branch = getBranch(branchId);
        return branch == null ? "" : branch.getAddress();
    }
    public boolean addCatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, LinkedList<String> categories, double size, int measureUnit) {
        if (getCatalogItem(id) != null)
            return false;
        Category category = new Category(categories, new Size(size, MeasureUnit.values()[measureUnit]));
        Random rnd = new Random();
        int shelvesLocation = rnd.nextInt(Branch.BACKSTART);
        int backLocation = rnd.nextInt(Branch.BACKSTART, Branch.BACKEND + 1);
        CatalogItem catalogItem = new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation);
        catalogItemDataMapper.insert(catalogItem);
        return true;
    }
    public boolean addCatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, LinkedList<String> categories, double size, int measureUnit, int shelfLife) {
        if (getCatalogItem(id) != null)
            return false;
        Random rnd = new Random();
        Category category = new Category(categories, new Size(size, MeasureUnit.values()[measureUnit]));
        int shelvesLocation = rnd.nextInt(Branch.BACKSTART);
        int backLocation = rnd.nextInt(Branch.BACKSTART, Branch.BACKEND + 1);
        CatalogItem catalogItem = new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation, shelfLife);
        catalogItemDataMapper.insert(catalogItem);
        return true;
    }
    public boolean removeCatalogItem(int id) {
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        catalogItemDataMapper.delete(catalogItem);
        return true;
    }
    public String getCatalogIdName(int id){
        CatalogItem catalogItem = getCatalogItem(id);
        return catalogItem == null ? null : catalogItem.getName();
    }
    public int barcodeToId(int barcode, int branchId){
        Branch branch = getBranch(branchId);
        return branch == null ? -1 : branch.barcodeToId(barcode);
    }
    public boolean isRequired(int catalogId, int branchId){
        Branch branch = getBranch(branchId);
        if(branch == null)
            return false;
        CatalogItem catalogItem = getCatalogItem(catalogId);
        return branch.getTotalIdAmount(catalogId) < catalogItem.getMinCapacity();
    }
    public boolean needsShelvesIncrement(int catalogId, int branchId){
        Branch branch = getBranch(branchId);
        if(branch == null)
            return false;
        CatalogItem catalogItem = getCatalogItem(catalogId);
        return branch.getShelvesIdAmount(catalogId) < catalogItem.getMinCapacity() / 2;
    }
    public int findBranchOfBarcode(int barcode) {
        for (Branch branch : branchDataMapper.findAll())
            if (branch.containsBarcode(barcode))
                return branch.getId();
        return -1;
    }
    public int getLocationOfBarcode(int barcode, int branchId){
        Branch branch = getBranch(branchId);
        return branch.getStockItemLocation(barcode);
    }
    public String getStockItemDamage(int barcode, int branchId){
        Branch branch = getBranch(branchId);
        DamageType damage = branch.getStockItemDamage(barcode);
        if(damage == null)
            return "";
        return damage.toString();
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, LocalDate expirationDate, int damage) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        if(expirationDate != null)
            return branch.addStockItem(catalogItem, barcode, costPrice, expirationDate, DamageType.values()[damage]);
        return branch.addStockItem(catalogItem, barcode, costPrice, DamageType.values()[damage]);
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, LocalDate expirationDate, int damage, boolean front) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        if(expirationDate != null)
            return branch.addStockItem(catalogItem, barcode, costPrice, expirationDate, DamageType.values()[damage], front);
        return branch.addStockItem(catalogItem, barcode, costPrice, DamageType.values()[damage], front);
    }
    public boolean removeStockItem(int barcode, int branchId){
        return getBranch(branchId).removeStockItem(barcode);
    }
    public boolean setSellPrice(int id, double sellPrice){
        CatalogItem catalogItem = getCatalogItem(id);
        if(catalogItem == null)
            return false;
        catalogItem.setSellPrice(sellPrice);
        catalogItemDataMapper.update(catalogItem);
        return true;
    }
    public boolean setCatalogItemCostumerDiscount(int id, LocalDate expirationDate, double value, boolean isPercentage, int minAmount){
        CatalogItem catalogItem = getCatalogItem(id);
        if(catalogItem == null)
            return false;
        CostumerDiscount costumerDiscount = new CostumerDiscount(expirationDate, value, isPercentage, minAmount);
        catalogItem.setCostumerDiscount(costumerDiscount);
        catalogItemDataMapper.update(catalogItem);
        return true;
    }
    public void setCategoryDiscount(LinkedList<String> categories, double size, int measureUnit, LocalDate expirationDate, double value, boolean isPercentage, int minAmount){
        CostumerDiscount costumerDiscount = new CostumerDiscount(expirationDate, value, isPercentage, minAmount);
        Category category = new Category(categories, new Size(size, MeasureUnit.values()[measureUnit]));
        for(CatalogItem catalogItem : catalogItemDataMapper.findAllFromCategory(category)) {
            catalogItem.setCostumerDiscount(costumerDiscount);
            catalogItemDataMapper.update(catalogItem);
        }
    }
    public boolean setMinCapacity(int id, int amount){
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        catalogItem.setMinCapacity(amount);
        catalogItemDataMapper.update(catalogItem);
        return true;
    }
    public boolean setDamage(int barcode, int type, int branchId){
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        return branch.setStockItemDamage(barcode, DamageType.values()[type]);
    }
    public boolean moveStockItem(int barcode, int branchId){
        Branch branch = getBranch(branchId);
        return branch.transferItem(barcode);
    }
    // Reports
    public AllCatalogReport generateAllCatalogReport(){
        AllCatalogReport report = new AllCatalogReport();
        for(CatalogItem catalogItem : catalogItemDataMapper.findAll())
            report.addToReport(catalogItem);
        return report;
    }
    public CategoryReport generateCategoryReport(LinkedList<LinkedList<String>> categoriesStrList) {
        CategoryReport report = new CategoryReport();
        for (CatalogItem catalogItem : catalogItemDataMapper.findAll()) {
            for (LinkedList<String> categoriesStr : categoriesStrList) {
                if(catalogItem.isFromCategory(categoriesStr)){
                    report.addToReport(catalogItem);
                    break;
                }
            }
        }
        return report;
    }
    public RequiredStockReport generateRequiredStockReport(int branchId){
        Branch branch = getBranch(branchId);
        RequiredStockReport report = new RequiredStockReport(branchId);
        if(branch == null)
            return report;
        for(CatalogItem catalogItem : catalogItemDataMapper.findAll())
            if(branch.getTotalIdAmount(catalogItem.getId()) < catalogItem.getMinCapacity())
                report.addToReport(catalogItem);
        return report;
    }
    public StockItemsReport generateStockItemsReport(int branchId){
        StockItemsReport report = new StockItemsReport();
        Branch branch = getBranch(branchId);
        if (branch == null)
            return report;
        for (StockItem item : branch.findAllFromBranch())
            report.addToReport(item);
        return report;
    }
    public DamagedReport generateDamagedStockReport(int branchId){
        DamagedReport report = new DamagedReport();
        Branch branch = getBranch(branchId);
        if (branch == null)
            return report;
        for (StockItem stockItem : branch.findAllFromBranch())
            if(stockItem.needsToBeReturned())
                report.addToReport(stockItem);
        return report;
    }
    public boolean createShortageOrder(int branchId){
        RequiredStockReport report = generateRequiredStockReport(branchId);
        if(report.getReportData().size() == 0)
            return false;
        return OrderController.getInstance().createNewMissingItemsOrder(report);
    }
    public boolean updatePeriodReport(int reportId, int branchId)throws Exception{
        LinkedList<Integer> reportCatalogItems;reportCatalogItems = OrderController.getInstance().allItemsInPeriodicReport(reportId, branchId);
        if(reportCatalogItems == null)
            return false;
        Branch branch = getBranch(branchId);
        HashMap<Integer, Integer> reportQuantities = new HashMap<>();
        for(Integer catalogId : reportCatalogItems){
            Optional<CatalogItem> catalogItemOpt = catalogItemDataMapper.find(catalogId.toString());
            if(catalogItemOpt.isEmpty())
                continue;
            CatalogItem catalogItem = catalogItemOpt.get();
            reportQuantities.put(catalogId, catalogItem.getMinCapacity() * 2 - branch.getTotalIdAmount(catalogId));
        }
        return OrderController.getInstance().updateReport(reportId, reportQuantities);
    }
    public int catalogIdAccordingToNameManufacturerCategory(String name, String manufacturer, String category)throws Exception
    {
        Optional<CatalogItem> catalogItemOpt = this.catalogItemDataMapper.findAccordingToNameManufacturerCategory(name, manufacturer, category);
        if(catalogItemOpt.isEmpty())
            throw new Exception("No item in stock fits to given details.");
        CatalogItem cItem = catalogItemOpt.get();
        return cItem.getId();
    }
}