package br.ufsc.egc.dbpedia.reader.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {

	private final Remote rmiImplementation;
	private final String name;

	public RmiServer(Remote rmiImplementation, String name) {
		this.rmiImplementation = rmiImplementation;
		this.name = name;
	}

	public void start() {
		try {
			Remote stub = UnicastRemoteObject.exportObject(rmiImplementation, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind(name, stub);
		} catch (RemoteException | AlreadyBoundException e) {
			throw new RuntimeException("Error while registering the object", e);
		}
	}

	public void stop() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			registry.unbind(name);
		} catch (RemoteException | NotBoundException e) {
			throw new RuntimeException("Error while unregistering the object", e);
		}
	}

}
