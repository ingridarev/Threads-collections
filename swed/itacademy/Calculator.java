package swed.itacademy;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Calculator {

    public static void evaluateRunners(List<Runner> runners, List<Functions> functions) {
        for (int i = 0; i < runners.size(); i++) {
            Runner runner = runners.get(i);
            int totalPoints = runner.getAdvantagePoints();
            for (Functions function : functions) {
                totalPoints += function.calculateAdvantagePoints(runner);
            }
            Runner updatedRunner = Runner.Builder.builder()
                    .withName(runner.getName())
                    .withPersonalId(runner.getPersonalId())
                    .withStartingNumber(runner.getStartingNumber())
                    .withFinalPosition(runner.getFinalPosition())
                    .withAdvantagePoints(totalPoints)
                    .withBirthDate(runner.getBirthDate())
                    .build();
            runners.set(i, updatedRunner);
        }
        Collections.sort(runners, Comparator.comparing(Runner::getAdvantagePoints).reversed());

        System.out.println();
        System.out.println("Runners sorted by advantage points: ");

        for (Runner runner : runners) {
            System.out.println("Name: " + runner.getName());
            System.out.println("Advantage Points: " + runner.getAdvantagePoints());
            System.out.println();
        }
    }

    public static Integer addPointsIfBornOnWeekend(Runner runner) {
        if (runner.getBirthDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                runner.getBirthDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            return 3;
        }
        return 0;
    }

    public static Integer addPointsIfNotBornInWinter(Runner runner) {
        if (runner.getBirthDate().getMonth().getValue() > Month.FEBRUARY.getValue() &&
                runner.getBirthDate().getMonth().getValue() < Month.DECEMBER.getValue()) {
            return 2;
        }
        return 0;
    }

    public static Integer addPointsIfGoodStartingPosition(Runner runner) {
        if (runner.getStartingNumber() <= 25) {
            return 1;
        }
        return 0;
    }

    @FunctionalInterface
    public interface Functions {
        int calculateAdvantagePoints(Runner runner);
    }

}
