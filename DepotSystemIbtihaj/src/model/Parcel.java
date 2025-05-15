package model;

public class Parcel {
    private String parcelID;
    private double weight;
    private double width;
    private double length;
    private double height;
    private int daysInDepo;
    private String status;

    //Parcel attributes
    public Parcel(String parcelID, double weight, double width, double length, double height, int daysInDepo, String status) {
        this.parcelID = parcelID;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.daysInDepo = daysInDepo;
        this.status = status;
    }

    // Getters for the parcelID
    public String getParcelID() {
        return parcelID;
    }

    //Setter for the Parcel ID
    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    //Getter for the Weight
    public double getWeight() {
        return weight;
    }

    //Setter for the weight
    public void setWeight(double weight) {
        this.weight = weight;
    }

    //getter for the width
    public double getWidth() {
        return width;
    }

    //setter for the width
    public void setWidth(double width) {
        this.width = width;
    }

    //getter for the length
    public double getLength() {
        return length;
    }

    //setter for the length
    public void setLength(double length) {
        this.length = length;
    }

    //getter for the height
    public double getHeight() {
        return height;
    }

    //setter for the height
    public void setHeight(double height) {
        this.height = height;
    }

    //getter for the days in the depo
    public int getDaysInDepo() {
        return daysInDepo;
    }

    //setter for the days in depo
    public void setDaysInDepo(int daysInDepo) {
        this.daysInDepo = daysInDepo;
    }

    //getter for statuc
    public String getStatus() {
        return status;
    }

    //setter for the status
    public void setStatus(String status) {
        this.status = status;
    }

	
}
