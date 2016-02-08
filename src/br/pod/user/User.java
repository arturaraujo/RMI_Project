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
		User user;
		String string, errorMessage = "";
		while (true){
			string = JOptionPane.showInputDialog(errorMessage + "Informe seu nome: ");
			errorMessage = "";
			try {
				if(string == null){
					System.exit(0);
				}
				user = new User(string);
				break;
			} catch (UserException e) {
				errorMessage = "(" + e.getMessage() + ")\n\n";
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		boolean exit = false;
		while (!exit){
			string = JOptionPane.showInputDialog(menu(errorMessage));
			errorMessage = "";
			switch(string.trim()){
				case "1":
					user.sendAll();
					break;
				case "2":
					break;
				case "3":
					break;
				case "4":
					break;
				case "5":
					exit = true;
					break;
				default:
					errorMessage = "(O comando informado é inválido)\n\n";
					break;
			}
		}
		try {
			user.server.unRegister(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private static String menu(String errorMessage){
		StringBuilder menu = new StringBuilder(errorMessage + "Escolha uma das opções abaixo:\n");
		menu.append("1 - Enviar no grupo\n");
		menu.append("2 - Enviar direct\n");
		menu.append("3 - Listar usuários\n");
		menu.append("4 - Mudar nome de usuario\n");
		menu.append("5 - Sair do chat\n");
		return menu.toString();
	}
	
	private void sendAll(){
		String string;
		while(true){
			string = JOptionPane.showInputDialog("Mensagem:");
			if(string == null)
				break;
			try {
				this.server.sendAll(this.name, string);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
