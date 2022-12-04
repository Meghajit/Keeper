package keeper.entity;

import java.io.Serializable;

public class Customer implements Serializable {
    private final String uuid;
    private final String firstName;
    private final String lastName;
    private final String emailId;

    public Customer(String uuid, String firstName, String lastName, String emailId) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getUuid() {
        return uuid;
    }
}
