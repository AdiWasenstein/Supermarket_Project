package Stock.src;

public class DamagedReport extends Report {

    public void generate_report(){
        for (Item current : items) {
            if (!current.get_damaged().equals("NONE"))
                System.out.format("Item's name: %s, Item's category: %s, DamagedType: %s", current.catalog_item.get_name()
                        , current.get_category(), current.get_damaged());
            else
                System.out.format("Item's name: %s, Item's category: %s, DamagedType: Expired, Duration of Expired: %d",
                        current.catalog_item.get_name(), current.get_category(), current.date_difference());
        }
    }
}
