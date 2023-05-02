package SuperLi.src.BusinessLogic;

import SuperLi.src.DataAccess.OrderDataMapper;
import org.graalvm.collections.Pair;

import java.security.InvalidParameterException;
import java.util.*;

public class OrderManagment {
    private static OrderManagment instance = new OrderManagment();
    private OrderDataMapper orderDataMapper;

    private OrderManagment() {
        this.orderDataMapper = OrderDataMapper.getInstance();
    }

    public OrderManagment getInstance() {
        return instance;
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

    public static HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> startOrderProcess(LinkedList<Pair<Integer, Integer>> orderedItems, LinkedList<Supplier> suppliers) throws Exception {
        LinkedList<Pair<Integer, Integer>> itemsSeperateToUnits = itemsSeperateOrCompletly(orderedItems, suppliers).getLeft();//a list of all the items we need to separate.
        LinkedList<Pair<Integer, Integer>> itemsCompletely = itemsSeperateOrCompletly(orderedItems, suppliers).getRight();//a list of all the items that there is at least one supplier who can supply it completely.
        //combinations for itemsCompletely
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> listCombinationsFullItems = new LinkedList<>();
        combinationsForFullItems(suppliers, itemsCompletely, listCombinationsFullItems, new HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>());
        if (itemsSeperateToUnits.isEmpty())//meaning there is no item we need to separate to units. therefore we would like to check maybe there is a supplier that can supply all ordered items.
        {
            LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> listOfoneSupplierSuppliesAll = isOneSupplierCanSupplyAllExists(listCombinationsFullItems);
            if (!listOfoneSupplierSuppliesAll.isEmpty()) {
                return findCheappestCombination(listOfoneSupplierSuppliesAll);
            }
        }
        for (int i = 0; i < listCombinationsFullItems.size(); i++)//running over each combination of separating the full items
        {
            for (int j = 0; j < itemsSeperateToUnits.size(); j++)//for each partial item
            {
                LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItem = new LinkedList<>();
                //list of all the combinations for this current partial item
                LinkedList<Supplier> fullCombiSuppliers = new LinkedList<>();
                fullCombiSuppliers = createSuppliersList(listCombinationsFullItems.get(i).keySet());
                listCombinationsForSeperateItem = combinationsForPartialItems(fullCombiSuppliers, itemsSeperateToUnits.get(j).getLeft(), itemsSeperateToUnits.get(j).getRight(), new HashMap<Supplier, Integer>(), new LinkedList<HashMap<Supplier, Integer>>());
                if (listCombinationsForSeperateItem.isEmpty())//meaning there is no combination to supply this partial item with these suppliers
                {
                    LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItemAmongALLsuppliers = new LinkedList<>();
                    listCombinationsForSeperateItemAmongALLsuppliers = combinationsForPartialItems(suppliers, itemsSeperateToUnits.get(j).getLeft(), itemsSeperateToUnits.get(j).getRight(), new HashMap<Supplier, Integer>(), new LinkedList<HashMap<Supplier, Integer>>());
                    if (listCombinationsForSeperateItemAmongALLsuppliers.isEmpty())// meaning there is no way to supply this item
                        throw new Exception("Can't supply item with the id: " + itemsSeperateToUnits.get(j).getLeft());
                    else {
                        HashMap<Supplier, Integer> cheapestCombi = findTheCheapestSeparatingItemToUnitsCombination(itemsSeperateToUnits.get(j).getLeft(), listCombinationsForSeperateItemAmongALLsuppliers);
                        AddSuppliersToCurrentCombination(itemsSeperateToUnits.get(j).getLeft(), cheapestCombi, listCombinationsFullItems.get(i));
                    }
                } else {
                    HashMap<Supplier, Integer> cheapestCombination = findTheCheapestSeparatingItemToUnitsCombination(itemsSeperateToUnits.get(j).getLeft(), listCombinationsForSeperateItem);
//                    this.checkSuppliersOnBothCombinations(itemsSeperateToUnits.get(j).getLeft(), listCombinationsForSeperateItem, listCombinationsFullItems.get(i));
                    addPartialCombiToFullCombi(itemsSeperateToUnits.get(j).getLeft(), cheapestCombination, listCombinationsFullItems.get(i));
                }
            }
        }
        //at this point we have all the combinations including all full items and partial items.
        return findCheappestCombination(listCombinationsFullItems);
    }

    private static HashMap<Supplier, Integer> findTheCheapestSeparatingItemToUnitsCombination(int itemId, LinkedList<HashMap<Supplier, Integer>> listCombinationsForSeperateItem) {
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

    private static void AddSuppliersToCurrentCombination(int itemId, HashMap<Supplier, Integer> combinationOfSeparatingItem, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationFullItems) {
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
                ;
                combinationFullItems.remove(sup);
                combinationFullItems.put(sup, tempi);
            }
        }
    }

