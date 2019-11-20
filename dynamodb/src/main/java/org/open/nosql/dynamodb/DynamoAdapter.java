package org.open.nosql.dynamodb;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.open.nosql.CreateAdapter;
import org.open.nosql.Deleteable;
import org.open.nosql.GetAdapter;
import org.open.nosql.NoSqlAdapter;
import org.open.nosql.PostAdapter;
import org.open.nosql.Updateable;
import org.open.nosql.exception.AdapterException;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class DynamoAdapter extends NoSqlAdapter implements Deleteable<String>, Updateable<String>, GetAdapter<Item>, PostAdapter, CreateAdapter {

	private AmazonDynamoDB client;
	
	private static DynamoAdapter dynamoAdapter;
	
	//Key : TableName Value : List < attributes >
	private static Map<String, List<String>> uniquenessMap;
	
	private static final Object LOCK = new Object();

	private DynamoAdapter (DynamoProperties dynamoProperties) {
		this.client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1"))
				.build();
		
		//fill the uniqueness map using the dynamoProperties
	}
	
	public static void initializeDynamoAdapter() throws AdapterException {
		synchronized (DynamoAdapter.getLock()) {
			if (DynamoAdapter.dynamoAdapter == null) {
				try {
					DynamoAdapter.dynamoAdapter= new DynamoAdapter(DynamoProperties.initializeDynamoProperties("/propertiesPath"));
				} catch (Exception e) {
					e.printStackTrace();
					throw new AdapterException ("Unable to instantiate DynamoAdapter. " + e.getMessage());
				}
			}
		}
	}
	
	public static GetAdapter<Item> fetchGetAdapter() {
		return (GetAdapter<Item>) dynamoAdapter;
	}
	
	public static PostAdapter fetchPostAdapter() {
		return (PostAdapter) dynamoAdapter;
	}
	
	public static CreateAdapter fetchCreateAdapter() {
		return (CreateAdapter) dynamoAdapter;
	}
	
	public Updateable fetchUpdateAdapter() {
		return (Updateable) dynamoAdapter;
	}

	public void post(String tableName, Map<String, String> primaryMap, Map<String, String> infoMap) {
		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable(tableName);

        try {
            System.out.println("Adding a new item...");
            Set<String> set= primaryMap.keySet();
            Object[] keyArray= set.stream().toArray();
            String keyOne= (String) keyArray[0];
            String keyTwo= (String) keyArray[1];
            PutItemOutcome outcome = table.putItem(new Item().with(keyTwo, primaryMap.get(keyTwo)).with(keyOne, primaryMap.get(keyOne)).withMap("info", infoMap));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

	}

	public Item get(String tableName, String primaryKey, String rangeKey, Object primaryValue, Object rangeValue) {
		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(primaryKey, primaryValue, rangeKey, rangeValue);
		return table.getItem(spec);
	}

	public void update(String item) {
		// TODO Auto-generated method stub

	}

	public void delete(String item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void create(String tableName) {
		DynamoDB dynamoDB = new DynamoDB(client);
		try {
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
			e.printStackTrace();
		}


	}

	public AmazonDynamoDB getClient() {
		return client;
	}

	public void setClient(AmazonDynamoDB client) {
		this.client = client;
	}

	public static DynamoAdapter getDynamoAdapter() {
		return dynamoAdapter;
	}

	public static void setDynamoAdapter(DynamoAdapter dynamoAdapter) {
		DynamoAdapter.dynamoAdapter = dynamoAdapter;
	}

	private static synchronized Object getLock() {
		return LOCK;
	}

}
