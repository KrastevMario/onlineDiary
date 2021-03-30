package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.util.DBConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SectionDBDao {


    public static List<SectionFromDbDTO> getPublicSectionsFollowedByMe(int userId){

        List<SectionFromDbDTO> list = new ArrayList<>();

        Connection c = DBConnector.getInstance().getConnection();

        String sql = "select s.id,s.title,s.content,s.privacy,s.created_at from sections as s\n" +
                "join diaries as d on d.id = s.diary_id\n" +
                "join users as u on u.id = d.user_id\n" +
                "join users_have_followers as uhf on uhf.user_id = u.id\n" +
                "where s.privacy = \"public\" and uhf.following_user_id = ?";
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


    public static List<SectionFromDbDTO> getSharedWithMeSection(int userId){
        List<SectionFromDbDTO> list = new ArrayList<>();

        Connection c = DBConnector.getInstance().getConnection();

        String sql = "select s.id,s.title,s.content,s.privacy,s.created_at from sections as s\n" +
                "join shared_sections as ss on ss.section_id = s.id\n" +
                "join users as u on u.id = ss.user_id\n" +
                "where u.id = ?\n";
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

}