    //this
    private static LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> isOneSupplierCanSupplyAllExists(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> listCombinationsFullItems) {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> returnList = new LinkedList<>();
        for (int i = 0; i < listCombinationsFullItems.size(); i++) {
            if (listCombinationsFullItems.get(i).size() == 1) {
                returnList.add(listCombinationsFullItems.get(i));
            }
        }
        return returnList;
    }

    private static LinkedList<Supplier> createSuppliersList(Set<Supplier> sup) {
        LinkedList<Supplier> suppliers = new LinkedList<>();
        for (Supplier s : sup) {
            suppliers.add(s);
        }
        return suppliers;
    }

    //    private HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> checkSuppliersOnBothCombinations(int itemId, LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,Integer>> listCombinationsForOneSeperateItem, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combinationFullItems)
    private void checkSuppliersOnBothCombinations(int itemId, LinkedList<HashMap<Supplier, Integer>> listCombinationsForOneSeperateItem, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationFullItems) {

        for (int i = 0; i < listCombinationsForOneSeperateItem.size(); i++) //running over all the combinations for separating the item
        {
//            if(isFullCombiContainsPartialCombi(listCombinationsForOneSeperateItem.get(i), combinationFullItems))
//            {
//                combinationFullItems = this.addPartialCombiToFullCombi(itemId, listCombinationsForOneSeperateItem.get(i), combinationFullItems);
            this.addPartialCombiToFullCombi(itemId, listCombinationsForOneSeperateItem.get(i), combinationFullItems);
//            }
        }
    }

    //    private HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> addPartialCombiToFullCombi(int itemId, HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> partialItemCombi, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> fullItemCombi)
    private static void addPartialCombiToFullCombi(int itemId, HashMap<Supplier, Integer> partialItemCombi, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> fullItemCombi) {
//        System.out.println("check this:");
//        System.out.println(partialItemCombi);
//        System.out.println(fullItemCombi);
//        HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> temp = new HashMap<>(fullItemCombi);
//        HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> t = new HashMap<>();
        for (Supplier sup : partialItemCombi.keySet()) {
//            fullItemCombi.get(sup).add(Pair.create(itemId,partialItemCombi.get(sup)));
            LinkedList<Pair<Integer, Integer>> temp = new LinkedList<>();
            temp = (LinkedList<Pair<Integer, Integer>>) fullItemCombi.get(sup).clone();
            temp.add(Pair.create(itemId, partialItemCombi.get(sup)));
            ;
            fullItemCombi.remove(sup);
            fullItemCombi.put(sup, temp);
//            temp.add(Pair.create(itemId,partialItemCombi.get(sup)));//OLAY ZRICHA
//            t.get(sup).add(Pair.create(itemId,partialItemCombi.get(sup)));
//            t.put(sup,temp);
        }
//        return t;
    }

