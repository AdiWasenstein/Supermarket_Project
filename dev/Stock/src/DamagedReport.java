package Stock.src;

import java.util.ArrayList;

public class DamagedReport implements IReport {
    ArrayList<Item> items;

    public DamagedReport(){
        this.items = new ArrayList<>();
    }
    public void add_to_report(Item item){items.add(item);}
    public void generate_report(){
        for(Item current : items){
            String damage_type;
            if(current.get_damaged() != DamageType.NONE){
                damage_type = current.get_damaged().name();
                if(current.is_expired())
                    damage_type = damage_type + String.format(" and Expired %d days ago", current.date_difference());
            }
            else
                damage_type = String.format("Expired %d days ago", current.date_difference());
            System.out.format("Barcode: %d; %s; Item's category: %s; Location: %d; DamagedType: %s\n",current.get_barcode(), current.get_catalog_item().get_name()
                    , current.get_catalog_item().get_category(), current.get_location(), damage_type);
        }
    }
}