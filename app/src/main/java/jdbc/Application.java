package jdbc;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;

public class Application {
    public static void main(String[] args) throws SQLException {

        List<String> products = List.of("computer", "mobile phone", "tv", "kettle");


        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet")) {

            var sql = "CREATE TABLE products "
                    + "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255))";

            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            // BEGIN (write your solution here)

            String sqlAdd = "INSERT INTO products (name) VALUES (?), (?), (?), (?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAdd)) {
                for (int i = 0; i < products.size(); i++) {
                    ps.setString(i+1, products.get(i));
                }
                ps.executeUpdate();
            }

            // END

            var sql3 = "SELECT * FROM products ORDER BY name";

            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name"));
                }
            }
        }
    }
}
