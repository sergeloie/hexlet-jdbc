package jdbc;

import java.sql.*;
import java.util.Optional;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection conn) {
        this.connection = conn;
    }

    public void save(User user) throws SQLException{

        if (user.getId() == null) {
            String sqlInsert = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (PreparedStatement preparedStatement =  connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            String sqlUpdate = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(sqlUpdate)) {
                preparedStatement2.setString(1, user.getName());
                preparedStatement2.setString(2, user.getPhone());
                preparedStatement2.setLong(3, user.getId());
                preparedStatement2.executeUpdate();
            }
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        String sqlGetUser = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement getUserStatement = connection.prepareStatement(sqlGetUser)) {
            getUserStatement.setLong(1, id);
            ResultSet resultSet = getUserStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String phone = resultSet.getString("phone");
                User user = new User(id, username, phone);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }
}
