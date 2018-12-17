package Model;

public class Reservation {
    private int id;
    private String username;
    private int departFlight;
    private int returnFlight;
    private int confirmation;
    private boolean checkedIn;
    private int bags;

    public Reservation(int id, String username, int departFlight, int returnFlight,
    		int confirmation, boolean checkedIn, int bags)
    {
        this.id = id;
        this.username = username;
        this.departFlight = departFlight;
        this.returnFlight = returnFlight;
        this.confirmation = confirmation;
        this.checkedIn = checkedIn;
        this.bags = bags;
    }


    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public int getDepartFlight() { return this.departFlight; }
    public int getReturnFlight() { return this.returnFlight; }
    public int getConfirmation() { return this.confirmation; }
    public boolean getCheckedIn() {return this.checkedIn; }
    public int getBags() { return this.bags; } 

}
