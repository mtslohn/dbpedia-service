package br.ufsc.egc.dbpeader.reader.test;

import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.egc.dbpedia.reader.service.DBPediaService;

public class DBpediaCategoriaBrasilTest {
	
	private DBPediaService service; 
	
	@Before
	public void prepare() {
		service = new DBPediaService();
	}
	
	@Test
	public void findNarrowConceptsTest() {
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
	public void findBroaderConceptsTest() {
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
	
	
}
