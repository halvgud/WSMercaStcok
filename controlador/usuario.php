    <?php
    
    require '/conexion/ConexionBD.php';
    
    class usuario
    {
        // Datos de la tabla "usuario"
        const NOMBRE_TABLA = "ms_usuario";
        const ID_USUARIO = "idUsuario";
        const NOMBRE = "nombre";
        const CONTRASENA = "contrasena";
        const USUARIO = "usuario";
        const CLAVE_API = "claveApi";
        //const NOMBRE = "nombre";
        const APELLIDO = "apellido";
        const SEXO = "sexo";
        const ID_SUCURSAL = "idSucursal";
        const CONTACTO = "contacto";
        const ID_ESTADO = "idEstado";
        const FECHA_ESTADO = "fechaEstado";
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
        const ID_NIVEL_AUTORIZACION="idNivelAutorizacion";
        const FECHA_SESION = "fechaSesion";
        public static function post($peticion)
        {
            if ($peticion[0] == 'registro') {
                return self::registrar();
            } else if ($peticion[0] == 'login') {
                return self::loguear();
    
            } else if ($peticion[0] == 'bloqueo') {
                return self::bloqueo();
            }else if ($peticion[0] == 'buscar_bloqueo') {
                return self::buscar_bloqueo();
            }else if ($peticion[0] == 'cambiar_pin') {
                return self::cambiar_pin();
            }else if ($peticion[0] == 'api') {
                return self::api();
            }else if($peticion[0]=='apiregistro'){
                return self::apiregistro();
            }
            else {
                throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
            }
        }
    
    
        /**
         * Crea un nuevo usuario en la base de datos
         */
        public static function registrar()
        {
            $cuerpo = file_get_contents('php://input');
            $usuario = json_decode($cuerpo);
    
            $resultado = self::crear($usuario);
    
            switch ($resultado) {
                case self::ESTADO_CREACION_EXITOSA:
                    http_response_code(200);
                    return
                        [
                            "estado" => self::ESTADO_CREACION_EXITOSA,
                            "mensaje" => utf8_encode("Registro con exito!")
                        ];
                    break;
                case self::ESTADO_CREACION_FALLIDA:
                    throw new ExcepcionApi(self::ESTADO_CREACION_FALLIDA, "Ha ocurrido un error");
                    break;
                default:
                    throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA, "Falla desconocida", 400);
            }
        }
    
        /**
         * Crea un nuevo usuario en la tabla "usuario"
         * @param mixed $datosUsuario columnas del registro
         * @return int codigo para determinar si la inserci�n fue exitosa
         */
        public static function apiregistro(){
            $post = json_decode(file_get_contents('php://input'),true);
            $claveApi2=$post['claveApi2'];
            $comando = " SELECT claveApi FROM ".self::NOMBRE_TABLA." WHERE claveApi='".$claveApi2."'";
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
            $sentencia->bindParam(1, $claveApi2);
            $sentencia->execute();
            $resultado = $sentencia->fetch();
            if(isset($resultado['claveApi'])){
            return true;}
            else{   
           return null;
            }
        }
        
        public static function crear()//$datosUsuario
        {
            $post = json_decode(file_get_contents('php://input'),true);
            
            //$idusuario=$post['idUsuario'];
            $claveApi2=$post['claveApi2'];
            $usuario=$post['usuario'];
            $contrasena =$post['contrasena'];
            $contrasenaEncriptada = self::encriptarContrasena($contrasena);
            $nombre = $post['nombre'];
            $apellido = $post['apellido'];
            $sexo=$post['sexo'];
            $contacto=$post['contacto'];
            $idsucursal=$post['idSucursal'];
            //$claveApi = self::generarClaveApi();
            $claveApi=$post['claveApi'];
            $idNivelAutorizacion=$post['idNivelAutorizacion'];
            $idEstado = $post['idEstado'];
            $fechaEstado = $post['fechaEstado'];
            $fechaSesion = $post['fechaSesion'];
            //
            //$idusuario=$datosUsuario->idUsuario;
            //$usuario=$datosUsuario->usuario;
            //$contrasena = $datosUsuario->contrasena;
            //$contrasenaEncriptada = self::encriptarContrasena($contrasena);
            //$nombre = $datosUsuario->nombre;
            //$apellido = $datosUsuario->apellido;
            //$sexo=$datosUsuario->sexo;
            //$contacto=$datosUsuario->contacto;
            //$idsucursal=$datosUsuario->idSucursal;
            ////$claveApi = self::generarClaveApi();
            //$claveApi=$datosUsuario->claveApi;
            //$idNivelAutorizacion=$datosUsuario->idNivelAutorizacion;
            //$idEstado = $datosUsuario->idEstado;
            //$fechaEstado = $datosUsuario->fechaEstado;
            //$fechaSesion = $datosUsuario->fechaSesion;
            
            
            //return var_dump($datosUsuario);
            try {
    
                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();
    
                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    //self::ID_USUARIO . ",
                self::USUARIO . ",".
                    self::CONTRASENA . "," .
                    self::NOMBRE . "," .
                    self::APELLIDO . "," .
                    self::SEXO . "," .
                    self::CONTACTO . "," .
                    self::ID_SUCURSAL . "," .
                    self::CLAVE_API . ",".
                    self::ID_NIVEL_AUTORIZACION.",".
                    self::ID_ESTADO.",".
                    self::FECHA_ESTADO .",".
                    self::FECHA_SESION
                    .")" .
                    " VALUES(?,?,?,?,?,?,?,?,?,?,now(),now())";
    
                $sentencia = $pdo->prepare($comando);
    
                //$sentencia->bindParam(1, $idusuario);
                $sentencia->bindParam(1, $usuario);
                $sentencia->bindParam(2, $contrasenaEncriptada);
                $sentencia->bindParam(3, $nombre);
                $sentencia->bindParam(4, $apellido);
                $sentencia->bindParam(5, $sexo);
                $sentencia->bindParam(6, $contacto);
                $sentencia->bindParam(7, $idsucursal);
                $sentencia->bindParam(8, $claveApi);
                $sentencia->bindParam(9,$idNivelAutorizacion);
                $sentencia->bindParam(10,$idEstado);
                //$sentencia->bindParam(12,$fechaEstado);
              // $sentencia->bindParam(13,$fechaSesion);
                if(!self::apiregistro($claveApi2)==null){
                $resultado = $sentencia->execute();
                    if ($resultado) {
                        return self::ESTADO_CREACION_EXITOSA;
                    } else {
                        return self::ESTADO_CREACION_FALLIDA;
                    }
                }
                else{
                     throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                        "Clave Api invalida",401);
                }
                
            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
    
        }
    
        /**
         * Protege la contrase�a con un algoritmo de encriptado
         * @param $contrasenaPlana
         * @return bool|null|string
         */
        public static function encriptarContrasena($contrasenaPlana)
        {
            if ($contrasenaPlana)
                return password_hash($contrasenaPlana, PASSWORD_DEFAULT);
            else return null;
        }
    
        public static function generarClaveApi()
        {
            return md5(microtime() . rand());
        }
    
        public static function loguear()
        {
            $respuesta = array();
    
           // $body = file_get_contents('php://input');
            //$usuario = json_decode($body);
            $usuario = json_decode(file_get_contents('php://input'),true);
    
            $correo = $usuario['usuario'];
            $contrasena = $usuario['contrasena'];
    
    
            if (self::autenticar($correo, $contrasena)==TRUE) {
                $usuarioBD = self::obtenerUsuarioPorUsuario($correo);
    
                if ($usuarioBD != NULL) {
                    http_response_code(200);
                    $respuesta["nombre"] = $usuarioBD["nombre"];
                    $respuesta["usuario"] = $usuarioBD["usuario"];
                    $respuesta["claveApi"] = $usuarioBD["claveApi"];
                    $respuesta['idNivelAutorizacion']=$usuarioBD["idNivelAutorizacion"];
                    return ["estado" => 1, "datos" => $respuesta];
                } else {
                    throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                        "Ha ocurrido un error",401);
                }
            } else {
                throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS,
                    utf8_encode("usuario o contraseña inválidos"),401);
            }
        }
        public static function dumy ($correo,$gcm){
            //$post=json_decode(file_get_contents('php://input'),true);
            //$usuario=$post['usuario'];
            //$gcm=$post['claveGCM'];
            // $correo = $usuario['usuario'];
            $comando2 = "UPDATE ms_usuario SET fechaSesion=NOW(),claveGCM='".$gcm."' WHERE usuario='".$correo."'";
    
            try {
    
                $sentencia2 = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando2);
    
               if ($sentencia2->execute()) {
                    http_response_code(200);
                    return
                        [
                            "estado" => self::ESTADO_EXITO,
                            "datos" => $sentencia2->rowCount()
                        ];
                } else
                    throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");
            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(),401);
            }
        }
        public static function autenticar($correo, $contrasena,$gcm)
        {
            $comando = "SELECT idUsuario,contrasena,IDESTADO,idNivelAutorizacion FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::USUARIO . "=? ";
    
            try {
    
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
                $sentencia->bindParam(1, $correo);
    
                $sentencia->execute();
    
                if ($sentencia) {
                    $resultado = $sentencia->fetch();
                   // return var_dump(self::validarEstado($resultado['IDESTADO']));
                  //  if (self::validarEstado($resultado['IDESTADO'],$resultado['idUsuario'])) {
                    if(self::validarContrasena($contrasena, $resultado['contrasena'])&&self::dumy($correo,$gcm)){
                   //self::generarClaveApi();
                        $claveApi = self::generarClaveApi();
                        
                        try {
                            $post = json_decode(file_get_contents('php://input'),true);
                            //return var_dump($post);
                                $comando = "UPDATE ".self::NOMBRE_TABLA." SET ".self::CLAVE_API." ='".$claveApi."' WHERE idUsuario='".$resultado['idUsuario']."'";
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
                        
                        
                            return true;
                        }else {
                          //   http_response_code(401);
                        //return ["estado" => 11, "datos" => "Usuario Bloqueado"];
                        return false;
                        }
                   
                } else {
                    return false;
                }
            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage(),401);
            }
           
        }
    
        public static function validarContrasena($contrasenaPlana, $contrasenaHash)
        {
    //        return var_dump(password_verify($contrasenaPlana, $contrasenaHash));
            return password_verify($contrasenaPlana, $contrasenaHash);
        }
    
    
        public static function obtenerUsuarioPorUsuario($correo)
        {
            $comando = "SELECT " .
                self::NOMBRE . "," .
                self::CONTRASENA . "," .
                self::USUARIO . "," .
                self::CLAVE_API .",".
                "idNivelAutorizacion".
                " FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::USUARIO . "=?";
    
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
            $sentencia->bindParam(1, $correo);
    
            if ($sentencia->execute())
                return $sentencia->fetch(PDO::FETCH_ASSOC);
            else
                return null;
        }
    
        /**
         * Otorga los permisos a un usuario para que acceda a los recursos
         * @return null o el id del usuario autorizado
         * @throws Exception
         */
        public static function autorizar()
        {
            $cabeceras = apache_request_headers();
    
            if (isset($cabeceras["Authorization"])) {
    
                $claveApi = $cabeceras["Authorization"];
    
                if (usuario::validarClaveApi($claveApi)) {
                    return usuario::obtenerIdUsuario($claveApi);
                } else {
                    throw new ExcepcionApi(
                        self::ESTADO_CLAVE_NO_AUTORIZADA, "Clave de API no autorizada", 401);
                }
    
            } else {
                throw new ExcepcionApi(
                    self::ESTADO_AUSENCIA_CLAVE_API,
                    utf8_encode("Se requiere Clave del API para autenticaci�n"));
            }
        }
    
        /**
         * Comprueba la existencia de la clave para la api
         * @param $claveApi
         * @return bool true si existe o false en caso contrario
         */
        public static function validarClaveApi($claveApi)
        {
            $comando = "SELECT COUNT(" . self::ID_USUARIO . ")" .
                " FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::CLAVE_API . "=?";
    
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
            $sentencia->bindParam(1, $claveApi);
    
            $sentencia->execute();
    
            return $sentencia->fetchColumn(0) > 0;
        }
    
        /**
         * Obtiene el valor de la columna "idUsuario" basado en la clave de api
         * @param $claveApi
         * @return null si este no fue encontrado
         */
        public static function obtenerIdUsuario($claveApi)
        {
            $comando = "SELECT " . self::ID_USUARIO .
                " FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::CLAVE_API . "=?";
    
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
            $sentencia->bindParam(1, $claveApi);
    
            if ($sentencia->execute()) {
                $resultado = $sentencia->fetch();
                return $resultado['idUsuario'];
            } else
                return null;
        }
        public static function bloqueo()
        {
            try {
                $post = json_decode(file_get_contents('php://input'),true);
                //return var_dump($post);
                    $comando = "UPDATE ".self::NOMBRE_TABLA." SET ".self::ID_ESTADO." ='B', fechaEstado=NOW() WHERE usuario='".$post['usuario']."'";
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
        //public static function validarEstado($idEstado,$idUsuario)
        //{
        //    try{
        //        $comando ="select valor, fechaEstado from ms_usuario mu left join
        //        ms_parametro mp on (mp.accion='CONFIGURACION_GENERAL' and mp.parametro='ESTADO_VALIDO_LOGIN'
        //        and valor = ?) where mu.idUsuario = ?";
        //       // $comando = "SELECT VALOR FROM ms_parametro mp
        //       // inner join ms_usuario mu (mp.idUsuario = ? ) WHERE accion='CONFIGURACION_GENERAL' AND parametro='ESTADO_VALIDO_LOGIN' and valor = ?";
        //        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
        //        $sentencia->bindParam(1,$idEstado);
        //        $sentencia->bindParam(2,$idUsuario);
        //        
        //        if($sentencia->execute()){
        //            $resultado = $sentencia->fetch();
        //           
        //            if(isset($resultado['valor'])){
        //                return true;
        //            }else{
        //                
        //                throw new ExcepcionApi(self::ESTADO_USUARIO_BLOQUEADO,'El Usuario ha sido bloqueado desde '.$resultado['fechaEstado'].'                                          Favor de esperar 5 minutos a partir de dicha hora',401);
        //            }
        //            
        //        }else {return false;}
        //    }catch (PDOException $e){
        //        throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        //    }
        //}
            /*
            try {
                $post = json_decode(file_get_contents('php://input'),true);
                //return var_dump($post);
                    $comando = "SELECT idEstado FROM ".self::NOMBRE_TABLA." WHERE usuario='".$post['usuario']."'";
                    // Preparar sentencia
                    $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
            $sentencia->bindParam(1, $usuario);
    
            if ($sentencia->execute()) {
                $resultado = $sentencia->fetch();
                return $resultado['usuario'];
            } else
                return null;
    
            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }*/
            public static function cambiar_pin()
        {
                  $post = json_decode(file_get_contents('php://input'),true);
                   $usuario = $post['usuario'];
                   $pin_viejo=$post['pin_viejo'];
            //$contrasena = $usuario['contrasena'];
       
            if (self::autenticar($usuario, $pin_viejo)==TRUE) {
                //$usuarioBD = self::obtenerUsuarioPorUsuario($correo);self::encriptarContrasena($pin_nuevo);
                 $comando = "UPDATE ".self::NOMBRE_TABLA." SET ".self::CONTRASENA." ='".self::encriptarContrasena($post['pin_nuevo'])."' WHERE usuario='".$post['usuario']."'";
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
    
                if ($usuarioBD != NULL) {
                    http_response_code(200);
                } else {
                    throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                        "Ha ocurrido un error",401);
                }
            } else {
                throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS,
                    utf8_encode("usuario o contraseña inválidos"),401);
            }
        }
        public static function api()
        {
            $post = json_decode(file_get_contents('php://input'),true);
                   $usuario = $post['claveApi'];
            try{
                
                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();
                $comando ="select claveApi from ms_usuario where claveApi='".$usuario."' AND claveApi!=''
                and fechaSesion>now()- interval 1 day";
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
                $sentencia->bindParam(1, $usuario);
                //return var_dump($comando);
                $sentencia->execute();
              
                if($sentencia){
                    $resultado = $sentencia->fetch();
                  // return var_dump($resultado);
                    if(isset($resultado['claveApi'])){
                        //return true;
                        return ["estado" => self::ESTADO_EXITO];
                    }else{
                        
                        throw new ExcepcionApi(self::ESTADO_USUARIO_BLOQUEADO,'No hay clave api',401);
                    }
                    
                }else {return false;}
            }catch (PDOException $e){
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
        }
            
    }

