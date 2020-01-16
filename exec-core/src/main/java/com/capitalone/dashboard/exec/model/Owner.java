package com.capitalone.dashboard.exec.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection="people")
public class Owner {
    private String username;
    private String firstName;
    private String lastName;
    private String userId;
    private String jobTitle;
    private String role;


    public Owner(String firstName, String lastName, String userId, String jobTitle, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.jobTitle = jobTitle;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(firstName, owner.firstName) &&
                Objects.equals(lastName, owner.lastName) &&
                Objects.equals(userId, owner.userId) &&
                Objects.equals(jobTitle, owner.jobTitle) &&
                Objects.equals(role, owner.role) &&
                Objects.equals(username, owner.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(firstName, lastName, userId, jobTitle, role, username);
    }
}
