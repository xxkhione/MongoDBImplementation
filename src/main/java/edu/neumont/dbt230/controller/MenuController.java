/**
 * @author dsargent
 * @createdOn 8/18/2024 at 8:24 AM
 * @projectName MongoDBProject
 * @packageName edu.neumont.dbt230.controller;
 */
package edu.neumont.dbt230.controller;

import edu.neumont.dbt230.model.Employee;
import edu.neumont.dbt230.view.Display;

import java.util.List;

public class MenuController {
    public void run() {
        long startTime;
        long endTime;
        do{
            startTime = System.nanoTime();
            //FileManipulator.createIndexes();
            Display.welcomeMsg();
            int selection = Display.mainMenu();
            switch(selection) {
                case 1: // 1. Create an employee file
                    String fName = Display.getFirstName();
                    String lName = Display.getLastName();
                    int hireYear = Display.getHireYear();
                    //FileManipulator.createFile(formatEmployeeContents(fName, lName, hireYear));
                    break;
                case 2: // 2. Update an employee file
                    int id = Display.getIDSearch();
                    int option = Display.updateMenu();
                    switch(option) {
                        case 1: // First Name
                            String updatedFirstName = Display.updateFirstName();
                            //FileManipulator.updateFile(id, option, updatedFirstName);
                            break;
                        case 2: // Last Name
                            String updatedLastName = Display.updateLastName();
                            //FileManipulator.updateFile(id, option, updatedLastName);
                            break;
                        case 3: // Hire Year
                            int updatedHireYear = Display.updateHireYear();
                            //FileManipulator.updateFile(id, option, String.valueOf(updatedHireYear));
                            break;
                    }
                    break;
                case 3: // 3. Delete an employee file
                    int idDelete = Display.getIDSearch();
                    //FileManipulator.deleteFile(idDelete);
                    break;
                case 4: // 4. Search for an employee
                    int searchOption = Display.searchMenu();
                    switch(searchOption){
                        case 1: //search by ID
//                            int searchID = Display.getIDSearch();
//                            //Employee searchedEmployee = FileManipulator.searchById(searchID);
//                            if(searchedEmployee != null){
//                                Display.printSingleEmployee(searchedEmployee);
//                            } else { Display.errorMsg(); }
//                            break;
//                        case 2: //search by last name
//                            String lastName = Display.getLastName();
//                            //List<Employee> employeesWithLastName = FileManipulator.searchByLastName(lastName);
//                            if(employeesWithLastName != null){
//                                Display.printEmployeesWithGivenLastName(employeesWithLastName);
//                            } else { Display.errorMsg(); }
//                            break;
                    }
                    break;
                case 5: // 5. View all employees
                    int choice = Display.displayMenu();
                    switch(choice){
                        case 1: //Display by ID
                           // Display.printIdIndexEmployees();
                            break;
                        case 2: //Display by Last Name
                            //Display.printLastNameIndexEmployees();
                            break;
                        case 3: //Display unsorted employees
                            Display.printEmployees();
                            break;
                    }
                    break;
                default: // 6. Quit
                    Display.quit();
                    return;
            }
            endTime = System.nanoTime();
            Display.printTimeTakenToExecute((endTime - startTime));
        } while(true);
    }

    private String formatEmployeeContents(String firstName, String lastName, int hireYear){
        StringBuilder contents = new StringBuilder();
        contents.append(firstName + ", ");
        contents.append(lastName + ", ");
        contents.append(hireYear);
        return contents.toString();
    }
}
