package ru.practicum.shareit.item.util;

public class GenerateItemId {
    private static int id = 0;

    public static int getId() {
        return ++id;
    }
}
