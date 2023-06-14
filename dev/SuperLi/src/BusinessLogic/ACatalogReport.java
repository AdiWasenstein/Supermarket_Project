package BusinessLogic;

import java.util.LinkedList;

public abstract class ACatalogReport extends AReport {
    LinkedList<CatalogItem> catalogItems;
    public ACatalogReport() {
        catalogItems = new LinkedList<>();
    }
    public void addToReport(CatalogItem catalogItem) {
        catalogItems.add(catalogItem);
    }
    public abstract String[] getRecordData(CatalogItem catalogItem);
    public LinkedList<String[]> initializeRecords(){
        LinkedList<String[]> records = new LinkedList<>();
        for(CatalogItem catalog_item : catalogItems)
            records.add(getRecordData(catalog_item));
        return records;
    }
}