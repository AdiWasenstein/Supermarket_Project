package Stock.src;

public class CategoryReport extends ACatalogReport{
    public void print_item(CatalogItem catalog_item){
        String output = catalog_item.toString();
        output = output + String.format("; Locations: Back %d Shelves %d; Amount: Back %d Shelves %d",
                catalog_item.get_back_location(), catalog_item.get_shelves_location(), catalog_item.get_back_amount(),
                catalog_item.get_shelves_amount());
        System.out.println(output);
    }
}
