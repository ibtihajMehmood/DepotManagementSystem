package model;

import java.io.*;
import java.util.*;

public class ParcelMap {
    private List<Parcel> parcels;

    public ParcelMap() {
        parcels = new ArrayList<>();
    }

    // Load parcels from a CSV parcels file
    public void loadParcelsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); //skip for the place of the headers
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
                    parcels.add(new Parcel(
                        values[0],                      // parcelID
                        Double.parseDouble(values[1]),  // weight
                        Double.parseDouble(values[2]),  // width
                        Double.parseDouble(values[3]),  // length
                        Double.parseDouble(values[4]),  // height
                        Integer.parseInt(values[5]),    // daysInDepo
                        values[6]                       // status
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading parcels: " + e.getMessage());
        }
    }

    //getter for the parcel list
    public List<Parcel> getParcels() {
        return parcels;
    }
}