    //not needed if the pizul is in my shita:
//    private boolean isFullCombiContainsPartialCombi(HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> partialItemCombi, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> fullItemCombi)
//    {
//        for (SuperLi.src.BusinessLogic.Supplier sup : partialItemCombi.keySet())
//        {
//            if(!fullItemCombi.containsKey(sup))
//                return false;
//        }
//        return true;
//    }
    //this method returns pair with the intersection group and the difference group of supplier's items and the ordered items.
    private static Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> getIntersectionAndDifferenceItems(Supplier sup, LinkedList<Pair<Integer, Integer>> items) {
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
    public static LinkedList<HashMap<Supplier, Integer>> combinationsForPartialItems(LinkedList<Supplier> suppliers, int itemId, int quantity, HashMap<Supplier, Integer> combination, LinkedList<HashMap<Supplier, Integer>> resultList) {
        if (quantity <= 0) {
//            System.out.println("combination: "+combination);
//            resultList.add(combination);
//            System.out.println("resultlist: "+ resultList);
            HashMap<Supplier, Integer> temp = new HashMap<>(combination);
            resultList.add(temp);
            return resultList;
        }
        Pair<Integer, Integer> max = findSupplierWithMaxUnitsOfItem(suppliers, itemId);
        LinkedList<Pair<Integer, Integer>> maxSup = allSuppliersWithSameNumOfUnits(max.getRight(), suppliers, itemId);
        for (int i = 0; i < maxSup.size(); i++) {
//            System.out.println(suppliers.get(maxSup.get(i).getLeft()) + ": " + maxSup.get(i).getRight());
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
//            this.ITryMyBestOK(sups, itemId, quantity-sendQ,combination,resultList);
            resultList = combinationsForPartialItems(sups, itemId, quantity - sendQ, combination, resultList);
//            combi = new HashMap<>();
//            resultList.add(this.ITryMyBestOK(sups, itemId, quantity-sendQ,combination,resultList));
//            System.out.println("resultlist: "+resultList);
            combination.remove(suppliers.get(maxSup.get(i).getLeft()));
        }
        return resultList;
    }

    private static Pair<Integer, Integer> findSupplierWithMaxUnitsOfItem(LinkedList<Supplier> suppliers, int itemId) {
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

    private static LinkedList<Pair<Integer, Integer>> allSuppliersWithSameNumOfUnits(int numUnits, LinkedList<Supplier> suppliers, int itemId) {
        LinkedList<Pair<Integer, Integer>> listSup = new LinkedList<>();
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getNumberOfItemUnitsCanSupply(itemId) == numUnits) {
                listSup.add(Pair.create(i, numUnits));
            }
        }
        return listSup;
    }

