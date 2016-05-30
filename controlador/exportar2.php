<?php

    class exportar2{
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
            if ($peticion[0] == 'exportarInventarioWS') {
                return self::exportarInventarioWS();
            }else {
                throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
            }
        }

        public static function exportarInventarioWS()
        {
            $comando = "SELECT idInventario as idInventarioLocal, art_id, existenciaSolicitud, existenciaRespuesta, idUsuario, fechaSolicitud, fechaRespuesta, existenciaEjecucion, idEstado, idInventarioExterno as idInventario, mss.idSucursal
                          FROM ms_inventario INNER JOIN ms_sucursal mss
                           WHERE idEstado='P' AND fechaRespuesta>curdate();";
            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->execute();
                $resultado=$sentencia->FetchAll(PDO::FETCH_ASSOC);
                if ($resultado) {
                    $comando2="UPDATE ms_inventario SET idEstado='E' WHERE idEstado='P' and fechaRespuesta>curdate()";
                        $sentencia2=ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando2);
                        $sentencia2->execute();

                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito",
                                "data" => $resultado
                            ]
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "No se han encontrado resultados",402);

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
            }
        }
    }