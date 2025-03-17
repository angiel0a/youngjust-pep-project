package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message insertMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                int messageId = (int)rs.getLong("message_id");
                return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int messageId = (int)rs.getLong("message_id");
                int postedBy = (int)rs.getLong("posted_by");
                String messageText = rs.getString("message_text");
                Long time = rs.getLong("time_posted_epoch");
                
                messages.add(new Message(messageId, postedBy, messageText, time));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message getMessageById(int messageId){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int postedBy = (int)rs.getLong("posted_by");
                String messageText = rs.getString("message_text");
                Long time = rs.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int deleteMessageById(int messageId){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, messageId);

            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateMessageById(int messageId, Message message){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, message.getMessage_text());
            stmt.setInt(2, messageId);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getAllMessagesById(int accountId){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message INNER JOIN account ON message.posted_by = account.account_id WHERE account.account_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int messageId = (int)rs.getLong("message_id");
                int postedBy = (int)rs.getLong("posted_by");
                String messageText = rs.getString("message_text");
                Long time = rs.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, time));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
