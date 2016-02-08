package br.pod.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.pod.user.UserIF;

public interface ServerIF extends Remote{
	public void register(UserIF user) throws RemoteException, UserException;
	public void unRegister(UserIF user) throws RemoteException;
	public void sendAll(String sender, String message) throws RemoteException;
	public void sendTo(String sender, String user, String message) throws RemoteException, UserException;
}
