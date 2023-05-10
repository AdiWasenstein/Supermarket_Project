package SuperLi.src.BusinessLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class StockItem {
    CatalogItem catalogItem;
    int barcode;
    double costPrice;
    LocalDate expirationDate;
    DamageType damageType;
    int branchId;
    int location;
    public StockItem(CatalogItem catalogItem, int barcode, double costPrice, LocalDate expirationDate, DamageType damageType, int branchId, int location){
        this.catalogItem = catalogItem;
        this.barcode = barcode;
        this.costPrice = costPrice;
        this.expirationDate = expirationDate;
        this.damageType = damageType;
        this.branchId = branchId;
        this.location = location;
    }
    public StockItem(CatalogItem catalogItem, int barcode, double costPrice, DamageType damageType, int branchId, int location){
        this.catalogItem = catalogItem;
        this.barcode = barcode;
        this.costPrice = costPrice;
        this.expirationDate = LocalDate.now().plusDays(catalogItem.getShelfLife());
        this.damageType = damageType;
        this.branchId = branchId;
        this.location = location;
    }
    public CatalogItem getCatalogItem(){
        return this.catalogItem;
    }
    public int getBarcode(){return this.barcode;}
    public double getCostPrice(){return this.costPrice;}
    public long dateDifference(){
        return ChronoUnit.DAYS.between( this.expirationDate, LocalDate.now());
    }
    public boolean isExpired(){ return dateDifference() > 0;}
    public String getExpirationString(){return this.expirationDate.format(DateTimeFormatter.ofPattern("d/M/yy"));}
    public LocalDate getExpiration(){return this.expirationDate;}
    public DamageType getDamage(){return this.damageType;}
    public void setDamage(DamageType type){
        this.damageType = type;
    }
    public int getBranchId(){return this.branchId;}
    public int getLocation(){ return this.location;}
    public void setLocation(int location){this.location = location;}
    public boolean needsToBeReturned(){return isExpired() || getDamage().ordinal() < 4;}
    public String toString(){
        return String.format("ID: %d; Barcode: %d; Price: %.1f₪; Location: %d; Manufacturer: %s; Amount: %d; Shelves Amount: %d; Back Amount: %d",
                getCatalogItem().getId(), getBarcode(), getCatalogItem().getDiscountedPrice(), getLocation(), getCatalogItem().getManufacturer(),
                getCatalogItem().getTotalAmount(this.branchId), getCatalogItem().getShelvesAmount(this.branchId), getCatalogItem().getBackAmount(this.branchId));
    }
}