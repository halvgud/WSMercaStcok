<?php
/**
 * User: Edwin
 * Date: 2016-05-25
 * Time: 9:20
 */
class importar{
    public static function traducirMensaje($codigoDeError,$e)
    {

        if ($codigoDeError == "23000") {
            return "El usuario que intentó registrar ya existe, favor de validar la información";
        } else if ($codigoDeError == "HY093") {
            return 'El número de parámetros enviados es incorrecto, favor de contactar a Sistemas';
        } else if ($codigoDeError == '42S02') {
            return "Tabla inexistente, favor de contactar a Sistemas";
        } else {
            return $e->getMessage();
        }
    }
    public static function post($peticion)
    {
        if ($peticion[0] == 'parametro') {
            return self::parametro();
        } else if ($peticion[0] == 'usuario') {
            return self::usuario();
        } else if ($peticion[0] == 'inventario') {
            return self::inventario();
        }else if ($peticion[0]=='ajuste'){
            return self::Ajuste();
        }
        else {
            throw new ExcepcionApi(404, "Url mal formada", 404);
        }
    }



    public static function usuario()
    {
        ConexionBD::obtenerInstancia()->_destructor();
        $usuario = json_decode(file_get_contents('php://input'));
        // Preparar operaci�n de modificaci�n para cada contacto

        $comando = "insert into ms_usuario (idUsuario,usuario,contrasena,nombre,apellido,sexo,contacto,idSucursal,claveApi,idNivelAutorizacion,idEstado,fechaEstado,fechaSesion
      ,claveGCM) values (:idUsuario,:usuario,:password,:nombre,:apellido,:sexo,:contacto,:idSucursal,:claveApi,:idNivelAutorizacion,:idEstado,now(),now(),'')
        on duplicate key update nombre=:nombre,apellido=:apellido,sexo=:sexo,contacto=:contacto,
        idSucursal=:idSucursal,idNivelAutorizacion=:idNivelAutorizacion,idEstado=:idEstado";
        // Preparar la sentencia update
        ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);




        // Procesar array de contactos
        $contador=0;
        foreach ($usuario->data as $usuarioRow) {
            $idusuario =$usuarioRow->idUsuario;
            $usuario =$usuarioRow->usuario;
            $password = $usuarioRow->password;
            $nombre = $usuarioRow->nombre;
            $apellido = $usuarioRow->apellido;
            $sexo = $usuarioRow->sexo;
            $contacto = $usuarioRow->contacto;
            $idNivelAutorizacion=$usuarioRow->idNivelAutorizacion;
            $idEstado = $usuarioRow->idEstado;
            $idSucursal =$usuarioRow->idSucursal;
            $claveApi="";
            // $claveGCM = $usuarioRow->claveGCM;
            $sentencia->bindParam("password", $password);
            $sentencia->bindParam("nombre", $nombre);
            $sentencia->bindParam("apellido", $apellido);
            $sentencia->bindParam("sexo", $sexo);
            $sentencia->bindParam("claveApi",$claveApi);
            $sentencia->bindParam("contacto", $contacto);
            $sentencia->bindParam("idNivelAutorizacion", $idNivelAutorizacion);
            $sentencia->bindParam("idEstado", $idEstado);
            $sentencia->bindParam("idUsuario",$idusuario);
            $sentencia->bindParam("usuario",$usuario);
            $sentencia->bindParam("idSucursal",$idSucursal);
            $sentencia->execute();
            $contador++;
        }
        return   $arreglo =[
            "estado" => 200,
            "success" => "",
            "data" => $contador
        ];
        ConexionBD::obtenerInstancia()->obtenerBD()->commit();
    }

    public static function parametro(){
        $postrequest = json_decode(file_get_contents('php://input'));
        ConexionBD::obtenerInstancia()->_destructor();
        $comando = "insert into ms_parametro (accion, parametro, valor, comentario, usuario, fechaActualizacion) values (:accion, :parametro, :valor, :comentario, :usuario, :fechaActualizacion)
        on duplicate key update accion=:accion, parametro=:parametro, valor=:valor, comentario=:comentario, usuario=:usuario, fechaActualizacion=:fechaActualizacion";

        try{
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $contador=0;
            if(isset($postrequest->data)){
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                foreach ($postrequest->data as $post) {
                    $sentencia->bindParam("accion", $post->accion);
                    $sentencia->bindParam("parametro", $post->parametro);
                    $sentencia->bindParam("valor", $post->valor);
                    $sentencia->bindParam("comentario", $post->comentario);
                    $sentencia->bindParam("usuario", $post->usuario);
                    $sentencia->bindParam("fechaActualizacion", $post->fechaActualizacion);
                    $resultado = $sentencia -> execute();
                    $contador++;
                }

                if($resultado){
                    http_response_code(200);
                    ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                    return
                        [
                            "estado" => 200,
                            "success" => "",
                            "data" => $resultado
                        ];

                } else {
                    http_response_code(400);
                    ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                    return
                        [
                            "estado" => "warning",
                            "mensaje" => "",
                            "data" => $resultado
                        ];

                }//else
            }else {
                ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                throw new ExcepcionApi(401, "parametro data no recibido", 401);
            }
        }catch(PDOException $e){
            ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
            $codigoDeError=$e->getCode();
            $error =self::traducirMensaje($codigoDeError,$e);
            throw new ExcepcionApi($e->getCode(), $error, 401);
        }
    }
    public static function inventario()
    {//Checar
        ConexionBD::obtenerInstancia()->_destructor();
        $json = json_decode(file_get_contents('php://input'));

        $comando = "INSERT INTO ms_inventario (idInventario,art_id,existenciaSolicitud,existenciaRespuesta,idUsuario,
                      fechaSolicitud,fechaRespuesta,existenciaEjecucion,idEstado)
                      VALUES (:idInventario,:art_id,:existenciaSolicitud,:existenciaRespuesta,:idUsuario,now(),
                      :fechaRespuesta,:existenciaEjecucion,:idEstado)
                      on duplicate key update art_id=:art_id;";
        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        $contador = 0;
        ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
        foreach ($json->data as $jsonRow) {
            $idInventario = $jsonRow->idInventario;
            $art_id = $jsonRow->art_id;
            $existenciaSolicitud = self::obtenerExistencia($art_id);
            $existenciaRespuesta = $jsonRow->existenciaRespuesta;
            $idUsuario = $jsonRow->idUsuario;
            //$fechaSolicitud = $jsonRow->fechaSolicitud;
            $fechaRespuesta = $jsonRow->fechaRespuesta;
            $existenciaEjecucion = $jsonRow->existenciaEjecucion;
            $idEstado = $jsonRow->idEstado;
            $sentencia->bindParam("art_id", $art_id);
            $sentencia->bindParam("existenciaSolicitud", $existenciaSolicitud);
            $sentencia->bindParam("existenciaRespuesta", $existenciaRespuesta);
            $sentencia->bindParam("idUsuario", $idUsuario);
            //$sentencia->bindParam("fechaSolicitud", $fechaSolicitud);
            $sentencia->bindParam("fechaRespuesta", $fechaRespuesta);
            $sentencia->bindParam("existenciaEjecucion", $existenciaEjecucion);
            $sentencia->bindParam("idEstado", $idEstado);
            $sentencia->bindParam("idInventario",$idInventario);

            $sentencia->execute();
            $contador++;
        }
        ConexionBD::obtenerInstancia()->obtenerBD()->commit();
        return $arreglo = [
            "estado" => 200,
            "success" => "",
            "data" => $contador
        ];
    }
    public static function obtenerExistencia($art_id)
    {
        $comando = "SELECT existencia FROM articulo WHERE art_id=:art_id";
        try {
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->bindParam("art_id",$art_id);
            $sentencia->execute();
            $resultado=$sentencia->fetchAll(PDO::FETCH_OBJ);
            if ($resultado) {
                return
                    $resultado[0]->existencia
                    ;
            } else
                throw new ExcepcionApi(402, "No se han encontrado resultados",402);

        } catch (PDOException $e) {
            throw new ExcepcionApi(402, $e->getMessage(), 402);
        }
    }
    public static function Ajuste(){
        $postrequest = json_decode(file_get_contents('php://input'));
        ConexionBD::obtenerInstancia()->_destructor();
        $resultado=false;
        $comando = "insert into ms_ajuste(idAjuste,
                                          idInventario,
                                          fecha,
                                          idUsuario,
                                          clave,
                                          precioCompra,
                                          factor,
                                          cantidadAnterior,
                                          cantidadAjuste,
                                          idEstado)
                                          values(
                                          :idAjuste,
                                          :idInventario,
                                          now(),
                                          :idUsuario,
                                          :clave,
                                          :precioCompra,
                                          :factor,
                                          :cantidadAnterior,
                                          :cantidadAjuste,
                                          :idEstado
                                          )";

        try{
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $contador=0;
            if(isset($postrequest->data)){
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                foreach ($postrequest->data as $post) {
                    $sentencia->bindParam("idAjuste", $post->idAjuste);
                    $sentencia->bindParam("idInventario", $post->idInventario);
                    $sentencia->bindParam("idUsuario", $post->idUsuario);
                    $sentencia->bindParam("clave", $post->clave);
                    $sentencia->bindParam("precioCompra", $post->precioCompra);
                    $sentencia->bindParam("factor", $post->factor);
                    $sentencia->bindParam("cantidadAnterior", $post->cantidadAnterior);
                    $sentencia->bindParam("cantidadAjuste", $post->cantidadAjuste);
                    $sentencia->bindParam("idEstado", $post->idEstado);
                    $resultado = $sentencia -> execute();
                    $contador++;
                }
                if($resultado){
                    http_response_code(200);
                    ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                    return
                        [
                            "estado" => 200,
                            "success" => "",
                            "data" => $contador
                        ];

                } else {
                    http_response_code(202);
                    ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                    return
                        [
                            "estado" => "warning",
                            "mensaje" => "",
                            "data" => $resultado
                        ];

                }//else
            }else {
                ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                throw new ExcepcionApi(401, "parametro data no recibido", 401);
            }
        }catch(PDOException $e){
            ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
            $codigoDeError=$e->getCode();
            $error =self::traducirMensaje($codigoDeError,$e);
            throw new ExcepcionApi($e->getCode(), $error, 401);
        }
    }
}