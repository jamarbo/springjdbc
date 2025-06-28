package com.example.springjdbc;

import java.sql.*;

class ejemploJdbc{

    public static void main (String args[]){

        String url  = "jdbc:h2:mem:testdb";
        String usr  = "pa";
        String pass = "";

        try(Connection conn =  DriverManager.getConnection(url, usr, pass);
            Statement smt =  conn.createStatement()){

            smt.execute("CREATE TABLE empleados (id INT PRIMARY KEY, nombre VARCHAR(100))");
            smt.execute("INSERT INTO empleados VALUES (1, 'JAIVER'), (2, 'Gabriella'), (3, 'Valeria')");

            ResultSet rs  = smt.executeQuery("SELECT * FROM empleados");

            while (rs.next())
                System.out.println (rs.getInt("id")+" - "+ rs.getString("nombre"));

        } catch (SQLException e){
            e.printStackTrace();
        }


    }

}