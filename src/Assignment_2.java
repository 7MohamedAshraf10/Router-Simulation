import java.io.IOException;
import java.util.*;

public class Assignment_2 {

    public static Scanner scanner = new Scanner(System.in);


    public static class Router implements Runnable {

        Network network = new Network();
        Semaphore semaphore;

        public Router() {
            semaphore = new Semaphore(network.N);
        }

        public void connect() throws InterruptedException {

            for (int i = 0; i < network.devices.size(); i++) {
                Thread t = new Thread( this, network.devices.get(i).getName());
                t.start();
            }

        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Random random = new Random();
            try {
                String name = "";
                semaphore.reserve(Thread.currentThread().getName());

                name = Thread.currentThread().getName();
                String outF1 = "- Connection " + Network.connectionNumber(name, 0) + ": "
                        + Thread.currentThread().getName() + " Occupied";
                System.out.println(outF1);

                Thread.currentThread().sleep(1000);
                String outF2 = "- Connection " + Network.connectionNumber(name, 0) + ": "
                        + Thread.currentThread().getName() + " Performs online activity";
                System.out.println(outF2);

                Thread.currentThread().sleep((random.nextInt(5) + 1) * 1000);

                semaphore.release(Thread.currentThread().getName());

                Thread.currentThread().stop();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public static class Semaphore {
        public Integer bound = 0;


        public Semaphore(Integer bound) {
            // TODO Auto-generated constructor stub
            this.bound = bound;

        }

        public synchronized void reserve(String name) throws InterruptedException, IOException {
            bound--;
            if (bound < 0) {
                int g = 0;
                String out = "";
                for (int i = 0; i < Network.devices.size(); i++) {
                    if (Network.devices.get(i).getName().equals(name)) {
                        out = name + " ( " + Network.devices.get(i).getType() + " )" + " Arrived and waiting";
                        break;
                    }
                }
                System.out.println(out);
                wait();
            } else {
                int g = 0;
                String out = "";

                for (int i = 0; i < Network.devices.size(); i++) {
                    if (Network.devices.get(i).getName().equals(name)) {
                        out = name + " ( " + Network.devices.get(i).getType() + " )" + " Arrived";
                        break;
                    }
                }
                System.out.println(out);

            }

        }

        public synchronized void release(String name) throws IOException {
            bound++;
            if (bound <= 0)
                notify();
            String out = "- Connection " + Network.connectionNumber(name, 1) + ": " + name + " Logged out";
            System.out.println(out);

        }

        public Integer getBound() {
            return bound;
        }

        public void setBound(Integer bound) {
            this.bound = bound;
        }
    }


    public static class Device {
        String name;
        String type;

        public Device(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public Device() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Network{
        public static Scanner input = new Scanner(System.in);
        public static ArrayList<Device> devices = new ArrayList<Device>();
        public static ArrayList<String> names = new ArrayList<String>();
        public static ArrayList<Boolean> state = new ArrayList<Boolean>();
        public static int N;
        public static int TC;
        public static String ch;

        public static void main(String[] args) throws InterruptedException, IOException {



                System.out.println("What is number of WI-FI Connections?");
                N = input.nextInt();

                System.out.println("What is number of devices clients want to connect?");
                TC = input.nextInt();

                String name = "";
                String type = "";

                for (int i = 0; i < TC; i++) {
                    System.out.print("Enter the name and the type with respect for (name type): ");
                    name = input.next();
                    type = input.next();

                    devices.add(new Device(name, type));
                }

                for (int j = 0; j < N; j++) {
                    names.add("");
                    state.add(false);
                }

                Router routerClass = new Router();
                routerClass.connect();



        }

        public synchronized static int connectionNumber(String name, int x) {

            int connectionNum = 0;
            int flag = 0;

            if (x == 1) {
                for (int k = 0; k < N; k++) {
                    if (names.get(k).equals(name)) {
                        names.set(k, "");
                        state.set(k, false);
                        connectionNum = k + 1;
                    }
                }
            } else {

                for (int i = 0; i < N; i++) {

                    if (name.equals(names.get(i))) {
                        connectionNum = i + 1;
                        flag++;
                    }
                }
                if (flag == 0) {

                    for (int j = 0; j < N; j++) {
                        if (state.get(j) == false) {
                            state.set(j, true);
                            connectionNum = j + 1;
                            names.set(j, name);
                            break;
                        }
                    }
                }
            }

            return connectionNum;
        }
    }




}
