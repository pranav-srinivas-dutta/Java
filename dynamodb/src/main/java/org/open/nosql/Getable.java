package org.open.nosql;

public interface Getable <K> {
	public K get(String tableName, String primaryKey, String rangeKey, Object primaryValue, Object rangeValue);
}
