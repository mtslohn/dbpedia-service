package br.ufsc.egc.dbpedia.reader.service;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DBPediaService extends Remote {

	Term findTree(String conceptName, int levels) throws RemoteException;

	List<String> getNarrowConcepts(String conceptName) throws RemoteException;

	List<String> getAllNarrowConcepts(String conceptName) throws RemoteException;

	List<String> getBroaderConcepts(String conceptName) throws RemoteException;

	List<String> getAllBroaderConcepts(String conceptName) throws RemoteException;

	List<String> getBroaderConceptsUntilLevel(String conceptName, int maxLevel) throws RemoteException;

}