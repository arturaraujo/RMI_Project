package br.pod.user;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import br.pod.server.ServerIF;
import br.pod.server.UserException;

public class User extends UnicastRemoteObject implements UserIF {
	protected String name;
	protected ServerIF server;

	public User(String name) throws RemoteException, UserException {
		try {
			this.server = (ServerIF) Naming.lookup("Server");
			this.name = name;
			this.server.register(this);
		} catch (MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receive(String message) throws RemoteException {
		System.out.println(message);
	}

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}
	
	public static void main(String[] args) {
		User user = null;
		String string;
		while (true){
			string = JOptionPane.showInputDialog("Informe seu nome: ");
			try {
				if(string.equals("0"))
					break;
				user = new User(string);
				break;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		while (!string.equals("0")){
			//TODO Menu para o usuário. Por enquanto só está mandando pra todo mundo.
			string = JOptionPane.showInputDialog("Mensagem: ");
			try {
				user.server.sendAll(user.getName(), string);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
}
