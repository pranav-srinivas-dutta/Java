package com.open.to;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.open.to.db.MySqlPooledConnection;
import com.open.to.db.PooledConnection;
import com.open.to.db.Exception.PooledConnectionException;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired (required = true)
	public PooledConnection pooledConnection;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/adduser", method = RequestMethod.GET)
	public ResponseEntity<String> home(Locale locale, Model model) {
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
		return null;
	}
	
}