    public static void combinationsForFullItems(LinkedList<Supplier> suppliers, LinkedList<Pair<Integer, Integer>> items, LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> resultList, HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination) {
        for (int i = 0; i < suppliers.size(); i++) {
            combination = new HashMap<>();
            Pair<LinkedList<Pair<Integer, Integer>>, LinkedList<Pair<Integer, Integer>>> pair = getIntersectionAndDifferenceItems(suppliers.get(i), items);
            combination.put(suppliers.get(i), pair.getLeft());
            LinkedList<Supplier> sups = new LinkedList<>(suppliers);
//            sups = suppliers;
            sups.remove(i);
            LinkedList<Pair<Integer, Integer>> leftItems1 = pair.getRight();
            LinkedList<Pair<Integer, Integer>> leftItems2 = new LinkedList<>(leftItems1);
            HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combi = new HashMap<>(combination);
////            combi = combination;
////            this.recTry(sups);
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


    //    public LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>>> listOfAllCombinations(LinkedList<SuperLi.src.BusinessLogic.Supplier> suppliers, LinkedList<Pair<Integer,Integer>> items, LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>>> resultList, HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combination)
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
//        if (pair.getLeft().isEmpty())//if intersection is empty
//            continue;
        LinkedList<Supplier> sups = suppliers;
        sups.remove(0);
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combi = combination;
        combi.put(supplier, pair.getLeft());
        combination.put(supplier, pair.getLeft());
//    suppliers.remove(index);
//    System.out.println(pair.getRight());
        listOfAllCombinations(sups, pair.getRight(), resultList, combination);
        listOfAllCombinations(suppliers, items, resultList, combi);
//        //stop crieteria
//        if(items.isEmpty()) {
//            resultList.add(combination);
//            return resultList;
//        }
//        if(suppliers.isEmpty())
//            return resultList;
//        for (int i=0; i<suppliers.size();i++)
//        {
//            SuperLi.src.BusinessLogic.Supplier supplier = suppliers.get(i);
//            Pair<LinkedList<Pair<Integer,Integer>>,LinkedList<Pair<Integer,Integer>>> pair = this.getIntersectionAndDifferenceItems(supplier,items);
//            if (pair.getLeft().isEmpty())//if intersection is empty
//                continue;
//            LinkedList<SuperLi.src.BusinessLogic.Supplier> sups = suppliers;
//            sups.remove(i);
//            combination.put(supplier,pair.getLeft());
//            resultList = listOfAllCombinations(sups,pair.getRight(),resultList,combination);
//        }
//        return resultList;
////        return null;
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
//            HashMap<SuperLi.src.BusinessLogic.Supplier, LinkedList<Integer>> updated
//            findSuppliersCombination(suppliersPossiblitiesSorted.remove(entry.getKey()), updatedMissingItems);
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

    public static double findCostOfCombination(HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination) {
        if (combination == null || combination.isEmpty())
            return -1;
        double totalCostOfCombi = 0;
        for (Map.Entry<Supplier, LinkedList<Pair<Integer, Integer>>> entry : combination.entrySet()) {
            totalCostOfCombi += findCostOfOrder(Pair.create(entry.getKey(), entry.getValue()));
        }
        return totalCostOfCombi;
    }

    // given supplier and list of items he can supply (market id and amount), returns how much it will cost after discounts
    private static double findCostOfOrder(Pair<Supplier, LinkedList<Pair<Integer, Integer>>> order) {
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
    private static LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> findMinimal(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> allCombis) {
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
    public static HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> findCheappestCombination(LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> allCombies) {
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
    private static Order makeOrderForSupplier(Pair<Supplier, LinkedList<Pair<Integer, Integer>>> supAndItems, int branchNumber) {
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
            OrderItem currItem = new OrderItem(supplier, suppItem, amount, discountValue, finalPrice);
            orderItems.add(currItem);
        }
        Order order = new Order(supplier, orderItems, branchNumber);
        OrderDiscount bestOrderDiscount = supplier.getSupplierContract().getDiscountDocument().
                bestOrderDiscount(amount, order.getCostOfOrder());
        if (bestOrderDiscount != null) {
            double costAfterDiscount = bestOrderDiscount.GetPriceAfterDiscount(order.getCostOfOrder());
            order.setCostOfOrder(costAfterDiscount);
        }
        return order;
    }

    // given the best combination to order, returns list of orders
    private static LinkedList<Order> getOrdersForCombi(HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationToOrder, int branchNumber) {

        LinkedList<Order> orders = new LinkedList<>();
        for (Map.Entry<Supplier, LinkedList<Pair<Integer, Integer>>> curr : combinationToOrder.entrySet()) {
            Supplier supplier = curr.getKey();
            LinkedList<Pair<Integer, Integer>> linkedList = curr.getValue();
            Pair<Supplier, LinkedList<Pair<Integer, Integer>>> pair = Pair.create(supplier, linkedList);
            orders.add(makeOrderForSupplier(pair, branchNumber));
        }
        return orders;
    }

    public static LinkedList<Order> creatOrder(LinkedList<Pair<Integer, Integer>> missingItems, int branchNumber) throws Exception {
        if (missingItems.isEmpty())
            return null;
        // TODO - need to ad somewhere calling the time method
        LinkedList<Order> orders = new LinkedList<>();
        HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combinationToOrder = OrderManagment.startOrderProcess(missingItems, SystemManagment.allSuppliers);
        orders = getOrdersForCombi(combinationToOrder, branchNumber);
        return orders;
    }

    public static void confirmOrders(LinkedList<Order> orders, int branchNumber) {
        // updates all the relevent databases with the new order
        for (Order order : orders) {
            SystemManagment.allOrders.add(order);
            order.getOrderSupplier().addOrder(order);
            SystemManagment.allBranches.get(branchNumber).addOrder(order);
        }
    }

    // TODO - ALL NEXT SIGNATURE ARE FOR THE NEW HANFAZOT

    // this method receives all combinations and filter to only combinations with minimal arriving time
    // TODO - need to check this method!
    public LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> getFastestCombos(LinkedList<HashMap<Supplier,
            LinkedList<Pair<Integer, Integer>>>> allCombis)
    {
        LinkedList<HashMap<Supplier, LinkedList<Pair<Integer, Integer>>>> result = new LinkedList<>();
        int minTimeOfArrival = Integer.MAX_VALUE;

        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis) {
             int curr = findArrivalTimeOfCombo(combination);
             if (curr < minTimeOfArrival)
                 minTimeOfArrival = curr;
        }

        for (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination : allCombis)
        {
            int curr = findArrivalTimeOfCombo(combination);
            if (curr == minTimeOfArrival)
                result.add(combination);
        }

        return result;
        // finding the fastest time
        // filter to all fastest combinations

    }


     // find the max arrival time in the combination and returns it
    private int findArrivalTimeOfCombo (HashMap<Supplier, LinkedList<Pair<Integer, Integer>>> combination)
    {
        int time = 0;
        // need to find the day today
        //for (Supplier sup : combination.keySet())
            //if (sup.daysTillArrives(today) > time)
            // time = sup.daysTill...

            

        return time;
    }


    // TODO - ALL NEXT SIGNATURES ARE FOR PERIODIC REPORT!

    // creating report- logic part
    public PeriodicReport createPeriodicReport(int suppId, LinkedList<Pair<Integer, Integer>> items) //TODO parameter
    {
        return null;
    }

    // creating order- logic part
    public Order createPeriodicOrder(PeriodicReport report)
    {
        return null;
    }

}


