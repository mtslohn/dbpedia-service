package br.ufsc.egc.dbpedia.reader.service;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;
import br.ufsc.egc.dbpedia.reader.conf.PropertyLoader;
import br.ufsc.egc.dbpedia.reader.conf.ServiceProperty;
import br.ufsc.egc.dbpedia.reader.util.tdb.DBPediaTDBCreator;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.ResultSetStream;
import org.apache.jena.tdb.TDBFactory;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBPediaServiceImpl implements DBPediaService {

	private final Logger LOGGER = Logger.getLogger(getClass());

	private Model model;

	public DBPediaServiceImpl() {
		LOGGER.info("Loading DBPedia service...");
		loadModel();
		LOGGER.info("DBPedia service loaded");
	}

	private void loadModel() {
		PropertyLoader propertyLoader = new PropertyLoader();
		String tdbSchemaPath = propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_PATH);
		File directory = new File(tdbSchemaPath);

		if (!directory.exists()) {
			directory.mkdirs();
		}
		model = TDBFactory.createDataset(tdbSchemaPath).getDefaultModel();
		if (model.isEmpty()) {
			model = new DBPediaTDBCreator().createTDBSchema(
					propertyLoader.getProperty(ServiceProperty.CATEGORIES_LABELS_FILE),
					propertyLoader.getProperty(ServiceProperty.CATEGORIES_SKOS_FILE),
					propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_PATH)
			);
		}
	}

	@WebMethod
	public List<String> getNarrowConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?narrowLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . " + "?narrow skos:broader ?concept . "
				+ "?narrow rdfs:label ?narrowLabel " + "} ";

		return queryResultList(queryString, qs -> qs.getLiteral("narrowLabel").getString());

	}

	@WebMethod
	public List<String> getAllNarrowConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?narrowLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . "
				+ "?narrow skos:broader* ?concept . "
				+ "?narrow rdfs:label ?narrowLabel " + "} ";

		return queryResultList(queryString, qs -> qs.getLiteral("narrowLabel").getString());

	}

	@WebMethod
	public List<String> getBroaderConceptsUntilLevel(String conceptName, int maxLevel) {

		String queryStringTemplate = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?broaderLabel WHERE { "
				+ "?concept rdfs:label \"%s\"@pt . "
				+ "?concept skos:broader{1, %s} ?broader . "
				+ "?broader rdfs:label ?broaderLabel " + "} ";

		String queryString = String.format(queryStringTemplate, conceptName,
				maxLevel);

		return queryResultList(queryString, qs -> qs.getLiteral("broaderLabel").getString());

	}

	@WebMethod
	public List<String> getAllBroaderConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?broaderLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . "
				+ "?concept skos:broader* ?broader . "
				+ "?broader rdfs:label ?broaderLabel " + "} ";

		return queryResultList(queryString, qs -> qs.getLiteral("broaderLabel").getString());

	}

	@WebMethod
	public List<String> getBroaderConcepts(String conceptName) {

		BasicPattern basicPattern = new BasicPattern();

		Var varConcept = Var.alloc("concept");
		Var varBroader = Var.alloc("broader");
		Var varBroaderLabel = Var.alloc("broaderLabel");

		final String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";
		final String SKOS_URI = "http://www.w3.org/2004/02/skos/core#";

		basicPattern.add(new Triple(varConcept, NodeFactory.createURI(RDFS_URI + "label"), NodeFactory.createLiteral(conceptName, "pt")));
		basicPattern.add(new Triple(varConcept, NodeFactory.createURI(SKOS_URI + "broader"), varBroader));
		basicPattern.add(new Triple(varBroader, NodeFactory.createURI(RDFS_URI + "label"), varBroaderLabel));

		Op op = new OpBGP(basicPattern);

		QueryIterator queryIterator = Algebra.exec(op, model);

		List<String> broaderConcepts = new ArrayList<>();

		List<String> resultVars = new ArrayList<>();
		resultVars.add("broaderLabel");
		ResultSet rs = new ResultSetStream(resultVars, model, queryIterator);

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			String concept = solution.getLiteral("broaderLabel").getString();
			if (!concept.startsWith("!")) { // meta-category -- non-taxonomic
				broaderConcepts.add(concept);
			}
		}

		return broaderConcepts;

	}

	private List<String> queryResultList(String queryString, Function<QuerySolution, String> mapper) {
		LOGGER.debug(queryString);

		Query query = QueryFactory.create(queryString);
		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> results = new ArrayList<>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			results.add(mapper.apply(solution));
		}

		return results;
	}

	public Term findTree(String conceptName, int levels) {
		Term term = new Term();
		term.setLabel(conceptName);
		mountTree(term, levels);
		return term;
	}

	private void mountTree(Term term, int levels) {
		if (levels >= 1) {
			List<String> sons = getBroaderConcepts(term.getLabel());
			for (String son : sons) {
				Term sonTerm = new Term();
				sonTerm.setLabel(son);
				term.addSon(sonTerm);
				mountTree(sonTerm, (levels - 1));
			}
		}
	}

}
