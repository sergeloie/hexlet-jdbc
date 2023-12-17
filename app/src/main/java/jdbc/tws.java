package jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;

public class tws {

    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:mycars")) {

            var createTable = "CREATE TABLE cars (id BIGINT PRIMARY KEY AUTO_INCREMENT,  brand VARCHAR(100), model VARCHAR(100))";
            try (var create = conn.createStatement()) {
                create.execute(createTable);
            }

            var addCars = "INSERT INTO cars (brand, model) VALUES ('kia', 'sorento'), ('bmw', 'x5'), ('audi', 'q7')";
            try (var add = conn.createStatement()){
                add.executeUpdate(addCars);
            }

            var getCars = "SELECT * FROM cars ORDER BY brand";
            try (var get = conn.createStatement()) {
                var resultSet = get.executeQuery(getCars);
                while (resultSet.next()) {
                    System.out.printf("%s %s%n", resultSet.getString("brand"), resultSet.getString("model"));
                }
            }
        }
    }
}
