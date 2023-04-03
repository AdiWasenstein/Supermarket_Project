package Stock.src;

import java.util.ArrayList;

public abstract class ACatalogReport implements IReport { // For Category/Stock Report
    ArrayList<CatalogItem> catalog_items;

    public ACatalogReport() {
        catalog_items = new ArrayList<>();
    }
    public void add_to_report(CatalogItem catalog_item) {
        catalog_items.add(catalog_item);
    }

    @Override
    public void generate_report() {
        for (CatalogItem catalog_item : catalog_items) {
            print_item(catalog_item);
        }
    }
    public abstract void print_item(CatalogItem catalog_item);
}