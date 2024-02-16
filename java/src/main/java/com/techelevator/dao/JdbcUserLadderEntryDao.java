package com.techelevator.dao;

import com.techelevator.model.UserLadderEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserLadderEntryDao implements UserLadderEntryDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserLadderEntryDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "INSERT INTO user_ladder (user_id, team_id, points, percentage, position) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userLadderEntry.getUserId(), userLadderEntry.getTeamId(), userLadderEntry.getPoints(), userLadderEntry.getPercentage(), userLadderEntry.getPosition());
    }

    @Override
    public void updateUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "UPDATE user_ladder SET points = ?, percentage = ?, position = ? WHERE user_id = ? AND team_id = ?";
        jdbcTemplate.update(sql, userLadderEntry.getPoints(), userLadderEntry.getPercentage(), userLadderEntry.getPosition(), userLadderEntry.getUserId(), userLadderEntry.getTeamId());
    }

    public void deleteUserLadderEntry(int userId, int teamId) {
        String sql = "DELETE FROM user_ladder WHERE user_id = ? AND team_id = ?";
        jdbcTemplate.update(sql, userId, teamId);
    }

    @Override
    public UserLadderEntry getUserLadderEntry(int userId, int teamId) {
        String sql = "SELECT * FROM user_ladder WHERE user_id = ? AND team_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, teamId);
        if (results.next()) {
            return mapRowToUserLadder(results);
        } else {
            return null;
        }
    }

    @Override
    public List<UserLadderEntry> getAllUserLadderEntries(int userId) {
        List<UserLadderEntry> userLadderEntries = new ArrayList<>();
        String sql = "SELECT * FROM user_ladder WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            userLadderEntries.add(mapRowToUserLadder(results));
        }
        return userLadderEntries;
    }

    private UserLadderEntry mapRowToUserLadder(SqlRowSet row) {
        UserLadderEntry userLadder = new UserLadderEntry(row.getInt("user_id"), row.getInt("team_id"), row.getInt("points"), row.getInt("percentage"), row.getInt("position"));
        return userLadder;
    }
}