package com.capitalone.dashboard.exec.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entry(objectClasses = { "person", "top" })
public final class Person {

    @Id
    private Name dn;

    @Attribute(name = "cn")
    private String ntid;

    @Attribute(name = "description")
    private String description;

    @Attribute(name = "mail")
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Attribute(name = "sn")
    private String lastName;

    @Attribute(name = "manager")
    private String manager;

    @Attribute(name = "objectCategory")
    private String objectCategory;

    @Attribute(name = "distinguishedName")
    private String distinguishedName;

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
    }

    @Attribute(name = "displayName")
    private String displayName;

    @Attribute(name = "directReports")
    private Set<String> directReports;

    @Attribute(name = "thumbnailPhoto")
    private byte[] thumbnailPhoto;

    public Set<String> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(Set<String> directReports) {
        this.directReports = directReports;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getName() {
        return this.displayName;
    }

    public Person() {
    }

    public Person(String fullName, String lastName) {
        this.ntid = fullName;
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getNtid() {
        return ntid;
    }

    public void setNtid(String ntid) {
        this.ntid = ntid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getManager() {
        if (manager != null) {
            return manager;
        } else {
            return "";
        }
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * Method to get manager's NTID
     *
     * @return manager NTID
     */
    public String getManagerNTID() {
        return ((!getManager().isEmpty()) ? getManager().split(",")[0].split("=")[1] : "");
    }

    /**
     * Method to get all direct reporting person NTID
     *
     * @return List of NTID
     */
    public List<String> getReportingNTIDs() {
        return (directReports != null)
                ? directReports.stream().map(entry -> entry.split(",")[0].split("=")[1]).collect(Collectors.toList())
                : new ArrayList<String>();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dn=", dn)
                .append("NTID=", ntid)
                .append("lastname=", lastName).toString();
    }

}