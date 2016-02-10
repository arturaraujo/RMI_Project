package br.pod.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.pod.user.UserIF;

public interface ServerIF extends Remote{
	public void register(UserIF user) throws RemoteException, UserException;
	public void unRegister(UserIF user) throws RemoteException;
	public void sendAll(String sender, String message) throws RemoteException;
	public void sendTo(String sender, String user, String message) throws RemoteException, UserException;
	public void rename(UserIF user, String newName) throws RemoteException, UserException;
	public List<String> list(UserIF user) throws RemoteException;
}
