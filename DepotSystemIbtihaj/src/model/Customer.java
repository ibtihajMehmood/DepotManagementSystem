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

    // Getter for First name
    public String getFirstName() {
        return firstName;
    }
    
    //setter for first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //getter for last name
    public String getLastName() {
        return lastName;
    }

    //setter last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //getter for parcelID
    public String getParcelID() {
        return parcelID;
    }

	//setter for parcel ID
    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }
}
