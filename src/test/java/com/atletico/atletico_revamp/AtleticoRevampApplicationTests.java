package com.atletico.atletico_revamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtleticoRevampApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@Test
	public void testDatabaseConnection() throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			assertNotNull(connection, "Database connection should not be null");
			System.out.println("Database connected successfully: " + connection.getMetaData().getURL());
		}
	}

}
