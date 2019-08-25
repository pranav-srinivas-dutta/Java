package com.open.to.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.stereotype.Service;

import com.open.to.db.Exception.PooledConnectionException;

@Service
public class MySqlPooledConnection implements PooledConnection {

	private static PooledConnection pooledConnection;
	private  DataSource datasource;
	private static final Map<String, Connection> CONNECTIONMAP;
	private static final Map<String, Object> CONNECTIONLOCK;

	PoolProperties poolProperties = new PoolProperties();

	static {
		CONNECTIONLOCK= new ConcurrentHashMap<> ();
		CONNECTIONMAP= new ConcurrentHashMap<> ();
	}

	private MySqlPooledConnection() throws IOException {
		this.poolProperties = new PoolProperties();
		Properties properties= new Properties();
		properties.load(new FileReader("/home/pranav/Documents/Git Hub/pranav-srinivas-dutta/Java/workspace-chat/workspace-chat/src/main/resources/workplace-chat-database.properties"));
		
		this.poolProperties.setUrl(properties.getProperty("url"));
		this.poolProperties.setDriverClassName(properties.getProperty("driverClassName"));
		this.poolProperties.setUsername(properties.getProperty("username"));
		this.poolProperties.setPassword(properties.getProperty("password"));
		this.poolProperties.setJmxEnabled(Boolean.parseBoolean(properties.getProperty("jmxEnabled")));
		this.poolProperties.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("testWhileIdle")));
		this.poolProperties.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("testOnBorrow")));
		this.poolProperties.setValidationQuery(properties.getProperty("validationQuery"));
		this.poolProperties.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("testOnReturn")));
		this.poolProperties.setValidationInterval(Integer.parseInt(properties.getProperty("validationInterval")));
		this.poolProperties.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("timeBetweenEvictionRunsMillis")));
		this.poolProperties.setMaxActive(Integer.parseInt(properties.getProperty("maxActive")));
		this.poolProperties.setInitialSize(Integer.parseInt(properties.getProperty("initialSize")));
		this.poolProperties.setMaxWait(Integer.parseInt(properties.getProperty("maxWait")));
		this.poolProperties.setRemoveAbandonedTimeout(Integer.parseInt(properties.getProperty("removeAbandonedTimeout")));
		this.poolProperties.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("minEvictableIdleTimeMillis")));
		this.poolProperties.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));
		this.poolProperties.setLogAbandoned(Boolean.parseBoolean(properties.getProperty("logAbandoned")));
		this.poolProperties.setRemoveAbandoned(Boolean.parseBoolean(properties.getProperty("removeAbandoned")));
		//this.poolProperties.setJdbcInterceptors(properties.getProperty("jdbcInterceptors"));

		datasource = new DataSource();
		datasource.setPoolProperties(this.poolProperties);
	}

	@Override
	public synchronized Connection getConnection(Object seeker) throws SQLException, PooledConnectionException {
		Connection connection = null;

		if (seeker != null) {
			String hashCode= seeker.hashCode() + "";
			if (CONNECTIONLOCK.containsKey(hashCode)) {
				synchronized (CONNECTIONLOCK.get(hashCode)) {
					if (CONNECTIONMAP.containsKey(hashCode)) {
						connection= CONNECTIONMAP.get(hashCode);
					}
				}
			} else {
				Object LOCK= new Object();

				CONNECTIONLOCK.put(hashCode, LOCK);

				synchronized (CONNECTIONLOCK.get(hashCode)) {
					connection = datasource.getConnection();
					CONNECTIONMAP.put(hashCode, connection);
					connection.setAutoCommit(false);
				}
			}
		} else {
			throw new PooledConnectionException ("Please specify an object that is the seeker of the Pooled Connection");
		}

		return connection;
	}

	@Override
	public synchronized void closeConnection(Object seeker) throws PooledConnectionException, SQLException {
		if (seeker != null) {
			String hashCode= seeker.hashCode() + "";
			if (CONNECTIONLOCK.containsKey(hashCode)) {
				Object LOCK= CONNECTIONLOCK.get(hashCode);
				synchronized (LOCK) {
					Connection connection= CONNECTIONMAP.get(hashCode);
					
					if (connection != null) {
						if (!connection.isClosed()) {
							connection.commit();
							connection.close();
						}
					}
					
					CONNECTIONMAP.remove(hashCode);
					CONNECTIONLOCK.remove(hashCode);
				}
			} else {
				throw new PooledConnectionException("Could not find an entry for the seeker mentioned.");
			}
		} else {
			throw new PooledConnectionException("Cannot close connection of an unknown seeker. Please specify a seeker.");
		}

	}

	public synchronized static PooledConnection getPooledConnection() throws PooledConnectionException {
		try {
			if (pooledConnection != null)
				return pooledConnection;
			else
				return pooledConnection= new MySqlPooledConnection();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PooledConnectionException("Unable to Create a new Connection");
		}
	}
	
	public static void main (String[] args) {
		try {
			PooledConnection pc= MySqlPooledConnection.getPooledConnection();
			Connection connection= pc.getConnection(pc);
			System.out.println("This is the connection object: " + connection);
			
			PreparedStatement pst= null;
			
			try {
				pst= connection.prepareStatement("INSERT INTO userdetails (username, PASSWORD) values ('Pranav', 'Paswsword');");
				pst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			pc.closeConnection(pc);
		} catch (PooledConnectionException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
