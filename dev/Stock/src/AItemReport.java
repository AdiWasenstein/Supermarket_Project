package Stock.src;

import java.util.ArrayList;

public abstract class AItemReport implements IReport {
    ArrayList<Item> items;
    public AItemReport() { items = new ArrayList<>();}
    public void add_to_report(Item item){items.add(item);}
    public void generate_report() {
        for (Item item : items) {
            print_item(item);
        }
    }
    public abstract void print_item(Item item);
}
