package model;

import java.io.*;
import java.util.*;

public class ParcelMap {
    private List<Parcel> parcels;

    //Creates the List for the parcelsss
    public ParcelMap() {
        parcels = new ArrayList<>();
    }

    // Load parcels from a CSV parcels file
    public void loadParcelsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
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
    
    //Finding parcel by ID method
    public Parcel findParcelByID(String parcelId) {
        for (Parcel parcel : parcels) {
            if (parcel.getParcelID().equals(parcelId)) {
                return parcel;
            }
        }
        return null;  // if no parcel found
    }

}
