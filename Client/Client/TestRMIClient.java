package Client;

import Server.Interface.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.util.concurrent.CountDownLatch;

import java.util.*;
import java.io.*;

public class TestRMIClient extends TestClient
{
    private static String s_serverHost = "localhost";
    // recommended to hange port last digits to your group number
    private static int s_serverPort = 1045;
    private static String s_serverName = "MiddleWare";

    //TODO: ADD YOUR GROUP NUMBER TO COMPILE
    private static String s_rmiPrefix = "group_45_";

    public static void main(String args[])
    {
        if (args.length > 0)
        {
            s_serverHost = args[0];
        }
        if (args.length > 1)
        {
            s_serverName = args[1];
        }
        if (args.length > 2)
        {
            System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUsage: java client.RMIClient [server_hostname [server_rmiobject]]");
            System.exit(1);
        }

        // Get a reference to the RMIRegister
        try {
            CountDownLatch latch = new CountDownLatch(1);

            // Thread 1
            Thread thread1 = new Thread(() -> {
                try {
                    TestRMIClient client1 = new TestRMIClient();
                    client1.connectServer();

                    latch.await();

                    client1.start("1");
                    System.out.println("Client 1 started at: " + System.nanoTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    TestRMIClient client2 = new TestRMIClient();
                    client2.connectServer();

                    latch.await(); // Wait for the signal

                    client2.start("2");
                    System.out.println("Client 2 started at: " + System.nanoTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            thread1.start();
            thread2.start();

            // Give threads time to connect and reach the await() call
            Thread.sleep(200);

            // Release both threads simultaneously
            System.out.println("Releasing both clients...");
            latch.countDown();

            // Optional: wait for threads to complete
            thread1.join();
            thread2.join();

        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public TestRMIClient()
    {
        super();
    }

    public void connectServer()
    {
        connectServer(s_serverHost, s_serverPort, s_serverName);
    }

    public void connectServer(String server, int port, String name)
    {
        try {
            boolean first = true;
            while (true) {
                try {
                    Registry registry = LocateRegistry.getRegistry(server, port);
                    m_resourceManager = (IResourceManager)registry.lookup(s_rmiPrefix + name);
                    System.out.println("Connected to '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
                    break;
                }
                catch (NotBoundException|RemoteException e) {
                    if (first) {
                        System.out.println("Waiting for '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
                        first = false;
                    }
                }
                Thread.sleep(500);
            }
        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
}


