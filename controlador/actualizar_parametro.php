<?php
class ACTUALIZAR_PARAMETRO
{
    const TABLA_PARAMETRO = 'ms_parametro';
    const VALOR = 'valor';
    
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;
    const ESTADO_URL_INCORRECTA=105;

    public static function post($peticion){
            if ($peticion[0] == 'parametro') {
                                return self::cambiarEstado($peticion[1]);
            }
            else {
                throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
              
        }
    }

    public static function cambiarEstado($parametro)
    {
        try {
            $post = json_decode(file_get_contents('php://input'),true);
            //return var_dump($post);
                $comando = "UPDATE ".self::TABLA_PARAMETRO." SET ".self::VALOR." ='".$post['valor']."' WHERE parametro='".$parametro."'";
                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->rowCount()
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

}