package Stock.src;

public class DamagedReport extends Report {

    public void generate_report(){
        for (Item current : items) {
            String damage_type;
            if(current.get_damaged() != DamagedType.NONE){
                damage_type = current.get_damaged().name();
                if(current.is_expired())
                    damage_type = damage_type + String.format(" and Expired %d days ago", current.date_difference());
            }
            else
                damage_type = String.format("Expired %d days ago", current.date_difference());
            System.out.format("Barcode: %d, %s, Item's category: %s, Location: %d, DamagedType: %s\n",current.get_barcode(), current.get_catalog_item().get_name()
                    , current.get_category(), current.get_place(), damage_type);
        }
    }
}
