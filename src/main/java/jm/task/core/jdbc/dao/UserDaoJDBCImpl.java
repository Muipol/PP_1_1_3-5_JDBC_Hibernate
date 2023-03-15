package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_USERS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(65), lastName VARCHAR(65), age INT)";
    private static final String DROP_USERS_TABLE_SQL = "DROP TABLE IF EXISTS Users";
    private static final String SAVE_USER_SQL = "INSERT INTO Users(name, lastName, age) VALUES(?, ?, ?)";
    private static final String REMOVE_USER_BY_ID_SQL = "DELETE FROM Users WHERE id = ?";
    private static final String GET_ALL_USERS_SQL = "SELECT * FROM Users";
    private static final String CLEAN_USERS_TABLE_SQL = "TRUNCATE Users";

    private static final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_USERS_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_USERS_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SAVE_USER_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(REMOVE_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL);

            while (resultSet.next()) {
                User user = new User();
                user.setId((long) resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setAge((byte) resultSet.getInt("age"));
                user.setLastName(resultSet.getString("lastName"));
                users.add(user);
            }
            System.out.println(users.toString());
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
        return null;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_USERS_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка работы с БД");
        }
    }
}
