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
                $comando = "SELECT a.art_id,mi.idInventario, a.".self::ID_CATEGORIA.", a.".self::DESCRIPCION. " AS NombreArticulo, a.".self::EXISTENCIA." AS Existencia, U.".self::UNIDAD." AS Unidad, MI.".self::ID_ESTADO.",a.granel,a.clave FROM " . self::TABLA_ARTICULO . " A INNER JOIN ".self::TABLA_INVENTARIO."
                MI ON ( MI.".self::ID_ARTICULO."=A.".self::ID_ARTICULO.") inner join ms_usuario mu on (mu.claveApi='". $post['claveApi']."' AND mu.claveApi!='') INNER JOIN ".self::TABLA_CATEGORIA." D ON ( D.".self::ID_CATEGORIA."=A.".self::ID_CATEGORIA.")
                INNER JOIN Unidad U ON (a.UnidadCompra=U.Uni_ID) WHERE a.".self::ID_CATEGORIA."=".$post['cat_id']." AND a.".self::SERVICIO."=0 AND ".self::EXISTENCIA." >0
                AND MI.".self::ID_ESTADO." IN (SELECT ".self::VALOR_FILTRO." 
                FROM ".self::TABLA_PARAMETRO." WHERE ACCION='".self::ACCION_FILTRO."' AND
                PARAMETRO='".self::PARAMETRO_FILTRO."')";
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);


            $sentencia->execute();
            $resultado=$sentencia->fetchAll(PDO::FETCH_ASSOC);
            // Ejecutar sentencia preparada
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
        try {
            $post = json_decode(file_get_contents('php://input'),true);//ID_CATEGORIA

            $claveApi2=$post['claveApi2'];
                $comando = "UPDATE ".self::TABLA_INVENTARIO." SET idEstado =(SELECT distinct VALOR FROM MS_PARAMETRO WHERE PARAMETRO='ID_ESTADO_PROCESADO'),
                fechaRespuesta=NOW(), existenciaRespuesta='".$post['existenciaRespuesta']."' ,
                existenciaEjecucion=(SELECT EXISTENCIA FROM ARTICULO WHERE art_id='".$post['art_id']."')
                WHERE idInventario='".$post['idInventario']."'";
                // Preparar sentencia
                //return $comando;
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idContacto e idUsuario<
                //$sentencia->bindParam(1, $idTabla, PDO::PARAM_INT);a
                //$sentencia->bindParam(2, $nombre, PDO::PARAM_INT);
                //for ($i = 0 ;$i<100000000;$i++){


                //}

            // Ejecutar sentencia preparada
            if(!usuario::apiregistro($claveApi2)==null){
               if ($sentencia->execute()) {
                    http_response_code(200);
                    return
                        [
                            "estado" => self::ESTADO_EXITO,
                            "datos" => $sentencia->rowCount()
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

                    if ($resultado) {
                        return self::ESTADO_CREACION_EXITOSA;
                    } else {
                        return self::ESTADO_CREACION_FALLIDA;
                    }//cec15f7a58ba7eca2dd9cdfe52910b8f
                }
                else{
                     throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                        "Clave Api invalida",401);
                }

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }

}