<?php

class importar2{
    const ESTADO_USUARIO_BLOQUEADO=11;
    const ESTADO_CREACION_EXITOSA = 1;
    const ESTADO_CREACION_FALLIDA = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_AUSENCIA_CLAVE_API = 4;
    const ESTADO_CLAVE_NO_AUTORIZADA = 5;
    const ESTADO_URL_INCORRECTA = 6;
    const ESTADO_FALLA_DESCONOCIDA = 7;
    const ESTADO_PARAMETROS_INCORRECTOS = 8;
    const ESTADO_EXITO=9;
    const ESTADO_ERROR=10;

    public static function post($peticion)
    {
        if ($peticion[0] == 'importarInventarioWS') {
            return self::importarInventarioWS();
        }else {
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
        }
    }

    public static function importarInventarioWS()
    {//Checar

        $json = json_decode(file_get_contents('php://input'));

        $comando = "INSERT INTO ms_inventario (idInventario,art_id,existenciaSolicitud,existenciaRespuesta,idUsuario,
                      fechaSolicitud,fechaRespuesta,existenciaEjecucion,idEstado,idInventarioExterno)
                      VALUES (0,:art_id,:existenciaSolicitud,:existenciaRespuesta,:idUsuario,:fechaSolicitud,
                      :fechaRespuesta,:existenciaEjecucion,:idEstado,:idInventarioExterno);";
        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        $contador = 0;
        foreach ($json->data as $jsonRow) {
            $idInventario = $jsonRow->idInventario;
            $idInventarioLocal = $jsonRow->idInventarioLocal;
            $idSucursal = $jsonRow->idSucursal;
            $art_id = $jsonRow->art_id;
            $existenciaSolicitud = self::obtenerExistencia($art_id);
            $existenciaRespuesta = $jsonRow->existenciaRespuesta;
            $idUsuario = $jsonRow->idUsuario;
            $fechaSolicitud = $jsonRow->fechaSolicitud;
            $fechaRespuesta = $jsonRow->fechaRespuesta;
            $existenciaEjecucion = $jsonRow->existenciaEjecucion;
            $idEstado = $jsonRow->idEstado;
            $sentencia->bindParam("art_id", $art_id);
            $sentencia->bindParam("existenciaSolicitud", $existenciaSolicitud);
            $sentencia->bindParam("existenciaRespuesta", $existenciaRespuesta);
            $sentencia->bindParam("idUsuario", $idUsuario);
            $sentencia->bindParam("fechaSolicitud", $fechaSolicitud);
            $sentencia->bindParam("fechaRespuesta", $fechaRespuesta);
            $sentencia->bindParam("existenciaEjecucion", $existenciaEjecucion);
            $sentencia->bindParam("idEstado", $idEstado);
            $sentencia->bindParam("idInventarioExterno",$idInventario);

            $sentencia->execute();
            $contador++;
        }
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
                throw new ExcepcionApi(self::ESTADO_ERROR, "No se han encontrado resultados",402);

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
        }
    }
}