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
                        System.out.println("Valoare medie pe comanda august farmacia dona: " + resultSet.getFloat("avgAugust"));

                        break;
                    // cate comenzi de antibiotice a primit farmacia Vlad in 2020
                    case 3:
                        resultSet = statement.executeQuery("select count(*) as countOrders\n" +
                                "from order_item\n" +
                                "         left join medicines m on order_item.medicine_id = m.id\n" +
                                "         left join category on m.category_id = category.id\n" +
                                "         left join orders o on order_item.order_id = o.id\n" +
                                "         left join pharmacy p on o.pharmacy_id = p.id\n" +
                                "where p.name='vlad' and category.name='antibiotice' and EXTRACT(YEAR FROM o.delivery_date) = 2020");
                        resultSet.next();
                        System.out.println("Numar de comenzi antibiotice farmacia Vlad 2020: " + resultSet.getInt("countOrders"));
                        break;

                    //care e farmacia care a comandat cel mai mult in 2020 , ca valoare absoluta
                    case 4:
                        resultSet = statement.executeQuery("select p.name, sum(m.price * oi.quantity) as total from Orders\n" +
                                "left join pharmacy p on orders.pharmacy_id = p.id\n" +
                                "left join order_item oi on orders.id = oi.order_id\n" +
                                "left join medicines m on oi.medicine_id = m.id\n" +
                                "where EXTRACT(YEAR FROM orders.delivery_date) = 2020\n" +
                                "group by p.id\n" +
                                "order by total DESC\n" +
                                "limit 1");
                        resultSet.next();
                        System.out.println("Farmacia cu cele mai valoarese comenzi pe 2020: " + resultSet.getString("name"));
                        break;

                    //afisarea tuturor farmaciilor care au pe stoc un anumit medicament
                    case 5:
                        String medicine;
                        medicine = scanner.next();
                        resultSet = statement.executeQuery("select pharmacy.name, pharmacy.city from stocks\n" +
                                "left join medicines m on stocks.medicine_id = m.id\n" +
                                "left join pharmacy on stocks.pharmacy_id = pharmacy.id\n" +
                                "where m.name = " + "'" +  medicine + "'");
                        while(resultSet.next()){
                            System.out.println(resultSet.getString("name") + " " + resultSet.getString("city"));
                        }
                        break;

                    //afisarea cantitatilor totale pentru un medicament selectat dintr-un anumit oras:
                    case 6:
                        System.out.println("Introduce a city: ");
                        String city = scanner.next();
                        System.out.println("Introduce a medicine: ");
                        String searchedMedicine = scanner.next();
                        resultSet = statement.executeQuery("select sum(quantity)  as cityQuantity from stocks\n" +
                                "left join medicines m on stocks.medicine_id = m.id\n" +
                                "left join pharmacy on stocks.pharmacy_id = pharmacy.id\n" +
                                "where m.name = " + "'" + searchedMedicine + "'" +  " and pharmacy.city = " + "'" + city + "'" );
                        resultSet.next();
                        System.out.println("Stoc " + searchedMedicine + " in " + city + ": " +  resultSet.getInt("cityQuantity"));
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
