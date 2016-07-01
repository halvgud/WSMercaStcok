<?php
class CATEGORIA
{
    const TABLA_INVENTARIO = 'ms_inventario';
    const TABLA_ARTICULO = 'articulo';
    const TABLA_CATEGORIA = 'categoria';
    const ID_INVENTARIO = 'idInvientario';
    const ID_ARTICULO = 'art_id';
    const ID_CATEGORIA = 'cat_id';
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;

    const DESCRIPCION='nombre';

    public static function post ($peticion)
    {
        if (empty($peticion[0]))
            return self::obtenerCategoria();
        else
            http_response_code(404);
    }

    public static function obtenerCategoria()
    {
        try {
            $post = json_decode(file_get_contents('php://input'), true);
            /*$comando = "select * from (SELECT a.cat_id, d.nombre,sum(case when existenciaRespuesta>0 then 1 else 0 end)
            as procesado,count(*) AS CANTIDAD FROM articulo A INNER JOIN ms_inventario MI ON
            ( MI.art_id=A.art_id) INNER JOIN categoria D ON ( D.cat_id=A.cat_id)
             inner join ms_usuario mu on (mu.claveApi=:claveApi AND mu.claveApi!='') group by d.nombre) tt where tt.procesado<tt.cantidad;";*/
            $comando = "SELECT * FROM (
                            SELECT a.cat_id, d.nombre,sum(CASE WHEN mi.idEstado IN('P','E') THEN 1 WHEN mi.idEstado='A' THEN 0 END)
                            AS procesado,sum(1) AS CANTIDAD
                            FROM articulo a
                            INNER JOIN ms_inventario mi ON (mi.art_id=a.art_id)
                            INNER JOIN categoria d ON (d.cat_id = a.cat_id)
                            INNER JOIN ms_usuario mu ON (mu.claveApi=:claveApi AND mu.claveApi!='')
                            GROUP BY d.nombre) tt WHERE tt.procesado<tt.cantidad;";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->bindParam("claveApi", $post['claveApi']);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll(PDO::FETCH_ASSOC);
            if ($resultado) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $resultado
                    ];
            } else {
                http_response_code(202);
                return
                    [
                        "estado" => "vacio",
                        "datos" => $resultado
                    ];
            }
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }
}