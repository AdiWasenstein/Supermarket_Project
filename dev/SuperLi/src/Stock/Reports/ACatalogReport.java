package SuperLi.src.Stock.Reports;

import SuperLi.src.CatalogItem;

import java.util.ArrayList;

public abstract class ACatalogReport extends AReport {
    ArrayList<CatalogItem> catalogItems;
    public ACatalogReport() {
        catalogItems = new ArrayList<>();
    }
    public void addToReport(CatalogItem catalog_item) {
        catalogItems.add(catalog_item);
    }
    public abstract String[] getRecordData(CatalogItem catalog_item);
    public ArrayList<String[]> initializeRecords(){
        ArrayList<String[]> records = new ArrayList<>();
        for(CatalogItem catalog_item : catalogItems)
            records.add(getRecordData(catalog_item));
        return records;
    }
}