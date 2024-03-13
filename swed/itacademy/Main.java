package swed.itacademy;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<String> runnerIds = Runner.getAllRunnersIDs();
        List<Runner> numbersList = Collections.synchronizedList(new ArrayList<>());

        AtomicInteger atomic = new AtomicInteger();

        System.out.println("Runners ids: " + runnerIds);

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("Runners names: ");
            for (String runnerId : runnerIds) {
                executorService.submit(() -> {
                    try {
                        System.out.println(Runner.getRunnerById(runnerId).getName());

                        Runner runner = Runner
                                .Builder.builder()
                                .withStartingNumber(atomic.incrementAndGet())
                                .withName(Runner.getRunnerById(runnerId).getName())
                                .withPersonalId(runnerId)
                                .withBirthDate(RandomDate.
                                        generateRandomDateOfBirth(LocalDate.of(1980, 1, 1),
                                                LocalDate.of(2007, 12, 31)))
                                .withAdvantagePoints(0)
                                .build();

                        numbersList.add(runner);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    executorService.shutdown();
                });
            }
        }
        numbersList.sort(Comparator.comparing(Runner::getStartingNumber));
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime));


        Map<String, List<Runner>> groupedRunners = numbersList.stream()
                .collect(Collectors.groupingBy(runner -> validateAndFormat(runner.getName())));


        Map<String, List<Runner>> sameNameRunners = groupedRunners.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        List<String> runnerDuplicatesWithFormattedNames = new ArrayList<>();

        sameNameRunners.keySet().forEach(runnerName -> {
            String formattedName = validateAndFormat(runnerName);
            runnerDuplicatesWithFormattedNames.add(runnerName);
        });

        for (Map.Entry<String, List<Runner>> entry : sameNameRunners.entrySet()) {
            String formattedName = entry.getKey();

            for (Runner runner : entry.getValue()) {
                System.out.println("Duplicate name " + formattedName + " with starting number : " + runner.getStartingNumber());
            }
        }

        List<Calculator.Functions> functions = List.of(
                Calculator::addPointsIfBornOnWeekend,
                Calculator::addPointsIfNotBornInWinter,
                Calculator::addPointsIfGoodStartingPosition
        );

        Calculator.evaluateRunners(numbersList, functions);

        System.out.println("Runners with advantage points: " + numbersList);
    }

    public static String validateAndFormat(String name) {
        String withoutSpaces = name.replaceAll(" ", "");
        return withoutSpaces.substring(0, 1).toUpperCase() + withoutSpaces.substring(1).toLowerCase();
    }
}
