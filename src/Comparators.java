//Nathan Zheng 116192673 CSE 214.R02

import java.util.Comparator;

/**
 * Used to sort the patient lists.
 *
 * @author Nathan Zheng
 * @version 1.0
 * @since 1.0
 */
public class Comparators {

    /**
     * A comparator that compares patients based on
     * the number of connections.
     */
    public static class NumConnectionsComparator
            implements Comparator<Patient> {
        TransplantGraph mainGraph;

        /**
         * Constructs a NumConnectionsComparator.
         *
         * @param mainGraph the transplant graph
         *                  containing patients and connections
         */
        public NumConnectionsComparator(TransplantGraph mainGraph) {
            this.mainGraph = mainGraph;
        }

        /**
         * Compares two patients based on the number of connections.
         *
         * @param o1 the first patient
         * @param o2 the second patient
         * @return integer values indicating whether the first patient
         * was greater, equal, or less than the second patient
         */
        @Override
        public int compare(Patient o1, Patient o2) {
            int count1 = connections(o1);
            int count2 = connections(o2);
            if (count1 != count2) {
                return Integer.compare(count1, count2);
            }
            return Integer.compare(o1.getID(), o2.getID());
        }

        /**
         * Counts the number of connections for the given patient.
         *
         * @param p the patient to count connections for
         * @return the number of connections
         */
        public int connections(Patient p) {
            int count = 0;
            boolean[][] matrix = mainGraph.getConnections();
            if (p.isDonor()) {
                for (int j = 0; j < mainGraph.getRecipients().size(); j++) {
                    if (matrix[p.getID()][j]) {
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < mainGraph.getDonors().size(); i++) {
                    if (matrix[i][p.getID()]) {
                        count++;
                    }
                }
            }
            return count;
        }
    }

    /**
     * A comparator that compares patients based on their blood type.
     */
    public static class BloodTypeComparator
            implements Comparator<Patient> {
        /**
         * Compares two patients based on their blood type.
         *
         * @param o1 the first patient
         * @param o2 the second patient
         * @return integer values indicating whether the first patient
         * was greater, equal, or less than the second patient
         */
        @Override
        public int compare(Patient o1, Patient o2) {
            String bt1 = o1.getBloodType().getBloodType();
            String bt2 = o2.getBloodType().getBloodType();
            return bt1.compareTo(bt2);
        }
    }

    /**
     * A comparator that compares patients based on their organ name.
     */
    public static class OrganComparator
            implements Comparator<Patient> {
        /**
         * Compares two patients based on the organ name.
         *
         * @param o1 the first patient
         * @param o2 the second patient
         * @return integer values indicating whether the first patient
         * was greater, equal, or less than the second patient
         */
        @Override
        public int compare(Patient o1, Patient o2) {
            String or1 = o1.getOrgan();
            String or2 = o2.getOrgan();
            return or1.compareTo(or2);
        }
    }
}
