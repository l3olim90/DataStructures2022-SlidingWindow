import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RecordCreator {
    public static void main(String[] args) {
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            Scanner input = new Scanner(System.in);
            PrintWriter output = new PrintWriter(new FileWriter("applicants50.csv"));
            System.out.print("Enter number of records: ");
            int num = input.nextInt();

            output.println(num + ",18-Jul-2022");
            Random rand = new Random();
            for (int i = 0; i < num; i++){
                String expDate = format.format(between(format.parse("18-Dec-2021"), format.parse("18-Dec-2023")));
                String tripDate = format.format(between(format.parse("18-Aug-2022"), format.parse("18-Dec-2023")));
                int priority = rand.nextInt(4);
                String trip = "";
                switch (priority){
                    case 0:
                        trip += ",NIL,N";
                        break;
                    case 1:
                        trip += "," + tripDate + ",V";
                        break;
                    case 2:
                        trip += "," + tripDate + ",B";
                        break;
                    case 3:
                        trip += "," + tripDate + ",S";
                        break;
                }
                output.println(i + ",T123456,Person" + i + "," + expDate + trip);
            }

            output.close();
            System.out.println("File Created.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }
}
