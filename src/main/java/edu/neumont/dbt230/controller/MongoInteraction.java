package edu.neumont.dbt230.controller; /**
 * @author dsargent
 * @createdOn 8/17/2024 at 6:22 PM
 * @projectName MongoDBProject
 * @packageName PACKAGE_NAME;
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import edu.neumont.dbt230.model.Employee;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.max;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class MongoInteraction {
    public static final String DATABASE_NAME = "DBT230";
    public static final String COLLECTION_NAME = "people";
    public static final String MONGO_URI = "mongodb+srv://dev:dev@cluster.xrzowho.mongodb.net/?retryWrites=true&w=majority&appName=Cluster";
    public static int maxID;

    //Connection method given by MongoDB
    public static void mongoTestConnection() {
        String connectionString = "mongodb+srv://dev:dev@cluster.xrzowho.mongodb.net/?retryWrites=true&w=majority&appName=Cluster";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    //Got help from Gemini with this one since it includes JsonNode
    public static void insertManyDocuments(String json) throws IOException {
        JsonNode rootNode = TxtConversion.objectMapper.readTree(new File(json));

        List<Document> documents = new ArrayList<>();
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                documents.add(Document.parse(node.toString()));
            }
        }

        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        collection.insertMany(documents);
    }

    public static void insertOneDocument(String json) {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME, Document.class);
        collection.insertOne(Document.parse(json));
        mongoClient.close();
    }

    public static void updateEmployee(int id, int option, String updatedValue) {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);

        switch (option) {
            case 1: //updates the first name
                collection.updateOne(Filters.eq("id", id), Updates.set("firstName", updatedValue));
                break;
            case 2: //updates the last name
                collection.updateOne(Filters.eq("id", id), Updates.set("lastName", updatedValue));
                break;
            case 3: //updates the hire year
                collection.updateOne(Filters.eq("id", id), Updates.set("hireYear", Integer.parseInt(updatedValue)));
                break;
        }

        mongoClient.close();
    }

    public static void deleteEmployee(int id) {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);

        collection.deleteOne(Filters.eq("id", id));
        mongoClient.close();
    }

    public static Employee searchForID(int id) {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        Document employee = collection.find(Filters.eq("id", id)).first();

        if (employee != null) {
            int eId = employee.getInteger("id");
            String eFirstName = employee.getString("firstName");
            String eLastName = employee.getString("lastName");
            int eHireYear = employee.getInteger("hireYear");
            Employee foundEmployee = new Employee(eId, eFirstName, eLastName, eHireYear);
            return foundEmployee;
        }
        return null;
    }

    public static List<Employee> searchForLastName(String lastName) {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        FindIterable<Document> employeesWithLastName = collection.find(Filters.eq("lastName", lastName));

        if (employeesWithLastName != null) {
            List<Employee> employees = new ArrayList();
            for (Document employee : employeesWithLastName) {
                int eId = employee.getInteger("id");
                String eFirstName = employee.getString("firstName");
                String eLastName = employee.getString("lastName");
                int eHireYear = employee.getInteger("hireYear");
                Employee foundEmployee = new Employee(eId, eFirstName, eLastName, eHireYear);
                employees.add(foundEmployee);
            }
            return employees;
        }
        return null;
    }

    public static void findMaxID() {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);

        //Aggregate pipeline
        Bson grouping = group(null, max("maxID", "$id"));
        Bson projectStage = project(fields(include("maxID")));

        Document result = collection.aggregate(Arrays.asList(grouping, projectStage)).first();
        maxID = result.getInteger("maxID");
        mongoClient.close();
    }

    public static void createIndexes() {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        collection.createIndex(Indexes.ascending("id", "lastName"));
        mongoClient.close();
    }

    public static List<Employee> getEmployeesByLastName() {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        Bson filterIndex = Filters.exists("lastName");
        FindIterable<Document> allEmployeesByLastName = collection.find(filterIndex);

        List<Employee> employees = new ArrayList();
        for (Document employee : allEmployeesByLastName) {
            int eId = employee.getInteger("id");
            String eFirstName = employee.getString("firstName");
            String eLastName = employee.getString("lastName");
            int eHireYear = employee.getInteger("hireYear");
            Employee foundEmployee = new Employee(eId, eFirstName, eLastName, eHireYear);
            employees.add(foundEmployee);
        }
        return employees;
    }
    public static List<Employee> getEmployeesByID() {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
        Bson filterIndex = Filters.exists("id");
        FindIterable<Document> allEmployeesByid = collection.find(filterIndex);

        List<Employee> employees = new ArrayList();
        for (Document employee : allEmployeesByid) {
            int eId = employee.getInteger("id");
            String eFirstName = employee.getString("firstName");
            String eLastName = employee.getString("lastName");
            int eHireYear = employee.getInteger("hireYear");
            Employee foundEmployee = new Employee(eId, eFirstName, eLastName, eHireYear);
            employees.add(foundEmployee);
        }
        return employees;
    }
}