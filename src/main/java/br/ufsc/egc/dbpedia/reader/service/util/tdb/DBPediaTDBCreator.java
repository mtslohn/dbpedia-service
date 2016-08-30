package br.ufsc.egc.dbpedia.reader.service.util.tdb;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

public class DBPediaTDBCreator {
	
	public static final String CATEGORY_LABELS_FILE = "category-labels_pt.ttl";
	public static final String SKOS_CATEGORIES_FILE = "skos-categories_pt.ttl";

	public static final String TDB_DIRECTORY = "resources/tdb/dbpedia";
	
	public Model createTDB() {
		Model model = ModelFactory.createDefaultModel();
		model = TDBFactory.createDataset(TDB_DIRECTORY).getDefaultModel();


		InputStream labelsStream = FileManager.get().open(CATEGORY_LABELS_FILE);
		InputStream hierarchyStream = FileManager.get().open(SKOS_CATEGORIES_FILE);

		model.read(labelsStream, null, "TURTLE");
		model.read(hierarchyStream, null, "TURTLE");
		
		return model;
	}
	
	public static void main(String[] args) {
		new DBPediaTDBCreator().createTDB().close();;
	}

}
