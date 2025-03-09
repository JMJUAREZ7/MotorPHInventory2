package MotorPh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Inventory {
    
    public static Comparator<Inventory> ENGINE_COMPARATOR = 
        Comparator.comparing(Inventory::getEngine, String.CASE_INSENSITIVE_ORDER);

    private Date date;
    private boolean stock; 
    private String bName;
    private String engine;
    private boolean status; 

    public Inventory(Date dateEntered, boolean stock, String bName, String engineId, boolean status) {
        this.date = dateEntered;
        this.stock = stock;
        this.bName = bName;
        this.engine = engineId;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public boolean getStock() {
        return stock;
    }

    public String getBrand() {
        return bName;
    }

    public String getEngine() {
        return engine;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%-13s %-9s %-12s %-12s %-10s",
            dateFormat.format(date),
            stock ? "New" : "Old",
            bName,
            engine,
            status ? "On-Hand" : "Sold");
    }

    public static Inventory fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(parts[0].trim());
                boolean stock = Boolean.parseBoolean(parts[1].trim());
                String bName = parts[2].trim();
                String engine = parts[3].trim();
                boolean status = Boolean.parseBoolean(parts[4].trim());
                if (bName.isEmpty() || engine.isEmpty()) {
                    throw new IllegalArgumentException("Invalid line format: " + line);
                }
                return new Inventory(date, stock, bName, engine, status);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + parts[0].trim(), e);
            }
        } else {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
    }
}
