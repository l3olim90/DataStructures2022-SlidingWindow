import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Applicant implements Comparable<Applicant>{
    private int applicationID;
    private String passportID;
    private String name;
    private Date expiryDate;
    private boolean expired;
    private Date tripDate;
    private int daysToTrip;
    private char tripPurpose;

    // Constructor to set up the Applicant object
    public Applicant(String applicationID, String passportID, String name,
                     String expiryDate, String tripDate, String tripPurpose, String today){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        if (checkTripPurpose(tripPurpose)) this.tripPurpose = tripPurpose.charAt(0);
        else throw new IllegalArgumentException("Trip purpose incorrect");

        try{
            this.applicationID = Integer.parseInt(applicationID);
        } catch(Exception e){ throw new IllegalArgumentException("Invalid application ID.");  }

        try {
            this.expiryDate = format.parse(expiryDate);
            Date todayDate = format.parse(today);
            expired = getNumDays(this.expiryDate, todayDate) < 180;
            if (!tripDate.equals("NIL")) {
                this.tripDate = format.parse(tripDate);
                this.daysToTrip = (int) getNumDays(this.tripDate, todayDate);
            }
            else
                this.tripDate = null;
        } catch (ParseException e){ throw new IllegalArgumentException("Incorrect date format."); }

        this.passportID = passportID;
        this.name = name;
    }

    // Auxiliary constructor to simplify the code required to construct the Applicant object during file reading
    public Applicant(String[] tokens, String today){
        this(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], today);
    }

    // Method to obtain the number of days between two dates
    // Returns -1 if the beforeDate is after the afterDate
    // Method is utilised in the constructor
    private long getNumDays(Date afterDate, Date beforeDate){
        long diffDateMilli = afterDate.getTime() - beforeDate.getTime();
        if (diffDateMilli < 0)
            return -1;
        else
            return TimeUnit.DAYS.convert(diffDateMilli, TimeUnit.MILLISECONDS);
    }

    // Getter to return the application ID
    public int getApplicationID(){return applicationID;}

    // Returns the summarised details of the Applicant object
    public String getSummarisedDetails(){ return applicationID + ". "+ passportID + " (" + name + ")"; }

    // Checker to check if the trip purpose is valid
    // Used in the constructor
    private boolean checkTripPurpose(String tripPurpose){
        return (tripPurpose.length() == 1) && (tripPurpose.charAt(0) == 'B' || tripPurpose.charAt(0) == 'V'
                || tripPurpose.charAt(0) == 'S' || tripPurpose.charAt(0) == 'N');
    }

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        if (tripDate == null)
            return applicationID + "," + passportID + "," + name + ","
                    + format.format(expiryDate) + "," + "NIL" + ","
                    + 'N';
        else
            return applicationID + "," + passportID + "," + name + ","
                    + format.format(expiryDate) + "," + format.format(tripDate) + ","
                    + tripPurpose;
    }

    // The overridden compareTo method to compare the priority of Applicants.
    // Note that "higher priority" should be a positive value and "lower priority" should be a negative value.
    @Override
    public int compareTo(Applicant a){
        // Complete the code below this comment.
        // You are allowed to add auxiliary private methods in the class if required
        if (this.expired && !a.expired)
            return 1;
        else if (!this.expired && a.expired)
            return -1;
        else{
            if (calcTripVal(this.tripPurpose) > calcTripVal(a.tripPurpose))
                return 1;
            else if (calcTripVal(a.tripPurpose) > calcTripVal(this.tripPurpose))
                return -1;
            else{
                return Integer.compare(a.daysToTrip, this.daysToTrip);
            }
        }
    }
    private int calcTripVal(char tripPurpose){
        int tripValue = -1;
        switch (tripPurpose){
            case 'S':
                tripValue = 3;
                break;
            case 'B':
                tripValue = 2;
                break;
            case 'V':
                tripValue = 1;
                break;
            case 'N':
                tripValue = 0;
                break;
        }
        return tripValue;
    }
}
