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

    public static String getCardNumberRandom() {
        int randomNumberLength = faker.random().nextInt(16);
        return faker.number().digits(randomNumberLength);
    }

    public static String getCardNumberEmpty() {
        return "";
    }

    public static String getPrevMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate prevMonth = currentDate.minusMonths(1);
        return prevMonth.format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        return String.format("%02d", currentYear % 100);
    }

    public static String getPrevYear() {
        int currentYear = Integer.parseInt(getCurrentYear());
        int prevYear = currentYear - 1;
        return String.format("%02d", prevYear % 100);
    }

    public static String getYearPlusFive() {
        int currentYear = Integer.parseInt(getCurrentYear());
        int plusYear = currentYear + 5;
        return String.format("%02d", plusYear % 100);
    }

    public static String getMonth() {
        return String.format("%02d", faker.number().numberBetween(1, 13));
    }

    public static String getInvalidMonth() {
        return "00";
    }

    public static String getEmptyMonth() {
        return "";
    }

    public static String getYear() {
        return String.format("%02d", faker.number().numberBetween(24, 29));
    }

    public static String getEmptyYear() {
        return "";
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

    public static String getEmptyOwner() {
        return "";
    }

    public static String getCVC() {
        return faker.number().digits(3);
    }

    public static String getCVC1number() {
        return faker.number().digits(1);
    }

    public static String getCVC2number() {
        return faker.number().digits(2);
    }

    public static String getEmptyCVC() {
        return "";
    }
}
