package Lesson_4.server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:udb.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNewUsers(int id, String login, String pass, String nickName) {
        int hash = pass.hashCode();
        String sql = String.format("INSERT INTO users (id, login, password, nickname) VALUES('%s', '%s', '%s', '%s')", id, login, hash, nickName);

        try {
            int t = stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        int hash = pass.hashCode();
        String sql = String.format("SELECT nickname FROM users where login = '%s' and password = '%s'", login, hash);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
//                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void blackList(String userNick, String blockNick){
        String sql = String.format("INSERT INTO BlackList (user, BlockUser) VALUES('%s', '%s')", userNick, blockNick);
        try {
            int t = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkBlackList(String userNick, String blockNick) {
        String sql = String.format("SELECT BlockUser FROM BlackList where user = '%s' and BlockUser = '%s'", blockNick, userNick);

        try {
            ResultSet rs1 = stmt.executeQuery(sql);

            if (rs1.next()) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String changeNicke(String oldNicke, String newNicke){
        String sql = String.format("SELECT nickname FROM users WHERE nickname = '%s'", newNicke);

        try {
            ResultSet rs1 = stmt.executeQuery(sql);

            if (!rs1.next()) {
                String sql2 = String.format("UPDATE users SET nickname = '%s' WHERE nickname = '%s'", newNicke, oldNicke);
                int rs2 = stmt.executeUpdate(sql2);
                return newNicke;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int numberRow() {
        String sql = String.format("SELECT nickname FROM users");
        int result = 1;

        try {
            ResultSet rs1 = stmt.executeQuery(sql);
            rs1.afterLast();
            result = rs1.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
