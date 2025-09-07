//Nathan Zheng 116192673 CSE 214.R02

import java.io.Serializable;

/**
 * Contain a String member variable bloodType to denote a specific
 * blood type, and a static method which can be invoked to determine
 * if two blood types are compatible with each other.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class BloodType implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bloodType;
    private final String[] bt = {"A", "B", "AB", "O"};

    /**
     * Constructs an instance of BloodType.
     *
     * @param bloodType the string representing the blood type
     * @throws PatientException.InvalidBloodTypeException if the
     * blood type is invalid
     */
    public BloodType(String bloodType) throws
            PatientException.InvalidBloodTypeException {
        this.bloodType = bloodType.toUpperCase();
        boolean isValidBT = false;
        for (String i : bt) {
            if (this.bloodType.equals(i)) {
                isValidBT = true;
            }
        }
        if (!isValidBT) {
            throw new PatientException.InvalidBloodTypeException
                    ("No such Blood Type exists.");
        }
    }

    /**
     * Returns the blood type.
     *
     * @return the blood type
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Determines whether two blood types are compatible,
     * returning true if they are, and false otherwise.
     *
     * @param recipient the recipient's BloodType
     * @param donor the donor's BloodType
     * @return true if the donor is compatible
     * with the recipient, false otherwise
     */
    public static boolean isCompatible(BloodType recipient, BloodType donor) {
        String r = recipient.getBloodType();
        String d = donor.getBloodType();
        if (r.equals("O")) {
            return d.equals("O");
        } else if (r.equals("A")) {
            return d.equals("O") || d.equals("A");
        } else if (r.equals("B")) {
            return d.equals("O") || d.equals("B");
        } else if (r.equals("AB")) {
            return true;
        }
        return false;
    }
}
