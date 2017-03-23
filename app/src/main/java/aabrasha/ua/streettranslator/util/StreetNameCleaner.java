package aabrasha.ua.streettranslator.util;

/**
 * @author Andrii Abramov on 12/25/16.
 */
public class StreetNameCleaner {

    public static String clean(String name) {

        if (name == null) {
            return null;
        }

        return name.replaceAll("ул\\.\\s*", "")
                .replaceAll("улица\\s*", "")
                .replaceAll("бул\\.\\s*", "")
                .replaceAll("бульвар\\s*", "")
                .replaceAll("пл\\.\\s*", "")
                .replaceAll("площадь\\s*", "")
                .replaceAll("пр\\.\\s*", "")
                .replaceAll("просп\\.\\s*", "")
                .replaceAll("проспект\\s*", "")
                .replaceAll("пер\\.\\s*", "")
                .replaceAll("переулок\\s*", "");
    }

}
