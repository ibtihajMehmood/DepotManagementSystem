package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import model.*;

public class ManagerGUI extends JFrame {

    private QueueofCustomers queueOfCustomers;
    private ParcelMap parcelMap;
    private JPanel topPanel;
    private JTable parcelTable;
    private JTable customerTable;
    
    private boolean isParcelView = true; //boolean since the buttons for the views are different

    Worker worker = new Worker(); //Object for the Worker class created.
    

    public ManagerGUI() {
        Log.getInstance().logAction("ManagerGUI initialized.");

        queueOfCustomers = new QueueofCustomers();
        queueOfCustomers.loadCustomersFromCSV("src/view/customers.csv");
        Log.getInstance().logAction("Customer List loaded.");

        parcelMap = new ParcelMap();
        parcelMap.loadParcelsFromCSV("src/view/parcels.csv");
        Log.getInstance().logAction("Parcel List loaded.");

        Log.getInstance().logAction("Setting up the initial Management System Dashboard.");
        setTitle("Depot Management System Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Log.getInstance().logAction("Buttons added on the top of the panel.");
        addTopButtonPanel();
        showParcelList();
    }

    //Method to add buttons to the top panel
    private void addTopButtonPanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); //buttons aligned to the left
        refreshTopButtons();
        add(topPanel, BorderLayout.NORTH); //located at the head of the panel
    }

    //Method to refresh the buttons on the top for the Customer and Parcel View
    private void refreshTopButtons() {
        topPanel.removeAll();

        //Buttons for the Parcel View
        if (isParcelView) {
            JButton addParcelButton = new JButton("Add Parcel"); //adding a parcel 
            JButton removeParcelButton = new JButton("Remove Parcel"); //removing a parcel
            JButton processCustomerButton = new JButton("Process Customer"); //processing the parcel collection for a customer
            JButton searchParcelButton = new JButton("Search Parcel");//searching for a parcel
            JButton writeWaitingListReport = new JButton("Waiting List Report");//loading the report for the waiting list
            JButton CollectedListReport = new JButton("Collected List Report");//loading the report for the list of collected parcels

            //Linking the parcels to the methods
            addParcelButton.addActionListener(e -> addParcel());
            removeParcelButton.addActionListener(e -> removeParcel());
            processCustomerButton.addActionListener(e -> processCustomer());
            searchParcelButton.addActionListener(e -> searchParcel());
            writeWaitingListReport.addActionListener(e -> writeWaitingListReport());
            CollectedListReport.addActionListener(e -> writeCollectedListReport());

            //Buttons on the top panel
            Log.getInstance().logAction("Adding the parcel buttons to the top panel");
            topPanel.add(addParcelButton);
            topPanel.add(removeParcelButton);
            topPanel.add(processCustomerButton);
            topPanel.add(searchParcelButton);
            topPanel.add(writeWaitingListReport);
            topPanel.add(CollectedListReport);

            //if the view is of the customers
        } else {
            JButton addCustomerButton = new JButton("Add Customer"); //add customer to the list
            JButton removeCustomerButton = new JButton("Remove Customer"); //remove the customer from the list
            JButton priceToPayButton = new JButton("Price Breakdown");//price breakdown in terms of discounts and added fee

            addCustomerButton.addActionListener(e -> addCustomer());
            removeCustomerButton.addActionListener(e -> removeCustomer());
            priceToPayButton.addActionListener(e -> showPriceToPay());

            Log.getInstance().logAction("Adding the cusomter buttons to the top panel");
            topPanel.add(addCustomerButton);
            topPanel.add(removeCustomerButton);
            topPanel.add(priceToPayButton);
        }

        revalidate();
        repaint();
    }
    

    
    //Writing the report for the collected parcels
    private void writeCollectedListReport() {
        List<Parcel> collectedParcels = parcelMap.getParcels()
            .stream()
            .filter(parcel -> "collected".equalsIgnoreCase(parcel.getStatus()))
            .collect(Collectors.toList()); //only add the parcel in the text file if it is marked as collected

        String filePath = "src/view/CollectedParcelList.txt"; //filepath

        // The data is not appended, it is cleared when the function is called
        Log.getInstance().logAction("Writing to the text file for a list of parcels that have been collected");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write("Collected List of Parcels\n");
            writer.write("===========================\n");

            if (collectedParcels.isEmpty()) {
                writer.write("No parcels have been collected yet.\n");
                Log.getInstance().logError("No parcels have been collected.");

                //Display the attributes of the parcels in the text file
            } else {
                for (Parcel parcel : collectedParcels) {
                    writer.write(String.format(
                        "Parcel ID: %s, Weight: %s, Width: %s, Length: %s, Height: %s, Days in Depo: %s, Status: %s\n",
                        parcel.getParcelID(),
                        parcel.getWeight(),
                        parcel.getWidth(),
                        parcel.getLength(),
                        parcel.getHeight(),
                        parcel.getDaysInDepo(),
                        parcel.getStatus()
                    ));
                }
                //Calculate the total number of parcels collected
                writer.write("\nTotal Parcels Collected: " + collectedParcels.size() + "\n");
            }

            Log.getInstance().logAction("Report has been generated.");
            JOptionPane.showMessageDialog(
                null,
                "Collected List Report successfully written to " + filePath,
                "Report Generated",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException e) {
            e.printStackTrace();
            Log.getInstance().logError("Report could not be generated.");
            JOptionPane.showMessageDialog(
                null,
                "Failed to write Collected List Report",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Writing the report for parcels that are waiting to be collected
    private void writeWaitingListReport() {
    	//Gets the list of parcels with the status collecting
        List<Parcel> waitingParcels = parcelMap.getParcels()
            .stream()
            .filter(parcel -> "collecting".equalsIgnoreCase(parcel.getStatus()))
            .collect(Collectors.toList());

        String filePath = "src/view/WaitingListofParcels.txt";

        Log.getInstance().logAction("Loads the text file for the ");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Waiting List of Parcels\n");
            writer.write("=========================\n");

            for (Parcel parcel : waitingParcels) {
                writer.write(String.format("Parcel ID: %s, Weight: %s, Width: %s, Length: %s, Height: %s, Days in Depo: %s, Status: %s\n",
                    parcel.getParcelID(),
                    parcel.getWeight(),
                    parcel.getWidth(),
                    parcel.getLength(),
                    parcel.getHeight(),
                    parcel.getDaysInDepo(),
                    parcel.getStatus()));
            }

            writer.write("\nTotal Parcels Waiting for Collection: " + waitingParcels.size() + "\n");

            Log.getInstance().logAction("Waiting List Report has been written successfully.");
            JOptionPane.showMessageDialog(null, "Waiting List Report successfully written to " + filePath, "Report Generated", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.getInstance().logError("Failed to write the report for the waiting list.");
            JOptionPane.showMessageDialog(null, "Failed to write Waiting List Report", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        
        
    //loads the parcel list from the csv file to the table
    private void showParcelList() {
        isParcelView = true;
        refreshTopButtons();

        //headings for the table
        Log.getInstance().logAction("Add headings to the table for the parcel view.");
        String[] columnNames = {"Parcel ID", "Weight", "Width", "Length", "Height", "Days in Depo", "Status"};
        List<Parcel> parcels = parcelMap.getParcels();
        String[][] data = new String[parcels.size()][7];

        //loops through each and every value of the parcel and adds it to the table
        Log.getInstance().logAction("Adding the list of parcels in the table.");
        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            data[i][0] = parcel.getParcelID();
            data[i][1] = String.valueOf(parcel.getWeight());
            data[i][2] = String.valueOf(parcel.getWidth());
            data[i][3] = String.valueOf(parcel.getLength());
            data[i][4] = String.valueOf(parcel.getHeight());
            data[i][5] = String.valueOf(parcel.getDaysInDepo());
            data[i][6] = parcel.getStatus();
        }
        
        parcelTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(parcelTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER); //center aligned

        JButton customerListButton = new JButton("View the Customer List"); //button to take the manager to the customer list
        customerListButton.addActionListener(e -> showCustomerList());
        panel.add(customerListButton, BorderLayout.SOUTH); //south of the panel
        refreshPanel(panel);
    }

   

    //method to add parcels
    private void addParcel() {
        JFrame addParcelFrame = new JFrame("Add Parcel");
        addParcelFrame.setSize(400, 400); //adding parcel panel
        addParcelFrame.setLayout(new GridLayout(7, 2));
        addParcelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Adding the list of text fields
        Log.getInstance().logAction("Adding the text fields.");
        JTextField parcelIDField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField widthField = new JTextField();
        JTextField lengthField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField daysInDepoField = new JTextField();

        //adding the labels for the text inputs
        Log.getInstance().logAction("Adding the labels for the text fields.");
        addParcelFrame.add(new JLabel("Parcel ID (e.g., X001, X123):"));
        addParcelFrame.add(parcelIDField);
        addParcelFrame.add(new JLabel("Weight:"));
        addParcelFrame.add(weightField);
        addParcelFrame.add(new JLabel("Width:"));
        addParcelFrame.add(widthField);
        addParcelFrame.add(new JLabel("Length:"));
        addParcelFrame.add(lengthField);
        addParcelFrame.add(new JLabel("Height:"));
        addParcelFrame.add(heightField);
        addParcelFrame.add(new JLabel("Days in Depo:"));
        addParcelFrame.add(daysInDepoField);

        //Button at the bottom to add the parcel to the list
        JButton addButton = new JButton("Add Parcel");
        addParcelFrame.add(addButton);

        addButton.addActionListener(e -> {
            try {
                String parcelID = parcelIDField.getText().trim();
                if (!parcelID.matches("^X\\d{3}$") || parcelID.equals("X000")) {
                    JOptionPane.showMessageDialog(addParcelFrame, "Parcel ID must follow the format 'X###' (e.g., X001).");
                    Log.getInstance().logError("Parcel ID has not been entered correctly.");
                    return;
                }
                    
                 // Duplicate Parcel Check using Stream (More Modern Approach)
                    boolean isDuplicate = parcelMap.getParcels().stream()
                        .anyMatch(parcel -> parcel.getParcelID().equals(parcelID));

                    if (isDuplicate) {
                        JOptionPane.showMessageDialog(addParcelFrame, "A parcel with this ID already exists. Please use a unique Parcel ID.");
                        Log.getInstance().logError("Duplicate Parcel ID detected: " + parcelID);
                        return;
                    }
                


                //Attributes of parcels
                double weight = Double.parseDouble(weightField.getText().trim());
                double width = Double.parseDouble(widthField.getText().trim());
                double length = Double.parseDouble(lengthField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                int daysInDepo = Integer.parseInt(daysInDepoField.getText().trim());

                if (weight <= 0 || width <= 0 || length <= 0 || height <= 0 || daysInDepo <= 0) {
                    JOptionPane.showMessageDialog(addParcelFrame, "All numeric fields must be positive values.");
                    Log.getInstance().logError("All numeric fields should be positive");
                    return;
                }

                //adding the parcel to the list
                Worker worker = new Worker(parcelMap);
                worker.addParcel(parcelID, weight, width, length, height, daysInDepo);

                JOptionPane.showMessageDialog(addParcelFrame, "Parcel added successfully!");
                Log.getInstance().logAction("Parcel added successfully.");
                addParcelFrame.dispose();
                showParcelList(); //refreshes the list to show the newly added parcel
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addParcelFrame, "Please enter valid numeric values.");
                Log.getInstance().logError("Incorrect values added");
            }
        });
        addParcelFrame.setVisible(true);
    }

    
 // Removing parcel method
    private void removeParcel() {
        int selectedRow = parcelTable.getSelectedRow(); //allows the user to select a row to delete

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a parcel to remove.");
            Log.getInstance().logError("No row was picked to delete");
            return;
        }

        //When a row is selected, the values are callibrated and checked in the parcelMap
        String parcelID = (String) parcelTable.getValueAt(selectedRow, 0);
        Worker worker = new Worker(parcelMap);
        worker.removeParcel(parcelID);
        Log.getInstance().logAction("Parcel has been removed.");
        JOptionPane.showMessageDialog(this, "Parcel has been removed.");
        showParcelList(); //refreshed the parcel list
    }
   
    //Breakdown of the payment to be made
    private void showPriceToPay() {
        int selectedRow = customerTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a parcel to calculate the fee.");
            Log.getInstance().logError("Parcel has not been selected.");
            return;
        }
        // Retrieve the parcel ID from the customerTable
        String parcelID = (String) customerTable.getValueAt(selectedRow, 2);

        // Retrieve the parcel using the parcelID
        Parcel selectedParcel = null;
        for (Parcel parcel : parcelMap.getParcels()) {
            if (parcel.getParcelID().equals(parcelID)) {
                selectedParcel = parcel;
                break;
            }
        }

        if (selectedParcel != null) {
            String feeBreakdown = worker.generateFeeBreakdown(selectedParcel);
            JOptionPane.showMessageDialog(this, feeBreakdown, "Fee Breakdown", JOptionPane.INFORMATION_MESSAGE);
            Log.getInstance().logAction("Fee breakdown displayed for parcel ID: " + parcelID);
        } else {
            JOptionPane.showMessageDialog(this, "Parcel not found.");
            Log.getInstance().logError("Parcel not found.");
        }
    }


    //Processing the customer pickup parcel 
    private void processCustomer() {
        // Check if the customer queue is empty
        List<Customer> customers = queueOfCustomers.getCustomerQueue();
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers in the queue.");
            Log.getInstance().logError("No customer in the queue.");
            return;
        }

        // By following the LIFO queue data structure 
        // the first name of the queue is shown
        Customer firstCustomer = customers.get(0);
        String customerDetails = "First Name: " + firstCustomer.getFirstName() + "\n" +
                                 "Last Name: " + firstCustomer.getLastName() + "\n" +
                                 "Parcel ID: " + firstCustomer.getParcelID();

        // Display customer details and ask if they collected the parcel
        int response = JOptionPane.showConfirmDialog(
            this,
            "Customer Details:\n" + customerDetails + "\n\nHas the customer collected their parcel?",
            "Process Customer",
            JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            // Update parcel status to "Collected"
        	Log.getInstance().logAction("Update the parcel status to collected.");

            String parcelID = firstCustomer.getParcelID();
            boolean statusUpdated = false;
            
            for (Parcel parcel : parcelMap.getParcels()) {
                if (parcel.getParcelID().equals(parcelID)) {
                    parcel.setStatus("Collected");
                    statusUpdated = true;
                    break;
                }
            }

            //Status updated
            if (statusUpdated) {
                JOptionPane.showMessageDialog(this, "Parcel status updated to 'Collected'.");
                Log.getInstance().logAction("Parcel status updated.");

                // Remove the customer from the queue after processing
                Log.getInstance().logAction("Remove the customer from the list.");
                queueOfCustomers.removeCustomer(parcelID);
                showParcelList(); // Refresh the parcel list
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update parcel status. Parcel not found.");
                Log.getInstance().logError("Failed to update the parcel status.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Customer processing canceled.");
            Log.getInstance().logError("Customer processing has been canceled.");
        }
    }
    
    //Searching for the parcel method
    private void searchParcel() {
    	Log.getInstance().logAction("Entering the parcel ID.");
        String parcelId = JOptionPane.showInputDialog(this, "Enter the Parcel ID to search:");

        // Check if the user entered a Parcel ID
        if (parcelId == null || parcelId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Parcel ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            Log.getInstance().logError("Field has been left empty.");
            return;
        }
        // Looping through to find the parcel by matching the Parcel ID
        Parcel foundParcel = null;
        for (Parcel parcel : parcelMap.getParcels()) {
            if (parcel.getParcelID().equalsIgnoreCase(parcelId)) {
                foundParcel = parcel;
                break;
            }
        }

        // Display the values of the parcel
        if (foundParcel != null) {
            String parcelDetails = String.format(
                "Parcel ID: %s\nWeight: %s\nWidth: %s\nLength: %s\nHeight: %s\nDays in Depo: %s\nStatus: %s",
                foundParcel.getParcelID(),
                foundParcel.getWeight(),
                foundParcel.getWidth(),
                foundParcel.getLength(),
                foundParcel.getHeight(),
                foundParcel.getDaysInDepo(),
                foundParcel.getStatus()
            );
            
            JOptionPane.showMessageDialog(this, parcelDetails, "Parcel Details", JOptionPane.INFORMATION_MESSAGE);
            Log.getInstance().logAction("PArcel details shown");
        } else {
            // If no parcel is found with the given Parcel ID
            JOptionPane.showMessageDialog(this, "Parcel not found with ID: " + parcelId, "Parcel Not Found", JOptionPane.ERROR_MESSAGE);
            Log.getInstance().logError("Parcel not found");

        }
    }

    //Loading the Customer View for the table
    private void showCustomerList() {
        isParcelView = false;
        refreshTopButtons();

        String[] columnNames = {"First Name", "Last Name", "Parcel ID"};
        List<Customer> customers = queueOfCustomers.getCustomerQueue();
        String[][] data = new String[customers.size()][3];

        Log.getInstance().logAction("Loading the csv file.");
        //Looping through the csv for the customer details
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getFirstName();
            data[i][1] = customer.getLastName();
            data[i][2] = customer.getParcelID();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        if (customerTable == null) {
            customerTable = new JTable(model);
        } else {
            customerTable.setModel(model);
        }

        JScrollPane scrollPane = new JScrollPane(customerTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER); //centralise the pane layout

        JButton parcelListButton = new JButton("View the Parcel List"); //add the button to go back to the parcel view
        parcelListButton.addActionListener(e -> showParcelList());
        panel.add(parcelListButton, BorderLayout.SOUTH); //added at the bottom
        refreshPanel(panel);
    }

   
    //adding the customer to the list method
    private void addCustomer() {
        JFrame addCustomerFrame = new JFrame("Add Customer");
        addCustomerFrame.setSize(400, 200);
        addCustomerFrame.setLayout(new GridLayout(4, 2));
        addCustomerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create text fields
        Log.getInstance().logAction("Adding the text fields for the customer addition.");
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField parcelIDField = new JTextField();

        // Add labels and text fields to the frame
        Log.getInstance().logAction("Adding the labels for the text fields.");
        addCustomerFrame.add(new JLabel("First Name:"));
        addCustomerFrame.add(firstNameField);
        addCustomerFrame.add(new JLabel("Last Name:"));
        addCustomerFrame.add(lastNameField);
        addCustomerFrame.add(new JLabel("Parcel ID:"));
        addCustomerFrame.add(parcelIDField);

        // Create an "Add Customer" button
        Log.getInstance().logAction("adding the Add button.");
        JButton addButton = new JButton("Add Customer");
        addCustomerFrame.add(addButton);

        // Add action listener for the button
        addButton.addActionListener(e -> {
            // Get the text from the fields
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String parcelID = parcelIDField.getText().trim();

            // Check if any field is empty
            if (firstName.isEmpty() || lastName.isEmpty() || parcelID.isEmpty()) {
                // Show error if any field is empty
                JOptionPane.showMessageDialog(addCustomerFrame, "All fields must be filled.");
                Log.getInstance().logError("The field(s) have been left empty");
                return;
            }

            // Check if the parcel ID exists in the parcel list
            boolean parcelExists = false;
            for (Parcel parcel : parcelMap.getParcels()) {
                if (parcel.getParcelID().equals(parcelID)) {
                    parcelExists = true;
                    break;
                }
            }
            if (!parcelExists) {
                JOptionPane.showMessageDialog(addCustomerFrame, "The provided Parcel ID does not exist.");
                Log.getInstance().logError("ParcelId does not exist.");
                return;
            }

            // Create a new customer object with the provided details
            Customer newCustomer = new Customer(firstName, lastName, parcelID);

            // Add the new customer to the queue
            Log.getInstance().logAction("adding the customer to the queue.");
            queueOfCustomers.addCustomer(newCustomer);
            showCustomerList(); //refresh the customer list
            Log.getInstance().logAction("Customer added: " + newCustomer.getFirstName() + " " + newCustomer.getLastName() + " with Parcel ID: " + newCustomer.getParcelID());
            JOptionPane.showMessageDialog(addCustomerFrame, "Customer added successfully!");
            addCustomerFrame.dispose();
            showCustomerList(); // refresh the customer list view
        });
        addCustomerFrame.setVisible(true);
    }

    
    //Removing the customer method
    private void removeCustomer() {
        int selectedRow = customerTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to remove.");
            Log.getInstance().logError("No row selected.");
            return;
        }
        //Parcel ID is in the thrid column
        String parcelID = (String) customerTable.getValueAt(selectedRow, 2);
        boolean isRemoved = queueOfCustomers.removeCustomer(parcelID);

        if (isRemoved) {
            JOptionPane.showMessageDialog(this, "Customer has been removed.");
            Log.getInstance().logAction("Customer removed.");
            showCustomerList(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found or could not be removed.");
            Log.getInstance().logError("Customer not found.");

        }
    }

    //Refreshing the panel
    private void refreshPanel(JPanel panel) {
        getContentPane().removeAll();
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerGUI().setVisible(true);
        });
    }
}
