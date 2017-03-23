package aabrasha.ua.streettranslator.util;

import java.util.Collection;

/**
 * @author Andrii Abramov on 8/31/16.
 */
public final class CollectionsUtil {

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> items) {
        return items.toArray((T[]) new Object[items.size()]);
    }

}
