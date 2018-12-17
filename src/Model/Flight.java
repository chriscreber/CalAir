package Model;

public class Flight
{
    private int flightNum;
    private String source;
    private String destination;
    private String status;
    private long depart;
    private int duration;
    private int emptySeats;
    private float price;

    public Flight(int flightNum, String source, String destination, long depart, int duration, int emptySeats, float price,String status)
    {
        this.flightNum = flightNum;
        this.source = source;
        this.destination = destination;
        this.depart = depart;
        this.duration = duration;
        this.emptySeats = emptySeats;
        this.price = price;
        this.status = status;
    }

    public Flight() {}

    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDepart(long depart) { this.depart = depart; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setPrice(float price) { this.price = price; }

    public int getFlightNum() { return this.flightNum; }
    public String getSource() { return this.source; }
    public String getDestination() { return this.destination; }
    public long getDepartUnix() { return this.depart; }
    public String getDepartHuman() { return new java.util.Date((long)this.depart*1000).toString(); }
    public int getDuration() { return this.duration; }
    public int getEmptySeats() { return this.emptySeats; }
    public float getPrice() { return this.price; }
    public String getStatus() { return this.status; }
//    public int getFlightNumber(){return flightNum;}

    public String toString()
    {
        return("Flight Num: " + KeyManager.convertFlightNum(flightNum) + " | Source: " + source + " | Destination: " + destination + " | Departure: " + DateConverter.getInstance().localDateToString(depart) + " | Duration: " + DateConverter.getInstance().minutesToHourMinutes(duration) + " | Seats: " + String.valueOf(emptySeats) + " | Price: " + String.valueOf(price));
    }


}
