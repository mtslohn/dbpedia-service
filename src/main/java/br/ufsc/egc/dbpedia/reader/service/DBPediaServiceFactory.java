package br.ufsc.egc.dbpedia.reader.service;

import java.io.IOException;

public interface DBPediaServiceFactory {

    DBPediaService buildFromProperties() throws IOException;

    DBPediaService build(String tdbSchemaFolder, String categoriesLabelsFile, String categoriesSkosFile)
            throws IOException;
}
