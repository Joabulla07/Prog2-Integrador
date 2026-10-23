package concesionaria.config;

import org.sql2o.Sql2o;


public class ConexionDB {
   public static final Sql2o sql2o = new Sql2o(Config.URL, Config.USER, Config.PASS);
}
