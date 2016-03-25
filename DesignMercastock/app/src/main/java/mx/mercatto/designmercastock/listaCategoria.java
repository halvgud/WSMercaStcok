package mx.mercatto.designmercastock;

/**
 * Created by Juan Carlos De León on 24/03/2016.
 */
public class listaCategoria {

        private String id;
        private String name;

        public listaCategoria(String i, String n) {
            id = i;
            name = n;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }

}
