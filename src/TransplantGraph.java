//Nathan Zheng 116192673 CSE 214.R02

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains an adjacency matrix for the organ donors and recipients.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class TransplantGraph implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Patient> donors;
    private ArrayList<Patient> recipients;
    public static final int MAX_PATIENTS = 100;
    private boolean[][] connections;

    /**
     * Constructs an instance of TransplantGraph
     */
    public TransplantGraph() {
        donors = new ArrayList<>();
        recipients = new ArrayList<>();
        connections = new boolean[MAX_PATIENTS][MAX_PATIENTS];
    }

    /**
     * Returns the list of donor patients.
     *
     * @return list of donors
     */
    public ArrayList<Patient> getDonors() {
        return donors;
    }

    /**
     * Returns the list of recipient patients.
     *
     * @return list of recipients
     */
    public ArrayList<Patient> getRecipients() {
        return recipients;
    }

    /**
     * Returns the compatibility matrix.
     *
     * @return 2D boolean array of connections
     */
    public boolean[][] getConnections() {
        return connections;
    }

    /**
     * Creates and returns a new TransplantGraph object,
     * intialized with the donor information found in donorFile
     * and the recipient information found in recipientFile.
     *
     * @param donorFile donor file
     * @param recipientFile recipient file
     * @return a constructed TransplantGraph
     * @throws FileNotFoundException if a file cannot be read
     * @throws PatientException.FullListException if a list
     * exceeds MAX_PATIENTS
     * @throws PatientException.InvalidBloodTypeException if
     * an invalid blood type is found
     */
    public static TransplantGraph buildFromFiles
            (String donorFile, String recipientFile) throws
            FileNotFoundException,
            PatientException.FullListException,
            PatientException.InvalidBloodTypeException {
        TransplantGraph graphFile = new TransplantGraph();
        Scanner scannerD = new Scanner (new File(donorFile));
        while (scannerD.hasNext()) {
            String[] partsD = scannerD.nextLine().split(",");
            int id = Integer.parseInt(partsD[0].trim());
            String name = partsD[1].trim();
            int age = Integer.parseInt(partsD[2].trim());
            String organ = partsD[3].trim();
            BloodType bt = new BloodType(partsD[4].trim());
            Patient newDonor = new Patient(name, organ, age, bt, id, true);
            graphFile.addDonor(newDonor);
        }
        Scanner scannerR = new Scanner (new File(recipientFile));
        while (scannerR.hasNext()) {
            String[] partsR = scannerR.nextLine().split(",");
            int id = Integer.parseInt(partsR[0].trim());
            String name = partsR[1].trim();
            int age = Integer.parseInt(partsR[2].trim());
            String organ = partsR[3].trim();
            BloodType bt = new BloodType(partsR[4].trim());
            Patient newRecipient = new Patient
                    (name, organ, age, bt, id, false);
            graphFile.addRecipient(newRecipient);
        }
        for (int i = 0; i < graphFile.donors.size(); i++) {
            Patient tempDonor = graphFile.donors.get(i);
            for (int j = 0; j < graphFile.recipients.size(); j++) {
                Patient tempRecipient = graphFile.recipients.get(j);
                if (tempDonor.getOrgan().equals
                        (tempRecipient.getOrgan()) &&
                        BloodType.isCompatible(tempRecipient.getBloodType(),
                                tempDonor.getBloodType())) {
                    graphFile.connections[i][j] = true;
                }
            }
        }
        return graphFile;
    }

    /**
     * Adds the specified Patient to the recipients list.
     * This method also updates the connections adjacency matrix.
     *
     * @param patient recipient to be added
     * @throws PatientException.FullListException if recipient list is full
     */
    public void addRecipient(Patient patient) throws
            PatientException.FullListException {
        if (recipients.size() >= MAX_PATIENTS) {
            throw new PatientException.
                    FullListException("Patient list is full.");
        }
        patient.setID(recipients.size());
        recipients.add(patient);
        for (int i = 0; i < donors.size(); i++) {
            Patient d = donors.get(i);
            if (BloodType.isCompatible
                    (patient.getBloodType(), d.getBloodType())
                    && d.getOrgan().equals(patient.getOrgan())) {
                connections[i][patient.getID()] = true;
            }
        }
    }

    /**
     * Adds the specified Patient to the donors list.
     * This method also updates the connections adjacency matrix.
     *
     * @param patient donor to be added
     * @throws PatientException.FullListException if donor list is full
     */
    public void addDonor(Patient patient) throws
            PatientException.FullListException {
        if (donors.size() >= MAX_PATIENTS) {
            throw new PatientException.
                    FullListException("Patient list is full.");
        }
        patient.setID(donors.size());
        donors.add(patient);
        for (int j = 0; j < recipients.size(); j++) {
            Patient r = recipients.get(j);
            if (BloodType.isCompatible
                    (patient.getBloodType(), r.getBloodType())
                    && r.getOrgan().equals(patient.getOrgan())) {
                connections[patient.getID()][j] = true;
            }
        }
    }

    /**
     * Removes the specified Patient from the donors list.
     * This method also updates the connections adjacency
     * matrix, removing all connections to this donor, and removing
     * the row associated with the patient from the matrix.
     *
     * @param name name of the donor to remove
     * @throws PatientException.PatientDoesNotExistException if no
     * donor with the name exists
     */
    public void removeDonor(String name) throws
            PatientException.PatientDoesNotExistException {
        int index = -1;
        for (int i = 0; i < donors.size(); i++) {
            if (donors.get(i).getName().equalsIgnoreCase(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new PatientException.PatientDoesNotExistException
                    ("Patient does not exist.");
        }
        donors.remove(index);
        for (int i = index; i < donors.size(); i++) {
            donors.get(i).setID(i);
        }
        for (int r = 0; r < recipients.size(); r++) {
            for (int d = index; d < donors.size(); d++) {
                connections[d][r] = connections[d+1][r];
            }
            connections[donors.size()][r] = false;
        }
    }

    /**
     * Removes the specified Patient from the recipient list.
     * This method also updates the connections adjacency
     * matrix, removing all connections to this recipient, and removing
     * the row associated with the patient from the matrix.
     *
     * @param name name of the recipient to remove
     * @throws PatientException.PatientDoesNotExistException if no
     * recipient with the name exists
     */
    public void removeRecipient(String name) throws
            PatientException.PatientDoesNotExistException {
        int index = -1;
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getName().equalsIgnoreCase(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new PatientException.PatientDoesNotExistException
                    ("Patient does not exist.");
        }
        recipients.remove(index);
        for (int i = index; i < recipients.size(); i++) {
            recipients.get(i).setID(i);
        }
        for (int d = 0; d < donors.size(); d++) {
            for (int r = index; r < recipients.size(); r++) {
                connections[d][r] = connections[d][r+1];
            }
            connections[d][recipients.size()] = false;
        }
    }

    /**
     * Prints the formatted heading for the donor list.
     */
    public void printDonorHeading() {
        System.out.printf("%-6s | %-18s | %-3s | %-14s | %-10s | %s%n",
                "Index", "Donor Name", "Age", "Organ Donated",
                "Blood Type", "Recipient IDs");
        System.out.println("===========================================" +
                "================================");

    }

    /**
     * Prints the formatted heading for the recipient list.
     */
    public void printRecipientHeading() {
        System.out.printf("%-6s | %-18s | %-3s | %-14s | %-10s | %s%n",
                "Index", "Recipient Name", "Age", "Organ Needed",
                "Blood Type", "Donor IDs");
        System.out.println("===========================================" +
                "================================");
    }

    /**
     * Prints all organ recipients' information in a neatly formatted table.
     */
    public void printAllRecipients() {
        printRecipientHeading();
        for (int i = 0; i < recipients.size(); i++) {
            StringBuilder donorID = new StringBuilder();
            Patient r = recipients.get(i);
            for (int j = 0; j < donors.size(); j++) {
                if (connections[j][i]) {
                    if (donorID.length() > 0) {
                        donorID.append(", ");
                    }
                    donorID.append(j);
                }
            }
            System.out.printf("%-6d | %-18s | %-3d | %-14s | %-10s | %s%n",
                    r.getID(), r.getName(), r.getAge(), r.getOrgan(),
                    r.getBloodType().getBloodType(), donorID.toString());
        }
    }

    /**
     * Prints all organ donors' information in a neatly formatted table.
     */
    public void printAllDonors() {
        printDonorHeading();
        for (int i = 0; i < donors.size(); i++) {
            StringBuilder recipientID = new StringBuilder();
            Patient d = donors.get(i);
            for (int j = 0; j < recipients.size(); j++) {
                if (connections[i][j]) {
                    if (recipientID.length() > 0) {
                        recipientID.append(", ");
                    }
                    recipientID.append(j);
                }
            }
            System.out.printf("%-6d | %-18s | %-3d | %-14s | %-10s | %s%n",
                    d.getID(), d.getName(), d.getAge(), d.getOrgan(),
                    d.getBloodType().getBloodType(), recipientID.toString());
        }
    }

    /**
     * Updates the matrix between donors and recipients.
     */
    public void updateMatrix() {
        for (int i = 0; i < donors.size(); i++) {
            for (int j = 0; j < recipients.size(); j++) {
                connections[i][j] = BloodType.isCompatible
                        (recipients.get(j).getBloodType(),
                                donors.get(i).getBloodType())
                        && recipients.get(j).getOrgan().equals
                        (donors.get(i).getOrgan());
            }
        }
    }
}
