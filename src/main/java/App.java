import com.google.gson.Gson;
import edu.miu.cs.cs489appsd.lab1b.model.Employee;
import edu.miu.cs.cs489appsd.lab1b.model.PensionPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Employee daniel = new
                Employee(1000, "Daniel", "Agar", "2018-01-17",105_945.50,
                new PensionPlan("EX1089 ", "2023-01-17", 100));
        Employee benard = new
                Employee(1001, "Benard", "Shaw", "2019-04-03",197_750.00,
                null);
        Employee carly = new
                Employee(1002, "Carly", "Agar", "2014-05-16",842_000.75,
                new PensionPlan("SM2307", "2014-05-16", 1_555.50));
        Employee wesley = new
                Employee(1003, "Wesley", "Schneider", "2019-05-02",74_500.00,
                null);
        List<Employee> employeeList = Arrays.asList(daniel, benard, carly, wesley);

        // Define a comparator for sorting by last name in ascending order
        Comparator<Employee> lastNameComparator = Comparator.comparing(Employee::getLastName);

        // Define a comparator for sorting by yearly salary in descending order
        Comparator<Employee> salaryComparator = Comparator.comparingDouble(Employee::getYearlySalary).reversed();

        // Combine the two comparators using thenComparing
        Comparator<Employee> combinedComparator = lastNameComparator.thenComparing(salaryComparator);

        // Sort the list using the combined comparator
        Collections.sort(employeeList, combinedComparator);

        System.out.println("List of employee");
        Gson gson = new Gson();
        System.out.println(gson.toJson(employeeList));
        System.out.println("---------");

        // Filter employees who are not enrolled for pension and will qualify for enrollment next month
        List<Employee> upcomingEnrollees = filterUpcomingEnrollees(employeeList);

        // Sort the filtered list by employment date in ascending order
        upcomingEnrollees.sort(Comparator.comparing(Employee::getEmploymentDate));

        // Convert the filtered list to JSON
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(upcomingEnrollees);

        // Print the JSON
        System.out.println("Monthly Upcoming Enrollees Report");
        System.out.println(json);
        System.out.println("---------");
    }

    private static List<Employee> filterUpcomingEnrollees(List<Employee> employees) {
        LocalDate firstDayOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfNextMonth = firstDayOfNextMonth.plusMonths(1).minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Employee> upcomingEnrollees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPensionPlan() == null &&
                    (LocalDate.parse(employee.getEmploymentDate(), formatter).plusYears(5).isAfter(firstDayOfNextMonth.minusDays(1)) &&
                            LocalDate.parse(employee.getEmploymentDate(), formatter).plusYears(5).isBefore(lastDayOfNextMonth.plusDays(1)))) {
                upcomingEnrollees.add(employee);
            }
        }
        return upcomingEnrollees;
    }
}
