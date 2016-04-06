package mx.mercatto.designmercastock;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by Juan Carlos De León on 05/04/2016.
 */
public class Configuracion {
   public static void Inicializar(){
        BackGroundTask bgt;
        try {
            JSONObject config=new JSONObject();
            config.put("","");
            bgt = new BackGroundTask("http://192.168.1.17/wsMercaStock/parametro/accion/CONFIGURACION_TERMINAL", "POST",config);
            config=bgt.execute().get();

            if(config!= null) {
                JSONArray countries = config.getJSONArray(Configuracion.getDatos());

                // looping through All countries
                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);
                    setDatos(c.getString("parametro").equals("TAG_DATOS") ? c.getString("valor") : getDatos());
                    setIdLogin(c.getString("parametro").equals("TAG_ID_LOGIN") ? c.getString("valor") : getIdLogin());
                    setDescripcionLogin(c.getString("parametro").equals("TAG_DESCRIPCION_LOGIN") ? c.getString("valor") : getDescripcionLogin());
                    setApiUrlLogin(c.getString("parametro").equals("API_URL_LOGIN") ? c.getString("valor") : getApiUrlLogin());
                    setIdCategoria(c.getString("parametro").equals("TAG_ID_CATEGORIA") ? c.getString("valor") : getIdCategoria());
                    setDescripcionCategoria(c.getString("parametro").equals("TAG_DESCRIPCION_CATEGORIA") ? c.getString("valor") : getDescripcionCategoria());
                    setCantidadCategoria(c.getString("parametro").equals("TAG_CANTIDAD_CATEGORIA") ? c.getString("valor") : getCantidadCategoria());
                    setApiUrlCategoria(c.getString("parametro").equals("API_URL_CATEGORIA") ? c.getString("valor") : getApiUrlCategoria());
                    setIdArticulo(c.getString("parametro").equals("TAG_ID_ARTICULO") ? c.getString("valor") : getIdArticulo());
                    setDescripcioArticulo(c.getString("parametro").equals("TAG_DESCRIPCION_ARTICULO") ? c.getString("valor") : getDescripcioArticulo());
                    setUnidadArticulo(c.getString("parametro").equals("TAG_UNIDAD_ARTICULO") ? c.getString("valor") : getUnidadArticulo());
                    setExistenciaArticulo(c.getString("parametro").equals("TAG_EXISTENCIA_ARTICULO") ? c.getString("valor") : getExistenciaArticulo());
                    setIdInventarioArticulo(c.getString("parametro").equals("TAG_CANTIDAD_ARTICULO") ? c.getString("valor") : getIdInventarioArticulo());
                    setApiUrlArticulo(c.getString("parametro").equals("API_URL_ARTICULO") ? c.getString("valor") : getApiUrlArticulo());
                    setIdInventario(c.getString("parametro").equals("TAG_ID_INVENTARIO") ? c.getString("valor") : getIdInventario());
                    setValorInventario(c.getString("parametro").equals("TAG_VALOR_ID_INVENTARIO") ? c.getString("valor") : getValorInventario());
                    setApiUrlInventario(c.getString("parametro").equals("API_URL_INVENTARIO") ? c.getString("valor") : getApiUrlInventario());
                    setIdRegistro(c.getString("parametro").equals("TAG_ID_REGISTRO") ? c.getString("valor") : getIdRegistro());
                    setDescripcioRegistro(c.getString("parametro").equals("TAG_DESCRIPCION_REGISTRO") ? c.getString("valor") : getDescripcioRegistro());
                    setApiUrlRegistro(c.getString("parametro").equals("API_URL_REGISTRO") ? c.getString("valor") : getApiUrlRegistro());
                }
            }
        }catch (JSONException e){
        }catch (ExecutionException e){
        }catch (InterruptedException e){
        }
        //Mandas llamar funcion que se conecta a la bd mediante rest,
        //utilizar Backgroundtask para realizar esta funcion
        //setMostrarMensajeBienvenida(json.Object("MOSTRAR_MENSAJE"));

        //Generales
      /*  setDatos(json.Object("TAG_DATOS"));

        //Login
        setIdLogin(json.Object("TAG_ID_LOGIN"));
        setDescripcionLogin(json.Object("TAG_DESCRIPCION_LOGIN"));
        setApiUrlLogin(json.Object("API_URL_LOGIN"));
        //setTagApiLoginLogin(json.Object("MAP_API_LOGIN_LOGIN"));
        //setClaveApi(json.Object("CLAVE_API_LOGIN"));

        //Lista Departamentos
        setIdCategoria(json.Object("TAG_ID_CATEGORIA"));
        setDescripcionCategoria(json.Object("TAG_DESCRIPCION_CATEGORIA"));
        setCantidadCategoria(json.Object("TAG_CANTIDAD_CATEGORIA"));
        setApiUrlCategoria(json.Object("API_URL_CATEGORIA"));

        //Lista Artículo
        setIdArticulo(json.Object("TAG_ID_ARTICULO"));
        setDescripcioArticulo(json.Object("TAG_DESCRIPCION_ARTICULO"));
        setUnidadArticulo(json.Object("TAG_UNIDAD_ARTICULO"));
        setExistenciaArticulo(json.Object("TAG_EXISTENCIA_ARTICULO"));
        setIdInventarioArticulo(json.Object("TAG_CANTIDAD_ARTICULO"));
        setApiUrlArticulo(json.Object("API_URL_ARTICULO"));

        //Formulario Artículo
        setIdInventario(json.Object("TAG_ID_INVENTARIO"));
        setValorInventario(json.Object("TAG_VALOR_ID_INVENTARIO"));
        setApiUrlInventario(json.Object("API_URL_INVENTARIO"));

        //Registro
        setIdRegistro(json.Object("TAG_ID_REGISTRO"));
        setDescripcioRegistro(json.Object("TAG_DESCRIPCION_REGISTRO"));
        setApiUrlRegistro(json.Object("API_URL_REGISTRO"));
        //setTagMapApiLoginRegistro(json.Object("MAP_API_LOGIN_REGISTRO"));
*/
    }


    private static String _ValorVerdadero="TRUE";
    public static String getValorVerdadero() { return _MostrarMensajeBienvenida; }
    private static void set_ValorVerdadero(String ValorVerdadero){ _ValorVerdadero=ValorVerdadero; }

    private static String _MostrarMensajeBienvenida;
    public static String getMostrarMensajeBienvenida() { return _MostrarMensajeBienvenida; }
    private static void setMostrarMensajeBienvenida(String MostrarMensajeBienvenida) { _MostrarMensajeBienvenida = MostrarMensajeBienvenida; }

    private static String _Datos="datos";
    public static String getDatos(){ return _Datos;}
    private static void setDatos(String Datos) {_Datos=Datos;}

    private static String _IdLogin="idSucursal";
    public static String getIdLogin(){ return _IdLogin;}
    private static void setIdLogin(String IdLogin) {_IdLogin=IdLogin;}

    private static String _DescripcionLogin="nombre";
    public static String getDescripcionLogin(){ return _DescripcionLogin;}
    private static void setDescripcionLogin(String DescripcionLogin) {_DescripcionLogin=DescripcionLogin;}

    private static String _ApiUrlLogin="http://192.168.1.17/wsMercaStock/sucursal";
    public static String getApiUrlLogin(){ return _ApiUrlLogin;}
    private static void setApiUrlLogin(String ApiUrlLogin) {_ApiUrlLogin=ApiUrlLogin;}

    private static String _IdCategoria="cat_id";
    public static String getIdCategoria(){ return _IdCategoria;}
    private static void setIdCategoria(String IdCategoria) {_IdCategoria=IdCategoria;}

    private static String _DescripcionCategoria="nombre";
    public static String getDescripcionCategoria(){ return _DescripcionCategoria;}
    private static void setDescripcionCategoria(String DescripcionCategoria) {_DescripcionCategoria=DescripcionCategoria;}

    private static String _CantidadCategoria="CANTIDAD";
    public static String getCantidadCategoria(){ return _CantidadCategoria;}
    private static void setCantidadCategoria(String CantidadCategoria) {_CantidadCategoria=CantidadCategoria;}

    private static String _ApiUrlCategoria="http://192.168.1.17/wsMercaStock/categoria";
    public static String getApiUrlCategoria(){ return _ApiUrlCategoria;}
    private static void setApiUrlCategoria(String ApiUrlCategoria) {_ApiUrlCategoria=ApiUrlCategoria;}

    private static String _IdArticulo="cat_id";
    public static String getIdArticulo(){ return _IdArticulo;}
    private static void setIdArticulo(String IdArticulo) {_IdArticulo=IdArticulo;}

    private static String _DescripcioArticulo="NombreArticulo";
    public static String getDescripcioArticulo(){ return _DescripcioArticulo;}
    private static void setDescripcioArticulo(String DescripcioArticulo) {_DescripcioArticulo=DescripcioArticulo;}

    private static String _UnidadArticulo="Unidad";
    public static String getUnidadArticulo(){ return _UnidadArticulo;}
    private static void setUnidadArticulo(String UnidadArticulo) {_UnidadArticulo=UnidadArticulo;}

    private static String _ExistenciaArticulo="Existencia";
    public static String getExistenciaArticulo(){ return _ExistenciaArticulo;}
    private static void setExistenciaArticulo(String ExistenciaArticulo) {_ExistenciaArticulo=ExistenciaArticulo;}

    private static String _IdInventarioArticulo="idInventario";
    public static String getIdInventarioArticulo(){ return _IdInventarioArticulo;}
    private static void setIdInventarioArticulo(String IdInventarioArticulo) {_IdInventarioArticulo=IdInventarioArticulo;}

    private static String _ApiUrlArticulo="http://192.168.1.17/wsMercaStock/articulo/obtener";
    public static String getApiUrlArticulo(){ return _ApiUrlArticulo;}
    private static void setApiUrlArticulo(String ApiUrlArticulo) {_ApiUrlArticulo=ApiUrlArticulo;}

    private static String _IdInventario="idInventario";
    public static String getIdInventario(){ return _IdInventario;}
    private static void setIdInventario(String IdInventario) {_IdInventario=IdInventario;}

    private static String _ValorInventario="";
    public static String getValorInventario(){ return _ValorInventario;}
    private static void setValorInventario(String ValorInventario) {_ValorInventario=ValorInventario;}

    private static String _ApiUrlInventario="http://192.168.1.17/wsMercaStock/articulo/actualizar";
    public static String getApiUrlInventario(){ return _ApiUrlInventario;}
    private static void setApiUrlInventario(String ApiUrlInventario) {_ApiUrlInventario=ApiUrlInventario;}

    private static String _IdRegistro="idSucursal";
    public static String getIdRegistro(){ return _IdRegistro;}
    private static void setIdRegistro(String IdRegistro) {_IdRegistro=IdRegistro;}

    private static String _DescripcioRegistro="nombre";
    public static String getDescripcioRegistro(){ return _DescripcioRegistro;}
    private static void setDescripcioRegistro(String DescripcioRegistro) {_DescripcioRegistro=DescripcioRegistro;}

    private static String _ApiUrlRegistro="http://192.168.1.17/wsMercaStock/usuario/registro";
    public static String getApiUrlRegistro(){ return _ApiUrlRegistro;}
    private static void setApiUrlRegistro(String TagApiUrlRegistro) {_ApiUrlRegistro=TagApiUrlRegistro;}


}
