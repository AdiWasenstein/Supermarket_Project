package SuperLi.src.Stock.Reports;

import SuperLi.src.Branch;
import SuperLi.src.Stock.StockItem;

import java.util.ArrayList;

public abstract class AItemReport extends AReport{
    ArrayList<StockItem> items;
    public AItemReport() { items = new ArrayList<>();}
    public void add_to_report(StockItem item){items.add(item);}
    public abstract String[] get_data(StockItem item);
    public void initialize_records() {
        for (StockItem item : items) {
            records.add(get_data(item));
        }
    }
    public String get_location(StockItem item){
        return (item.getLocation() < Branch.BACKSTART ? "Shelves" : "Back") + String.format(" - %d", item.getLocation());
    }
}
