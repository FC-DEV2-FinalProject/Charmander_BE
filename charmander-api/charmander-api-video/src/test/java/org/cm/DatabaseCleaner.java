package org.cm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DatabaseCleaner implements InitializingBean {

    private static final List<String> SYSTEM_TABLE_NAMES = List.of("CONSTANTS", "ENUM_VALUES", "INDEXES",
            "INDEX_COLUMNS", "INFORMATION_SCHEMA_CATALOG_NAME", "IN_DOUBT", "LOCKS",
            "QUERY_STATISTICS", "RIGHTS", "ROLES", "SESSIONS", "SESSION_STATE", "SETTINGS", "SYNONYMS", "USERS");


    @PersistenceContext
    EntityManager entityManager;

    List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        var session = entityManager.unwrap(Session.class);
        session.doWork(this::extractTableNames);

    }

    private void extractTableNames(Connection connection) throws SQLException {
        var tableNames = new ArrayList<String>();
        var tables = connection.getMetaData()
                .getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            final String tableName = tables.getString("TABLE_NAME");
            if (!SYSTEM_TABLE_NAMES.contains(tableName.toUpperCase())) {
                tableNames.add(tableName);
            }
        }

        this.tableNames = tableNames;
    }


    public void execute() {
        var session = entityManager.unwrap(Session.class);
        session.doWork(this::cleanUpDatabase);
    }

    private void cleanUpDatabase(Connection connection) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (final String tableName : tableNames) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1");
        }
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
    }

}
