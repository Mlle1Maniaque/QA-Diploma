package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String getCardNumberApproved() {
        return "4444444444444441";
    }

    public static String getCardNumberDeclined() {
        return "4444444444444442";
    }

    public static String getCardNumberNotExist() {
        return faker.number().digits(16);
    }

    public static String getCardNumberShort() {
        int randomNumberLength = faker.random().nextInt(16);
        return faker.number().digits(randomNumberLength);
    }

    public static String generateMonth(int addMonths) {
        return LocalDate.now().plusMonths(addMonths).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYear(int addYears) {
        return LocalDate.now().plusYears(addYears).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidMonth() {
        return "00";
    }

    public static String getOwnersName() {
        return faker.name().fullName();
    }

    public static String getNumberOwner() {
        return faker.number().digit();
    }

    public static String getSpecialCharactersOwner() {
        return "!@#$%^&*()_+-";
    }

    public static String getCVC(int length) {
        return faker.number().digits(length);
    }

    public static String getEmptyValue() {
        return "";
    }
}
