package MotorPh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Insert {
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final Comparator<Inventory> CURRENT_COMPARATOR = 
        Comparator.comparing(Inventory::getDate)
            .thenComparing(Inventory::getBrand)
            .thenComparing(Inventory::getEngine);

    public static void addItems(Scanner scanner, List<Inventory> inventoryList) {
        int n; 
        while (true) {
            System.out.print("How many items to add?: ");
            try {
                n = scanner.nextInt();
                scanner.nextLine(); 
                break; 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); 
            }
        }

        for (int i = 0; i < n; i++) {
            Date date = getDateInput(scanner, "Date entered (yyyy-MM-dd): ");
            boolean stock = getBooleanInput(scanner, "New or Old stock (1=new/0=old): ");
            String brand = getNonEmptyInput(scanner, "Brand (cannot be empty): ");
            String engine = getUniqueEngineInput(scanner, inventoryList, "Engine Number (cannot be empty): ");
            boolean status = getBooleanInput(scanner, "Available/sold (1=available/0=sold): ");

            Inventory temp = new Inventory(date, stock, brand, engine, status);
            boolean confirmed = false;

            while (!confirmed) {
                System.out.println("Please review the entered item:");
                System.out.println("============================================================");
                System.out.printf("%-13s %-9s %-12s %-12s %-10s%n", "Date", "Stock", "Brand", "Engine", "Status");
                System.out.println(temp); 
                System.out.println("============================================================");
                System.out.print("Is this item correct? (1=yes/2=no): ");
                String userConf = scanner.nextLine();

                if (userConf.equals("1")) {
                    insertInOrder(inventoryList, temp);
                    System.out.println("Item added successfully.");
                    confirmed = true;
                } else {                    
                    boolean keepEditing = true;
                    while (keepEditing) {
                        System.out.println("Select the field you want to edit:");
                        System.out.println("1: Date");
                        System.out.println("2: Stock");
                        System.out.println("3: Brand");
                        System.out.println("4: Engine Number");
                        System.out.println("5: Status");
                        System.out.println("6: Cancel editing and discard this item");
                        System.out.print("Your choice: ");
                        String choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                date = getDateInput(scanner, "Enter new Date (yyyy-MM-dd): ");
                                break;
                            case "2":
                                stock = getBooleanInput(scanner, "Enter new Stock (1=new/0=old): ");
                                break;
                            case "3":
                                brand = getNonEmptyInput(scanner, "Enter new Brand: ");
                                break;
                            case "4":
                                engine = getUniqueEngineInput(scanner, inventoryList, "Enter new Engine Number: ");
                                break;
                            case "5":
                                status = getBooleanInput(scanner, "Enter new Status (1=available/0=sold): ");
                                break;
                            case "6":
                                System.out.println("Discarding this item.");
                                keepEditing = false;
                                confirmed = true; 
                                temp = null;
                                break;
                            default:
                                System.out.println("Invalid option, please try again.");
                                continue;
                        }

                        if (temp != null) {
                            temp = new Inventory(date, stock, brand, engine, status);
                        }

                        if (temp != null) {
                            System.out.println("Updated item:");
                            System.out.println(temp);
                            System.out.print("Is this updated item correct? (1=yes/2=no): ");
                            String finalConf = scanner.nextLine();
                            if (finalConf.equals("1")) {
                                insertInOrder(inventoryList, temp);
                                System.out.println("Item added successfully.");
                                keepEditing = false;
                                confirmed = true;
                            }
                        }
                    } 
                }
            }

            // Stop loop functionality
            if (i < n - 1) {
                while (true) {
                    System.out.print("Continue adding next item? (1=yes/0=no): ");
                    String answer = scanner.nextLine().trim();
                    if (answer.equals("0")) {
                        System.out.println("Stopping item addition early.");
                        return;
                    } else if (answer.equals("1")) {
                        break;
                    }
                    System.out.println("Please enter '1' or '0'.");
                }
            }
        }
    }

    // Helper methods
    public static String getNonEmptyInput(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println("Input cannot be empty!");
        } while (input.isEmpty());
        return input;
    }

    public static Date getDateInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return DATE_FORMAT.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    public static boolean getBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("1")) return true;
            if (input.equals("0")) return false;
            System.out.println("Invalid input. Please enter '1' or '0'.");
        }
    }

    private static String getUniqueEngineInput(Scanner scanner, List<Inventory> list, String prompt) {
        while (true) {
            String engine = getNonEmptyInput(scanner, prompt);
            if (list.stream().noneMatch(i -> i.getEngine().equalsIgnoreCase(engine))) {
                return engine;
            }
            System.out.println("Engine number must be unique!");
        }
    }

    private static void insertInOrder(List<Inventory> list, Inventory item) {
        int index = Collections.binarySearch(list, item, CURRENT_COMPARATOR);
        list.add(index < 0 ? -index - 1 : index, item);
    }
}