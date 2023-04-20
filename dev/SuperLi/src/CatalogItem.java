package SuperLi.src;

public class CatalogItem {
    int id;
    String name;
    Category category;
    String manufacturer;
    double sellPrice;
    int minCapacity;
    int shelvesAmount;
    int backAmount;
    int shelvesLocation;
    int backLocation;
    Discount discount;
    public CatalogItem(int id, String name, String manufacturer, double sellPrice, int minCapacity, Category category, int shelvesLocation, int backLocation){
        this.id = id;
        this.shelvesAmount = 0;
        this.backAmount = 0;
        this.name = name;
        this.manufacturer = manufacturer;
        this.sellPrice = sellPrice;
        this.minCapacity = minCapacity;
        this.category = category;
        this.shelvesLocation = shelvesLocation;
        this.backLocation = backLocation;
        this.discount = null;
    }
    public int getId(){ return this.id;}
    public String getName(){return this.name;}
    public String getManufacturer(){return this.manufacturer;}
    public Category getCategory(){return this.category;}
    public double getPrice(){return this.sellPrice;}
    public void setPrice(double price){this.sellPrice = price;}
    public int getMinCapacity() { return this.minCapacity;}
    public void setMinCapacity(int capacity){this.minCapacity = capacity;}
    public int getTotalAmount(){return this.shelvesAmount + this.backAmount;}
    public int getShelvesAmount(){ return this.shelvesAmount;}
    public void setShelvesAmount(int amount){this.shelvesAmount = amount;}
    public void incShelves(){setShelvesAmount(this.shelvesAmount + 1);}
    public void decShelves(){setShelvesAmount(this.shelvesAmount - 1);}
    public int getBackAmount(){ return this.backAmount;}
    public void setBackAmount(int amount){this.backAmount = amount;}
    public void incBack(){setBackAmount(this.backAmount + 1);}
    public void decBack(){setBackAmount(this.backAmount - 1);}
    public int getShelvesLocation(){return this.shelvesLocation;}
    public int getBackLocation(){return this.backLocation;}
    public String toString(){
        return String.format("ID: %d; %s; %s; Manufacturer: %s; %.1f₪; Amount: %d; Min Capacity: %d; Discount: %s",
                id, category, name, manufacturer, sellPrice, shelvesAmount + backAmount, minCapacity,(discount == null ? "" : discount.toString()));
    }
    public Discount getDiscount(){return this.discount;}
    public void setDiscount(Discount discount){this.discount = discount;}
    public double getDiscountedPrice() {
        if(this.discount == null)
            return this.sellPrice;
        return getDiscountedPrice(this.discount.getMinCapacity());
    }
    public double getDiscountedPrice(int amount) {
        if(discount == null)
            return this.sellPrice;
        return this.discount.generateDiscount(this.sellPrice, amount);
    }
    public boolean isFromCategory(Category category){return category.equals(this.getCategory());}
    public boolean isFromCategory(String prime_category){
        return prime_category.equals(getCategory().get_prime_category());
    }
    public boolean isFromCategory(String prime_category, String sub_category){
        return isFromCategory(prime_category) &&
                sub_category.equals(category.get_sub_category());
    }
}