package swed.itacademy;

import java.time.LocalDate;
import java.util.Random;

public class RandomDate {
    public static LocalDate generateRandomDateOfBirth(LocalDate startDate, LocalDate endDate) {
        Random random = new Random();

        int randomYear = startDate.getYear() + random.nextInt(endDate.getYear() - startDate.getYear() + 1);
        int randomMonth = random.nextInt(12) + 1;
        int daysInMonth = LocalDate.of(randomYear, randomMonth, 1).lengthOfMonth();
        int randomDay = random.nextInt(daysInMonth) + 1;

        return LocalDate.of(randomYear, randomMonth, randomDay);
    }

    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(2007, 12, 31);

        LocalDate randomDateOfBirth = generateRandomDateOfBirth(startDate, endDate);
    }
}