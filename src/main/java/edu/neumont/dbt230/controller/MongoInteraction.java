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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoInteraction {
    public static final String DATABASE_NAME = "DBT230";
    public static final String COLLECTION_NAME = "people";
    public static final String MONGO_URI = "mongodb+srv://dev:dev@cluster.xrzowho.mongodb.net/?retryWrites=true&w=majority&appName=Cluster";

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

    public static void insertManyDocuments(String json) throws IOException {
        JsonNode rootNode = TxtConversion.objectMapper.readTree(new File(json));

        List<Document> documents = new ArrayList<>();
        if(rootNode.isArray()){
            for(JsonNode node : rootNode){
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
}
