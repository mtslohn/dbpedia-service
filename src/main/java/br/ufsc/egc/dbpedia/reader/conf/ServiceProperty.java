package br.ufsc.egc.dbpedia.reader.conf;

public enum ServiceProperty {

    CATEGORIES_LABELS_FILE("file.categories.labels"),
    CATEGORIES_SKOS_FILE("file.categories.skos"),
    TDB_SCHEMA_FOLDER("tdb.schema.path");

    final String propertyName;

    ServiceProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
