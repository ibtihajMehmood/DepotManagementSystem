package model;

import java.util.List;

public class Worker {
	
	 public Worker() {
	    }

    private ParcelMap parcelMap;
    private QueueofCustomers queueOfCustomers;

    //Worker is able to get all the values of the parcel list and customer list
    public Worker(ParcelMap parcelMap) {
        this.parcelMap = parcelMap;
    }

    public Worker(QueueofCustomers queueOfCustomers) {
        this.queueOfCustomers = queueOfCustomers;
    }
    
    //Adding the parcel with the default status of collecting
    public void addParcel(String parcelID, double weight, double width, double length, double height, int daysInDepo) {
        Parcel newParcel = new Parcel(parcelID, weight, width, length, height, daysInDepo, "Collecting");
        parcelMap.getParcels().add(newParcel);
        System.out.println("Parcel added: " + newParcel.getParcelID() + " with status: Collecting");
    }
    
    //Removing the parce
    public void removeParcel(String parcelID) {
        boolean removed = parcelMap.getParcels().removeIf(parcel -> parcel.getParcelID().equals(parcelID));
        if (removed) {
            System.out.println("Parcel removed: " + parcelID);
        } else {
            System.out.println("Failed to remove parcel: " + parcelID);
        }
    }
    

    
 // Calculate the fee breakdown for a parcel
    public String generateFeeBreakdown(Parcel parcel) {
        double baseFee = 100.0; // Base fee
        
        // Additional fee for extra days
        double depotFee = 0.0;
        if (parcel.getDaysInDepo() >= 4) {
            depotFee = (parcel.getDaysInDepo() - 3) * 10;
        }

        // Overweight fee
        double weightFee = 0.0;
        if (parcel.getWeight() > 10) {
            weightFee = 20.0;
        }

        // Total before discount
        double totalFee = baseFee + depotFee + weightFee;

        // Discount logic
        double discountPercentage = 0.0;
        String parcelID = parcel.getParcelID();
        char lastDigit = parcelID.charAt(parcelID.length() - 1);

        if (lastDigit == '2') {
            discountPercentage = 2.0;
        } else if (lastDigit == '8') {
            discountPercentage = 8.0;
        }

        double discountAmount = totalFee * (discountPercentage / 100);
        double finalAmount = totalFee - discountAmount;

        // Breakdown fee
        return String.format(
            "Fee Breakdown:\n" +
            "Base Fee: $%.2f\n" +
            "Depot Fee: $%.2f\n" +
            "Weight Fee: $%.2f\n" +
            "Discount: -$%.2f (%.0f%%)\n" +
            "Total Payment: $%.2f",
            baseFee, depotFee, weightFee, discountAmount, discountPercentage, finalAmount
        );
    }

      


}
