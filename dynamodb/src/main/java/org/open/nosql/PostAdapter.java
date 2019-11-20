package org.open.nosql;

import java.util.Map;

public interface PostAdapter {
	public void post(String tableName, Map<String, String> primaryMap, Map<String, String> infoMap);

}
