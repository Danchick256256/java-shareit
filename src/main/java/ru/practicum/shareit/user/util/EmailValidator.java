package ru.practicum.shareit.user.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
