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
            return self::obtenerArticulo();
        }
        else {
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
        }
    }

    public static function obtenerArticulo()
    {
        try {
            $post = json_decode(file_get_contents('php://input'),true);
                $comando = "SELECT a.".self::ID_ARTICULO.", a.".self::ID_CATEGORIA.", a.".self::DESCRIPCION." AS NombreArticulo, U.".self::UNIDAD." AS Unidad  FROM " . self::TABLA_ARTICULO . " A INNER JOIN ".self::TABLA_INVENTARIO."
                MI ON ( MI.".self::ID_ARTICULO."=A.".self::ID_ARTICULO.") INNER JOIN ".self::TABLA_CATEGORIA." D ON ( D.".self::ID_CATEGORIA."=A.".self::ID_CATEGORIA.")
                INNER JOIN Unidad U ON (a.UnidadCompra=U.Uni_ID) WHERE a.".self::ID_CATEGORIA."=".$post['cat_id']." AND a.".self::SERVICIO."=0 AND ".self::EXISTENCIA." >0" ;

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idContacto e idUsuario<
                //$sentencia->bindParam(1, $idTabla, PDO::PARAM_INT);
                //$sentencia->bindParam(2, $nombre, PDO::PARAM_INT);
                //for ($i = 0 ;$i<100000000;$i++){
                    
                    
                //}
                
            // Ejecutar sentencia preparada
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
    }

}