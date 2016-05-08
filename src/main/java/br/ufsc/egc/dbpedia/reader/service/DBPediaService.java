package br.ufsc.egc.dbpedia.reader.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.ResultBinding;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.ResultSetStream;
import org.apache.jena.util.FileManager;
import org.apache.log4j.Logger;

import br.ufsc.egc.curriculumextractor.model.taxonomy.Term;

public class DBPediaService {

	private static final String CATEGORY_LABELS = "category-labels_pt.ttl",
			SKOS_CATEGORIES = "skos-categories_pt.ttl";

	private static DBPediaService instance;

	private final Logger LOGGER = Logger.getLogger(getClass());

	private Model model;

	private DBPediaService() {

		loadModel();
	}

	public static DBPediaService getInstance() {
		if (instance == null) {
			instance = new DBPediaService();
		}
		return instance;
	}

	private void loadModel() {

		model = ModelFactory.createDefaultModel();

		InputStream labelsStream = FileManager.get().open(CATEGORY_LABELS);
		InputStream hierarchyStream = FileManager.get().open(SKOS_CATEGORIES);

		model.read(labelsStream, null, "TURTLE");
		model.read(hierarchyStream, null, "TURTLE");

	}

	public List<String> findNarrowConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?narrowLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . " + "?narrow skos:broader ?concept . "
				+ "?narrow rdfs:label ?narrowLabel " + "} ";

		LOGGER.debug(queryString);

		Query query = QueryFactory.create(queryString);

		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> narrowConcepts = new ArrayList<String>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("narrowLabel");
			narrowConcepts.add(literal.getString());
		}

		return narrowConcepts;

	}

	public List<String> findAllNarrowConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?narrowLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . "
				+ "?narrow skos:broader* ?concept . "
				+ "?narrow rdfs:label ?narrowLabel " + "} ";

		LOGGER.debug(queryString);

		Query query = QueryFactory.create(queryString);

		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> narrowConcepts = new ArrayList<String>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("narrowLabel");
			narrowConcepts.add(literal.getString());
		}

		return narrowConcepts;

	}
	
	
	//TODO refactoring
//	private ParameterizedSparqlString sparqlString = null;

	public List<String> findBroaderConcepts(String conceptName) {
		
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
		
		List<String> resultVars = new ArrayList<String>();
		resultVars.add("broaderLabel");
		ResultSet rs = new ResultSetStream(resultVars , model, queryIterator);
		
//		if (sparqlString == null) {
//			sparqlString = new ParameterizedSparqlString();
//
//			sparqlString.append("PREFIX dbc:<http://purl.org/dc/terms/> "
//					+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
//					+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
//					+ "SELECT ?broaderLabel WHERE { " 
//					+ "?concept rdfs:label ?conceptName . "
//					+ "?concept skos:broader ?broader . "
//					+ "?broader rdfs:label ?broaderLabel " + "} ");
//		}
//		
//		sparqlString.setLiteral("conceptName", conceptName, "pt");

//		LOGGER.debug(sparqlString.toString());

//		Query query = sparqlString.asQuery();

//		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> broaderConcepts = new ArrayList<String>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("broaderLabel");
			if (literal.getString().startsWith("!")) { // meta-categoria - nao
														// eh taxonomica
				continue;
			}
			broaderConcepts.add(literal.getString());
		}
		
		queryIterator.close();

		return broaderConcepts;

	}

	public Term findTree(String conceptName, int levels) {
		Term term = new Term();
		term.setLabel(conceptName);

		mountTree(term, levels);

		return term;
	}

	private void mountTree(Term term, int levels) {

		if (levels >= 1) {

			List<String> sons = findBroaderConcepts(term.getLabel());

			for (String son : sons) {

				Term sonTerm = new Term();
				sonTerm.setLabel(son);

				term.addSon(sonTerm);

				mountTree(sonTerm, (levels - 1));
			}

		}

	}

	public List<String> findBroaderConcepts(String conceptName, int maxLevel) {

		String queryStringTemplate = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?broaderLabel WHERE { "
				+ "?concept rdfs:label \"%s\"@pt . "
				+ "?concept skos:broader{1, %s} ?broader . "
				+ "?broader rdfs:label ?broaderLabel " + "} ";

		String queryString = String.format(queryStringTemplate, conceptName,
				maxLevel);

		LOGGER.debug(queryString);

		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);

		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> broaderConcepts = new ArrayList<String>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("broaderLabel");
			broaderConcepts.add(literal.getString());
		}

		return broaderConcepts;

	}

	public List<String> findAllBroaderConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
				+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
				+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT ?broaderLabel WHERE { " + "?concept rdfs:label \""
				+ conceptName + "\"@pt . "
				+ "?concept skos:broader* ?broader . "
				+ "?broader rdfs:label ?broaderLabel " + "} ";

		LOGGER.debug(queryString);

		Query query = QueryFactory.create(queryString);

		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();

		List<String> broaderConcepts = new ArrayList<String>();

		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("broaderLabel");
			broaderConcepts.add(literal.getString());
		}

		return broaderConcepts;

	}

}
