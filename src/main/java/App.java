import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;


public class App {
	
	private static final String CATEGORY_LABELS = "category-labels_pt.ttl",
			ARTICLE_CATEGORIES = "skos-categories_pt.ttl";

	public static void main(String[] args) {
		
		Model model = ModelFactory.createDefaultModel();

		InputStream labelsStream = FileManager.get().open(CATEGORY_LABELS);
		InputStream hierarchyStream = FileManager.get().open(ARTICLE_CATEGORIES);
		
		model.read(labelsStream, null, "TURTLE");
		model.read(hierarchyStream, null, "TURTLE");
		
		model.write(System.out);
		
	}

}
