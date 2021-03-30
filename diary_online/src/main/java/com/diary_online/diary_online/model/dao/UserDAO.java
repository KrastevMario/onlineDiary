package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.dto.UserFromDbDTO;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.util.DBConnector;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean isAlreadyFollowing(int userId, int userToFollowId) {
        String sql = "SELECT user_id, following_user_id FROM users_have_followers WHERE following_user_id = ? AND user_id = ?";
        Connection connection = DBConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userToFollowId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static List<SectionFromDbDTO> showAllMySection(int userId){
        List<SectionFromDbDTO> list = new ArrayList<>();

        Connection c = DBConnector.getInstance().getConnection();

        String sql = "select s.id,s.title,s.content,s.privacy,s.created_at from sections as s\n" +
                "join diaries as d on d.id = s.diary_id\n" +
                "join users as u on u.id = d.user_id\n" +
                "where u.id = ?\n" +
                "order by s.created_at desc";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                SectionFromDbDTO section = new SectionFromDbDTO(rs.getInt("id"),rs.getString("title"),
                        rs.getString("content"),rs.getString("privacy"),
                        rs.getTimestamp("created_at").toLocalDateTime() );


                list.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<UserFromDbDTO> showFollowers(int myId){
        List<UserFromDbDTO> list = new ArrayList<>();

        Connection c = DBConnector.getInstance().getConnection();

        String sql = "select u.first_name,u.last_name,u.email,u.username from users as u\n" +
                "join users_have_followers as uhf on uhf.following_user_id = u.id\n" +
                "where uhf.user_id = ?;";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, myId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
               UserFromDbDTO userFromDbDTO = new UserFromDbDTO(rs.getString("first_name"),
                       rs.getString("last_name"),
                       rs.getString("email"),
                       rs.getString("username"));


                list.add(userFromDbDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean isSectionSharedUser(Section section, User user) {
        int sectionId = section.getId();
        int userId = user.getId();

        String sql = "SELECT section_id, user_id FROM shared_sections WHERE section_id = ? AND user_id = ?";
        Connection connection = DBConnector.getInstance().getConnection();
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

    public static boolean hasUserLikedSection(User user, Section section) {
        int sectionId = section.getId();
        int userId = user.getId();

        String sql = "SELECT section_id, user_id FROM sections_have_likes WHERE section_id = ? AND user_id = ?";
        Connection connection = DBConnector.getInstance().getConnection();
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
