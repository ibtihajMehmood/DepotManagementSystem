package model;

import java.io.*;
import java.util.*;

public class QueueofCustomers {
    private Queue<Customer> customerQueue;

    public QueueofCustomers() {
        customerQueue = new LinkedList<>();
    }

    // Method to load customers from the CSV file named customers.csv
    public void loadCustomersFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); 
            
            List<Customer> tempCustomerList = new ArrayList<>();
            
            while ((line = br.readLine()) != null) {
                String[] customerData = line.split(",");
                String firstName = customerData[0].trim(); //firstname
                String lastName = customerData[1].trim(); //lastname
                String parcelID = customerData[2].trim(); //parcelID
                tempCustomerList.add(new Customer(firstName, lastName, parcelID));
            }
            
            // Sort the customer list by customer surname
            tempCustomerList.sort(Comparator.comparing(Customer::getLastName));
            
            // Clear the current queue and add sorted customers
            customerQueue.clear();
            customerQueue.addAll(tempCustomerList);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Getter for the customer queue
    public List<Customer> getCustomerQueue() {
        return new ArrayList<>(customerQueue);
    }
}
