package mx.mercatto.mercastock;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import mx.mercatto.mercastock.BGT.BackGroundTask;

/**
 * Created by Juan Carlos De León on 05/04/2016.
 */
public class Configuracion {
    public static void Inicializar(Activity activity){
        BackGroundTask bgt;
        try {
            JSONObject config = new JSONObject();
            config.put("", "");
            bgt = new BackGroundTask("http://192.168.0.39/wsMercaStock/parametro/accion/CONFIGURACION_TERMINAL", "POST", config, activity, 5);
            bgt.execute();

        }catch (JSONException e){

        }

    }
    public static Boolean Finalizado=false;
    private static String _ApiUrl="http://192.168.0.39/";
    public  static String getApiUrl(){ return  _ApiUrl;}
    public  static  void setApiUrl(String  ApiUrl){_ApiUrl=ApiUrl;}

    private static String _ProcesadoCategoria="procesado";
    public static String getProcesadoCategoria(){return _ProcesadoCategoria;}
    public static void setProcesadoCategoria(String ProcesadoCategoria){_ProcesadoCategoria=ProcesadoCategoria;}

    private static String _FlagBloqueoPorIntentos="TRUE";
    public static String getFlagBloqueoPorIntentos(){return _FlagBloqueoPorIntentos;}
    public static void setFlagBloqueoPorIntentos(String FlagBloqueoPorIntentos){_FlagBloqueoPorIntentos=FlagBloqueoPorIntentos;}
    private  static String _FlagBloqueoCantidad="3";
    public static  String getFlagBloqueoCantidad(){return  _FlagBloqueoCantidad;}
    public  static void  setFlagBloqueoCantidad(String FlagBloqueoCantidad){_FlagBloqueoCantidad=FlagBloqueoCantidad;}

    private static String _FlagBloqueoTiempo="2";
    public static String getFlagBloqueoTiempo(){return  _FlagBloqueoTiempo;}
    public static void setFlagBloqueoTiempo(String FlagBloqueoTiempo){_FlagBloqueoTiempo=FlagBloqueoTiempo;}

    private static String _idSucursal="idSucursal";
    public static String getIdSucursal(){return  _idSucursal;}
    public static void setidSucursal(String idSucursal){_idSucursal=idSucursal;}

    private static String _DescripcionSucursal="nombre";
    public static String getDescripcionSucursal(){return  _DescripcionSucursal;}
    public static void setDescripcionSucursal(String DescripcionSucursal){_DescripcionSucursal=DescripcionSucursal;}



    private static String _ApiUrlBloqueo="wsMercaStock/usuario/bloqueo";
    public static  String getApiUrlBloqueo(){return ((_ApiUrlBloqueo.contains("http://")?_ApiUrlBloqueo:getApiUrl()+_ApiUrlBloqueo));}
    public static void setApiUrlBloqueo(String ApiUrlBloqueo){_ApiUrlBloqueo=ApiUrlBloqueo;}


    private static String _ValorVerdadero="TRUE";
    public static String getValorVerdadero() { return _MostrarMensajeBienvenida; }
    public static void set_ValorVerdadero(String ValorVerdadero){ _ValorVerdadero=ValorVerdadero; }

    private static String _MostrarMensajeBienvenida;
    public static String getMostrarMensajeBienvenida() { return _MostrarMensajeBienvenida; }
    public static void setMostrarMensajeBienvenida(String MostrarMensajeBienvenida) { _MostrarMensajeBienvenida = MostrarMensajeBienvenida; }

    private static String _Datos="datos";
    public static String getDatos(){ return _Datos;}
    public static void setDatos(String Datos) {_Datos=Datos;}

    private static String _IdLogin="idSucursal";
    public static String getIdLogin(){ return _IdLogin;}
    public static void setIdLogin(String IdLogin) {_IdLogin=IdLogin;}

    private static String _DescripcionLogin="nombre";
    public static String getDescripcionLogin(){ return _DescripcionLogin;}
    public static void setDescripcionLogin(String DescripcionLogin) {_DescripcionLogin=DescripcionLogin;}

    private static String _ApiUrlSucursal ="wsMercaStock/sucursal";
    public static String getApiUrlSucursal(){return ((_ApiUrlSucursal.contains("http://")? _ApiUrlSucursal : getApiUrl()+ _ApiUrlSucursal));}
    public static void setApiUrlSucursal(String ApiUrlSucursal) {
        _ApiUrlSucursal =ApiUrlSucursal;}

    private static String _ApiUrlLogIn ="wsMercaStock/usuario";
    public static String getApiUrlLogIn(){return ((_ApiUrlLogIn.contains("http://")? _ApiUrlLogIn :getApiUrl()+ _ApiUrlLogIn));}
    public static void setApiUrlLogIn(String ApiUrlLogIn) {
        _ApiUrlLogIn =ApiUrlLogIn;}

    private static String _IdCategoria="cat_id";
    public static String getIdCategoria(){ return _IdCategoria;}
    public static void setIdCategoria(String IdCategoria) {_IdCategoria=IdCategoria;}

    private static String _DescripcionCategoria="nombre";
    public static String getDescripcionCategoria(){ return _DescripcionCategoria;}
    public static void setDescripcionCategoria(String DescripcionCategoria) {_DescripcionCategoria=DescripcionCategoria;}

    private static String _CantidadCategoria="cantidad";
    public static String getCantidadCategoria(){ return _CantidadCategoria;}
    public static void setCantidadCategoria(String CantidadCategoria) {_CantidadCategoria=CantidadCategoria;}

