package com.travelsky.acms.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
	private static Connection conn;

	public static Connection getConnection() throws Exception {
		if (conn == null) {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@10.1.15.67:1522:acmsdev", "acms_dev",
					"acms_dev");
			System.out.println("*******************************");
			System.out.println("[info] Connection success!");
			System.out.println("*******************************");
		}
		return conn;
	}
}
