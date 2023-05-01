package SuperLi.src.Stock.Reports;

import SuperLi.src.Branch;
import SuperLi.src.Stock.StockItem;

import java.util.ArrayList;

public abstract class AStockItemReport extends AReport{
    ArrayList<StockItem> stockItems;
    public AStockItemReport() { stockItems = new ArrayList<>();}
    public void addToReport(StockItem stockItem){
        stockItems.add(stockItem);}
    public abstract String[] getRecordData(StockItem stockItem);
    public ArrayList<String[]> initializeRecords() {
        ArrayList<String[]> records = new ArrayList<>();
        for (StockItem item : stockItems) {
            records.add(getRecordData(item));
        }
        return records;
    }
    public String getLocation(StockItem item){
        return (item.getLocation() < Branch.BACKSTART ? "Shelves" : "Back") + String.format(" - %d", item.getLocation());
    }
}
