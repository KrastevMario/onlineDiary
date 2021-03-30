package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean isAlreadyFollowing(int userId, int userToFollowId) {
        String sql = "SELECT user_id, following_user_id FROM users_have_followers WHERE following_user_id = ? AND user_id = ?";
        Connection connection = DBConnector.getInstance().getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userToFollowId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
