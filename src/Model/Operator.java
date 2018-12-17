package Model;

public class Operator {
    private static Operator operator = null;
    private User me;

    private Operator() {}

    public static Operator getInstance() {
        return operator == null ? (operator = new Operator()) : operator;
    }

    public void setUser(User me) { this.me = me; }

    public User getUser() { return this.me; }
}
