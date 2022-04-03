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

	public void register() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			Remote stub = UnicastRemoteObject.exportObject(rmiImplementation, 0);

			register(registry, stub, 1);
		} catch (RemoteException e) {
			throw new RuntimeException("Error while preparing to register the object!", e);
		}
	}

	private void register(Registry registry, Remote stub, int attempt) throws RemoteException {
		try {
			registry.bind(name, stub);
		} catch (AlreadyBoundException e) {
			if (attempt >= 3) {
				throw new RuntimeException("Failed to register " + name + " after " + attempt + " attempts", e);
			}
			unregisterQuietly(registry);
			register(registry, stub, attempt + 1);
		}
	}

	private void unregisterQuietly(Registry registry) throws RemoteException {
		try {
			registry.unbind(name);
		} catch (NotBoundException e1) {
			// silently fail
		}
	}

}
