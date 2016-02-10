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
			if(string == null)
				break;
			errorMessage = "";
			switch(string.trim()){
				case "1":
					user.sendAll();
					break;
				case "2":
					user.sendTo();
					break;
				case "3":
					break;
				case "4":
					user.rename();
					break;
				case "5":
					exit = true;
					break;
				default:
					errorMessage = "(O comando informado � inv�lido)\n\n";
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
		StringBuilder menu = new StringBuilder(errorMessage + "Escolha uma das op��es abaixo:\n");
		menu.append("1 - Enviar no grupo\n");
		menu.append("2 - Enviar direct\n");
		menu.append("3 - Listar usu�rios\n");
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
	
	private void sendTo(){
		String destinatary = "", errorMessage = "";
		boolean exit = false;
		while(!exit){
			destinatary = JOptionPane.showInputDialog(errorMessage + "Informe o nome do usuario:");
			if(destinatary == null){
				exit = true;
				break;
			}
			try {
				this.server.sendTo(this.getName(), destinatary.trim(), null);
				exit = false;
				break;
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (UserException e) {
				errorMessage = "(" + e.getMessage() + ")\n\n";
			}
		}
		String message;
		while(!exit){
			message = JOptionPane.showInputDialog(errorMessage + "Direct para '" + destinatary + "':");
			if(message == null)
				break;
			try {
				this.server.sendTo(this.name, destinatary, message);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (UserException e) {
				errorMessage = "(" + e.getMessage() + ")\n\n";
			}
		}
	}
	
	private void rename(){
		String newName = "", errorMessage = "";
		while(true){
			newName = JOptionPane.showInputDialog(errorMessage + "Informe o seu novo nome de usuario:");
			if(newName == null)
				break;
			try {
				this.server.rename(this, newName);
				this.name = newName;
				break;
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (UserException e) {
				errorMessage = "(" + e.getMessage() + ")\n\n";
			}
		}
	}
}
