package br.ufsc.egc.util.tdb;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import java.io.IOException;
import java.io.InputStream;

public class TDBCreator {

	public Model createTDBSchema(String tdbSchemaFolder, SourceFile... turtleFiles) throws IOException {
		Model model = TDBFactory.createDataset(tdbSchemaFolder).getDefaultModel();
		for (SourceFile file : turtleFiles) {
			try (InputStream labelsStream = FileManager.get().open(file.filePath)) {
				model.read(labelsStream, null, file.lang);
			}
		}
		return model;
	}

	public static class SourceFile {
		String filePath;
		String lang;

		public SourceFile(String filePath, String lang) {
			this.filePath = filePath;
			this.lang = lang;
		}
	}

}
