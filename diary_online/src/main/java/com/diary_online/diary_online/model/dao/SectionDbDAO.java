package com.diary_online.diary_online.model.dao;

import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SectionDbDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SectionRepository sectionRepository;

    public List<SectionFromDbDTO> getPublicSectionsFollowedByMe(int userId) {

        List<SectionFromDbDTO> list = new ArrayList<>();

        String sql = "SELECT s.id,s.title,s.content,s.privacy,s.created_at FROM sections AS s\n" +
                "JOIN diaries AS d ON d.id = s.diary_id\n" +
                "JOIN users AS u ON u.id = d.user_id\n" +
                "JOIN users_have_followers AS uhf ON uhf.user_id = u.id\n" +
                "WHERE s.privacy = \"public\" AND uhf.following_user_id = ?";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionFromDbDTO section = new SectionFromDbDTO(rs.getInt("id"), rs.getString("title"),
                        rs.getString("content"), rs.getString("privacy"),
                        rs.getTimestamp("created_at").toLocalDateTime());


                list.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<SectionFromDbDTO> getSharedWithMeSection(int userId) {
        List<SectionFromDbDTO> list = new ArrayList<>();

        String sql = "SELECT s.id,s.title,s.content,s.privacy,s.created_at FROM sections AS s\n" +
                "JOIN shared_sections AS ss ON ss.section_id = s.id\n" +
                "JOIN users AS u ON u.id = ss.user_id\n" +
                "WHERE u.id = ?\n";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionFromDbDTO section = new SectionFromDbDTO(rs.getInt("id"), rs.getString("title"),
                        rs.getString("content"), rs.getString("privacy"),
                        rs.getTimestamp("created_at").toLocalDateTime());


                list.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<SectionFromDbDTO> getMySection(int userId) {

        List<SectionFromDbDTO> list = new ArrayList<>();

        String sql = " \n" +
                "        SELECT s.id,s.title,s.content,s.privacy,s.created_at FROM sections AS s\n" +
                "        JOIN diaries AS d ON d.id = s.diary_id\n" +
                "        JOIN users AS u ON u.id = d.user_id\n" +
                "        WHERE u.id = ?";
        try (Connection c = jdbcTemplate.getDataSource().getConnection();) {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionFromDbDTO section = new SectionFromDbDTO(rs.getInt("id"), rs.getString("title"),
                        rs.getString("content"), rs.getString("privacy"),
                        rs.getTimestamp("created_at").toLocalDateTime());


                list.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteChildSections(int diaryId) {

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {

            String sql = "DELETE FROM sections\n" +
                    "        WHERE diary_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, diaryId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String removeLike(int userId, int sectionId) {
        if (sectionRepository.findById(sectionId).isEmpty()) {
            throw new NotFoundException("The section is invalid.");
        }
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            String sql = "SELECT * FROM sections_have_likes WHERE section_id = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sectionId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String deleteQuery = "DELETE FROM sections_have_likes WHERE section_id = ? AND user_id = ?";
                PreparedStatement pr = connection.prepareStatement(deleteQuery);
                pr.setInt(1, sectionId);
                pr.setInt(2, userId);
                pr.executeUpdate();
                return "like removed successful";
            }
            throw new BadRequestException("You never like this section");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NotFoundException("connection fail");
    }

    public String removeDislike(int userId, int sectionId) {
        if (sectionRepository.findById(sectionId).isEmpty()) {
            throw new NotFoundException("The section is invalid.");
        }

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            String sql = "SELECT section_id, user_id FROM sections_have_dislikes WHERE section_id = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sectionId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String deleteQuery = "DELETE FROM sections_have_dislikes WHERE section_id = ? AND user_id = ?";
                PreparedStatement pr = connection.prepareStatement(deleteQuery);
                pr.setInt(1, sectionId);
                pr.setInt(2, userId);
                pr.executeUpdate();
                return "dislike removed successful";
            }
            throw new BadRequestException("You never dislike this section");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NotFoundException("connection fail");
    }

    public boolean isAlreadyShared(int sectionId, int userId) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {
            String sql = "SELECT * FROM shared_sections WHERE section_id = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sectionId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String removeShare(int userId, int sectionId) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();) {

            String deleteQuery = "DELETE FROM shared_sections WHERE section_id = ? AND user_id = ?";
            PreparedStatement pr = connection.prepareStatement(deleteQuery);
            pr.setInt(1, sectionId);
            pr.setInt(2, userId);
            pr.executeUpdate();
            return "Unshare section successful";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NotFoundException("connection fail");
    }

}
