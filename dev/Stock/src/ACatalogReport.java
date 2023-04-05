package Stock.src;

import java.util.ArrayList;

public abstract class ACatalogReport extends AReport {
    ArrayList<CatalogItem> catalog_items;
    public ACatalogReport() {catalog_items = new ArrayList<>();}
    public void add_to_report(CatalogItem catalog_item) {
        catalog_items.add(catalog_item);
    }
    public abstract String[] get_data(CatalogItem catalog_item);
    public void initialize_records(){
        for(CatalogItem catalog_item : catalog_items)
            records.add(get_data(catalog_item));
    }
}