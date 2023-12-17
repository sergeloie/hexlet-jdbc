package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class pstmnt {

    public static void main(String[] args) throws SQLException {

        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:users")) {

            String sqlCreate = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(100), phone VARCHAR(100))";
            try (Statement statement = conn.createStatement()) {
                statement.execute(sqlCreate);
            }

            String sqlAdd = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (PreparedStatement addStatement = conn.prepareStatement(sqlAdd, Statement.RETURN_GENERATED_KEYS)) {
                addStatement.setString(1, "Sarah");
                addStatement.setString(2, "9518000939");
                addStatement.executeUpdate();
                printID(addStatement);

                addStatement.setString(1, "John");
                addStatement.setString(2, "9048191086");
                addStatement.executeUpdate();
                printID(addStatement);

                addStatement.setString(1, "Kyle");
                addStatement.setString(2, "3519001086");
                addStatement.executeUpdate();
                printID(addStatement);
            }

            String sqlRemove = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement removeStatement = conn.prepareStatement(sqlRemove)) {
                removeStatement.setString(1, "John");
                removeStatement.executeUpdate();
            }

            String sqlPrint = "SELECT * FROM users";
            try (Statement printStatement = conn.createStatement()) {
                ResultSet resultSet = printStatement.executeQuery(sqlPrint);
                while (resultSet.next()) {
                    System.out.printf("%s %s%n", resultSet.getString("username"), resultSet.getString("phone"));
                }
            }
        }
    }

    public static void printID(PreparedStatement ps) throws SQLException {
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            System.out.println(generatedKeys.getLong(1));
        } else {
            throw new SQLException("DB have not returned id after saving the entity");
        }
    }
}
