package ua.od.vassio.backup.liquibase;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by vzakharchenko on 01.08.14.
 */
public class StringComparator implements Comparator,Serializable {

    private static final long serialVersionUID = -7460490007108961174L;

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
        String stemp1 = StringUtils.lowerCase(o1.toString(), Locale.getDefault());
        String stemp2 = StringUtils.lowerCase(o2.toString(), Locale.getDefault());

        return stemp1.compareTo(stemp2);
    }
}