package com.bissonthomas.gamefarm.model;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DBMongoConnection {

    public static final String DATABASE_NAME = "gamefarmdatabase";
    public static final String COLLECTION_FLOWER_NAME = "flowers";
    public static final String COLLECTION_GROUND_NAME = "grounds";
    public static final String COLLECTION_MAP_NAME = "maps";

    public static final String ID_FLOWERS = "id";
    public static final String NAME_FLOWERS = "name";
    public static final String DESCRIPTION_FLOWERS = "description";
    public static final String GROWTIME_FLOWERS = "growtime";
    public static final String PRICE_FLOWERS = "price";
    public static final String SELLING_PRICE_FLOWERS = "sellingPrice";
    public static final String STARTING_FLOWERS = "starting";
    public static final String HARVEST_FLOWERS = "nbHarvest";
    public static final String MAP_FLOWERS = "map";

    public static final String ID_GROUNDS = "id";
    public static final String X_GROUNDS = "positionX";
    public static final String Y_GROUNDS = "positionY";
    public static final String MAP_GROUNDS = "map";

    public static final String ID_MAP = "id";
    public static final String PRICE_MAP = "price";
    public static final String X_MAP = "positionX";
    public static final String Y_MAP = "positionY";

    private final String[][] flowersName = {{"Eglantine","0,0"},{"Jasmin","0,0"},{"Rose","1,0"},{"Orchidée","-1,0"},{"Geranium","0,1"},
            {"Tulipe","-1,0"}, {"Tournesol","0,1"},{"Lavande","0,0"},{"Paquerette","0,1"},{"Coquelicot","0,-1"},
            {"Lila","intro"}};

    private final String[][] groundsPositionAndMap = {{"10","10","0,0"},{"200","200","0,0"},{"20","20","1,0"}};

    private final String[][] map = {{"0","0","0"},{"200","1","0"},{"5000","-1","0"},{"50000","0","1"}, {"250000","0","-1"}};

    private MongoClient mongo;
    private MongoCredential credential;
    private MongoDatabase database;

    public DBMongoConnection() {
        // Creating a Mongo client
        mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        credential = MongoCredential.createCredential("sampleUser", DATABASE_NAME,
                "password".toCharArray());

        // Accessing the database
        database = mongo.getDatabase(DATABASE_NAME);
    }

    public boolean collectionExists(final String collectionName) {
        for (String name : database.listCollectionNames()) {
            if(name.equals(collectionName)) return true;
        }
        return false;
    }

    public void createFlowerColection(){
        //Creating a collection
        database.createCollection(COLLECTION_FLOWER_NAME);

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection(COLLECTION_FLOWER_NAME);

        Document document;
        for(int i=0; i<flowersName.length;i++) {
            document = new Document(ID_FLOWERS, i)
                    .append(NAME_FLOWERS, flowersName[i][0])
                    .append(DESCRIPTION_FLOWERS, "desc : " + i)
                    .append(GROWTIME_FLOWERS, 5000)
                    .append(PRICE_FLOWERS, 10)
                    .append(SELLING_PRICE_FLOWERS, 10)
                    .append(MAP_FLOWERS, flowersName[i][1])
                    .append(HARVEST_FLOWERS, 3);
            if(flowersName[i][0].equals("Jasmin") || flowersName[i][0].equals("Lila") || flowersName[i][0].equals("Lavande"))
                document.append(STARTING_FLOWERS, true);
            else
                document.append(STARTING_FLOWERS, false);
            collection.insertOne(document);
        }

    }


    public void createMapColection(){
        //Creating a collection
        database.createCollection(COLLECTION_MAP_NAME);

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection(COLLECTION_MAP_NAME);

        Document document;
        for(int i=0; i<map.length;i++) {
            document = new Document(ID_MAP, i)
                    .append(PRICE_MAP, Integer.parseInt(map[i][0]))
                    .append(X_MAP, Integer.parseInt(map[i][1]))
                    .append(Y_MAP, Integer.parseInt(map[i][2]));
            collection.insertOne(document);
        }

    }

    public void createGroundCollection() {
        //Creating a collection
        database.createCollection(COLLECTION_GROUND_NAME);

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection(COLLECTION_GROUND_NAME);

        Document document;
        for(int i=0; i<groundsPositionAndMap.length;i++) {
            document = new Document(ID_GROUNDS, i)
                    .append(X_GROUNDS, Integer.parseInt(groundsPositionAndMap[i][0]))
                    .append(Y_GROUNDS, Integer.parseInt(groundsPositionAndMap[i][1]))
                    .append(MAP_GROUNDS, groundsPositionAndMap[i][2]);
            collection.insertOne(document);
        }
    }

    public ArrayList<JSONObject> getColectionFlowerIterable(boolean starting, String mapID) {
        ArrayList<JSONObject> allPlants = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_FLOWER_NAME);
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();

        // Getting the iterator
        Iterator it = iterDoc.iterator();
        JSONObject json;
        Document doc;
        while (it.hasNext()) {
            try {
                doc = (Document) it.next();
                json = new JSONObject(doc.toJson());
                if(starting) {
                    if (json.getBoolean(STARTING_FLOWERS))
                        allPlants.add(json);
                } else {
                    if (json.getString(MAP_FLOWERS).equals(mapID))
                        allPlants.add(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(allPlants.isEmpty()) return null;
        return allPlants;
    }

    public ArrayList<JSONObject> getColectionIterableGround(String mapID) {
        ArrayList<JSONObject> allGround = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_GROUND_NAME);
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();

        // Getting the iterator
        Iterator it = iterDoc.iterator();
        JSONObject json;
        Document doc;
        while (it.hasNext()) {
            try {
                doc = (Document) it.next();
                json = new JSONObject(doc.toJson());
                if(json.getString(MAP_GROUNDS).equals(mapID))
                    allGround.add(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(allGround.isEmpty()) return null;
        return allGround;
    }

    public ArrayList<JSONObject> getColectionIterableMap() {
        ArrayList<JSONObject> jsonMaps = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_MAP_NAME);
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();

        // Getting the iterator
        Iterator it = iterDoc.iterator();
        JSONObject json;
        Document doc;
        while (it.hasNext()) {
            try {
                doc = (Document) it.next();
                jsonMaps.add(new JSONObject(doc.toJson()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(jsonMaps.isEmpty()) return null;
        return jsonMaps;
    }

    public JSONObject getColectionIterableMap(int x, int y) {
        JSONObject jsonMap = null;
        MongoCollection<Document> collection = database.getCollection(COLLECTION_MAP_NAME);
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();

        // Getting the iterator
        Iterator it = iterDoc.iterator();
        JSONObject json;
        Document doc;
        while (it.hasNext()) {
            try {
                doc = (Document) it.next();
                json = new JSONObject(doc.toJson());
                if(json.getInt(X_MAP) == x && json.getInt(Y_MAP) == y)
                    jsonMap = json;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonMap;
    }
}
