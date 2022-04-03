package br.ufsc.egc.dbpeader.reader.test;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.curriculumextractor.model.taxonomy.Tree;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class DBpediaBrazilCategoryIntegrationTest {

    private static final Logger log = Logger.getLogger(DBpediaBrazilCategoryIntegrationTest.class.getName());

    private DBPediaService service;

    @Before
    public void prepare() throws IOException {
        service = new DBPediaServiceFactoryImpl().buildFromProperties();
    }

    @Test
    public void findNarrowConceptsTest() throws RemoteException {
        List<String> narrowConcepts = service.getNarrowConcepts("Brasil");
        log.info("Conceitos filhos: " + narrowConcepts.stream().collect(Collectors.joining(", ")));
        assertThat(narrowConcepts.isEmpty(), is(false));
        assertThat(narrowConcepts, is(not(empty())));
    }

    @Test
    public void findBroaderConceptsTest() throws RemoteException {
        List<String> broaderConcepts = service.getBroaderConcepts("Brasil");
        log.info("Conceitos pais: " + broaderConcepts.stream().collect(Collectors.joining(", ")));
        assertThat(broaderConcepts, is(not(empty())));
    }

    @Test
    public void findAllBroaderConceptsTest() throws RemoteException {
        List<String> broaderConcepts = service.getAllBroaderConcepts("Brasil");
        log.info("Conceitos pais: " + broaderConcepts.stream().collect(Collectors.joining(", ")));
        assertThat(broaderConcepts, is(not(empty())));
    }

    @Test
    public void findTreeTest() throws RemoteException {
        Term broaderConcepts = service.findTree("Brasil", 2);
        Tree tree = new Tree();
        tree.setRoots(Arrays.asList(broaderConcepts));
        log.info(tree.print());
        assertThat(broaderConcepts.getSons(), is(not(empty())));
    }

}
