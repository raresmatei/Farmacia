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

                    case 2:
                        int nrOrdersDonaAugust = 0;
                        int sumDonaAugust = 0;
                        int orderAvgDonaAugust = 0;

                        resultSet = statement.executeQuery("SELECT count(*) as countOrders from pharmacy left join orders on pharmacy.id = orders.pharmacy_id where pharmacy.name = 'dona'\n" +
                                "                                                                              and EXTRACT(MONTH FROM orders.delivery_date) = 8");
                        resultSet.next();
                        System.out.println("Numar de comenzi august farmacia dona: " + resultSet.getInt("countOrders"));

                        resultSet = statement.executeQuery("SELECT sum(order_item.quantity * m.price) as sumAugust from pharmacy left join orders on pharmacy.id = orders.pharmacy_id left join order_item on orders.id = order_item.order_id\n" +
                                "    left join medicines m on order_item.medicine_id = m.id\n" +
                                "     where pharmacy.name = 'dona' and EXTRACT(MONTH FROM orders.delivery_date) = 8");
                        resultSet.next();
                        System.out.println("Incasari august farmacia dona: " + resultSet.getInt("sumAugust"));

                        resultSet = statement.executeQuery("SELECT avg(order_item.quantity * m.price) as avgAugust from pharmacy left join orders on pharmacy.id = orders.pharmacy_id left join order_item on orders.id = order_item.order_id\n" +
                                "    left join medicines m on order_item.medicine_id = m.id\n" +
                                "     where pharmacy.name = 'dona' and EXTRACT(MONTH FROM orders.delivery_date) = 8");
                        resultSet.next();
                        System.out.println("Incasari august farmacia dona: " + resultSet.getFloat("avgAugust"));

                        break;

                    case 7:
                        System.out.println("Iesire program");
                        break;
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
