package at.jku.employeeonboardingsystem.ldap;

public class Person {
    private String fullName;
    private String lastName;
    private String uid;
    private String password;

    public Person() {
    }

    public Person(String fullName, String lastName) {
        this.fullName = fullName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
            "fullName='" + fullName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", uid='" + uid + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
