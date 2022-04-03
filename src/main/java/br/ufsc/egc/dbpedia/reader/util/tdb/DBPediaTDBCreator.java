package br.ufsc.egc.dbpedia.reader.util.tdb;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class DBPediaTDBCreator {

	public Model createTDBSchema(String categoriesLabelsFile, String categoriesSkosFile, String tdbSchemaPath) {
		ModelFactory.createDefaultModel();
		Model model = TDBFactory.createDataset(tdbSchemaPath).getDefaultModel();

		InputStream labelsStream = FileManager.get().open(categoriesLabelsFile);
		InputStream hierarchyStream = FileManager.get().open(categoriesSkosFile);

		model.read(labelsStream, null, "TURTLE");
		model.read(hierarchyStream, null, "TURTLE");

		return model;
	}

}
