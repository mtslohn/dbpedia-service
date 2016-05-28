package br.ufsc.egc.dbpedia.reader.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;

public interface DBPediaServiceInterface extends Remote {

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findNarrowConcepts(java.lang.String)
	 */
	List<String> findNarrowConcepts(String conceptName) throws RemoteException;

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findAllNarrowConcepts(java.lang.String)
	 */
	List<String> findAllNarrowConcepts(String conceptName) throws RemoteException;

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findBroaderConcepts(java.lang.String)
	 */
	List<String> findBroaderConcepts(String conceptName) throws RemoteException;

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findTree(java.lang.String, int)
	 */
	Term findTree(String conceptName, int levels) throws RemoteException;

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findBroaderConceptsARQ(java.lang.String, int)
	 */
	List<String> findBroaderConceptsARQ(String conceptName, int maxLevel) throws RemoteException;

	/* (non-Javadoc)
	 * @see br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface#findAllBroaderConcepts(java.lang.String)
	 */
	List<String> findAllBroaderConcepts(String conceptName) throws RemoteException;

}