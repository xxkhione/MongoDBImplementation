/**
 * @author dsargent
 * @createdOn 8/18/2024 at 6:14 AM
 * @projectName MongoDBProject
 * @packageName edu.neumont.dbt230;
 */
package edu.neumont.dbt230;

import edu.neumont.dbt230.controller.MenuController;
import edu.neumont.dbt230.controller.MongoInteraction;
import edu.neumont.dbt230.controller.TxtConversion;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //TxtConversion.convertTxtToJson();
        //MongoInteraction.insertManyDocuments(TxtConversion.JSON_PATH + "/people.json");   Inserted the original documents
        //MongoInteraction.createIndexes();     Created the indexes for the id and lastName fields
        MenuController.run();
    }
}
