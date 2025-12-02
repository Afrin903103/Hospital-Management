package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {

    private Connection connection;

    public Doctor(Connection connection){
        this.connection=connection;
    }

    public void viewDoctors(){
        String query="select * from doctors";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+---------------------------+");
            System.out.println("| Doctor Id  | Name               | Specialization            |");
            System.out.println("+------------+--------------------+---------------------------+");
            while (resultSet.next()){
                int Id=resultSet.getInt("Id");
                String Name=resultSet.getString("Name");
                String Specialization=resultSet.getString("Specialization");
                System.out.printf("|%-12s|%-20s|%-27s|\n", Id,Name,Specialization);
                System.out.println("+------------+--------------------+---------------------------+");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int Id){
        String query="select * from doctors where Id= ?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, Id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
