package br.pod.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.pod.user.UserIF;

public class Server extends UnicastRemoteObject implements ServerIF{
	HashMap<String, UserIF> users;

	protected Server() throws RemoteException {
		this.users = new HashMap<String, UserIF>();
	}

	@Override
	public void register(UserIF user) throws RemoteException, UserException{
		if (!users.containsKey(user.getName())){
			users.put(user.getName(), user);
		} else {
			throw new UserException("O nome de usuário informado já existe. Informe outro.");
		}
	}

	@Override
	public void unRegister(UserIF user) throws RemoteException {
		users.remove(user.getName());
		sendAll(user.getName(), "saiu do grupo.");
	}

	@Override
	public void sendAll(String sender, String message) throws RemoteException {
		String text = new String(sender + ": " + message);
		for(String userName : users.keySet()){
			if(!userName.equals(sender)){
				users.get(userName).receive(text);
			}
		}
	}

	@Override
	public void sendTo(String sender, String user, String message) throws RemoteException, UserException {
		if(users.containsKey(user)){
			if (message != null){
				String text = sender + " (direct): " + message;
				users.get(user).receive(text);
			}
		} else {
			throw new UserException("O usuário informado não existe.");
		}
	}

	@Override
	public void rename(UserIF user, String newName) throws RemoteException, UserException {
		if(!users.containsKey(newName)){
			users.put(newName, user);
			users.remove(user.getName());
			sendAll(user.getName(), "mudou o seu nome de usuário. Novo nome: '" + newName + "'.");
		} else {
			throw new UserException("O nome de usuário informado já existe");
		}
	}
	
	@Override
	public List<String> list(UserIF user) throws RemoteException {
		List<String> list = new ArrayList<String>(users.keySet());
		list.remove(user.getName());
		return list;
	}
	
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			Naming.bind("Server", new Server());
			System.out.println("Servidor rodando");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
