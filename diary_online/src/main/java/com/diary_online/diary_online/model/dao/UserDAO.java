package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean isAlreadyFollowing(int userId, int userToFollowId) {
        String sql = "SELECT user_id, following_user_id FROM users_have_followers WHERE following_user_id = ? AND user_id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userToFollowId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public List<User> showFollowers(int myId) {
        List<User> list = new ArrayList<>();

        String sql = "SELECT u.id,u.first_name,u.last_name,u.email,u.username,u.password,created_at FROM users AS u\n" +
                "JOIN users_have_followers AS uhf ON uhf.following_user_id = u.id\n" +
                "WHERE uhf.user_id = ?;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, myId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isSectionSharedUser(Section section, User user) {
        int sectionId = section.getId();
        int userId = user.getId();

        String sql = "SELECT section_id, user_id FROM shared_sections WHERE section_id = ? AND user_id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, sectionId);
                preparedStatement.setInt(2, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasUserLikedSection(User user, Section section) {
        int sectionId = section.getId();
        int userId = user.getId();

        String sql = "SELECT section_id, user_id FROM sections_have_likes WHERE section_id = ? AND user_id = ?";
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sectionId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean hasUserDisLikedSection(User user, Section section) {
        int sectionId = section.getId();
        int userId = user.getId();

        String sql = "SELECT section_id, user_id FROM sections_have_dislikes WHERE section_id = ? AND user_id = ?";
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sectionId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
