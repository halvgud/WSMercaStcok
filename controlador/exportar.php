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
            }else if(isset($peticion[1])&&$peticion[1]=='tipoPago'){
                return self::ventaTipoPago();
            }else if (isset($peticion[1])&&$peticion[1]=='cancelacion'){
                return self::VentaCancelacion();
            }
            else{
                return self::seleccionarVenta();
            }
        }
        else if($peticion[0]=='usuario'){
            return self::seleccionarUsuarios();
        }
        else if($peticion[0]=='listagcm'){
            return self::seleccionarListaGcm();
        }
        else if($peticion[0]=='inventario'){
            if(isset($peticion[1])&&$peticion[1]=='actualizar'){
                return self::actualizarInventario();
            }else{
                return self::exportarInventarioWS();
            }
        }
        else {
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
        }
    }

    public static function seleccionarUsuarios(){
        $comando ="select idUsuario,usuario,contrasena,nombre,apellido,sexo,contacto,idSucursal,claveApi,idNivelAutorizacion,idEstado,fechaEstado,fechaSesion,
        claveGCM from ms_usuario";
        try{
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->execute();
            $resultado=$sentencia->FetchAll(PDO::FETCH_ASSOC);
            if ($resultado) {
                return
                    [
                        $arreglo = [
                            "estado" => 200,
                            "success" => "Se ha exportado éxito",
                            "data" => $resultado
                        ]
                    ];
            } else{
                throw new ExcepcionApi(202, "No se han encontrado resultados",202);
            }
        }catch(PDOException $e){
            $codigoDeError=$e->getCode();
            $error =self::traducirMensaje($codigoDeError,$e);
            throw new ExcepcionApi($e->getCode(), $error, 401);
        }

    }
    public static function seleccionarDepartamento()
    {
        $comando = "SELECT d.dep_id, d.nombre, d.restringido, d.porcentaje, d.status, ms.idSucursal FROM departamento d INNER JOIN ms_sucursal ms on (ms.idEstado=1)";

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
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error",202);
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
        }
    }

    public static function seleccionarCategoria()
    {
        $comando = "SELECT c.cat_id,c.nombre,c.status,c.dep_id,ms.idSucursal FROM categoria c inner join ms_sucursal
            ms on (ms.idEstado=1)";

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
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error",202);
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }

    public static function seleccionarArticulo()
    {
        $postrequest= json_decode(file_get_contents('php://input'));
        $comando = "SELECT a.art_id, a.clave, a.claveAlterna, a.descripcion, a.servicio,
                        a.invMin, a.invMax, a.factor, a.precioCompra, a.preCompraProm as precioCompraProm, a.margen1, a.precio1, a.existencia,
                        a.lote, a.receta, a.granel, a.tipo, a.status, a.unidadCompra, a.unidadVenta, a.cat_id,
                        a.srp_id, ms.idSucursal FROM articulo a INNER JOIN ms_sucursal ms on (ms.idSucursal=:idSucursal)";

        try {
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->bindParam("idSucursal",$postrequest->idSucursal);
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
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error",202);

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
                        from venta v inner join ms_sucursal ms on ms.idEstado='1'
                        where v.ven_id>:ven_id and v.ven_id<:ven_id+250";
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
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error",202);
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
                        inner join ms_sucursal ms on ms.idEstado='1'
                        where ven_id>:ven_id and ven_id<:ven_id+250";
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
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error",202);

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
    public static function actualizarInventario(){
        try {
            $postrequest = json_decode(file_get_contents('php://input'));
            $comando = "UPDATE ms_inventario SET idEstado='E' WHERE idEstado='P' and idInventario=:idInventario";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $resultado=false;
            foreach ($postrequest->data as $jsonRow) {
                $idInventario = $jsonRow->idInventario;
                $sentencia->bindParam("idInventario", $idInventario);
                $resultado = $sentencia->execute();
            }
            if($resultado){
                http_response_code(200);
                return
                    [
                        "estado" => 200,
                        "success" => "se a actualizado los registros",
                        "data" => $postrequest
                    ];

            } else {
                http_response_code(400);
                return
                    [
                        "estado" => "warning",
                        "mensaje" => "",
                        "data" => $resultado
                    ];

            }//else
        }catch(PDOException $e){
            $codigoDeError=$e->getCode();
            $error =self::traducirMensaje($codigoDeError,$e);
            throw new ExcepcionApi($e->getCode(), $error, 401);
        }
    }
    public static function exportarInventarioWS()
    {
        $postrequest = json_decode(file_get_contents('php://input'));
        $comando = "SELECT idInventario, art_id, existenciaSolicitud, existenciaRespuesta, idUsuario, fechaSolicitud, fechaRespuesta, existenciaEjecucion, mi.idEstado, mss.idSucursal
                          FROM ms_inventario mi INNER JOIN ms_sucursal mss on mss.idEstado='1'
                           WHERE mi.idEstado='P' AND fechaRespuesta>curdate();";
        try {
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            //$sentencia->bindParam("idSucursal",$postrequest->idSucursal);
            $sentencia->execute();
            $resultado=$sentencia->FetchAll(PDO::FETCH_ASSOC);
            if ($resultado) {
                return
                    [
                        $arreglo = [
                            "estado" => 200,
                            "success" => "Se ha exportado éxito",
                            "data" => $resultado
                        ]
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "No se han encontrado resultados",202);
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(), 401);
        }
    }
    public static function VentaTipoPago()
    {
        ConexionBD::obtenerInstancia()->_destructor();
        try {
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $post = json_decode(file_get_contents('php://input'));
            $ultimoId=$post->data[0]->ven_id;
            $comando = "SELECT vtp.ven_id, vtp.tpa_id, vtp.total, vtp.monTotal,ms.idSucursal FROM ventatipopago vtp
                inner join ms_sucursal ms on (ms.idEstado = '1')
                WHERE ven_id>:ultimoId and ven_id<:ultimoId+250";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->bindParam("ultimoId", $ultimoId);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll(PDO::FETCH_ASSOC);
            if ($resultado) {
                http_response_code(200);
                ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "data" => $resultado
                    ];
            }else{
                throw new ExcepcionApi(self::ESTADO_ERROR, "No se han encontrado resultados",202);
            }
        }catch (PDOException $e) {
            ConexionBD::obtenerInstancia()->obtenerBD()->rollBack();
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
        finally{
            ConexionBD::obtenerInstancia()->_destructor();
        }
    }

    public static function VentaCancelacion(){
        try {
            ConexionBD::obtenerInstancia()->obtenerBD()->beginTransaction();
            $comando = "SELECT ven_id,ms.idSucursal from venta v
                        inner join ms_sucursal ms on (ms.idEstado='1') where status!='1'";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->execute();
            $resultado = $sentencia->fetchAll(PDO::FETCH_ASSOC);
            if ($resultado) {
                http_response_code(200);
                ConexionBD::obtenerInstancia()->obtenerBD()->commit();
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "data" => $resultado
                    ];
            }else{
                throw new ExcepcionApi(self::ESTADO_ERROR, "No se han encontrado resultados",202);
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

