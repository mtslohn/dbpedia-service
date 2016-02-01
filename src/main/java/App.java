import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class App {

	private static final String CATEGORY_LABELS = "category-labels_pt.ttl",
			SKOS_CATEGORIES = "skos-categories_pt.ttl";

	public static void main(String[] args) {
		
		Model model = loadModel();

		String queryString = "PREFIX dbc:<http://purl.org/dc/terms/> "
						+ "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
						+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
						+ "SELECT ?narrowLabel WHERE { "
						+ "?concept rdfs:label \"Brasil\"@pt . "
						+ "?narrow skos:broader ?concept . " 
						+ "?narrow rdfs:label ?narrowLabel "
						+ "} ";
		
		Query query = QueryFactory.create(queryString);
		
		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();
		
		while (rs.hasNext()) {
			QuerySolution solution = rs.nextSolution();
			Literal literal = solution.getLiteral("narrowLabel");
			System.out.println(literal.getString());
		}
		
	}

	private static Model loadModel() {

		Model model = ModelFactory.createDefaultModel();

		InputStream labelsStream = FileManager.get().open(CATEGORY_LABELS);
		InputStream hierarchyStream = FileManager.get().open(SKOS_CATEGORIES);

		model.read(labelsStream, null, "TURTLE");
		model.read(hierarchyStream, null, "TURTLE");
		return model;

	}

}
