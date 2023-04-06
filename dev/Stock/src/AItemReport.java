package Stock.src;

import java.util.ArrayList;

public abstract class AItemReport extends AReport{
    ArrayList<Item> items;
    public AItemReport() { items = new ArrayList<>();}
    public void add_to_report(Item item){items.add(item);}
    public abstract String[] get_data(Item item);
    public void initialize_records() {
        for (Item item : items) {
            records.add(get_data(item));
        }
    }
    public String get_location(Item item){
        return (item.get_location() < Branch.BACKSTART ? "Shelves" : "Back") + String.format(" - %d", item.get_location());
    }
}