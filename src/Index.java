import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;

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
                System.out.println("4. Get delivery time for an order");
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
                    int[] pizzas;
                    boolean takeaway = false;
                    String payment_type;
                    int customerId;
                    String note;
                    String street;
                    String city;
                    String country;
                    String zipcode;


                    System.out.println("How many pizzas do you want?");
                    int amount = scan.nextInt();
                    pizzas = new int[amount];
                    for(int i = 1; i <= amount;i++)
                    {
                        System.out.println("Which menu id will pizza " + i + " have?");
                        pizzas[i - 1] = scan.nextInt();
                    }


                    boolean done = false;
                    while(!done)
                    {
                        System.out.println("Will the order be take away? (y/n)");
                        String input = scan.nextLine();
                        if(input.equalsIgnoreCase("y")){takeaway = true; done = true;}
                        else if(input.equalsIgnoreCase("n")){takeaway = false; done = true;}
                        else{System.out.println("Invalid input try again");}
                    }

                    System.out.println("What will the payment type be?");
                    payment_type = scan.nextLine();

                    System.out.println("What is your customer ID?");
                    customerId = scan.nextInt();

                    System.out.println("In what street will it be delivered?");
                    street = scan.nextLine();

                    System.out.println("In what city will it be delivered?");
                    city = scan.nextLine();

                    System.out.println("In what country will it be delivered?");
                    country = scan.nextLine();

                    System.out.println("In what zipcode will it be delivered?");
                    zipcode = scan.nextLine();

                    System.out.println("Do you have any notes?");
                    note = scan.nextLine();

                    addOrder(pizzas,takeaway,payment_type,customerId,note,street,city,country,zipcode);
                }else if(choice == 3)
                {
                    //cancel an order
                    System.out.println("What is the id of the order you want to cancel");
                    int id = scan.nextInt();
                    cancelOrder(id);
                }else if(choice == 4)
                {
                    //get ETA of an order
                    System.out.println("What is the id of the order you want the delivery time of");
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

    public static void addOrder(int[] pizzas, boolean takeaway, String payment_type,int customerId, String note, String street,String city, String country, String zipcode)
    {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl + "/order").openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            JSONObject adress = new JSONObject();
            adress.put("street",street);
            adress.put("city",city);
            adress.put("country",country);
            adress.put("zipcode",zipcode);
            JSONObject data = new JSONObject();
            data.put("pizzas", pizzas);
            data.put("takeaway", takeaway);
            data.put("payment_type", payment_type);
            data.put("customer_id", customerId);
            data.put("note", note);
            data.put("delivery_address", adress);



            String text = data.toString();

            System.out.println(text);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();

            System.out.println(getResponse(connection));
        }
        catch (IOException e)
        {
            System.out.println("Error connecting to the server");
        }
    }

    public static String getData(String url,String method) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod(method);
        String output = getResponse(connection);
        return output;
    }

    public static String getResponse(HttpURLConnection connection) throws  IOException
    {
        String output = "";
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