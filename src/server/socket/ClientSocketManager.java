package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static view.ClientFrame.bet;
import static view.ClientFrame.txtLogCl;
import static view.ConnectionServerFrame.txtIP;


public class ClientSocketManager implements Runnable {

	private Socket socket;
	private ObjectOutputStream output_stream;
	private ObjectInputStream input_stream;
	public static String idClient;
	public static String client_msg;
	public static String btn_status;
	public ClientSocketManager(String idClient) {
		this.idClient = idClient;
	}

	private String getId() {
		return idClient;
	}

	public void run() {

			try{
				attemptToConnect();
				createStreams();
				initProcessing();
			}catch(IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				try {
					close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	private void initProcessing() throws IOException, InterruptedException {
		String server_msg = " ";
		client_msg = " ";
		btn_status = " ";
		try {
			server_msg = (String) input_stream.readObject();
			updateLog(server_msg);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(server_msg);
		do {

			client_msg = readFromInputText();
			System.out.println(client_msg);


			if(EchoThread.isInteger(client_msg) == false){
				sendDataToServerTxt(idClient);
				sendDataToServerTxt(client_msg);
			}else {
				btn_status = bet.getText();
				System.out.println(btn_status);
				sendDataToServerTxt(idClient);
				sendDataToServerTxt(client_msg);
				sendDataToServerTxt(btn_status);
			}


			try {
				server_msg = (String) input_stream.readObject();
				updateLog(server_msg);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println(server_msg);

		} while (!server_msg.contains("BYE"));
	}
	
	private void sendDataToServerTxt (String msg) throws IOException {
		output_stream.writeObject(msg);
		output_stream.flush();
	}
	
	private String readFromInputText() throws InterruptedException {
		Scanner in = new Scanner(System.in);
		do{
			//in.nextLine();
			client_msg = " ";
			System.out.print(client_msg);
			System.out.print("\r");
			//Thread.sleep(1000);
		}while(client_msg.equals(" "));
		return client_msg;
	}
	private String btnStatus() throws InterruptedException {
		Scanner in = new Scanner(System.in);
		do{
			//in.nextLine();
			btn_status = " ";
			System.out.print(btn_status);
			System.out.print("\r");
			//Thread.sleep(1000);
		}while(btn_status.equals(" "));
		return btn_status;
	}

	
	private void attemptToConnect() throws IOException {
		try {
			socket = new Socket(txtIP.getText(), 4040);
		}catch (NullPointerException e3){
			socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 4040);
		}

		/*String ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
		System.out.println(ip);*/

		System.out.println("CLIENT " + idClient + " -> CONNESSIONE VERSO UN SERVER ...");
		updateLog("CLIENT " + idClient + " -> CONNESSIONE VERSO UN SERVER ...");
		System.out.println("CLIENT " + idClient + " -> CONNESSIONE AVVENUTA VERSO "+ socket.getInetAddress()+" ALLA PORTA REMOTA "+socket.getPort() + " E ALLA PORTA LOCALE " + socket.getLocalPort());
		updateLog("CLIENT " + idClient + " -> CONNESSIONE AVVENUTA VERSO "+ socket.getInetAddress()+" ALLA PORTA REMOTA "+socket.getPort() + " E ALLA PORTA LOCALE " + socket.getLocalPort());

	}
	
	private void close() throws IOException {
		System.out.println("CLIENT " + idClient + " -> CHIUSURA CONNESSIONE SOCKET");
		updateLog("CLIENT " + idClient + " -> CHIUSURA CONNESSIONE SOCKET");
		if(output_stream != null && input_stream != null && socket != null) {
			output_stream.close();
			input_stream.close();
			socket.close();
		}
	}
	
	private void createStreams() throws IOException {
		output_stream = new ObjectOutputStream(socket.getOutputStream());
		output_stream.flush();
		
		input_stream = new ObjectInputStream(socket.getInputStream());
		System.out.println("CLIENT " + idClient + " -> STREAM CREATI");
		updateLog("CLIENT " + idClient + " -> STREAM CREATI");
	}
	
	private void updateLog(String text) {
		txtLogCl.append(String.format("%4d - %s\n", txtLogCl.getLineCount(), text));
	}

}
