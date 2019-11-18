package org.open.nosql.dynamodb;

import org.open.nosql.Deleteable;
import org.open.nosql.Getable;
import org.open.nosql.NoSqlAdapter;
import org.open.nosql.Postable;
import org.open.nosql.Updateable;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class DynamoAdapter extends NoSqlAdapter implements Deleteable<String>, Updateable<String>, Getable<Item>, Postable<String> {

	private AmazonDynamoDB client;
	
	public DynamoAdapter () {
		this.client = AmazonDynamoDBClientBuilder.standard()
	            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-south-1"))
	            .build();
	}
	
	public void post(String item) {
		// TODO Auto-generated method stub
		
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

	public AmazonDynamoDB getClient() {
		return client;
	}

	public void setClient(AmazonDynamoDB client) {
		this.client = client;
	}

}
