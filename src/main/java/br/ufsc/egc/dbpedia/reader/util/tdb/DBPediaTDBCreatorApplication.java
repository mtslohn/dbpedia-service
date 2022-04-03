package br.ufsc.egc.dbpedia.reader.util.tdb;

import br.ufsc.egc.dbpedia.reader.conf.PropertyLoader;
import br.ufsc.egc.dbpedia.reader.conf.ServiceProperty;

public class DBPediaTDBCreatorApplication {

    public static void main(String[] args) {
        PropertyLoader propertyLoader = new PropertyLoader();

        new DBPediaTDBCreator().createTDBSchema(
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_LABELS_FILE),
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_SKOS_FILE),
                propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_PATH)
        ).close();
    }

}
