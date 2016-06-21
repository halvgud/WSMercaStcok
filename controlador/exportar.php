<?php
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
            }else if($peticion[0]=='venta'){
                if(isset($peticion[1])&&$peticion[1]=='detalle'){
                    return self::seleccionarDetalleVenta();
                }else{
                return self::seleccionarVenta();
                }
            }
            else if($peticion[0]=='listagcm'){
                return self::seleccionarListaGcm();
            }
            else if ($peticion[0] == 'inventario') {
                return self::exportarInventarioWS();
            }
            else {
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
            $comando = "SELECT a.art_id, a.clave, a.claveAlterna, a.descripcion, a.servicio,
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
        public static function seleccionarVenta(){
            $postrequest= json_decode(file_get_contents('php://input'));
            $comando = "SELECT v.ven_id,v.fecha,v.subtotal0,v.subtotal,v.descuento,v.total,v.cambio,v.letra,v.monSubtotal0,v.monSubtotal,v.monDescuento,v.monTotal,v.monCambio,v.monLetra,v.monAbr,v.monTipoCambio,v.comentario,v.decimales,v.porPeriodo,v.ventaPorAjuste,v.puntos,v.monedas,v.status,v.tic_id,v.not_id,v.rem_id,v.caj_id,v.mon_id,v.rcc_id,v.can_caj_id,v.can_rcc_id,v.vnd_id,ms.idSucursal
                        from venta v inner join ms_sucursal ms
                        where v.ven_id>:ven_id limit 1000";
            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->bindParam("ven_id",$postrequest->data[0]->ven_id);
                $sentencia->execute();
                $resultado= $sentencia->FetchAll(PDO::FETCH_ASSOC);
                if ($resultado) {
                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito los Artículos",
                                "data" =>$resultado
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

        public static function seleccionarDetalleVenta(){
            $postrequest= json_decode(file_get_contents('php://input'));
            $comando = "select dv.ven_id,dv.art_id,dv.clave,dv.descripcion,dv.cantidad,dv.unidad,dv.precioNorSin,dv.precioNorCon,dv.precioSin,dv.precioCon,dv.importeNorSin,dv.importeNorCon,dv.importeSin,dv.importeCon,dv.descPorcentaje,dv.descTotal,dv.precioCompra,dv.importeCompra,dv.sinGravar,dv.caracteristicas,dv.orden,dv.detImp,dv.iepsActivo,dv.cuotaIeps,dv.cuentaPredial,dv.movVen,dv.movVenC,dv.monPrecioNorSin,dv.monPrecioNorCon,dv.monPrecioSin,dv.monPrecioCon,dv.monImporteNorSin,dv.monImporteNorCon,dv.monImporteSin,dv.monImporteCon,dv.nombreAduana,dv.fechaDocAduanero,dv.numeroDocAduanero,dv.lote,dv.receta,dv.tipo,dv.trr_id,dv.ncr_id,ms.idSucursal from detallev dv
                        inner join ms_sucursal ms
                        where dv.ven_id>:ven_id limit 1000";
            try {
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->bindParam("ven_id",$postrequest->data[0]->ven_id);
                $sentencia->execute();
                $resultado= $sentencia->FetchAll(PDO::FETCH_ASSOC);
                if ($resultado) {
                    http_response_code(200);
                    return
                        [
                            $arreglo = [
                                "estado" => 200,
                                "success" => "Se cargo con éxito los Artículos",
                                "data" =>$resultado
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

        public static function seleccionarListaGcm()
        {
            $postrequest = json_decode(file_get_contents('php://input'));
            $query = "select distinct claveGCM from ms_usuario where claveGCM!=''";
            try{
                $db = ConexionBD::obtenerInstancia()->obtenerBD();
                $sentencia = $db->prepare($query);
                $sentencia -> execute();
                $resultado = $sentencia->fetchAll(PDO::FETCH_COLUMN,0);
                if($resultado){
                     $arreglo =
                        [
                            "estado" => 200,
                            "success" => "",
                            "data" => $resultado
                        ];

                } else {
                    $arreglo =
                        [
                            "estado" => "warning",
                            "mensaje" => "",
                            "data" => $resultado
                        ];
                  }
            }catch(PDOException $e){
                $arreglo = [
                    "estado" =>$e -> getCode(),
                    "error" =>$e->getMessage(),
                    "data" => json_encode($postrequest)
                ];
            }
            finally{
                $db=null;
                return $arreglo;
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
                                "success" => "Se ha exportado éxito",
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

