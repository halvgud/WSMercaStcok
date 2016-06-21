<?php
class ACTUALIZAR
{
    const TABLA_INVENTARIO = 'ms_inventario';
    const ID_ARTICULO = 'art_id';
    const ID_ESTADO = 'idEstado';
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;
    const ESTADO_URL_INCORRECTA=105;
    const DESCRIPCION='descripcion';

    public static function post($peticion)
    {
        if ($peticion != '') {
            return self::cambiarEstado();
        }
        else {
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
        }
    }

    public static function cambiarEstado()
    {
        ConexionBD::obtenerInstancia()->_destructor();
        try {
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $post = json_decode(file_get_contents('php://input'),true);//ID_CATEGORIA
            $comando = "UPDATE ".self::TABLA_INVENTARIO." SET idEstado ='X
            ' WHERE idInventario='".$post['idInventario']."'";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            if ($sentencia->execute()) {
                http_response_code(200);
                ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->rowCount()
                    ];
            } else {
                ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");
            }
        } catch (PDOException $e) {
            ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }
}