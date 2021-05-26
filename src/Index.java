import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
//import org.json.JSONObject;

public class Index
{
    public static String serverUrl = "http://localhost:";
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Please enter the port number of the server");
        int portNum = scan.nextInt();
        serverUrl += portNum;
        while(!exit) {
            System.out.println("Welcome to the pizza ordering client");
            System.out.println("What do you want to do?");
            System.out.println("type the number of the action you want to do");
            System.out.println("1. Get pizza information");
            System.out.println("2. Handle Orders ");
            System.out.println("3. Exit");
            int action = scan.nextInt();
            if (action == 1) {
                System.out.println("What information do you want?");
                System.out.println("1. All pizza information");
                System.out.println("2. Information of one pizza");
                int choice = scan.nextInt();
                if(choice == 1)
                {
                    getAllPizzaData();
                }else if(choice == 2)
                {
                    System.out.println("Type the id of the pizza you want the information of");
                    int id = scan.nextInt();
                    getPizzaData(id);
                }else
                {
                    System.out.println("Invalid choice");
                }

            }
            else if (action == 2) {
                System.out.println("What do you want to do?");
                System.out.println("1. Get order information");
                System.out.println("2. Place an order");
                System.out.println("3. Cancel an order");
                System.out.println("4. Get ETA for an order");
                int choice = scan.nextInt();
                if(choice == 1)
                {
                    //get order data
                    System.out.println("What is the id of the order you want the information of");
                    int id = scan.nextInt();
                    getOrderData(id);
                }else if(choice == 2)
                {
                   //place an order
                }else if(choice == 3)
                {
                    //cancel an order
                    System.out.println("What is the id of the order you want to cancel");
                    int id = scan.nextInt();
                    cancelOrder(id);
                }else if(choice == 4)
                {
                    //get ETA of an order
                    System.out.println("What is the id of the order you want the ETA of");
                    int id = scan.nextInt();
                    getOrderETA(id);
                }else
                {
                    System.out.println("Invalid choice");
                }
            } else if (action == 3) {
                System.out.println("Goodbye");
                exit = true;
            } else {
                System.out.println("Invalid choice try again");
            }
        }
    }

    public static void getPizzaData(int id)
    {
        try {
            System.out.println(getData(serverUrl + "/pizza/" + id, "GET"));
        }catch (IOException e) {
            System.out.println("Error connecting to the server");
        }
    }

    public static void getOrderData(int id)
    {
        try {
            System.out.println(getData(serverUrl + "/order/" + id, "GET"));
        }catch (IOException e) {
            System.out.println("Error connecting to the server");
        }
    }

    public static void getOrderETA(int id)
    {
        try {
            System.out.println(getData(serverUrl + "/order/deliverytime/" + id, "GET"));
        }catch (IOException e) {
            System.out.println("Error connecting to the server");
        }
    }

    public static void getAllPizzaData()
    {
        try {
            System.out.println(getData(serverUrl + "/pizza/", "GET"));
        }catch (IOException e) {
            System.out.println("Error connecting to the server");
        }
    }

    public static void cancelOrder(int id)
    {
        try {
            getData(serverUrl + "/order/cancel/" + id, "PUT");
        }catch (IOException e) {
            System.out.println("Error connecting to the server");
        }
    }

    public static String getData(String url,String method) throws IOException
    {
        String output = "";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod(method);
        int responseCode = connection.getResponseCode();
        String response = "";

        InputStream stream = connection.getErrorStream();
        if (stream == null) {
            stream = connection.getInputStream();
        }

        Scanner scan = new Scanner(stream);
        while (scan.hasNextLine()) {
            response += scan.nextLine() + "\n";
        }
        if (responseCode != 200) {
            output += "Error: " + responseCode + ", ";
        }
        output += response;
        return output;
    }
}