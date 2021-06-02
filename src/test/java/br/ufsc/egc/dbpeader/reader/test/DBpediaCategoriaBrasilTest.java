package br.ufsc.egc.dbpeader.reader.test;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceInterface;
import br.ufsc.egc.dbpedia.reader.service.impl.DBPediaServiceImpl;

@Ignore(value = "It depends on mocking index resources instead of using the full index")
public class DBpediaCategoriaBrasilTest {
	
	private DBPediaServiceInterface service; 
	
	@Before
	public void prepare() {
		service = DBPediaServiceImpl.getInstance();
	}
	
	@Test
	public void findNarrowConceptsTest() throws RemoteException {
		List<String> narrowConcepts = service.findNarrowConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos filhos: ");
		ListIterator<String> iterator = narrowConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder.toString());
	}

	@Test
	public void findBroaderConceptsTest() throws RemoteException {
		List<String> broaderConcepts = service.findBroaderConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos pais: ");
		ListIterator<String> iterator = broaderConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder.toString());
	}
	
	@Test
	public void findAllBroaderConceptsTest() throws RemoteException {
		List<String> broaderConcepts = service.findAllBroaderConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos pais: ");
		ListIterator<String> iterator = broaderConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder.toString());
	}
	
	@Test
	public void findTreeTest() throws RemoteException {
		Term broaderConcepts = service.findTree("Brasil", 2);
		Tree tree = new Tree();
		tree.setRoots(Arrays.asList(broaderConcepts));
		System.out.println(tree.print());
	}
	
}
