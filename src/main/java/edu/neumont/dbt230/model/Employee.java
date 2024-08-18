/**
 * @author dsargent
 * @createdOn 8/18/2024 at 6:25 AM
 * @projectName MongoDBProject
 * @packageName edu.neumont.dbt230.model;
 */
package edu.neumont.dbt230.model;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private int hireYear;

    public Employee(int id, String firstName, String lastName, int hireYear) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setHireYear(hireYear);
    }

    //region getters/setters
    public int getId() {
        return id;
    }
    private void setId(int id) {
        if(id <= 0){
            throw new IllegalArgumentException();
        }
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    private void setFirstName(String firstName) {
        if(firstName == null || firstName.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    private void setLastName(String lastName) {
        if(lastName == null || lastName.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.lastName = lastName;
    }
    public int getHireYear() {
        return hireYear;
    }
    private void setHireYear(int hireYear) {
        if(hireYear <= 0){
            throw new IllegalArgumentException();
        }
        this.hireYear = hireYear;
    }
    //endregion


    @Override
    public String toString() {
        StringBuilder employeeToString = new StringBuilder();
        employeeToString.append("Employee id: " + getId());
        employeeToString.append("\nFirst Name: " + getFirstName());
        employeeToString.append("\nLast Name: " + getLastName());
        employeeToString.append("\nHire Year: " + getHireYear());
        return employeeToString.toString();
    }
}
