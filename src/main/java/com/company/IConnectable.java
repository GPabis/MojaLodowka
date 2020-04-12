package com.company;

import java.sql.*;

public interface IConnectable {
    String userName = "m11794_GPabis";
    String password = "HaslO2020";
    String dbUrl = "jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka";

    static Connection connectDefault (){
        try {
            Connection con = DriverManager.getConnection(dbUrl, userName, password);
            return con;
        }catch (Exception e){System.out.println(e);}
        return null;
    }

    static Connection connect (String dbUrl, String userName, String password){
        try {
            Connection con = DriverManager.getConnection(dbUrl, userName, password);
            return con;
        }catch (Exception e){System.out.println(e);}
        return null;
    }

    static void ClosingConnection(Connection con, PreparedStatement ps, Statement stmt, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {System.out.println(e);}
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {System.out.println(e);}
        }
        if(stmt != null){
            try {
                stmt.close();
            }catch (SQLException e){System.out.println(e);}
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {System.out.println(e);}
        }
    }
}
