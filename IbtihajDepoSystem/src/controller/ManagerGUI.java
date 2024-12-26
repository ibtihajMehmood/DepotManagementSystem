package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.*;

public class ManagerGUI extends JFrame {
    private QueueofCustomers queueOfCustomers;
    private ParcelMap parcelMap;
    private JPanel topPanel; // Persistent Top Panel for the buttons on the different views

    public ManagerGUI() {
        queueOfCustomers = new QueueofCustomers();
        queueOfCustomers.loadCustomersFromCSV("src/view/customers.csv"); // Load customers

        parcelMap = new ParcelMap();
        parcelMap.loadParcelsFromCSV("src/view/parcels.csv"); // Load parcels

        // Set up the frame
        setTitle("Depot Management System Dashboard");
        setSize(800, 600); //Frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add top button panel
        addTopButtonPanel();

        // Show the parcel list by default
        showParcelList();
    }

//    Buttons for the top panel
    private void addTopButtonPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Buttons for the Parcel View
        JButton addParcelButton = new JButton("Add Parcel");
        JButton removeParcelButton = new JButton("Remove Parcel");
        JButton processCustomerButton = new JButton("Process the Customer");

        // Add action listeners
        addParcelButton.addActionListener(e -> addParcel());
        removeParcelButton.addActionListener(e -> removeParcel());
        processCustomerButton.addActionListener(e -> processCustomer());

        // Add buttons to the top panel
        topPanel.add(addParcelButton);
        topPanel.add(removeParcelButton);
        topPanel.add(processCustomerButton);

        add(topPanel, BorderLayout.NORTH); // Add top panel to the NORTH region
    }

    //Show list of customers giving a back button to return to the parcel view
    private void showCustomerList() {
        String[] columnNames = {"First Name", "Last Name", "Parcel ID"};
        List<Customer> customers = queueOfCustomers.getCustomerQueue();
        String[][] data = new String[customers.size()][3];

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getFirstName();
            data[i][1] = customer.getLastName();
            data[i][2] = customer.getParcelID();
        }

        JTable customerTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Parcel List");
        backButton.addActionListener(e -> showParcelList());
        panel.add(backButton, BorderLayout.SOUTH);

        getContentPane().removeAll();
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    //Parcel View List
    private void showParcelList() {
        String[] columnNames = {"Parcel ID", "Weight", "Width", "Length", "Height", "Days in Depo", "Status"};
        List<Parcel> parcels = parcelMap.getParcels();
        String[][] data = new String[parcels.size()][7];

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

        JTable parcelTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(parcelTable);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton customerListButton = new JButton("Customer List View");
        customerListButton.addActionListener(e -> showCustomerList());
        panel.add(customerListButton, BorderLayout.SOUTH);

        getContentPane().removeAll();
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    //Addigng parcel button
    private void addParcel() {
        JOptionPane.showMessageDialog(this, "Add Parcel functionality not implemented yet.");
    }


    //Remove Parcel button
    private void removeParcel() {
        JOptionPane.showMessageDialog(this, "Remove Parcel functionality not implemented yet.");
    }

    //Processing Customer Button
    private void processCustomer() {
        JOptionPane.showMessageDialog(this, "Process Customer functionality not implemented yet.");
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerGUI managerGUI = new ManagerGUI();
            managerGUI.setVisible(true);
        });
    }
}
