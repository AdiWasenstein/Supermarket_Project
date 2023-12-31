package BusinessLogic;

import DataAccess.BranchDataMapper;
import DataAccess.OrderDataMapper;
import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class OrderManagment {
    private static OrderManagment instance = null;
    private OrderDataMapper orderDataMapper;
    //the next fields represent the constant time orders are executed according to periodic reports every day.
    private int hourToRunEveryDay;
    private int minuteToRunEveryDay;
    private int secondToRunEveryDay;
    private int millSecondToRunEveryDay;

    private OrderManagment() {
        this.orderDataMapper = OrderDataMapper.getInstance();
        this.hourToRunEveryDay = 10;
        this.minuteToRunEveryDay = 00;
        this.secondToRunEveryDay = 0;
        this.millSecondToRunEveryDay = 0;
    }

    public static OrderManagment getInstance() {
        if (instance == null)
            instance = new OrderManagment();
        return instance;
    }
    public int getHourToRunEveryDay()
    {
        return this.hourToRunEveryDay;
    }
    public int getMinuteToRunEveryDay()
    {
        return this.minuteToRunEveryDay;
    }
    public int getSecondToRunEveryDay()
    {
        return this.secondToRunEveryDay;
    }
    public int getMilliSecondToRunEveryDay()
    {
        return this.millSecondToRunEveryDay;
    }
    private static Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> itemsSeperateOrCompletly(LinkedList<Pair<Integer, Integer>> orderedItems, LinkedList<Supplier> suppliers) {
        LinkedList<Pair<Integer, Integer>> itemsSeperateToUnits = new LinkedList<>();
        LinkedList<Pair<Integer, Integer>> itemsSuppliedCompletely = new LinkedList<>();
        for (int i = 0; i < orderedItems.size(); i++) {
            boolean canBeSuppliedCompletly = false;
            for (int j = 0; j < suppliers.size(); j++) {
                if (suppliers.get(j).canSupplyMarketItem(orderedItems.get(i))) {
                    canBeSuppliedCompletly = true;
                    itemsSuppliedCompletely.add(orderedItems.get(i));
                    break;
                }
            }
            if (!canBeSuppliedCompletly)
                itemsSeperateToUnits.add(orderedItems.get(i));
        }
        return Pair.create(itemsSeperateToUnits, itemsSuppliedCompletely);
    }

    public HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> startOrderProcess(LinkedList<Pair<Integer, Integer>> orderedItems, LinkedList<Supplier> suppliers) throws Exception {
        // creating lists of items- one for items that can be supplied fully by one supplier and one for items that cannot
        LinkedList<Pair<Integer, Integer>> itemsSeperateToUnits = itemsSeperateOrCompletly(orderedItems, suppliers).getLeft();//a list of all the items we need to separate.
        LinkedList<Pair<Integer, Integer>> itemsCompletely = itemsSeperateOrCompletly(orderedItems, suppliers).getRight();//a list of all the items that there is at least one supplier who can supply it completely.
        //combinations for itemsCompletely
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> listCombinationsFullItems = new LinkedList<>();
        // finding full combination for items that can be supplied fully- in listCombinationsFullItems
        combinationsForFullItems(suppliers, itemsCompletely, listCombinationsFullItems, new HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>());
        for (int i = 0; i < listCombinationsFullItems.size(); i++)//running over each combination of separating the full items
        {
            for (int j = 0; j < itemsSeperateToUnits.size(); j++)//for each partial item
            {
                LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItem = new LinkedList<>();
                //list of all the combinations for this current partial item
                LinkedList<Supplier> fullCombiSuppliers = new LinkedList<>();
                // fullCombiSupliers contains all the suppliers in the current combination that can supply full items (outer loop)
                fullCombiSuppliers = createSuppliersList(listCombinationsFullItems.get(i).keySet());
                // among the suppliers in fullCombiSuppliers, checks all the options to split the current partial item (for specific one)
                listCombinationsForSeperateItem = combinationsForPartialItems(fullCombiSuppliers, itemsSeperateToUnits.get(j).getLeft(), itemsSeperateToUnits.get(j).getRight(), new HashMap<Supplier, Integer>(), new LinkedList<HashMap<Supplier, Integer>>());
                if (listCombinationsForSeperateItem.isEmpty())//meaning there is no combination to supply this partial item with these suppliers (cant split the item among this suppliers)
                {
                    // try to find supplier among other suppliers in the system to complete the combination
                    LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItemAmongALLsuppliers = new LinkedList<>();
                    listCombinationsForSeperateItemAmongALLsuppliers = combinationsForPartialItems(suppliers, itemsSeperateToUnits.get(j).getLeft(), itemsSeperateToUnits.get(j).getRight(), new HashMap<Supplier, Integer>(), new LinkedList<HashMap<Supplier, Integer>>());
                    if (listCombinationsForSeperateItemAmongALLsuppliers.isEmpty())// meaning there is no way to supply this item - error
                        throw new Exception("Can't supply item with the id: " + itemsSeperateToUnits.get(j).getLeft());
                    else {
                        HashMap<Supplier, Integer> fastestCombi = findTheFastestSeparatingItemToUnitsCombination(itemsSeperateToUnits.get(j).getLeft(), listCombinationsForSeperateItemAmongALLsuppliers);
                        AddSuppliersToCurrentCombination(itemsSeperateToUnits.get(j).getLeft(), fastestCombi, listCombinationsFullItems.get(i));
                    }
                } else { // we can split the current item between the suppliers already in the combination (fullCombiSuppliers)
                    HashMap<Supplier, Integer> fastCombination = findTheFastestSeparatingItemToUnitsCombination(itemsSeperateToUnits.get(j).getLeft(), listCombinationsForSeperateItem);
                    // after we found the best option, we merge the option to current combination being checked
                    addPartialCombiToFullCombi(itemsSeperateToUnits.get(j).getLeft(), fastCombination, listCombinationsFullItems.get(i));
                }
            }
        }
        //at this point we have all the combinations including all full items and partial items.
        // returns the cheapest combination among the fastest ones
        return findCheappestCombination(getFastestCombos(listCombinationsFullItems));
    }

    private HashMap<Supplier, Integer> findTheCheapestSeparatingItemToUnitsCombination(int itemId, LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItem) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> tempCombinationsList = new LinkedList<>();
        for (int i = 0; i < listCombinationsForSeperateItem.size(); i++) {
            HashMap<Supplier, Integer> currentCombi = new HashMap<>(listCombinationsForSeperateItem.get(i));
            HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> temp = new HashMap<>();
            for (Supplier sup : currentCombi.keySet()) {
                int saveAmount = currentCombi.get(sup);
                LinkedList<Pair<Integer, Integer>> tempList = new LinkedList<>();
                tempList.add(Pair.create(itemId, saveAmount));
                temp.put(sup, tempList);
            }
            tempCombinationsList.add(temp);
        }
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> minCostCombi = findCheappestCombination(tempCombinationsList);
        HashMap<Supplier, Integer> minCombi = new HashMap<>();
        for (Supplier s : minCostCombi.keySet()) {
            minCombi.put(s, minCostCombi.get(s).get(0).getRight());
        }
        return minCombi;
    }

    private HashMap<Supplier, Integer> findTheFastestSeparatingItemToUnitsCombination(int itemId, LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItem) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> tempCombinationsList = new LinkedList<>();
        for (int i = 0; i < listCombinationsForSeperateItem.size(); i++) {
            HashMap<Supplier, Integer> currentCombi = new HashMap<>(listCombinationsForSeperateItem.get(i));
            HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> temp = new HashMap<>();
            for (Supplier sup : currentCombi.keySet()) {
                int saveAmount = currentCombi.get(sup);
                LinkedList<Pair<Integer, Integer>> tempList = new LinkedList<>();
                tempList.add(Pair.create(itemId, saveAmount));
                temp.put(sup, tempList);
            }
            tempCombinationsList.add(temp);
        }
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> minArrivalCombie = getFastestCombos(tempCombinationsList).getFirst();
        HashMap<Supplier, Integer> minCombi = new HashMap<>();
        for (Supplier s : minArrivalCombie.keySet()) {
            minCombi.put(s, minArrivalCombie.get(s).get(0).getRight());
        }
        return minCombi;
    }

    private void AddSuppliersToCurrentCombination(int itemId, HashMap<Supplier, Integer> combinationOfSeparatingItem, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationFullItems) {
        for (Supplier sup : combinationOfSeparatingItem.keySet()) {
            if (!combinationFullItems.containsKey(sup)) {
                LinkedList<Pair<Integer, Integer>> temp = new LinkedList<>();
                temp.add(Pair.create(itemId, combinationOfSeparatingItem.get(sup)));
                combinationFullItems.put(sup, temp);
            } else {
                //
                LinkedList<Pair<Integer, Integer>> tempi = new LinkedList<>();
                tempi = (LinkedList<Pair<Integer, Integer>>) combinationFullItems.get(sup).clone();
                tempi.add(Pair.create(itemId, combinationOfSeparatingItem.get(sup)));
                combinationFullItems.remove(sup);
                combinationFullItems.put(sup, tempi);
            }
        }
    }

    //this
    private LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> isOneSupplierCanSupplyAllExists(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> listCombinationsFullItems) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> returnList = new LinkedList<>();
        for (int i = 0; i < listCombinationsFullItems.size(); i++) {
            if (listCombinationsFullItems.get(i).size() == 1) {
                returnList.add(listCombinationsFullItems.get(i));
            }
        }
        return returnList;
    }

    private LinkedList<Supplier> createSuppliersList(Set<Supplier> sup) {
        LinkedList<Supplier> suppliers = new LinkedList<>();
        for (Supplier s : sup) {
            suppliers.add(s);
        }
        return suppliers;
    }

    //    private HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> checkSuppliersOnBothCombinations(int itemId, LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,Integer>> listCombinationsForOneSeperateItem, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combinationFullItems)
    private void checkSuppliersOnBothCombinations(int itemId, LinkedList<HashMap<Supplier, Integer>> listCombinationsForOneSeperateItem, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationFullItems) {
        for (int i = 0; i < listCombinationsForOneSeperateItem.size(); i++) //running over all the combinations for separating the item
            this.addPartialCombiToFullCombi(itemId, listCombinationsForOneSeperateItem.get(i), combinationFullItems);
    }

    //    private HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> addPartialCombiToFullCombi(int itemId, HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> partialItemCombi, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> fullItemCombi)
    private void addPartialCombiToFullCombi(int itemId, HashMap<Supplier, Integer> partialItemCombi, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> fullItemCombi) {
        for (Supplier sup : partialItemCombi.keySet()) {
            LinkedList<Pair<Integer, Integer>> temp = new LinkedList<>();
            temp = (LinkedList<Pair<Integer, Integer>>) fullItemCombi.get(sup).clone();
            temp.add(Pair.create(itemId, partialItemCombi.get(sup)));
            fullItemCombi.remove(sup);
            fullItemCombi.put(sup, temp);
        }
    }
    //this method returns pair with the intersection group and the difference group of supplier's items and the ordered items.
    private Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> getIntersectionAndDifferenceItems(Supplier sup, LinkedList<Pair<Integer, Integer>> items) {
        LinkedList<Pair<Integer, Integer>> intersection = new LinkedList<Pair<Integer, Integer>>();
        LinkedList<Pair<Integer, Integer>> difference = new LinkedList<Pair<Integer, Integer>>();
        for (int i = 0; i < items.size(); i++) {
            if (sup.canSupplyMarketItem(items.get(i))) {
                intersection.add(items.get(i));
            } else {
                difference.add(items.get(i));
            }
        }
        Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> pair = Pair.create(intersection, difference);
        return pair;
    }

    //this method updates the resultlist to have all the combinations of parting an item's units.
    public LinkedList<HashMap<Supplier, Integer>> combinationsForPartialItems(LinkedList<Supplier> suppliers, int itemId, int quantity, HashMap<Supplier, Integer> combination, LinkedList<HashMap<Supplier, Integer>> resultList) {
        if (quantity <= 0) {
            HashMap<Supplier, Integer> temp = new HashMap<>(combination);
            resultList.add(temp);
            return resultList;
        }
        Pair<Integer, Integer> max = findSupplierWithMaxUnitsOfItem(suppliers, itemId);
        LinkedList<Pair<Integer, Integer>> maxSup = allSuppliersWithSameNumOfUnits(max.getRight(), suppliers, itemId);
        for (int i = 0; i < maxSup.size(); i++) {
            int m = maxSup.get(i).getLeft();
            LinkedList<Supplier> sups = new LinkedList<>(suppliers);
            sups.remove(m);
            int sendQ;
            if (quantity - maxSup.get(i).getRight() > 0)
                sendQ = maxSup.get(i).getRight();
            else {
                sendQ = quantity;
            }
            combination.put(suppliers.get(maxSup.get(i).getLeft()), sendQ);
            resultList = combinationsForPartialItems(sups, itemId, quantity - sendQ, combination, resultList);
            combination.remove(suppliers.get(maxSup.get(i).getLeft()));
        }
        return resultList;
    }

    private Pair<Integer, Integer> findSupplierWithMaxUnitsOfItem(LinkedList<Supplier> suppliers, int itemId) {
        int maxUnits = -1;
        int index = -1;
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getNumberOfItemUnitsCanSupply(itemId) > maxUnits) {
                maxUnits = suppliers.get(i).getNumberOfItemUnitsCanSupply(itemId);
                index = i;
            }
        }
        return Pair.create(index, maxUnits);
    }

    private LinkedList<Pair<Integer, Integer>> allSuppliersWithSameNumOfUnits(int numUnits, LinkedList<Supplier> suppliers, int itemId) {
        LinkedList<Pair<Integer, Integer>> listSup = new LinkedList<>();
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getNumberOfItemUnitsCanSupply(itemId) == numUnits) {
                listSup.add(Pair.create(i, numUnits));
            }
        }
        return listSup;
    }

    public void combinationsForFullItems(LinkedList<Supplier> suppliers, LinkedList<Pair<Integer, Integer>> items, LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> resultList, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination) {
        for (int i = 0; i < suppliers.size(); i++) {
            combination = new HashMap<>();
            Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> pair = getIntersectionAndDifferenceItems(suppliers.get(i), items);
            combination.put(suppliers.get(i), pair.getLeft());
            LinkedList<Supplier> sups = new LinkedList<>(suppliers);
            sups.remove(i);
            LinkedList<Pair<Integer, Integer>> leftItems1 = pair.getRight();
            LinkedList<Pair<Integer, Integer>> leftItems2 = new LinkedList<>(leftItems1);
            HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combi = new HashMap<>(combination);
            if (leftItems2.isEmpty())//if the supplier can supply everything by himself
            {
                combi.put(suppliers.get(i), pair.getLeft());
                resultList.add(combi);
                continue;
            }
            for (int j = 0; j < sups.size(); j++) {
                Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> pair2 = getIntersectionAndDifferenceItems(sups.get(j), leftItems2);
                if (!(pair2.getLeft().isEmpty())) {
                    combi.put(sups.get(j), pair2.getLeft());
                    leftItems2 = pair2.getRight();
                    if (leftItems2.isEmpty()) {
                        leftItems2 = new LinkedList<>(leftItems1);
                        resultList.add(combi);
                        combi = new HashMap<>(combination);
                    }
                }
            }
        }
    }
    public HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> listOfAllCombinations(LinkedList<Supplier> suppliers, LinkedList<Pair<Integer, Integer>> items, LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> resultList, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination) {
        if (items.isEmpty()) {
            resultList.add(combination);
            return combination;
        }
        if (suppliers.isEmpty()) {
            return null;
        }
        Supplier supplier = suppliers.get(0);
        Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> pair = this.getIntersectionAndDifferenceItems(supplier, items);
        LinkedList<Supplier> sups = suppliers;
        sups.remove(0);
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combi = combination;
        combi.put(supplier, pair.getLeft());
        combination.put(supplier, pair.getLeft());
        listOfAllCombinations(sups, pair.getRight(), resultList, combination);
        listOfAllCombinations(suppliers, items, resultList, combi);

        return null;
    }

    private HashMap<Supplier, LinkedList<Integer>> suppliersPossiblities(LinkedList<Pair<Integer, Integer>> missingItems, LinkedList<Supplier> suppliers) {
        if (missingItems == null || suppliers == null || missingItems.isEmpty() || suppliers.isEmpty())
            throw new InvalidParameterException("Need to recieve list of suppliers and items, not empty");
        HashMap<Supplier, LinkedList<Integer>> allPossibilities = new HashMap<>();
        for (Supplier supplier : suppliers) {
            LinkedList<Integer> suppliersItems = new LinkedList<Integer>();

            for (Pair<Integer, Integer> currItem : missingItems) {
                if (supplier.canSupplyMarketItem(currItem))
                    suppliersItems.add(currItem.getLeft());

            }
            allPossibilities.put(supplier, suppliersItems);
        }
        return allPossibilities;
    }

    private HashMap<Supplier, LinkedList<Integer>> findSuppliersCombination(HashMap<Supplier, LinkedList<Integer>> suppliersPossiblities, LinkedList<Pair<Integer, Integer>> missingItems) {
        boolean first = true;
        int maxSize = 0;
        HashMap<Supplier, LinkedList<Integer>> suppliersPossiblitiesSorted = this.suppliersPossiblitiesSorted(suppliersPossiblities);
        HashMap<Supplier, LinkedList<Integer>> temp = new HashMap<>();
        for (Map.Entry<Supplier, LinkedList<Integer>> entry : suppliersPossiblitiesSorted.entrySet()) {
            if (first) {
                maxSize = entry.getValue().size();
                temp.put(entry.getKey(), entry.getValue());
                first = false;
            } else {
                if (entry.getValue().size() == maxSize)
                    temp.put(entry.getKey(), entry.getValue());
                else
                    break;
            }
        }

        for (Map.Entry<Supplier, LinkedList<Integer>> entry : temp.entrySet()) {
            LinkedList<Pair<Integer, Integer>> updatedMissingItems = new LinkedList<>();
            for (Pair<Integer, Integer> currItem : missingItems) {
                if (!(entry.getKey().canSupplyMarketItem(currItem)))
                    updatedMissingItems.add(currItem);
            }
        }
        return null; //TO DELETE
    }

    private HashMap<Supplier, LinkedList<Integer>> suppliersPossiblitiesSorted(HashMap<Supplier, LinkedList<Integer>> suppliersPossiblities) {
        List<Map.Entry<Supplier, LinkedList<Integer>>> list = new LinkedList<Map.Entry<Supplier, LinkedList<Integer>>>(suppliersPossiblities.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Supplier, LinkedList<Integer>>>() {
            public int compare(Map.Entry<Supplier, LinkedList<Integer>> o1, Map.Entry<Supplier, LinkedList<Integer>> o2) {
                return Integer.compare(o2.getValue().size(), o1.getValue().size());
            }
        });
        // put data from sorted list to hashmap
        HashMap<Supplier, LinkedList<Integer>> temp = new LinkedHashMap<Supplier, LinkedList<Integer>>();
        for (Map.Entry<Supplier, LinkedList<Integer>> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public double findCostOfCombination(HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination) {
        if (combination == null || combination.isEmpty())
            return -1;
        double totalCostOfCombi = 0;
        for (Map.Entry<Supplier, LinkedList<Pair<Integer, Integer>>> entry : combination.entrySet()) {
            totalCostOfCombi += findCostOfOrder(Pair.create(entry.getKey(), entry.getValue()));
        }
        return totalCostOfCombi;
    }

    // given supplier and list of items he can supply (market id and amount), returns how much it will cost after discounts
    private double findCostOfOrder(Pair<Supplier, LinkedList<Pair<Integer, Integer>>> order) {
        double result = 0;
        int totalUnits = 0;
        for (Pair<Integer, Integer> marketItem : order.getRight()) {
            //summing the units in order in total
            totalUnits += marketItem.getRight();
            // finding the match supplier item to the missing market item
            SupplierItem currItem = order.getLeft().findMatchToMarketItem(marketItem.getLeft());
            if (currItem == null)
                throw new InvalidParameterException("Can't order item from supplier that he doesn't supply");
            // saving the best discount for the item
            ItemDiscount dis = order.getLeft().getSupplierContract().getDiscountDocument().findBestDiscount(currItem.getCatalogNumber(), marketItem.getRight(), currItem.getUnitPrice() * marketItem.getRight());
            // adding the price of current item to result
            if (dis == null) {
                result += currItem.getUnitPrice() * marketItem.getRight();
            } else
                result += dis.GetPriceAfterDiscount(currItem.getUnitPrice() * marketItem.getRight());
        }
        // now result includes items discount. need to calculate order discount
        OrderDiscount orderDiscount = order.getLeft().getSupplierContract().getDiscountDocument().bestOrderDiscount(totalUnits, result);
        if (orderDiscount != null)
            result = orderDiscount.GetPriceAfterDiscount(result);
        // end case
        if (result < 0)
            return 0;
        return result;
    }

    // given list of all combinations that supply all the items, returns list of the combinations demands minimum suppliers.
    private LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> findMinimal(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> allCombis) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> result = new LinkedList<>();
        int minSuppliers = Integer.MAX_VALUE;
        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis) {
            int suppliersCount = combination.keySet().size();
            if (suppliersCount < minSuppliers) {
                minSuppliers = suppliersCount;
            }
        }

        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> filteredCombinations = new LinkedList<>();
        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis) {
            int suppliersCount = combination.keySet().size();
            if (suppliersCount == minSuppliers) {
                filteredCombinations.add(combination);
            }
        }

        return filteredCombinations;
    }

    // given list of minimal suppliers combinations, find the chippest among them and returns the combination
    public HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> findCheappestCombination(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> allCombies) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> minimalSizeCombi = findMinimal(allCombies);
        double minCost = Double.MAX_VALUE;
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> cheapestCombination = null;
        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : minimalSizeCombi) {
            double cost = findCostOfCombination(combination);
            if (cost < minCost) {
                minCost = cost;
                cheapestCombination = combination;
            }
        }
        return cheapestCombination;
    }

    // given supplier and list of missing items, return order that matches
    private Order makeOrderForSupplier(Pair<Supplier, LinkedList<Pair<Integer, Integer>>> supAndItems, int branchNumber) {
        int marketId, amount = 0;
        double discountValue, initCost;
        LinkedList<OrderItem> orderItems = new LinkedList<>();

        if (supAndItems == null)
            return null;
        Supplier supplier = supAndItems.getLeft();
        for (Pair<Integer, Integer> curr : supAndItems.getRight()) {
            marketId = curr.getLeft();
            amount = curr.getRight();
            SupplierItem suppItem = supplier.findMatchToMarketItem(marketId);
            initCost = suppItem.getUnitPrice() * amount;
            ItemDiscount discount = supplier.getSupplierContract().getDiscountDocument().
                    findBestDiscount(suppItem.getCatalogNumber(), amount, initCost);
            if (discount != null) {
                discountValue = initCost - discount.GetPriceAfterDiscount(initCost);
            } else {
                discountValue = 0;
            }
            double finalPrice = initCost - discountValue;
            OrderItem currItem = new OrderItem( suppItem, amount, discountValue, finalPrice);
            orderItems.add(currItem);
        }
        LinkedList<Order> allOrders = OrderDataMapper.getInstance().findAll();
        int orderNumber = allOrders.size()+1;
        Order order = new Order(orderNumber, supplier, orderItems, branchNumber);
        OrderDiscount bestOrderDiscount = supplier.getSupplierContract().getDiscountDocument().
                bestOrderDiscount(amount, order.getCostOfOrder());
        if (bestOrderDiscount != null) {
            double costAfterDiscount = bestOrderDiscount.GetPriceAfterDiscount(order.getCostOfOrder());
            order.setCostOfOrder(costAfterDiscount);
        }


        addOrderToSystemData(order);
        return order;
    }
    private void addOrderToSystemData(Order order)
    {
        if (order == null)
            return;
        Supplier suppOfOrder = order.getOrderSupplier();
        Branch branchOfOrder = BranchDataMapper.getInstance().find(Integer.toString(order.branchNumber)).get();

        branchOfOrder.addOrder(order);
        // insert to DB
        this.orderDataMapper.insert(order);

    }
    // given the best combination to order, returns list of orders
    private LinkedList<Order> getOrdersForCombi(HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationToOrder, int branchNumber) {

        LinkedList<Order> orders = new LinkedList<>();
        for (Map.Entry<Supplier, LinkedList<Pair<Integer, Integer>>> curr : combinationToOrder.entrySet()) {
            Supplier supplier = curr.getKey();
            LinkedList<Pair<Integer, Integer>> linkedList = curr.getValue();
            Pair<Supplier, LinkedList<Pair<Integer, Integer>>> pair = Pair.create(supplier, linkedList);
            orders.add(makeOrderForSupplier(pair, branchNumber));
        }
        return orders;
    }

    public LinkedList<Order> creatMissingOrder(Map<Integer, Integer> missingItems, int branchNumber, LinkedList<Supplier> suppliers) throws Exception {
        if (missingItems.isEmpty())
            return null;
        LinkedList<Order> orders = new LinkedList<>();
        // transform data from map to list
        LinkedList<Pair<Integer, Integer>> missingItemsList = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : missingItems.entrySet()) {
            Pair<Integer, Integer> pair = new Pair<>(entry.getKey(), entry.getValue());
            missingItemsList.add(pair);
        }


        // Change by Yoav - switch SystemManagement to AdminController - to verify
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationToOrder = startOrderProcess(missingItemsList, suppliers);
        // Original
        orders = getOrdersForCombi(combinationToOrder, branchNumber);
        return orders;
    }

    public void confirmOrders(LinkedList<Order> orders, int branchNumber) {
        // updates all the relevant databases with the new order
        for (Order order : orders) {
            // Changes by Yoav - Confirm
            // END
            // Original Code - Commented to compile
           // order.getOrderSupplier().addOrder(order);
        }
    }



    // this method receives all combinations and filter to only combinations with minimal arriving time
    public LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> getFastestCombos(LinkedList<HashMap<Supplier,
            LinkedList<Pair<Integer, Integer>>>> allCombis)
    {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> result = new LinkedList<>();
        int minTimeOfArrival = Integer.MAX_VALUE;

        // finding the fastest time
        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis) {
             int curr = findArrivalTimeOfCombo(combination);
             if (curr < minTimeOfArrival)
                 minTimeOfArrival = curr;
        }

        // filter to all fastest combinations
        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis)
        {
            int curr = findArrivalTimeOfCombo(combination);
            if (curr == minTimeOfArrival)
                result.add(combination);
        }

        return result;
    }


     // find the max arrival time in the combination and returns it
    private int findArrivalTimeOfCombo (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination)
    {
        int time = 0;
        // need to find the day today
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        String dayName = dayOfWeek.toString().toLowerCase();
        Day today = Day.valueOf(dayName);
        // find time till all order arrives by max time supplier
        for (Supplier sup : combination.keySet())
            if (sup.daysTillArrives(today) > time)
                time = sup.daysTillArrives(today);
       // return time till all combination arrives
        return time;
    }

    // this method creates new periodic report and returns it
    public PeriodicReport createPeriodicReport(int reportId, int branchNumber, Supplier supp,  Day day, HashMap<Integer,Integer> items)
    {
        if (branchNumber <0 || supp == null || items.isEmpty())
            return null;

        HashMap<SupplierItem, Integer> itemsInReport = new HashMap<>();
        // for each item in the list given, find the match to supplier item and insert to the hash map
        for (Integer marketID : items.keySet())
        {
            itemsInReport.put(supp.findMatchToMarketItem(marketID), items.get(marketID));
        }
        PeriodicReport report = new PeriodicReport(reportId,branchNumber, day, supp,  itemsInReport);
        // update supplier
        return report;
    }


    // this method create new order based on given periodic report
    public Order createPeriodicOrder(PeriodicReport report)
    {
        // Supplier orderSupplier, LinkedList<OrderItem> orderItems, int branchNumber
        if (report == null)
            return null;
        // extract the supplioer from the report
        Supplier supp = report.getSupplier();
        // extract items
        HashMap<SupplierItem, Integer> items = report.getItems();
        // extract branch number
        int branchNumber = report.getBranchNumber();
        // first, create list of pairs of market id and amount from each item
        LinkedList<Pair<Integer, Integer>> itemsList = new LinkedList<Pair<Integer, Integer>>();

        for (Map.Entry<SupplierItem, Integer> entry : items.entrySet()) {
            Integer marketId = entry.getKey().GetMarketId();
            Integer quantity = entry.getValue();
            Pair<Integer, Integer> supAndItem = new Pair(marketId, quantity);
            itemsList.add(supAndItem);
        }
        Pair<Supplier, LinkedList<Pair<Integer, Integer>>> supAndItems = Pair.create(supp, itemsList);
        // creating the order
        return makeOrderForSupplier(supAndItems, branchNumber);
    }


    //this function creates all the orders for all the periodic reports that their day to order is today.
    public LinkedList<Order> createAllPeriodicOrdersOfToday(LinkedList<PeriodicReport> allReports)
    {
        LinkedList<Order> periodicOrdersCreatedToday = new LinkedList<>();
        // finding current day
        LocalDate date = LocalDate.now();
        DayOfWeek currentDayOfWeek = date.getDayOfWeek();
        String dayOfWeekString = currentDayOfWeek.toString();
        if(allReports.isEmpty())
            return periodicOrdersCreatedToday;
        for(int i=0; i<allReports.size();i++)
        {
            Day dayToOrder = allReports.get(i).getDayToOrder();
            String dayToOrderString = dayToOrder.toString();
            if(dayToOrderString.equalsIgnoreCase(dayOfWeekString))//meaning the day to order is today
            {
                periodicOrdersCreatedToday.add(this.createPeriodicOrder(allReports.get(i)));
            }
        }
        return periodicOrdersCreatedToday;
    }



    // this method receives periodic report and checks if the report was created on the same day it need to be sent
    // if so, make an order and returns the new order
    public Order createOrderOfNewPeriodic(PeriodicReport report)
    {
        if (report == null)
            return null;
        // if the day the report need to be sent is the day today, make a new order
        LocalDate date = LocalDate.now();
        DayOfWeek currentDayOfWeek = date.getDayOfWeek();
        String dayOfWeekString = currentDayOfWeek.toString();
        // check if the day to make the order is today
        if(report.getDayToOrder().toString().equalsIgnoreCase(dayOfWeekString))
            return createPeriodicOrder(report);
        return null;
    }




}


