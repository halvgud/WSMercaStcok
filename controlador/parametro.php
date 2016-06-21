<?php
    
    class parametro{
        const TABLA_PARAMETRO = 'ms_parametro';

    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;

    const INSERCIONES = "inserciones";
    const MODIFICACIONES = "modificaciones";
    const ELIMINACIONES = 'eliminaciones';
        
    
        
        public static function post($peticion){
            if ($peticion[0] == 'accion') {
                                return self::obtenerConfiguracion($peticion[1]);
            //}
            } else if($peticion[2]=='parametro') {
               // return self::loguear();
                                return self::obtenerConfiguracion($peticion[1],$peticion[3]);
            }
            //else {
               // throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
              // return self::obtenerConfiguracion($peticion[1]);
            //}
        }
        
        public static function get($peticion){

        if (!empty($peticion[0]))
            return self::obtenerConfiguracion($peticion[1]);
        else
            http_response_code(404);

        }
        
        public static function obtenerConfiguracion($accion, $parametro  = NULL){
            try {
                if (!$parametro) {
                    $comando = "SELECT * FROM " . self::TABLA_PARAMETRO .
                        " WHERE accion='".$accion."'";
    
                    // Preparar sentencia
                    $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                    // Ligar idUsuario
                    $sentencia->bindParam(1, $accion, PDO::PARAM_INT);
    
                } else {
                   $comando = "SELECT * FROM " . self::TABLA_PARAMETRO .
                        " WHERE accion='".$accion."' AND parametro='".$parametro."'";
                    //var_dump($comando);
                    // Preparar sentencia
                    $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                    // Ligar idContacto e idUsuario
                    $sentencia->bindParam(1, $accion, PDO::PARAM_INT);
                    $sentencia->bindParam(2, $parametro, PDO::PARAM_INT);
                }
    
                // Ejecutar sentencia preparada
                if ($sentencia->execute()) {
                    http_response_code(200);
                    return
                        [
                            "estado" => self::ESTADO_EXITO,
                            "datos" => $sentencia->fetchAll(PDO::FETCH_OBJ)
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
    
    
?>