package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Scheduler {
    private static Scheduler instance;

    private Scheduler() {}

    public static Scheduler getInstance() {
        return instance == null ? (instance = new Scheduler()) : instance;
    }

    public ArrayList<String> getValidDepartTimes(Flight departFlight) {
        ArrayList<String> departSuggestions = new ArrayList<>();
        FlightDatabase flightDatabase = FlightDatabase.getInstance();
        DateConverter dateConverter = DateConverter.getInstance();
        String destination = departFlight.getDestination();
        long runwayTime = departFlight.getDepartUnix();
        int duration = departFlight.getDuration();
        int timeSlot = 2400;

        for (int i = 0; i < 36; i++) {
            System.out.println(flightDatabase.planeIsAvailable(destination, runwayTime));
            if (!flightDatabase.planeIsAvailable(destination, runwayTime)) {
                continue;
            }

            System.out.println("checking time for return");
            if (!flightDatabase.enoughTimeForReturn(destination, runwayTime,
                    duration)) {
                continue;
            }

            if (flightDatabase.timeIsAvailable(runwayTime)) {
                departSuggestions.add(
                        dateConverter.timestampToDateTime(runwayTime).toString()
                );
            }

            runwayTime += timeSlot;
        }

        return departSuggestions;
    }

    public ArrayList<String> getValidReturnTimes(Flight departFlight) {
        FlightDatabase flightDatabase = FlightDatabase.getInstance();
        DateConverter dateConverter = DateConverter.getInstance();
        int runwayWait = 2400;
        long minReturnTime = departFlight.getDepartUnix()
                + departFlight.getDuration() + runwayWait;
        long nextDepart = flightDatabase.getNextDepart(departFlight);
        ArrayList<String> returnSuggestions = new ArrayList<>();

        System.out.println("depart time = " + departFlight.getDepartUnix());
        System.out.println("duration = " + departFlight.getDuration());
        for (long time = minReturnTime; time < nextDepart; time += runwayWait) {
            if (flightDatabase.timeIsAvailable(time)) {
                System.out.println("choice = " + time);
                returnSuggestions.add(
                        dateConverter.timestampToDateTime(time).toString()
                );
            }

            if (returnSuggestions.size() > 36) {
                break;
            }
        }

        return returnSuggestions;
    }
}
