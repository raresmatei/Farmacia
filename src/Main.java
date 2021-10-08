import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        //connectDB("jdbc:postgresql://localhost:5432/pharmacy");

        int option;
        Scanner scanner = new Scanner(System.in);
        ResultSet resultSet;

        do {
            System.out.println("Menu:");
            System.out.println("1- Listare magazine dintr un anumit oras");
            System.out.println("2- Raport comenzi farmacia Dona");
            System.out.println("3- Numar comenzi de antibiotice pentru farmacia Vlad din anul 2020");
            System.out.println("4- Farmacia cu cele mai valoroase comenzi pe anul 2020");
            System.out.println("5- Afisarea tutoror farmaciilor care au pe stoc un anumit medicament");
            System.out.println("6- Afisarea cantitatilor totale pentru un medicament selectat dintr-un anumit oras");
            System.out.println("7 - Iesire din program");
            System.out.println("Alege optiune:");

            option = scanner.nextInt();
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pharmacy", "postgres", "randyorton");
                Statement statement = connection.createStatement();
                switch (option) {
                    case 1:
                        System.out.println("Introduceti un oras");
                        String cityName = scanner.next();
                        resultSet = statement.executeQuery("select name from pharmacy where city=" + "'" + cityName +"'");
                        //print the result:
                        while(resultSet.next()){
                            System.out.println(resultSet.getString("name"));
                        }
                        break;
                        
                    case 7:
                        System.out.println("Iesire program");
                    default:
                        System.out.println("Optiune invalida");
                        break;
                }
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }while(option != 7);
    }
}
