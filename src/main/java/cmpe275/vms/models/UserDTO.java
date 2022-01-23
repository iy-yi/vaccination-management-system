package cmpe275.vms.models;

import cmpe275.vms.models.enums.Gender;

public class UserDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String dob;
    private String password;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String gender;
    private String number;

    public User getUserFromDTO() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setPassword(password);
        user.setDob(dob);
        user.setEmail(email);
        Address ad = new Address();
        ad.setNumber(number);
        ad.setCity(city);
        ad.setState(state);
        ad.setStreet(street);
        ad.setZip(zipcode);
        user.setAddress(ad);
        user.setGender(Gender.MALE);
        return user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
