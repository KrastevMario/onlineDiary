package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.dto.UserFromDbDTO;
import com.diary_online.diary_online.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
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
}
