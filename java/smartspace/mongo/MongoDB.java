package smartspace.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class MongoDB {

	public static void main(String[] args) {
		//Test for Mongo connection
		try {
		MongoClient mongoClient = new MongoClient("localhost",27017);
		DB db = mongoClient.getDB("smartspace");
		}
		catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

}
