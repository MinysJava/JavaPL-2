package Lesson_2.server;

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

    public static void setNewUsers(int id, String login, String pass, String nickName) {        // добавил метод для занесения пользователей. пароль храниться в хеше
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
        String sql = String.format("SELECT nickname FROM users where login = '%s' and password = '%s'", login, hash);   // сравнивает хеш паролей

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void blackList(String userNick, String blockNick){                    // добавляем в черный список
        String sql = String.format("INSERT INTO BlackList (user, BlockUser) VALUES('%s', '%s')", userNick, blockNick);
        try {
            int t = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkBlackList(String userNick, String blockNick) {          // проверяем на наличее в черном списке
        String sql = String.format("SELECT BlockUser FROM BlackList where user = '%s' and BlockUser = '%s'", blockNick, userNick);

        try {
            ResultSet rs1 = stmt.executeQuery(sql);

            if (rs1.next()) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
