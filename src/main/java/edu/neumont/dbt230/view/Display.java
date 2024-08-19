/**
 * @author dsargent
 * @createdOn 8/18/2024 at 8:25 AM
 * @projectName MongoDBProject
 * @packageName edu.neumont.dbt230.view;
 */
package edu.neumont.dbt230.view;

import edu.neumont.dbt230.controller.MongoInteraction;
import edu.neumont.dbt230.controller.TxtConversion;
import edu.neumont.dbt230.model.Employee;

import java.util.List;

public class Display {
    public static void welcomeMsg(){
        System.out.println("Welcome to the Employee Database!");
    }
    public static int mainMenu(){
        int option = Console.getIntInput("1. Create Employee File \n2. Update Employee File \n3. Delete Employee File \n4. Search for an Employee File \n" +
                "5. View All Employees \n6. Exit");
        return option;
    }

    public static String getFirstName(){
        String firstName = Console.getStringInput("Enter the Employee's first name: ");
        return firstName.toUpperCase();
    }
    public static String getLastName(){
        String lastName = Console.getStringInput("Enter the Employee's last name: ");
        return lastName.toUpperCase();
    }
    public static int getHireYear(){
        int hireYear = Console.getIntInput("Enter the Employee's hire year: ");
        return hireYear;
    }

    public static int searchMenu(){
        return Console.getIntInput("How would you like to find the employees? \n1. By ID \n2. By Last Name");
    }

    public static int getIDSearch(){
        int idSearch = Console.getIntInput("Enter the id of the wanted Employee: ");
        return idSearch;
    }

    public static int updateMenu(){
        int option = Console.getIntInput("Choose what you would like to update:\n 1. First Name\n 2. Last Name\n 3. Hire Year");
        return option;
    }
    public static String updateFirstName(){
        String firstName = Console.getStringInput("Enter the Employee's updated first name: ");
        return firstName.toUpperCase();
    }
    public static String updateLastName(){
        String lastName = Console.getStringInput("Enter the Employee's updated last name: ");
        return lastName.toUpperCase();
    }
    public static int updateHireYear(){
        int hireYear = Console.getIntInput("Enter the Employee's updated hire year: ");
        return hireYear;
    }

    public static void quit(){
        System.out.println("Exiting Application.....");
    }

    public static void successfulMsg(){
        System.out.println("Success!");
    }
    public static void errorMsg(){
        System.out.println("That employee does not exist!");
    }
    public static int displayMenu(){
        Console.writeLn("How would you like to see the employees?");
        return Console.getIntInput("1. By ID" + "\n" + "2. By Last Name" + "\n" + "3. Unsorted", 1, 3);
    }

    public static void printSingleEmployee(Employee employee){
        System.out.println(employee.toString());
    }
    public static void printEmployees(){
        System.out.println("Printing structured employees:");
        for(Employee employee : TxtConversion.getEmployeeData(TxtConversion.readFiles())){
            System.out.println(employee.toString());
            System.out.println("------------------------");
        }
    }
    public static void printIdIndexEmployees(){
        System.out.println("Printing employees by ID:");
        List<Employee> idIndex = MongoInteraction.getEmployeesByID();
        for(int i = 1; i < idIndex.size(); i ++){
            System.out.println(idIndex.get(i));
            System.out.println("------------------------");
        }
    }
    public static void printLastNameIndexEmployees(){
        System.out.println("Printing employees by last name");
        List<Employee> lastNameIndex = MongoInteraction.getEmployeesByLastName();
        for(int i = 1; i < lastNameIndex.size(); i++){
            System.out.println(lastNameIndex.get(i));
            System.out.println("------------------------");
        }
    }
    public static void printEmployeesWithGivenLastName(List<Employee> employees){
        for(Employee employee : employees){
            printSingleEmployee(employee);
        }
    }

    public static void printTimeTakenToExecute(long duration){
        long durationInSeconds = duration / 1_000_000_000;
        System.out.println("Executed in " + (durationInSeconds / 60) + " minutes and " + (durationInSeconds % 60) + " seconds.");
    }
}
