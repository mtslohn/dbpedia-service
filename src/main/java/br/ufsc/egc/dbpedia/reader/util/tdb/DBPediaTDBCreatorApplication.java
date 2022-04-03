package br.ufsc.egc.dbpedia.reader.util.tdb;

import br.ufsc.egc.dbpedia.reader.conf.PropertyLoader;
import br.ufsc.egc.dbpedia.reader.conf.ServiceProperty;

import java.io.IOException;

public class DBPediaTDBCreatorApplication {

    public static void main(String[] args) throws IOException {
        PropertyLoader propertyLoader = new PropertyLoader();

        new TDBCreator().createTDBSchema(
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_LABELS_FILE),
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_SKOS_FILE),
                propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_FOLDER)
        ).close();
    }

}
