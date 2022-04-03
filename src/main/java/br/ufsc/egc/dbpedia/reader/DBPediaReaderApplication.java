package br.ufsc.egc.dbpedia.reader;

import br.ufsc.egc.dbpedia.reader.server.RmiServer;
import br.ufsc.egc.dbpedia.reader.service.DBPediaService;
import br.ufsc.egc.dbpedia.reader.service.DBPediaServiceFactoryImpl;

import java.io.IOException;

public class DBPediaReaderApplication {

    public static void main(String[] args) throws IOException {
        DBPediaService service = new DBPediaServiceFactoryImpl().buildFromProperties();
        RmiServer rmiServer = new RmiServer(service, service.getClass().getSimpleName());
        rmiServer.register();
    }

}
