package aabrasha.ua.streettranslator.util;

import aabrasha.ua.streettranslator.model.SortMethod;
import aabrasha.ua.streettranslator.model.StreetEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static aabrasha.ua.streettranslator.util.StreetNameCleaner.clean;

/**
 * @author Andrii Abramov on 12/25/16.
 */
public final class StreetsSorters {

    private static Map<SortMethod, Comparator<StreetEntry>> comparators = new HashMap<>();

    static {
        comparators.put(SortMethod.BY_INSERTION_DATE, byInsertionDate());
        comparators.put(SortMethod.BY_NEW_NAME, byNewName());
        comparators.put(SortMethod.BY_OLD_NAME, byOldName());
    }

    public static Comparator<StreetEntry> getStreetEntryComparator(SortMethod sortMethod) {
        return comparators.get(sortMethod);
    }

    private static Comparator<StreetEntry> byInsertionDate() {
        return reversed(new Comparator<StreetEntry>() {
            @Override
            public int compare(StreetEntry s1, StreetEntry s2) {
                return s1.getInsertionDate().compareTo(s2.getInsertionDate());
            }
        });
    }

    private static Comparator<StreetEntry> byOldName() {
        return new Comparator<StreetEntry>() {
            @Override
            public int compare(StreetEntry s1, StreetEntry s2) {
                String s1Name = clean(s1.getOldName());
                String s2Name = clean(s2.getOldName());
                return StringUtils.compareIgnoreCase(s1Name, s2Name, false);
            }
        };
    }

    private static Comparator<StreetEntry> byNewName() {
        return new Comparator<StreetEntry>() {
            @Override
            public int compare(StreetEntry s1, StreetEntry s2) {
                String s1Name = clean(s1.getNewName());
                String s2Name = clean(s2.getNewName());
                return StringUtils.compareIgnoreCase(s1Name, s2Name, false);
            }
        };
    }

    private static Comparator<StreetEntry> reversed(final Comparator<StreetEntry> comparator) {
        return new Comparator<StreetEntry>() {
            @Override
            public int compare(StreetEntry s1, StreetEntry s2) {
                return -(comparator.compare(s1, s2));
            }
        };
    }

}
