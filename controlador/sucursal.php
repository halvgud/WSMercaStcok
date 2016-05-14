<?php
class SUCURSAL
{
    const TABLA_SUCURSAL = 'ms_sucursal';
    const ID_TABLA = 'idSucursal';
    const DESC_TABLA = 'nombre';
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;


    public static function get($peticion)
    {


        if (empty($peticion[0]))
            return self::obtenerSucursal();
        else
            http_response_code(404);

    }

    public static function obtenerSucursal()
    {
        try {

                $comando = "SELECT ".self::ID_TABLA.",".self::DESC_TABLA." FROM " . self::TABLA_SUCURSAL ;

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idContacto e idUsuario
                //$sentencia->bindParam(1, $idTabla, PDO::PARAM_INT);
                //$sentencia->bindParam(2, $nombre, PDO::PARAM_INT);

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return (["estado" => self::ESTADO_EXITO,"datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)]);
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