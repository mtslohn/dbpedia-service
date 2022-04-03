package br.ufsc.egc.dbpedia.reader;

import br.ufsc.egc.dbpedia.reader.server.RmiServer;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceImpl;

public class DBPediaReaderApplication {

    public static void main(String[] args) {
        DBPediaService service = new DBPediaServiceImpl();
        RmiServer rmiServer = new RmiServer(service, service.getClass().getSimpleName());
        rmiServer.start();
    }

}
