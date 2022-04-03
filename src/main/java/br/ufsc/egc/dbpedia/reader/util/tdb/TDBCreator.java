package br.ufsc.egc.dbpedia.reader.util.tdb;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import java.io.IOException;
import java.io.InputStream;

public class TDBCreator {

	public Model createTDBSchema(String tdbSchemaFolder, String... turtleFiles) throws IOException {
		ModelFactory.createDefaultModel();
		Model model = TDBFactory.createDataset(tdbSchemaFolder).getDefaultModel();

		for (String file : turtleFiles) {
			try (InputStream labelsStream = FileManager.get().open(file)) {
				model.read(labelsStream, null, "TURTLE");
			}
		}

		return model;
	}

}
