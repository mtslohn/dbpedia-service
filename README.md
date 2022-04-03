# DBpedia-service

This project allows querying terms in DBPedia categories to analyze which are the broader and the narrower categories
associated with them.

It's used inside https://github.com/mtslohn/taxonomy-extractor

## Requirements

* Java 8;
* The following turtle files from DBPedia (switch \<language> with the desired language):
    * category_labels_\<language>.ttl
    * skos_categories_\<language>.ttl

## Where do I get the resource files?

https://downloads.dbpedia.org/current/core-i18n/

For instance, this is for portuguese language:
https://downloads.dbpedia.org/current/core-i18n/pt/