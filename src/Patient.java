//Nathan Zheng 116192673 CSE 214.R02

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an active organ donor or recipient in the database.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class Patient implements Comparable<Patient>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String organ;
    private int age;
    private BloodType bloodType;
    private int ID;
    private boolean isDonor;

    /**
     * Constructs a new Patient object with the given attributes.
     *
     * @param name the name of the donor or recipient
     * @param organ the organ the patient is donating or receiving
     * @param age the age of the patient
     * @param bloodType the blood type of the patient
     * @param ID the ID number of the patient
     * @param isDonor true if the patient is a donor, false if recipient
     */
    public Patient(String name, String organ, int age,
                   BloodType bloodType, int ID, boolean isDonor) {
        this.name = name;
        this.organ = organ.toLowerCase();
        this.age = age;
        this.bloodType = bloodType;
        this.ID = ID;
        this.isDonor = isDonor;

    }

    /**
     * Returns the name of the patient.
     *
     * @return the patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the organ the patient is donating or receiving.
     *
     * @return the patient's organ
     */
    public String getOrgan() {
        return organ;
    }

    /**
     * Returns the age of the patient.
     *
     * @return the patient's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the blood type of the patient.
     *
     * @return the patient's blood type
     */
    public BloodType getBloodType() {
        return bloodType;
    }

    /**
     * Returns the unique ID of the patient.
     *
     * @return the patient's ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the ID of the patient.
     *
     * @param ID the new ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Returns whether the patient is a donor.
     *
     * @return true if the patient is a donor, false otherwise
     */
    public boolean isDonor() {
        return isDonor;
    }

    /**
     *  Compares this Patient object to o, comparing by
     *  ID such that the values should be sorted in ascending order.
     *
     * @param o the other patient to compare to
     * @return an integer indicating whether one is greater, equal, or
     * less than
     */
    @Override
    public int compareTo(Patient o) {
        return Integer.compare(this.getID(), o.getID());
    }

    /**
     * Returns a string representation of the patient.
     *
     * @return formatted patient information
     */
    public String toString() {
        return String.format("%-20s | %6d | %-15s | %8s",
                name, age, organ, bloodType);
    }
}
