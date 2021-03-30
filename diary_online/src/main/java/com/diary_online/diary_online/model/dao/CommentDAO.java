package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.model.dto.UserFromDbDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.repository.CommentRepository;
import com.diary_online.diary_online.util.DBConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentDAO {

    @Autowired
    CommentRepository commentRepository;
    public static int findCommentByUserAndSectionId(int userId,int sectionId){

        Connection c = DBConnector.getInstance().getConnection();
        int comentId = 0;

        String sql = "SELECT * FROM online_diary.comments\n" +
                "where user_id = ? and section_id = ?;";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, sectionId);
            ResultSet rs = ps.executeQuery();

            rs.next();
            comentId = rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comentId;
    }
}
