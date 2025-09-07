//Nathan Zheng 116192673 CSE 214.R02

import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Act as the main driver for the application.
 * This class contains the main method for the program,
 * which will first attempt to load any previously serialized
 * TransplantGraph object located in 'transplant.obj'.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class TransplantDriver {
    private static TransplantGraph graph;
    private static Scanner s = new Scanner(System.in);

    /**
     * The main method that runs the transplant menu.
     *
     * @param args command-line arguments
     * @throws PatientException.FullListException if the list
     * exceeds the max size
     * @throws IOException if reading/writing files fails
     * @throws PatientException.InvalidBloodTypeException if an invalid
     * blood type is encountered
     */
    public static void main(String[] args) throws
            PatientException.FullListException,
            IOException,
            PatientException.InvalidBloodTypeException {
        String option = "";
        loadFile();
        System.out.println();
        while (!option.equals("q")) {
            printMenu();
            System.out.println();
            System.out.print("Please select an option: ");
            option = s.nextLine().toLowerCase();
            if (option.equals("lr")) {
                System.out.println();
                graph.printAllRecipients();
                System.out.println();
            } else if  (option.equals("lo")) {
                System.out.println();
                graph.printAllDonors();
                System.out.println();
            } else if  (option.equals("ao")) {
                //Add Donor
                try {
                    System.out.println();
                    System.out.print("Please enter the organ donor name: ");
                    String name = s.nextLine();
                    System.out.print("Please enter the blood type of "
                            + name + ": ");
                    BloodType bloodType = new BloodType(s.nextLine());
                    System.out.print("Please enter the age of "
                            + name + ": ");
                    int age = Integer.parseInt(s.nextLine());
                    System.out.print("Please enter the organs "
                            + name + " is donating: ");
                    String organ = s.nextLine();
                    int id = graph.getDonors().size();
                    Patient newPatient = new Patient
                            (name, organ, age, bloodType, id, true);
                    graph.addDonor(newPatient);
                    graph.updateMatrix();
                    System.out.println();
                    System.out.println("The organ donor, " + name + ", " +
                            "has been added to the donor list with ID "
                            + id + ".\n");
                } catch (Exception e) {
                    System.out.println("\nPlease enter a valid value.\n");
                }
            } else if  (option.equals("ar")) {
                //Add Recipients
                try {
                    System.out.println();
                    System.out.print("Please enter new recipient's name: ");
                    String name = s.nextLine();
                    System.out.print("Please enter the " +
                            "recipient's blood type: ");
                    BloodType bloodType = new BloodType(s.nextLine());
                    System.out.print("Please enter the recipient's age: ");
                    int age = Integer.parseInt(s.nextLine());
                    System.out.print("Please enter the organ needed: ");
                    String organ = s.nextLine();
                    int id = graph.getRecipients().size();
                    Patient newPatient = new Patient
                            (name, organ, age, bloodType, id, false);
                    graph.addRecipient(newPatient);
                    graph.updateMatrix();
                    System.out.println();
                    System.out.println("The organ recipient, " + name + ", " +
                            "has been added to the recipient list with ID "
                            + id + ".\n");
                } catch (Exception e) {
                    System.out.println("\nPlease enter a valid value.\n");
                }
            } else if (option.equals("ro")) {
                System.out.print("\nPlease enter the name of the " +
                        "organ donor to remove: ");
                String name = s.nextLine();
                try {
                    graph.removeDonor(name);
                    System.out.println("\n" + name +
                            " was removed from the organ donor list.\n");
                } catch (PatientException.PatientDoesNotExistException e) {
                    System.out.println();
                    System.out.println("\nFailed to remove donor: No such " +
                            "patient named " + name
                            + "in list of donors.\n");
                }
            } else if  (option.equals("rr")) {
                System.out.print("\nPlease enter the name of the " +
                        "recipient to remove: ");
                String name = s.nextLine();
                try {
                    graph.removeRecipient(name);
                    System.out.println("\n" + name +
                            " was removed from the organ " +
                            "transplant waitlist.\n");
                } catch (PatientException.PatientDoesNotExistException e) {
                    System.out.println("\nFailed to remove " +
                            "recipient: No such " +
                            "patient named " + name
                            + "in list of recipients.\n");
                }
            } else if  (option.equals("sr")) {
                sortRecipients();
            } else if  (option.equals("so")) {
                sortDonors();
            } else if  (option.equals("q")) {
                saveFile(graph);
                System.out.println();
                System.out.println("\nWriting data to transplant.obj...");
                System.out.println("\nProgram terminating normally...");
            } else {
                System.out.println("\nPlease enter a valid option.\n");
            }
        }
    }

    /**
     * Prints the main menu of options to the user.
     */
    public static void printMenu() {
        System.out.println("Menu:\n" +
                "    (LR) - List all recipients\n" +
                "    (LO) - List all donors\n" +
                "    (AO) - Add new donor\n" +
                "    (AR) - Add new recipient\n" +
                "    (RO) - Remove donor\n" +
                "    (RR) - Remove recipient\n" +
                "    (SR) - Sort recipients\n" +
                "    (SO) - Sort donors\n" +
                "    (Q) - Quit");
    }

    /**
     * Prints the sorting submenu based on donor or recipient.
     *
     * @param isDonor true for donor menu, false for recipient menu
     */
    public static void printSubmenu(boolean isDonor) {
        if (isDonor) {
            System.out.println("\n(I) Sort by ID\n" +
                    "(N) Sort by Number of Recipients\n" +
                    "(B) Sort by Blood Type\n" +
                    "(O) Sort by Organ Donated\n" +
                    "(Q) Back to Main Menu\n");
        } else {
            System.out.println("\n(I) Sort by ID\n" +
                    "(N) Sort by Number of Donors\n" +
                    "(B) Sort by Blood Type\n" +
                    "(O) Sort by Organ Needed\n" +
                    "(Q) Back to Main Menu\n");
        }
    }

    /**
     * Loads data from a saved object file,
     * or from donor and recipient text files
     * if the object file does not exist.
     *
     * @throws PatientException.FullListException if patient list
     * exceeds max size
     * @throws FileNotFoundException if text files are not found
     * @throws PatientException.InvalidBloodTypeException if blood
     * type is invalid
     */
    public static void loadFile() throws
            PatientException.FullListException,
            FileNotFoundException,
            PatientException.InvalidBloodTypeException {
        try {
            FileInputStream file = new FileInputStream("transplant.obj");
            ObjectInputStream fin  = new ObjectInputStream(file);
            graph = (TransplantGraph) fin.readObject();
            fin.close();
            System.out.println("Loaded data from transplant.obj");
        } catch(IOException | ClassNotFoundException e) {
            System.out.println("transplant.obj not found. " +
                    "Creating new TransplantGraph object...");
            System.out.println("Loading data from 'donors.txt'...");
            System.out.println("Loading data from 'recipients.txt'...");
            graph = TransplantGraph.buildFromFiles("src/donors.txt",
                    "src/recipients.txt");
        }
    }

    /**
     * Saves the current transplant graph to an object file.
     *
     * @param objToWrite the transplant graph to save
     * @throws IOException if an IO error occurs
     */
    public static void saveFile(TransplantGraph objToWrite)
            throws IOException {
        //From HTML Document
        FileOutputStream file = new FileOutputStream("transplant.obj");
        ObjectOutputStream fout = new ObjectOutputStream(file);
        fout.writeObject(objToWrite);
        fout.close();
    }

    /**
     * Sorts and prints the list of recipients.
     */
    public static void sortRecipients() {
        String response = "";
        TransplantGraph tempGraph = graph;
        while (!response.equals("q")) {
            printSubmenu(false);
            System.out.print("Please select an option: ");
            response = s.nextLine().toLowerCase();
            if (response.equals("i")) {
                System.out.println();
                Collections.sort(graph.getRecipients());
                graph.updateMatrix();
                graph.printAllRecipients();
            } else if (response.equals("n")) {
                graph.getRecipients().sort
                        (new Comparators.NumConnectionsComparator(graph));
                graph.updateMatrix();
                graph.printAllRecipients();
            } else if (response.equals("b")) {
                System.out.println();
                graph.getRecipients().sort
                        (new Comparators.BloodTypeComparator());
                graph.updateMatrix();
                graph.printAllRecipients();
            } else if (response.equals("o")) {
                System.out.println();
                graph.getRecipients().sort
                        (new Comparators.OrganComparator());
                graph.updateMatrix();
                graph.printAllRecipients();
            } else if (response.equals("q")) {
                System.out.println();
                Collections.sort(graph.getRecipients());
                graph.updateMatrix();
                System.out.println("\nReturning to main menu.\n");
            } else {
                System.out.println("\nPlease enter a valid choice.");
            }
        }
    }

    /**
     * Sorts and prints the list of donors.
     */
    public static void sortDonors() {
        String response = "";
        while (!response.equals("q")) {
            printSubmenu(true);
            System.out.print("Please select an option: ");
            response = s.nextLine().toLowerCase();
            if (response.equals("i")) {
                System.out.println();
                Collections.sort(graph.getDonors());
                graph.updateMatrix();
                graph.printAllDonors();
            } else if (response.equals("n")) {
                graph.getDonors().sort
                        (new Comparators.NumConnectionsComparator(graph));
                graph.updateMatrix();
                graph.printAllDonors();
            } else if (response.equals("b")) {
                System.out.println();
                graph.getDonors().sort
                        (new Comparators.BloodTypeComparator());
                graph.updateMatrix();
                graph.printAllDonors();
            } else if (response.equals("o")) {
                System.out.println();
                graph.getDonors().sort
                        (new Comparators.OrganComparator());
                graph.updateMatrix();
                graph.printAllDonors();
            } else if (response.equals("q")) {
                System.out.println();
                Collections.sort(graph.getDonors());
                graph.updateMatrix();
                System.out.println("\nReturning to main menu.\n");
            } else {
                System.out.println("\nPlease enter a valid choice.");
            }
        }
    }
}
