import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {

    private static final Random random = new Random();

    // Lists of possible activities and preferences
    private static final List<String> indoorActivities = List.of(
            "Swimming", "Pool", "Billiards", "Movie Theater", "Museum", "Board Games", "Card Games"
    );
    private static final List<String> outdoorActivities = List.of(
            "Ski", "Snowboard", "Hiking", "Road Running", "Trail Running", "Biking", "Sightseeing"
    );
    private static final List<String> foodPreferences = List.of(
            "Chinese", "Korean", "Japanese", "Taiwanese", "Vietnamese", "Thai", "Italian", "Mexican", "American"
    );
    private static final String[] locations = {"Europe", "Asia", "North America", "South America", "Africa", "Australia"};

    // Possible options for demographic variables
    private static final String[] genderOptions = {"Male", "Female", "No preference"};
    private static final String[] agePreferences = {"Young", "Middle-aged", "Senior"};
    private static final String[] races = {"White", "Black", "Asian", "Hispanic", "Latino", "Indian", "Other"};

    private static final double RATIONALITY_RATE = 1.0; // Rate parameter for the exponential distribution

    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            // Generate in one thousand units
            generateAndSaveUserDataGaussian(i * 1000);
            generateAndSaveUserDataExponential(i * 1000);
            // Generate in million units
            // generateAndSaveUserDataGaussian(i * 1000000);
            // generateAndSaveUserDataExponential(i * 1000000);
        }
    }

    private static void generateAndSaveUserDataGaussian(int numUsers) {
        // Generate user data for a specified number of users
        List<UserData> users = IntStream.rangeClosed(1, numUsers)
                .mapToObj(i -> new UserData(
                        i,
                        1 + random.nextInt(5),
                        pickOne(genderOptions),
                        pickOne(agePreferences),
                        getRandomActivities(indoorActivities, 2, 3),
                        getRandomActivities(outdoorActivities, 2, 3),
                        getRandomActivities(foodPreferences, 2, 4),
                        random.nextBoolean(),
                        pickOne(races),
                        0.5 + 0.2 * random.nextGaussian(), // Rationality using Gaussian distribution
                        // exponentialRandom(RATIONALITY_RATE), // Rationality using exponential distribution
                        1000 + random.nextInt(9000), // Budget
                        pickOne(locations), // Location
                        1 + random.nextInt(30), // Duration of stay in days
                        1 + random.nextInt(10) // Number of places to visit
                ))
                .toList();

        // Output user data to a CSV file
        saveUsersToCSV(users, "travel_data_gaussian_" + numUsers + ".csv");
        System.out.println("Travel data generation complete for " + numUsers + " users using gaussian distribution!");
    }

    private static void generateAndSaveUserDataExponential(int numUsers) {
        // Generate user data for a specified number of users
        List<UserData> users = IntStream.rangeClosed(1, numUsers)
                .mapToObj(i -> new UserData(
                        i,
                        1 + random.nextInt(5),
                        pickOne(genderOptions),
                        pickOne(agePreferences),
                        getRandomActivities(indoorActivities, 2, 3),
                        getRandomActivities(outdoorActivities, 2, 3),
                        getRandomActivities(foodPreferences, 2, 4),
                        random.nextBoolean(),
                        pickOne(races),
                        // 0.5 + 0.2 * random.nextGaussian(), // Rationality using Gaussian distribution
                        exponentialRandom(RATIONALITY_RATE), // Rationality using exponential distribution
                        1000 + random.nextInt(9000), // Budget
                        pickOne(locations), // Location
                        1 + random.nextInt(30), // Duration of stay in days
                        1 + random.nextInt(10) // Number of places to visit
                ))
                .toList();

        // Output user data to a CSV file
        saveUsersToCSV(users, "travel_data_exponential_" + numUsers + ".csv");
        System.out.println("Travel data generation complete for " + numUsers + " users using exponential distribution!");
    }

    private static double exponentialRandom(double lambda) {
        return -Math.log(1 - random.nextDouble()) / lambda;
    }

    private static void saveUsersToCSV(List<UserData> users, String fileName) {
        // Save generated data to CSV, including headers
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("UserID,FamilySize,GenderPreference,AgePreference,IndoorActivity,OutdoorActivity,FoodPreference,Religious,Race,Rationality,Budget,Location,DurationOfStay,NumberOfPlaces\n");
            for (UserData user : users) {
                writer.write(user.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String pickOne(String[] options) {
        // Randomly pick one option from an array
        return options[random.nextInt(options.length)];
    }

    private static List<String> getRandomActivities(List<String> activities, int min, int max) {
        // Generate a list of random activities based on specified range
        int numActivities = min + random.nextInt(max - min + 1);
        return random.ints(numActivities, 0, activities.size())
                .mapToObj(activities::get)
                .toList();
    }

    static class UserData {
        // Data structure for storing user attributes
        int userId;
        int familySize;
        String genderPreference;
        String agePreference;
        List<String> indoorActivity;
        List<String> outdoorActivity;
        List<String> foodPreference;
        boolean religious;
        String race;
        double rationality;
        int budget;
        String location;
        int durationOfStay;
        int numberOfPlaces;

        public UserData(int userId, int familySize, String genderPreference, String agePreference, List<String> indoorActivity, List<String> outdoorActivity, List<String> foodPreference, boolean religious, String race, double rationality, int budget, String location, int durationOfStay, int numberOfPlaces) {
            this.userId = userId;
            this.familySize = familySize;
            this.genderPreference = genderPreference;
            this.agePreference = agePreference;
            this.indoorActivity = indoorActivity;
            this.outdoorActivity = outdoorActivity;
            this.foodPreference = foodPreference;
            this.religious = religious;
            this.race = race;
            this.rationality = rationality;
            this.budget = budget;
            this.location = location;
            this.durationOfStay = durationOfStay;
            this.numberOfPlaces = numberOfPlaces;
        }

        @Override
        public String toString() {
            return userId + "," + familySize + "," + "\"" + genderPreference + "\"" + "," + "\"" + agePreference + "\"" + "," +
                    "\"" + String.join(";", indoorActivity) + "\"" + "," + "\"" + String.join(";", outdoorActivity) + "\"" + "," +
                    "\"" + String.join(";", foodPreference) + "\"" + "," + religious + "," + race + "," + rationality + "," +
                    budget + "," + location + "," + durationOfStay + "," + numberOfPlaces;
        }
    }
}
