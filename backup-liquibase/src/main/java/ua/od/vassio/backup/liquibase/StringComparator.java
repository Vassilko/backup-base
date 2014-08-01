package ua.od.vassio.backup.liquibase;

import java.util.Comparator;

/**
 * Created by vzakharchenko on 01.08.14.
 */
public class StringComparator implements Comparator {

    private static StringComparator comparator = new StringComparator();

    public StringComparator() {
    }

    public static StringComparator getStringComparator() {
        return comparator;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        }
        String stemp1 = o1.toString().toLowerCase();
        String stemp2 = o2.toString().toLowerCase();

        return stemp1.compareTo(stemp2);
    }
}