package MotorPh;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Sort {

    private static final int THRESHOLD = 32;

    public static void sortInventory(List<Inventory> inventoryList, int option) {
        switch (option) {
            case 1: //By brand
                System.out.println("Sorting the inventory by brand...");
                Input.currentComparator = Comparator.comparing(Inventory::getBrand);
                hybridSort(inventoryList, 0, inventoryList.size() - 1, Input.currentComparator);
                break;
            case 2: //By engine
                System.out.println("Sorting the inventory by engine...");
                Input.currentComparator = Comparator.comparing(Inventory::getEngine, String.CASE_INSENSITIVE_ORDER);
                hybridSort(inventoryList, 0, inventoryList.size() - 1, Input.currentComparator);
                break;
            default:
                System.out.println("Invalid option. Please choose 1 for brand or 2 for engine.");
                return;
        }

        // Display 
        for (Inventory inv : inventoryList) {
            System.out.println(inv);
        }
    }

    private static void hybridSort(List<Inventory> inventoryList, int left, int right, Comparator<Inventory> comparator) {
        if (right - left < THRESHOLD) {
            insertionSort(inventoryList, left, right, comparator);
        } else {
            if (left < right) {
                int mid = (left + right) / 2;
                hybridSort(inventoryList, left, mid, comparator);
                hybridSort(inventoryList, mid + 1, right, comparator);
                merge(inventoryList, left, mid, right, comparator);
            }
        }
    }

    private static void insertionSort(List<Inventory> inventoryList, int left, int right, Comparator<Inventory> comparator) {
        for (int i = left + 1; i <= right; i++) {
            Inventory key = inventoryList.get(i);
            int j = i - 1;
            while (j >= left && comparator.compare(inventoryList.get(j), key) > 0) {
                inventoryList.set(j + 1, inventoryList.get(j));
                j--;
            }
            inventoryList.set(j + 1, key);
        }
    }

    private static void merge(List<Inventory> inventoryList, int left, int mid, int right, Comparator<Inventory> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Inventory> leftList = new ArrayList<>(n1);
        List<Inventory> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++) {
            leftList.add(inventoryList.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(inventoryList.get(mid + 1 + j));
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare(leftList.get(i), rightList.get(j)) <= 0) {
                inventoryList.set(k, leftList.get(i));
                i++;
            } else {
                inventoryList.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            inventoryList.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            inventoryList.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
}
