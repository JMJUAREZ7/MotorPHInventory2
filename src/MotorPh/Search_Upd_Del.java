package MotorPh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Search_Upd_Del {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static boolean isSorted(List<Inventory> inventoryList, Comparator<Inventory> comparator) {
        for (int i = 1; i < inventoryList.size(); i++) {
            if (comparator.compare(inventoryList.get(i - 1), inventoryList.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static Date getDateInput(Scanner scanner, String prompt) {
        while(true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            try {
                return DATE_FORMAT.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date input. Please enter a valid date in yyyy-MM-dd format.");
            }
        }
    }

    
    public static boolean getBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("yes")) {
                return true;
            } else if (input.equalsIgnoreCase("false") || input.equalsIgnoreCase("no")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }
    }

    //Non-empty input validation.
    public static String getNonEmptyInput(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid value.");
            }
        } while (input.isEmpty());
        return input;
    }
    
    public static String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    public static void searchInventory(Scanner scanner, List<Inventory> inventoryList) {
        System.out.print("Enter engine number to search: ");
        String searchEngine = scanner.nextLine();
        System.out.println("=====================================================================");

        if (inventoryList.isEmpty()) {
            System.out.println("Inventory list is empty.");
            System.out.println("=====================================================================");
            return;
        }
        
        //Engine comparator for binary search.
        Comparator<Inventory> engineComparator = Comparator.comparing(Inventory::getEngine, String.CASE_INSENSITIVE_ORDER);
        int foundIndex = -1;
        
        if (isSorted(inventoryList, engineComparator)) {
            Inventory key = new Inventory(null, false, "", searchEngine, false);
            foundIndex = Collections.binarySearch(inventoryList, key, engineComparator);
        } else {
            for (int i = 0; i < inventoryList.size(); i++) {
                if (inventoryList.get(i).getEngine().equalsIgnoreCase(searchEngine)) {
                    foundIndex = i;
                    break;
                }
            }
        }
        
        if (foundIndex < 0) {
            System.out.println("Item with engine '" + searchEngine + "' not found.");
            System.out.println("=====================================================================");
            return;
        }
        
        Inventory item = inventoryList.get(foundIndex);
        System.out.printf("             %-13s %-9s %-12s %-12s %-10s%n", "Date", "Stock", "Brand", "Engine", "Status");
        System.out.print("Item found:  ");
        System.out.println(item);
        System.out.println("=====================================================================");
        
        System.out.println("What would you like to do with this item?");
        System.out.println("1: Delete this item");
        System.out.println("2: Update this item");
        System.out.println("Any other input: Cancel");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        
        if (choice.equals("1")) {
            System.out.println("You have chosen to delete this item:");
            System.out.println("=====================================================================");
            System.out.printf("%-13s %-9s %-12s %-12s %-10s%n", "Date", "Stock", "Brand", "Engine", "Status");
            System.out.println(item);
            System.out.println("=====================================================================");
            System.out.print("Are you sure you want to delete this item? ((1)yes/(0)no): ");
            String confirmDel = scanner.nextLine();
            if (confirmDel.equalsIgnoreCase("yes") || confirmDel.equals("1")) {
                inventoryList.remove(foundIndex);
                System.out.println("Item successfully deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else if (choice.equals("2")) {
            Date newDate = item.getDate();
            boolean newStock = item.getStock();
            String newBrand = item.getBrand();
            boolean newStatus = item.getStatus();

            boolean updating = true;
            while (updating) {
                System.out.println("\nSelect the field you want to update:");
                System.out.println("1: Date (current: " + DATE_FORMAT.format(newDate) + ")");
                System.out.println("2: Stock (current: " + newStock + ")");
                System.out.println("3: Brand (current: " + newBrand + ")");
                System.out.println("4: Status (current: " + newStatus + ")");
                System.out.println("5: Confirm update and exit");
                System.out.println("6: Cancel update");
                System.out.print("Enter your choice: ");
                String updateChoice = scanner.nextLine();

                switch (updateChoice) {
                    case "1":
                        newDate = getDateInput(scanner, "Enter new date (yyyy-MM-dd): ");
                        break;
                    case "2":
                        newStock = getBooleanInput(scanner, "Enter new stock value (true/false): ");
                        break;
                    case "3":
                        newBrand = getNonEmptyInput(scanner, "Enter new Brand (cannot be empty): ");
                        break;
                    case "4":
                        newStatus = getBooleanInput(scanner, "Enter new status (true/false): ");
                        break;
                    case "5":
                        System.out.println("\nUpdated item details:");
                        System.out.println("Date: " + DATE_FORMAT.format(newDate));
                        System.out.println("Stock: " + newStock);
                        System.out.println("Brand: " + newBrand);
                        System.out.println("Engine: " + searchEngine);
                        System.out.println("Status: " + newStatus);
                        System.out.print("Confirm update? (1=yes/0=no): ");
                        String confirmUp = scanner.nextLine();
                        if (confirmUp.equalsIgnoreCase("yes") || confirmUp.equals("1")) {
                            inventoryList.set(foundIndex, new Inventory(newDate, newStock, newBrand, searchEngine, newStatus));
                            System.out.println("Item successfully updated.");
                        } else {
                            System.out.println("Update cancelled.");
                        }
                        updating = false;
                        break;
                    case "6":
                        System.out.println("Update cancelled, no changes applied.");
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            }
        } else {
            System.out.println("Operation cancelled.");
        }
        System.out.println("=====================================================================");
    }
}
