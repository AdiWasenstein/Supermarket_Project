package Stock.src;

public class StockReport extends ACatalogReport{
    public void print_item(CatalogItem catalog_item){
        System.out.printf("ID: %d; Name: %s; Need To Order: %d units%n", catalog_item.get_id(), catalog_item.get_name(),
                catalog_item.get_min_capacity() * 2 - catalog_item.get_total_amount());
    }
}
