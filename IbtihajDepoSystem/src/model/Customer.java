//package model;
//
//public class Customer {
//    // Attributes
//    private String firstName;
//    private String lastName;
//    private String parcelID;
//
//    // Constructor
//    public Customer(String firstName, String lastName, String parcelID) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.parcelID = parcelID;
//    }
//
//    // Default Constructor
//    public Customer() {
//    }
//
//    // Getter and Setter for firstName
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    // Getter and Setter for lastName
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    // Getter and Setter for parcelID
//    public String getParcelID() {
//        return parcelID;
//    }
//
//    public void setParcelID(String parcelID) {
//        this.parcelID = parcelID;
//    }
//}
//


package model;

public class Customer {
    private String firstName;
    private String lastName;
    private String parcelID;

    // Constructor
    public Customer(String firstName, String lastName, String parcelID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.parcelID = parcelID;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }
}
