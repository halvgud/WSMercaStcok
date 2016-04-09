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
            }else {
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
        public static function crear($datosUsuario)
        {
            $idusuario=$datosUsuario->idUsuario;
            $usuario=$datosUsuario->usuario;
            $nombre = $datosUsuario->nombre;
            $contrasena = $datosUsuario->contrasena;
            $contrasenaEncriptada = self::encriptarContrasena($contrasena);
            $apellido = $datosUsuario->apellido;
            $sexo=$datosUsuario->sexo;
            $contacto=$datosUsuario->contacto;
            $idsucursal=$datosUsuario->idSucursal;
            $claveApi = self::generarClaveApi();
    
            try {
    
                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();
    
                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::ID_USUARIO . ",".
                    self::USUARIO . ",".
                    self::CONTRASENA . "," .
                    self::NOMBRE . "," .
                    self::APELLIDO . "," .
                    self::SEXO . "," .
                    self::CONTACTO . "," .
                    self::ID_SUCURSAL . "," .
                    self::CLAVE_API . ")" .
                    " VALUES(?,?,?,?,?,?,?,?,?)";
    
                $sentencia = $pdo->prepare($comando);
    
                $sentencia->bindParam(1, $idusuario);
                $sentencia->bindParam(2, $usuario);
                $sentencia->bindParam(3, $contrasenaEncriptada);
                $sentencia->bindParam(4, $nombre);
                $sentencia->bindParam(5, $apellido);
                $sentencia->bindParam(6, $sexo);
                $sentencia->bindParam(7, $contacto);
                $sentencia->bindParam(8, $idsucursal);
                $sentencia->bindParam(9, $claveApi);
                
                $resultado = $sentencia->execute();
    
                if ($resultado) {
                    return self::ESTADO_CREACION_EXITOSA;
                } else {
                    return self::ESTADO_CREACION_FALLIDA;
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
        
        public static function autenticar($correo, $contrasena)
        {
            $comando = "SELECT contrasena,IDESTADO,idNivelAutorizacion FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::USUARIO . "=? ";
    
            try {
    
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
    
                $sentencia->bindParam(1, $correo);
    
                $sentencia->execute();
    
                if ($sentencia) {
                    $resultado = $sentencia->fetch();
                    //return var_dump(self::validarEstado($resultado['IDESTADO']));
                    if(self::validarContrasena($contrasena, $resultado['contrasena'])){
                        
                        if (self::validarEstado($resultado['IDESTADO'])) {
                            return true;
                        }else {
                          //   http_response_code(401);
                        //return ["estado" => 11, "datos" => "Usuario Bloqueado"];
                        return false;
                        }
                        
                    }else {
                        //http_response_code(401);
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
        public static function validarEstado($idEstado)
        {
            try{
                $comando = "SELECT VALOR FROM ms_parametro WHERE accion='CONFIGURACION_GENERAL' AND parametro='ESTADO_VALIDO_LOGIN' and valor = ?";
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                $sentencia->bindParam(1,$idEstado);
                if($sentencia->execute()){
                    $resultado = $sentencia->fetch();
                   
                    if(isset($resultado['VALOR'])){
                        return true;
                    }else{
                        throw new ExcepcionApi(self::ESTADO_USUARIO_BLOQUEADO,'usuario bloqueado',401);
                    }
                    
                }else {return false;}
            }catch (PDOException $e){
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
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
        }
    }

