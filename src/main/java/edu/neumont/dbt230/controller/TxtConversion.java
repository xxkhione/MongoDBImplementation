/**
 * @author dsargent
 * @createdOn 8/18/2024 at 6:15 AM
 * @projectName MongoDBProject
 * @packageName edu.neumont.dbt230.controller;
 */
package edu.neumont.dbt230.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neumont.dbt230.model.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtConversion {
    public static final File FILE_PATH = new File("C:/Courses/Q4/DBT230/PeopleData/people/long");
    public static final File JSON_PATH = new File("C:/Courses/Q4/DBT230/PeopleData/people/json");
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<Employee> employees;

    public static void convertTxtToJson(){
        employees = getEmployeeData(readFiles());
        String json = getJsonInfo(employees);
        createBulkJsonFile(json);
    }

    public static List<String> readFiles(){
        if(FILE_PATH.exists()){
            String[] employeeFiles = FILE_PATH.list();
            List<String> fileContent = new ArrayList<>();
            for(String employeeFile : employeeFiles){
                fileContent.add(readFile(employeeFile));
            }
            return fileContent;
        }
        return null;
    }
    private static String readFile(String id){
        String fileInformation = "";
        try{
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_PATH + "/" + id)));
            while(bReader.ready()){
                fileInformation = bReader.readLine();
            }
        } catch(IOException ioe){}
        return fileInformation;
    }

    public static List<Employee> getEmployeeData(List<String> employeeInfo){
        employees = new ArrayList<>();
        for(String info : employeeInfo){
            employees.add(getSingleEmployee(info));
        }
        return employees;
    }
    public static Employee getSingleEmployee(String info){
        Employee employee = null;
        int id = 0;
        String firstName = null;
        String lastName = null;
        int hireYear = 0;

        String[] infoParts = info.split(", ");
        id = Integer.parseInt(infoParts[0]);
        firstName = infoParts[1];
        lastName = infoParts[2];
        hireYear = Integer.parseInt(infoParts[3]);
        return employee = new Employee(id, firstName, lastName, hireYear);
    }

    public static String getJsonInfo(List<Employee> employees){
        try{
            return objectMapper.writeValueAsString(employees);
        } catch(JsonProcessingException jpe){}
        return null;
    }
    public static String getSingleJsonInfo(Employee employee){
        try{
            return objectMapper.writeValueAsString(employee);
        } catch(JsonProcessingException jpe){}
        return null;
    }
    public static void createBulkJsonFile(String contents) {
        writeJsonFile("people", contents);
    }
    public static void writeJsonFile(String id, String contents) {
        String path = FILE_PATH + "/" + id + ".json";
        BufferedWriter bWriter = null;
        try {
            bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
            try {
                bWriter.write(contents);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            } finally {
                bWriter.close();
            }
        } catch (IOException ioe) {}
    }
}
