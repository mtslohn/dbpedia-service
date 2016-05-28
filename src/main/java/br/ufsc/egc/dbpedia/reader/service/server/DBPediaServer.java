package br.ufsc.egc.dbpedia.reader.service.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

public class DBPediaServer {
	
	public static void main(String[] args) {
		
		DBPediaServiceInterface service = DBPediaServiceImpl.getInstance();

		try {
        	UnicastRemoteObject.exportObject(service, 0);
        	
        	// Bind the remote object's stub in the registry
        	Registry registry = LocateRegistry.getRegistry();
			registry.bind("DBPediaService", service);
		} catch (RemoteException | AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
