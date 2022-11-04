package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.address().city();
    }

    public static String generateNonRussianCity() {
        Faker faker = new Faker(new Locale("en"));
        return faker.address().city();
    }

    public static String generateNonRussianName() {
        Faker faker = new Faker(new Locale("en"));
        Name name = faker.name();
        return name.lastName() + " " + name.firstName();
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        Name name = faker.name();
        return name.lastName() + " " + name.firstName();
    }

    public static String generatePhone(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(
                    generateCity(locale),
                    generateName(locale),
                    generatePhone(locale)
            );
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}