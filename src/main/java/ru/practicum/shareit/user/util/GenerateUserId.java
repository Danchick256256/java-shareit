package ru.practicum.shareit.user.util;

public class GenerateUserId {
    private static int id = 0;

    public static int getId() {
        return ++id;
    }
}
