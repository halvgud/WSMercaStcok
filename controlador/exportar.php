<?php
    //require '../conexion/ConexionBD.php';
    class exportar
    {
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
            if ($peticion[0] == 'departamento') {
                return self::seleccionarDepartamento();
            } else if ($peticion[0] == 'categoria') {
                return self::seleccionarCategoria();
            } else if ($peticion[0] == 'articulo') {
                return self::seleccionarArticulo();
            }else {
                throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
            }
        }

        public static function seleccionarDepartamento()
        {
            $comando = "SELECT d.dep_id as dep_idLocal, d.nombre, d.restringido, d.porcentaje, d.status, ms.idSucursal FROM departamento d INNER JOIN ms_sucursal ms on (ms.idSucursal=ms.idSucursal)";

            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                if ($sentencia->execute()) {
                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito los Departamentos",
                                "data" => $sentencia->FetchAll(PDO::FETCH_ASSOC)
                            ]
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
            }
        }

        public static function seleccionarCategoria()
        {
            $comando = "SELECT c.cat_id as cat_id_Local,c.nombre,c.status,c.dep_id,ms.idSucursal FROM categoria c inner join ms_sucursal
            ms on (ms.idSucursal =ms.idSucursal)";

            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                if ($sentencia->execute()) {
                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito las Categorias",
                                "data" => $sentencia->FetchAll(PDO::FETCH_ASSOC)
                            ]
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
            }
            finally{
                ConexionBD::obtenerInstancia()->_destructor();
            }
        }

        public static function seleccionarArticulo()
        {
            $comando = "SELECT a.art_id as art_idLocal, a.clave, a.claveAlterna, a.descripcion, a.servicio,
                        a.invMin, a.invMax, a.factor, a.precioCompra, a.preCompraProm as precioCompraProm, a.margen1, a.precio1, a.existencia,
                        a.lote, a.receta, a.granel, a.tipo, a.status, a.unidadCompra, a.unidadVenta, a.cat_id,
                        a.srp_id, ms.idSucursal FROM articulo a INNER JOIN ms_sucursal ms on (ms.idSucursal=ms.idSucursal)";

            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                if ($sentencia->execute()) {
                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito los Artículos",
                                "data" => $sentencia->FetchAll(PDO::FETCH_ASSOC)
                            ]
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
            }
            finally{
                ConexionBD::obtenerInstancia()->_destructor();
            }
        }
    }

