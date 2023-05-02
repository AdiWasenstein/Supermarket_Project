package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.BranchDataMapper;
import SuperLi.src.DataAccess.CatalogItemDataMapper;
import java.time.LocalDate;
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
    public boolean addBranch(int id, String address){
        if(getBranch(id) != null)
            return false;
        branchDataMapper.insert(new Branch(address, id));
        return true;
    }
    public boolean addBranch(String address){
        return addBranch(branchDataMapper.findAll().size() + 1, address);
    }
    public CatalogItem getCatalogItem(int id) {
        Optional<CatalogItem> catalogItem = catalogItemDataMapper.find(Integer.toString(id));
        return catalogItem.orElse(null);
    }
    public Branch getBranch(int branchId) {
        Optional<Branch> branch = branchDataMapper.find(Integer.toString(branchId));
        return branch.orElse(null);
    }
    public boolean addCatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, Category category) {
        if (getCatalogItem(id) != null)
            return false;
        Random rnd = new Random();
        int shelvesLocation = rnd.nextInt(Branch.BACKSTART);
        int backLocation = rnd.nextInt(Branch.BACKSTART, Branch.BACKEND + 1);
        CatalogItem catalogItem = new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation);
        catalogItemDataMapper.insert(catalogItem);
        return true;
    }
    public boolean addCatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, Category category, int shelfLife) {
        if (getCatalogItem(id) != null)
            return false;
        Random rnd = new Random();
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
    public int findBranchOfBarcode(int barcode) {
        for (Branch branch : branchDataMapper.findAll())
            if (branch.containsBarcode(barcode))
                return branch.getId();
        return -1;
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, LocalDate expirationDate, DamageType damage) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        return branch.addStockItem(catalogItem, barcode, costPrice, expirationDate, damage);
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, LocalDate expirationDate, DamageType damage, boolean front) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        return branch.addStockItem(catalogItem, barcode, costPrice, expirationDate, damage, front);
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, DamageType damage) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        return branch.addStockItem(catalogItem, barcode, costPrice, damage);
    }
    public boolean addStockItem(int id, int branchId, int barcode, double costPrice, DamageType damage, boolean front) {
        if (findBranchOfBarcode(barcode) >= 0)
            return false;
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        Branch branch = getBranch(branchId);
        if (branch == null)
            return false;
        return branch.addStockItem(catalogItem, barcode, costPrice, damage, front);
    }
    public boolean removeStockItem(int barcode, int branchId){
        return getBranch(branchId).removeItem(barcode);
    }
    public boolean setSellPrice(int id, double sellPrice){
        CatalogItem catalogItem = getCatalogItem(id);
        if(catalogItem == null)
            return false;
        catalogItem.setSellPrice(sellPrice);
        return true;
    }
    public boolean setCostumerDiscount(int id, CostumerDiscount costumerDiscount){
        CatalogItem catalogItem = getCatalogItem(id);
        if(catalogItem == null)
            return false;
        catalogItem.setCostumerDiscount(costumerDiscount);
        return true;
    }
    public void setCategoryDiscount(Category category, CostumerDiscount costumerDiscount){
        for(CatalogItem catalogItem : catalogItemDataMapper.findAllFromCategory(category))
            catalogItem.setCostumerDiscount(costumerDiscount);
    }
    public boolean setMinCapacity(int id, int amount){
        CatalogItem catalogItem = getCatalogItem(id);
        if (catalogItem == null)
            return false;
        catalogItem.setMinCapacity(amount);
        return true;
    }

    // Reports
    public AllCatalogReport generateAllCatalogReport(){
        AllCatalogReport report = new AllCatalogReport();
        for(CatalogItem catalogItem : catalogItemDataMapper.findAll())
            report.addToReport(catalogItem);
        return report;
    }
    public CategoryReport generateCategoryReport(LinkedList<Category> categories, LinkedList<LinkedList<String>> categoriesStrList) {
        CategoryReport report = new CategoryReport();
        for (CatalogItem catalogItem : catalogItemDataMapper.findAll()) {
            if (categories.contains(catalogItem.getCategory())) {
                report.addToReport(catalogItem);
                continue;
            }
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
    public StockItemsReport generateItemReport(int branchId){
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
}