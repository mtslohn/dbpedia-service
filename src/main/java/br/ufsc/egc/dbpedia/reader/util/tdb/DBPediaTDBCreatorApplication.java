package br.ufsc.egc.dbpedia.reader.util.tdb;

import br.ufsc.egc.dbpedia.reader.conf.PropertyLoader;
import br.ufsc.egc.dbpedia.reader.conf.ServiceProperty;
import br.ufsc.egc.util.tdb.TDBCreator;
import br.ufsc.egc.util.tdb.TDBCreator.SourceFile;

import java.io.IOException;

public class DBPediaTDBCreatorApplication {

	public static void main(String[] args) throws IOException {
		PropertyLoader props = new PropertyLoader();

		new TDBCreator().createTDBSchema(
				props.getProperty(ServiceProperty.CATEGORIES_LABELS_FILE),
				new SourceFile(props.getProperty(ServiceProperty.CATEGORIES_SKOS_FILE), "TURTLE"),
				new SourceFile(props.getProperty(ServiceProperty.TDB_SCHEMA_FOLDER), "TURTLE")
		).close();
	}

}
