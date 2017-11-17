//Server.java
import java.net.*;
import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server 
{
	ServerSocket serverSocket;
	public static int PORT=8080;
	public static void main(String[] args)throws IOException
	{ 
		new Server().runServer();
	}
	public void runServer() throws IOException
	{
		serverSocket = new ServerSocket(PORT);
		System.out.println("Server up and ready for connection");
		while(true)
		{
			Socket socket = serverSocket.accept();
			new ServerThread(socket).start();
		}
	}
	

}