<?php
class ARTICULO
{
    const TABLA_INVENTARIO = 'ms_inventario';
    const TABLA_ARTICULO = 'articulo';
    const TABLA_CATEGORIA = 'categoria';
    const ID_INVENTARIO = 'idInvientario';
    const ID_ARTICULO = 'art_id';
    const ID_CATEGORIA = 'cat_id';
    const SERVICIO='servicio';
    const EXISTENCIA='existencia';
    const UNIDAD='nombre';
    const ID_ESTADO = 'idEstado';
    const TABLA_PARAMETRO = 'ms_parametro';
    const PARAMETRO_FILTRO = 'FILTRO';
    const ACCION_FILTRO = 'CONFIGURACION_ARTICULO';
    const VALOR_FILTRO = 'valor';
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;
    const ESTADO_URL_INCORRECTA=105;
    const ESTADO_FALLA_DESCONOCIDA = 7;
    const DESCRIPCION='descripcion';

    public static function post($peticion)
    {
        if ($peticion[0] == 'obtener') {
            return self::obtenerArticulo();
        }
        else if($peticion[0]=='actualizar'){
            return self::cambiarEstado();
        }else if ($peticion[0] == 'impuesto') {
            return self::seleccionar();
        }
        else{
            return self::obtenerArticulo();
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
        }
    }

    public static function obtenerArticulo()
    {
        try {
            $post = json_decode(file_get_contents('php://input'),true);//ID_CATEGORIA
            if(isset($post['cat_id'])&&isset($post['claveApi'])){
                $comando = "SELECT a.art_id,mi.idInventario, a.cat_id, a.descripcion AS NombreArticulo, a.existencia
                 AS Existencia, U.nombre AS Unidad, MI.idEstado,a.granel,a.clave
                 FROM articulo A INNER JOIN ms_inventario MI ON ( MI.art_id=A.art_id)
                 inner join ms_usuario mu on (mu.claveApi=:claveApi AND mu.claveApi!='')
                 INNER JOIN categoria D ON ( D.cat_id=A.cat_id)
                INNER JOIN Unidad U ON (a.UnidadCompra=U.Uni_ID)
                 WHERE a.cat_id=:cat_id AND a.servicio=0
                AND MI.idEstado IN (SELECT valor
                FROM ms_parametro WHERE ACCION='CONFIGURACION_ARTICULO' AND
                PARAMETRO='FILTRO')";
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->bindParam("claveApi",$post['claveApi']);
                $sentencia->bindParam("cat_id",$post['cat_id']);
            $sentencia->execute();
            $resultado=$sentencia->fetchAll(PDO::FETCH_ASSOC);
            if ($resultado>0) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $resultado
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");
            }
            else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Faltan parámetros por recibir");
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }
    public static function cambiarEstado()
    {
        ConexionBD::obtenerInstancia()->_destructor();
        try {
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $post = json_decode(file_get_contents('php://input'),true);//ID_CATEGORIA
            $claveApi2=$post['claveApi2'];
            $comando = "UPDATE ".self::TABLA_INVENTARIO." SET idEstado =(SELECT distinct VALOR FROM MS_PARAMETRO WHERE PARAMETRO='ID_ESTADO_PROCESADO'),
                fechaRespuesta=NOW(), existenciaRespuesta='".$post['existenciaRespuesta']."' ,
                existenciaEjecucion=(SELECT EXISTENCIA FROM ARTICULO WHERE art_id='".$post['art_id']."')
                WHERE idInventario='".$post['idInventario']."'";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            if(!usuario::apiregistro($claveApi2)==null){
               if ($sentencia->execute()) {
                   http_response_code(200);
                    ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                    return
                        [
                            "estado" => self::ESTADO_EXITO,
                            "datos" => $sentencia->rowCount()
                        ];
                } else{
                   ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");
                    }

                }
                else{
                    ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                     throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                        "Clave Api invalida",401);
                }

        } catch (PDOException $e) {
            ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }
    public static function seleccionar()
    {
        try {
            $comando = "SELECT art_id,imp_id FROM articuloimpuesto limit 500;";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }
}