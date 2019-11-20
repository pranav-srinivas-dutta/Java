package org.open.nosql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.open.nosql.dynamodb.DynamoAdapter;
import org.open.nosql.exception.AdapterException;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class DynamoDBTest {

	public static void main(String[] args) throws AdapterException {
		DynamoAdapter.initializeDynamoAdapter("/home/pranav/Documents/repositories/git/personal/Java/dynamodb/src/main/resources/dynamo.properties");

		/*
		 * CreateAdapter c= DynamoAdapter.fetchCreateAdapter(); c.create("Advisors");
		 */
		
		Map<String, String> primaryMap= new HashMap<String, String> ();
		primaryMap.put("adivisor_code", "AG0014");
		primaryMap.put("channel_code", "CH0007");
		primaryMap.put("phone_number", "94421442222");
		primaryMap.put("email", "pranav@pranav.com.");
		
		Map<String, String> infoMap= new HashMap<String, String> ();
		infoMap.put("city", "vellore");
		infoMap.put("country", "india");
		/* infoMap.put("city", "vellore"); */
		
		PostAdapter p= DynamoAdapter.fetchPostAdapter();
		p.post("Advisors", primaryMap, infoMap);
		//DynamoDBTest.insert();

		GetAdapter g = DynamoAdapter.fetchGetAdapter();
		System.out.println(g.get("Advisors", "adivisor_code", "channel_code", "AG0014", "CH0007"));
		System.out.println(g.get("Advisors", "adivisor_code", "channel_code", "phone_number#9442144222", "phone_number#9442144222"));
	}

	public static void update() {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("Advisors");

		String advisorCode = "AG0001";
		String channelCode = "CH0001";

		UpdateItemSpec updateItemSpec = new UpdateItemSpec()
				.withPrimaryKey("adivisor_code", advisorCode, "channel_code", channelCode)
				.withUpdateExpression("set info.subchannel_code = :r, info.designation=:p")
				.withValueMap(new ValueMap().withString(":r", "SUBCH0001").withString(":p", "DESIGN0002")
				/* .withList(":a", Arrays.asList("Larry", "Moe", "Curly")) */)
				.withReturnValues(ReturnValue.UPDATED_NEW);

		try {
			System.out.println("Updating the item...");
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

		} catch (Exception e) {
			System.err.println("Unable to update item: " + advisorCode + " " + channelCode);
			System.err.println(e.getMessage());
		}
	}

	public static void read() {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("Advisors");

		String adivisorCode = "AG0001";
		String channelCode = "CH0001";

		GetItemSpec spec = new GetItemSpec().withPrimaryKey("adivisor_code", adivisorCode, "channel_code", channelCode);

		try {
			System.out.println("Attempting to read the item...");
			Item outcome = table.getItem(spec);
			System.out.println("GetItem succeeded: " + outcome);

		} catch (Exception e) {
			System.err.println("Unable to read item: " + adivisorCode + " " + channelCode);
			System.err.println(e.getMessage());
		}

	}

	public static void insert() {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("Advisors");

		String agentCode = "AG0003";
		String channelCode = "CH0001";

		final Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("subchannel_code", "SCH0001");
		infoMap.put("designaiton", "DESG0001");

		try {
			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table.putItem(new Item()
					.withPrimaryKey("adivisor_code", agentCode, "channel_code", channelCode).withMap("info", infoMap));

			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

		} catch (Exception e) {
			System.err.println("Unable to add item: " + agentCode + " " + channelCode);
			System.err.println(e.getMessage());
		}

	}

	public static void createTable() {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1")).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Advisors";

		try {
			System.out.println("Attempting to create table; please wait...");
			Table table = dynamoDB.createTable(tableName,
					Arrays.asList(new KeySchemaElement("adivisor_code", KeyType.HASH), // Partition
							// key
							new KeySchemaElement("channel_code", KeyType.RANGE)), // Sort key
					Arrays.asList(new AttributeDefinition("adivisor_code", ScalarAttributeType.S),
							new AttributeDefinition("channel_code", ScalarAttributeType.S)),
					new ProvisionedThroughput(10L, 10L));
			table.waitForActive();
			System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}

	}

}
