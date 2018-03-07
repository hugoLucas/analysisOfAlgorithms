import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {

    private static int N_INDEX = 0;
    private static int S_INDEX = 1;
    private static int E_INDEX = 2;

    public static void main(String[] args) {
        try {
            int[] userInputs = getInputs();
            ArrayList<Interval> intervalList = createIntervalList(userInputs[N_INDEX], userInputs[S_INDEX], userInputs[E_INDEX]);
            ArrayList<Interval> schedule = intervalScheduler(intervalList);
        }catch (IOException e){
            System.out.println("ERROR - UNABLE TO RETRIEVE USER INPUT, PLEASE TRY AGAIN");
        }
    }

    private static ArrayList<Interval> intervalScheduler(ArrayList<Interval> intervalList){
        ArrayList<Interval> results = new ArrayList<>();

        ArrayList<Interval> intervalCopy = new ArrayList<>(intervalList.size());
        for (Interval i: intervalList)
            intervalCopy.add(i.cloneInterval());
        Collections.sort(intervalCopy);

        while(intervalCopy.size() > 0){
            Interval currentInterval = intervalCopy.get(0);
            intervalCopy.remove(0);

            ArrayList<Interval> toRemove = new ArrayList<>();
            for (Interval v: intervalCopy)
                if (v.conflictsWith(currentInterval))
                    toRemove.add(v);
            intervalCopy.removeAll(toRemove);

            results.add(currentInterval);
        }

        return results;
    }

    private static ArrayList<Interval> intervalPartioner(Interval[] intervalList){
        return null;
    }

    private static ArrayList<Interval> createIntervalList(int length, int minStart, int maxEnd){
        ArrayList<Interval> intervalList = new ArrayList<>(length);

        Random generator = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i ++){
            int startTime = minStart + generator.nextInt(maxEnd - minStart);
            int endTime = startTime + generator.nextInt(maxEnd - startTime) + 1;

            intervalList.add(new Interval(i+1, startTime, endTime));
        }

        return intervalList;
    }

    private static int[] getInputs() throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        String intervalMessage = "Please enter the amount of integers you want to create: ";
        int nIntervals = getInput(userInput, intervalMessage);

        boolean validInterval = false;
        int tStart = -1;
        int tEnd = -1;
        while (!validInterval) {
            String startTimeMessage = "Please enter the minimum start time: ";
            tStart = getInput(userInput, startTimeMessage);

            String endTimeMessage = "Please enter the maximum end time: ";
            tEnd = getInput(userInput, endTimeMessage);

            if(tStart < tEnd)
                validInterval = true;
            else
                System.out.println("\nError - the minimum start time must be greater than the maximum end time!\n");
        }

        return new int[]{nIntervals, tStart, tEnd};
    }

    private static int getInput(BufferedReader reader, String userMessage) throws IOException {
        while(true) {
            System.out.print(userMessage);
            String userInput = reader.readLine();
            try{
                int value = Integer.parseInt(userInput);
                if(value < 0)
                    throw new NumberFormatException(null);
                return value;
            }catch (NumberFormatException e){
                System.out.println("Input Error - Please enter a positive integer.");
            }
        }
    }

    static class Interval implements Comparable{

        private int intervalLabel;
        private int startTime;
        private int endTime;

         Interval(int index, int start, int end){
            this.intervalLabel = index;
            this.startTime = start;
            this.endTime = end;
        }

        boolean conflictsWith(Interval i) {
            if (this.startTime == i.getStartTime() || this.endTime == i.getEndTime())
                return true;
            else if (this.startTime <= i.getStartTime())
                return this.endTime >= i.getStartTime();
            else
                return i.getEndTime() >= this.startTime;
        }

        Interval cloneInterval(){
            return new Interval(this.getIntervalLabel(), this.getStartTime(), this.getEndTime());
        }

        int getStartTime() {
            return startTime;
        }

        int getEndTime() {
            return endTime;
        }

        int getIntervalLabel(){
             return intervalLabel;
        }

        @Override
        public String toString() {
            return String.format("<%03d>: %05d -> %05d", intervalLabel, startTime, endTime);
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Interval))
                throw new ClassCastException("This object is not an Interval!");
            else{
                Interval obj = (Interval) o;
                if(this.endTime < obj.getEndTime())
                    return -1;
                else if (this.endTime > obj.getEndTime())
                    return 1;
                else
                    return 0;
            }
        }
    }
}
