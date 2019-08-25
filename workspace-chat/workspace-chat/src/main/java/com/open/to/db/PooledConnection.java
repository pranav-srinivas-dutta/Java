package com.open.to.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.open.to.db.Exception.PooledConnectionException;

public interface PooledConnection {
	public Connection getConnection(Object seeker) throws SQLException, PooledConnectionException;
	public void closeConnection(Object seeker) throws SQLException, PooledConnectionException;

}
