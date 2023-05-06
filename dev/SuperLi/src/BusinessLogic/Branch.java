package SuperLi.src.BusinessLogic;
import SuperLi.src.DataAccess.StockItemDataMapper;

import java.time.LocalDate;
import java.util.*;

public class Branch {
    final int id;
    final String address;
    public final static int BACKSTART = 1000;
    public final static int BACKEND = 2000;
    LinkedList<Order> branchOrders;

    StockItemDataMapper stockItemDataMapper;
    public Branch(String address, int id){
        this.address = address;
        this.id = id;
        this.branchOrders = new LinkedList<>();
        stockItemDataMapper = StockItemDataMapper.getInstance();
    }
    public LinkedList<Order> getBranchOrders()
    {
        return this.branchOrders;
    }

    public void addOrder(Order order)
    {
        if (order != null)
            branchOrders.add(order);
    }

    public void showOrderHistory()
    {
        if (this.branchOrders.isEmpty())
            System.out.println("No orders yet to be made from this branch.");

        for (Order order: branchOrders)
            System.out.println(order);
    }
    public int getId(){return this.id;}
    public String getAddress(){return this.address;}
    // Stock Items
    public StockItem getStockItem(int barcode) {
        Optional<StockItem> stockItem = StockItemDataMapper.getInstance().find(Integer.toString(barcode));
        if(stockItem.isEmpty() || stockItem.get().getBranchId() != this.id)
            return null;
        return stockItem.get();
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, LocalDate expirationDate, DamageType damage, boolean forFront) {
        if(getStockItem(barcode) != null)
            return false;
        int location = forFront ? catalogItem.getShelvesLocation() : catalogItem.getBackLocation();
        StockItem stockItem = new StockItem(catalogItem, barcode, costPrice, expirationDate, damage, this.id, location);
        stockItemDataMapper.insert(stockItem);
        return true;
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, LocalDate expirationDate, DamageType damage){
        boolean forFront = catalogItem.getShelvesAmount(id) < catalogItem.getMinCapacity();
        return addStockItem(catalogItem, barcode, costPrice, expirationDate, damage, forFront);
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, DamageType damage, boolean forFront) {
        if(getStockItem(barcode) != null)
            return false;
        int location = forFront ? catalogItem.getShelvesLocation() : catalogItem.getBackLocation();
        StockItem stockItem = new StockItem(catalogItem, barcode, costPrice, damage, this.id, location);
        stockItemDataMapper.insert(stockItem);
        return true;
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, DamageType damage){
        boolean forFront = catalogItem.getShelvesAmount(id) < catalogItem.getMinCapacity();
        return addStockItem(catalogItem, barcode, costPrice, damage, forFront);
    }
    public boolean removeStockItem(int barcode){
        StockItem stockItem =  getStockItem(barcode);
        if(stockItem == null || stockItem.getBranchId() != this.id)
            return false;
        stockItemDataMapper.delete(stockItem);
        return true;
    }
    public boolean containsBarcode(int barcode){
        return getStockItem(barcode) != null;
    }
    public boolean setStockItemDamage(int barcode, DamageType damage){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setDamage(damage);
        return true;
    }
    public DamageType getStockItemDamage(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return null;
        return stockItem.getDamage();
    }
    public int getStockItemLocation(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return -1;
        return stockItem.getLocation();
    }
    public boolean transferItem(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        int currentLocation = stockItem.getLocation();
        if(currentLocation < Branch.BACKSTART)
            return transferFrontToBack(barcode);
        return transferBackToFront(barcode);
    }
    public boolean transferFrontToBack(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setLocation(stockItem.getCatalogItem().getBackLocation());
        return true;
    }
    public boolean transferBackToFront(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setLocation(stockItem.getCatalogItem().getShelvesLocation());
        return true;
    }
    public int barcodeToId(int barcode){
        StockItem stockItem = getStockItem(barcode);
        return stockItem == null ? -1 : stockItem.getCatalogItem().getId();
    }
    public int getShelvesIdAmount(int catalogId){
        return stockItemDataMapper.getShelvesIdAmount(this.id, catalogId);
    }
    public int getBackIdAmount(int catalogId){
        return stockItemDataMapper.getBackIdAmount(this.id, catalogId);
    }
    public int getTotalIdAmount(int catalogId){
        return getShelvesIdAmount(catalogId) + getBackIdAmount(catalogId);
    }
    public LinkedList<StockItem> findAllFromBranch(){
        return stockItemDataMapper.findAllFromBranch(this.id);
    }
}