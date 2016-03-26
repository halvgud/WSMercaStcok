package mx.mercatto.designmercastock.web;

public class RespuestaApi {
    private int estado;
    private String mensaje;

    public RespuestaApi(int code, String body) {
        this.estado = code;
        this.mensaje = body;
    }

    public RespuestaApi() {

    }

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return "(" + getEstado() + "): " + getMensaje();
    }
}
