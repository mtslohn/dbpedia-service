package br.ufsc.egc.dbpedia.reader.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Severity;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
							+ "SELECT ?narrowLabel WHERE { "
							+ "?concept rdfs:label \"" + conceptName  + "\"@pt . "
							+ "?narrow skos:broader ?concept . " 
							+ "?narrow rdfs:label ?narrowLabel "
							+ "} ";
		
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
							+ "SELECT ?narrowLabel WHERE { "
							+ "?concept rdfs:label \"" + conceptName  + "\"@pt . "
							+ "?narrow skos:broader* ?concept . " 
							+ "?narrow rdfs:label ?narrowLabel "
							+ "} ";
		
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

	public List<String> findBroaderConcepts(String conceptName) {

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
							+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
							+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
							+ "SELECT ?broaderLabel WHERE { "
							+ "?concept rdfs:label \"" + conceptName  + "\"@pt . "
							+ "?concept skos:broader ?broader . " 
							+ "?broader rdfs:label ?broaderLabel "
							+ "} ";
		
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
	
	public List<String> findBroaderConcepts(String conceptName, int maxLevel) {

		String queryStringTemplate = "PREFIX dbc:<http://purl.org/dc/terms/> "
							+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
							+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
							+ "SELECT ?broaderLabel WHERE { "
							+ "?concept rdfs:label \"%s\"@pt . "
							+ "?concept skos:broader{1, %s} ?broader . " 
							+ "?broader rdfs:label ?broaderLabel "
							+ "} ";
		
		String queryString = String.format(queryStringTemplate, conceptName, maxLevel);
		
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
							+ "SELECT ?broaderLabel WHERE { "
							+ "?concept rdfs:label \"" + conceptName  + "\"@pt . "
							+ "?concept skos:broader* ?broader . " 
							+ "?broader rdfs:label ?broaderLabel "
							+ "} ";
		
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
