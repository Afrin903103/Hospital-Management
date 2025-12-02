package System;

import java.sql.*;
import java.util.Scanner;

public class DbConnection {

    private static final String url="jdbc:mysql://localhost:3306/hospital";

    private static final String username="root";

    private static final String password="afrin";

    public static void main(String[] args){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient ");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors ");
                System.out.println("4. Book Appointment ");
                System.out.println("5. Exit ");
                System.out.println("Enter Your Choice: ");
                int choice= scanner.nextInt();

                switch (choice){
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatients();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                        break;
                    case 5:
                        System.out.println("Thank You for Visiting our Hospital");
                        return;
                    default:
                        System.out.println("Please, Enter Valid Choice!!! ");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int PatientId=scanner.nextInt();
        System.out.print("Enter Doctor Id:");
        int DoctorId=scanner.nextInt();
        System.out.print("Enter Appointment Date(YYYY-MM-DD): ");
        String AppointmentDate=scanner.next();
        if(patient.getPatientById(PatientId) && doctor.getDoctorById(DoctorId)){
            if (checkDoctorAvailability(DoctorId, AppointmentDate, connection)){
                String appointmentquery="insert into appointments(Patient_Id,Doctor_Id,Appointment_date) values(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement=connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1, PatientId);
                    preparedStatement.setInt(2, DoctorId);
                    preparedStatement.setString(3, AppointmentDate);
                    int rowsAffected=preparedStatement.executeUpdate();
                      if (rowsAffected>0) {
                        System.out.println("Congratulation Your Appointment has Booked!!!");
                      } else {
                        System.out.println("Failed to Book Your Appointment!!.");
                        }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not Available on this date, so Please try another Date!!! ");
            }
        }else {
            System.out.println("Either Doctor or Patient doesn't Exist!!.");
        }
    }

    public static boolean checkDoctorAvailability(int DoctorId, String AppointmentDate, Connection connection){
        String query="select count(*) from appointments where Doctor_Id=? and Appointment_Date=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, DoctorId);
            preparedStatement.setString(2, AppointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
             if (resultSet.next()){
                 int count=resultSet.getInt(1);
                 if (count==0){
                     return true;
                 }else{
                     return false;
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
