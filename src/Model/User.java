package Model;

import java.time.LocalDate;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private int role;

    public User(String username, String firstName, String lastName,
                String email, LocalDate birthDate, int role)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.role = role;
    }

    public String getUsername() { return this.username; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public LocalDate getBirthDate() { return this.birthDate; }
    public int getRole() { return this.role; }

    public boolean isAdmin() { return this.role > 0; }
}
