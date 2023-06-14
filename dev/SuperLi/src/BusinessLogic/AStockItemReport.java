package BusinessLogic;

import java.util.LinkedList;

public abstract class AStockItemReport extends AReport{
    LinkedList<StockItem> stockItems;
    public AStockItemReport() { stockItems = new LinkedList<>();}
    public void addToReport(StockItem stockItem){
        stockItems.add(stockItem);}
    public abstract String[] getRecordData(StockItem stockItem);
    public LinkedList<String[]> initializeRecords() {
        LinkedList<String[]> records = new LinkedList<>();
        for (StockItem item : stockItems) {
            records.add(getRecordData(item));
        }
        return records;
    }
    public String getLocation(StockItem item){
        return (item.getLocation() < Branch.BACKSTART ? "Shelves" : "Back") + String.format(" - %d", item.getLocation());
    }
}