    private static String _ApiUrlCategoria="wsMercaStock/categoria";
    public static String getApiUrlCategoria(){return ((_ApiUrlCategoria.contains("http://")?_ApiUrlCategoria:getApiUrl()+_ApiUrlCategoria));}
    public static void setApiUrlCategoria(String ApiUrlCategoria) {_ApiUrlCategoria=ApiUrlCategoria;}

    private static String _IdArticulo="cat_id";
    public static String getIdArticulo(){ return _IdArticulo;}
    public static void setIdArticulo(String IdArticulo) {_IdArticulo=IdArticulo;}

    private static String _DescripcioArticulo="NombreArticulo";
    public static String getDescripcioArticulo(){ return _DescripcioArticulo;}
    public static void setDescripcioArticulo(String DescripcioArticulo) {_DescripcioArticulo=DescripcioArticulo;}

    private static String _UnidadArticulo="Unidad";
    public static String getUnidadArticulo(){ return _UnidadArticulo;}
    public static void setUnidadArticulo(String UnidadArticulo) {_UnidadArticulo=UnidadArticulo;}

    private static String _ExistenciaArticulo="Existencia";
    public static String getExistenciaArticulo(){ return _ExistenciaArticulo;}
    public static void setExistenciaArticulo(String ExistenciaArticulo) {_ExistenciaArticulo=ExistenciaArticulo;}

    private static String _IdInventarioArticulo="idInventario";
    public static String getIdInventarioArticulo(){ return _IdInventarioArticulo;}
    public static void setIdInventarioArticulo(String IdInventarioArticulo) {_IdInventarioArticulo=IdInventarioArticulo;}

    private static String _ApiUrlArticulo="wsMercaStock/articulo/obtener";
    public static String getApiUrlArticulo(){return ((_ApiUrlArticulo.contains("http://")?_ApiUrlArticulo:getApiUrl()+_ApiUrlArticulo));}
    public static void setApiUrlArticulo(String ApiUrlArticulo) {_ApiUrlArticulo=ApiUrlArticulo;}

    private static String _IdInventario="idInventario";
    public static String getIdInventario(){ return _IdInventario;}
    public static void setIdInventario(String IdInventario) {_IdInventario=IdInventario;}

    private static String _ValorInventario="";
    public static String getValorInventario(){ return _ValorInventario;}
    public static void setValorInventario(String ValorInventario) {_ValorInventario=ValorInventario;}

    private static String _ApiUrlInventario="wsMercaStock/articulo/actualizar";
    public static String getApiUrlInventario(){return ((_ApiUrlInventario.contains("http://")?_ApiUrlInventario:getApiUrl()+_ApiUrlInventario));}
    public static void setApiUrlInventario(String ApiUrlInventario) {_ApiUrlInventario=ApiUrlInventario;}

    private static String _IdRegistro="idSucursal";
    public static String getIdRegistro(){ return _IdRegistro;}
    public static void setIdRegistro(String IdRegistro) {_IdRegistro=IdRegistro;}

    private static String _DescripcioRegistro="nombre";
    public static String getDescripcionRegistro(){ return _DescripcioRegistro;}
    public static void setDescripcionRegistro(String DescripcioRegistro) {_DescripcioRegistro=DescripcioRegistro;}

    private static String _ApiUrlRegistro="wsMercaStock/usuario/registro";
    public static String getApiUrlRegistro(){return ((_ApiUrlRegistro.contains("http://")?_ApiUrlRegistro:getApiUrl()+_ApiUrlRegistro));}
    public static void setApiUrlRegistro(String ApiUrlRegistro) {_ApiUrlRegistro=ApiUrlRegistro;}

    private static String _Confirmacion_Mensaje_Gurdado="TRUE";
    public static  String getConfirmacion_Mensaje_Gurdado() {return _Confirmacion_Mensaje_Gurdado;}
    public static void setConfirmacion_Mensaje_Gurdado(String Confirmacion_Mensaje_Gurdado) {_Confirmacion_Mensaje_Gurdado =Confirmacion_Mensaje_Gurdado;}

    private static String _Confirmacion_Habilitar_Decimales="TRUE";
    public  static  String getConfirmacion_Habilitar_Decimales() {return  _Confirmacion_Habilitar_Decimales;}
    public static  void setConfirmacion_Habilitar_Decimales(String Confirmacion_Habilitar_Decimales) {_Confirmacion_Habilitar_Decimales=Confirmacion_Habilitar_Decimales;}


    private static String _GranelArticulo="granel";
    public static String getGranelArticulo(){return  _GranelArticulo;}
    public static void setGranelArticulo(String GranelArticulo){_GranelArticulo=GranelArticulo;}

    private static String _ClaveArticulo="clave";
    public static String getClaveArticulo (){return  _ClaveArticulo;}
    public  static  void  setClaveArticulo(String ClaveArticulo){_ClaveArticulo=ClaveArticulo;}

    private static String _ApiUrlPin="wsMercaStock/usuario/cambiar_pin";
    public static String getApiUrlPin(){return ((_ApiUrlPin.contains("http://")?_ApiUrlPin:getApiUrl()+_ApiUrlPin));}
    public static void setApiUrlPin(String ApiUrlPin) {_ApiUrlPin=ApiUrlPin;}

    public  static  SharedPreferences settings; //= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    public  static  SharedPreferences.Editor editor;// = settings.edit();
}
