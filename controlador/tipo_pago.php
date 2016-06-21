<?php
    class tipo_pago
    {
        const ESTADO_EXITO = 100;
        const ESTADO_ERROR = 101;
        const ESTADO_ERROR_BD = 102;
        const ESTADO_MALA_SINTAXIS = 103;
        const ESTADO_NO_ENCONTRADO = 104;
        const ESTADO_URL_INCORRECTA=105;
        const ESTADO_FALLA_DESCONOCIDA = 7;

        public static function post($peticion)
        {
            if ($peticion[0]=='seleccionar'){
                return self::seleccionar();
            }else {
                http_response_code(404);
            }
        }

        public static function seleccionar()
        {
            ConexionBD::obtenerInstancia()->_destructor();
            try {
                ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
                $post = json_decode(file_get_contents('php://input'), true);
                $ultimoId=$post['ultimoId'];
                $comando = "SELECT ven_id, tpa_id, total, monTotal FROM ventatipopago WHERE ven_id>:ultimoId limit 500";
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->bindParam(ultimoId, $ultimoId);
                $sentencia->execute();
                $resultado = $sentencia->fetchAll(PDO::FETCH_ASSOC);
                if ($resultado > 0) {
                    http_response_code(200);
                    ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                    return
                        [
                            "estado" => self::ESTADO_EXITO,
                            "datos" => $resultado
                        ];
                }
            }catch (PDOException $e) {
                ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
            finally{
                ConexionBD::obtenerInstancia()->_destructor();
            }
        }
    }