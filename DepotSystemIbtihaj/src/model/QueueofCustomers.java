package model;

import java.io.*;
import java.util.*;

public class QueueofCustomers {
    private Queue<Customer> customerQueue;

    public QueueofCustomers() {
        customerQueue = new LinkedList<>();
    }

    // Load customers from the CSV file named customers.csv
    public void loadCustomersFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            
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
            customerQueue.clear();
            customerQueue.addAll(tempCustomerList);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Adding the Customer
    public void addCustomer(Customer newCustomer) {
        customerQueue.add(newCustomer);
    }
    
    //Removing the customer
    public boolean removeCustomer(String parcelID) {
        return customerQueue.removeIf(customer -> customer.getParcelID().equals(parcelID));
    }

    public List<Customer> getCustomerQueue() {
        return new ArrayList<>(customerQueue);
    }

}
