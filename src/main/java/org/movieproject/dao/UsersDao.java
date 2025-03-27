package org.movieproject.dao;

import org.movieproject.model.Users;
import org.movieproject.util.QueryUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsersDao {
    private final Connection connection;

    public UsersDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * 📌 모든 사용자 조회 (READ)
     * - XML에서 `getAllUsers` 쿼리를 가져와 실행
     */
    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllUsers"); // XML에서 쿼리 로드

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new Users(
                        rs.getInt("user_id"),
                        rs.getString("user_nickname"),
                        rs.getString("user_password"),
                        rs.getString("user_status"),
                        rs.getTimestamp("user_created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    // 📌 사용자 추가 (CREATE)
    public boolean addUser(Users users) {
        String query = QueryUtil.getQuery("addUser");

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, users.getUserNickname());
            ps.setString(2, users.getUserPassword());
            ps.setString(3, users.getUserStatus());

            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(4, Timestamp.valueOf(now));
            users.setUserCreatedAt(now); // 객체에도 시간

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
