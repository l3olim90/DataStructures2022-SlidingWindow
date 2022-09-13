import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class ApplicantTester {
    public static void main(String[] args) {
        // The below should print:
        // [1. E011964 (Shamir), 2. E013285 (Clarice), 3. T793412 (Jacky)]
        System.out.println(getPriorityApplicants(4, "applicants.csv"));

        System.out.println(getPriorityApplicants(2, "applicants.csv"));
        System.out.println(getPriorityApplicants(3, "applicants.csv"));
        System.out.println(getPriorityApplicants(1, "applicants.csv"));
        System.out.println(getPriorityApplicants(5, "applicants.csv"));
        System.out.println(getPriorityApplicants(100, "applicants.csv"));
        System.out.println(getPriorityApplicants(1, "applicants0.csv"));
        System.out.println(getPriorityApplicants(49, "applicants50.csv"));
        System.out.println(getPriorityApplicants(10, "applicants1000.csv"));
        System.out.println(getPriorityApplicants(750, "applicants1000.csv"));
    }

    /**
     *  Obtain an ArrayList of Strings of the summarised details of priority applicants
     * @param k The estimated daily workload capacity
     * @param filename The String filename of the file of applicants to be processed
     * @return The ArrayList of Strings of the summarised details of priority applicants
     */
    public static ArrayList<String> getPriorityApplicants(int k, String filename){
        ArrayList<String> result = new ArrayList<>();
        String line;
        String[] tokens;
        int numApplicants;
        String today;

        BufferedReader input = null;
        int count = -1;
        Deque<Applicant> q = new LinkedList<>();

        try {
            input = new BufferedReader(new FileReader(filename));
            tokens = input.readLine().split(",");
            numApplicants = Integer.parseInt(tokens[0]);
            today = tokens[1];

            int n = numApplicants - k + 1;
            int lastAppNo = -1;

            while ((line = input.readLine()) != null){
                Applicant app = new Applicant(line.split(","), today);
                count++;
                if (k >= numApplicants)
                    result.add(app.getSummarisedDetails());
                else {
                    if (count < n) {
                        while (!q.isEmpty() && app.compareTo(q.peekLast()) >= 0)
                            q.removeLast();
                        q.addLast(app);
                    } else {
                        if (q.peek().getApplicationID() != lastAppNo){
                            lastAppNo = q.peek().getApplicationID();
                            result.add(q.peek().getSummarisedDetails());
                        }
                        while (!q.isEmpty() && (q.peek().getApplicationID()) <= app.getApplicationID() - n)
                            q.removeFirst();
                        while (!q.isEmpty() && app.compareTo(q.peekLast()) >= 0)
                            q.removeLast();
                        q.addLast(app);
                    }
                }
            }
            if (q.peek() != null && !result.contains(q.peek().getSummarisedDetails()))
                result.add(q.peek().getSummarisedDetails());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
