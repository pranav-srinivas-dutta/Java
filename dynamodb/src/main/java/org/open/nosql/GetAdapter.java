package org.open.nosql;

public interface GetAdapter <K> {
	public K get(String tableName, String primaryKey, String rangeKey, Object primaryValue, Object rangeValue);
}
