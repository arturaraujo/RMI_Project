package br.pod.user;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserIF extends Remote{
	public String getName() throws RemoteException;
	public void receive(String message) throws RemoteException;
}
