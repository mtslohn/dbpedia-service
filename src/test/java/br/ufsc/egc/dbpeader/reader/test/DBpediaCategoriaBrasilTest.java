package br.ufsc.egc.dbpeader.reader.test;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceFactoryImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

@Ignore(value = "It depends on mocking index resources instead of using the full index")
public class DBpediaCategoriaBrasilTest {

	private DBPediaService service;

	@Before
	public void prepare() throws IOException {
		service = new DBPediaServiceFactoryImpl().buildFromProperties();
	}

	@Test
	public void findNarrowConceptsTest() throws RemoteException {
		List<String> narrowConcepts = service.getNarrowConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos filhos: ");
		ListIterator<String> iterator = narrowConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder);
	}

	@Test
	public void findBroaderConceptsTest() throws RemoteException {
		List<String> broaderConcepts = service.getBroaderConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos pais: ");
		ListIterator<String> iterator = broaderConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder);
	}
	
	@Test
	public void findAllBroaderConceptsTest() throws RemoteException {
		List<String> broaderConcepts = service.getAllBroaderConcepts("Brasil");
		StringBuilder builder = new StringBuilder("Conceitos pais: ");
		ListIterator<String> iterator = broaderConcepts.listIterator();
		while (iterator.hasNext()) {
			String concept = iterator.next();
			builder.append(concept);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		System.out.println(builder);
	}
	
	@Test
	public void findTreeTest() throws RemoteException {
		Term broaderConcepts = service.findTree("Brasil", 2);
		Tree tree = new Tree();
		tree.setRoots(Arrays.asList(broaderConcepts));
		System.out.println(tree.print());
	}
	
}
