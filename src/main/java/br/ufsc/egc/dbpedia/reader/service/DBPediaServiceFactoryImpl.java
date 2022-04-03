package br.ufsc.egc.dbpedia.reader.service;

import br.ufsc.egc.dbpedia.reader.conf.PropertyLoader;
import br.ufsc.egc.dbpedia.reader.conf.ServiceProperty;

import java.io.IOException;

public class DBPediaServiceFactoryImpl implements DBPediaServiceFactory {

    @Override
    public DBPediaService buildFromProperties() throws IOException {
        PropertyLoader propertyLoader = new PropertyLoader();
        return build(propertyLoader.getProperty(ServiceProperty.TDB_SCHEMA_FOLDER),
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_LABELS_FILE),
                propertyLoader.getProperty(ServiceProperty.CATEGORIES_SKOS_FILE));
    }

    @Override
    public DBPediaService build(String tdbSchemaFolder, String categoriesLabelsFile, String categoriesSkosFile)
            throws IOException {
        return new DBPediaServiceImpl(tdbSchemaFolder, categoriesLabelsFile, categoriesSkosFile);
    }

}
