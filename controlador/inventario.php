<?php

class ARTICULO
{
    const TABLA_INVENTARIO = 'ms_inventario';
    const ID_INVENTARIO = "idInventario";
    const ID_ARTICULO = "art_id";
    const EXISTENCIA_SOLICITUD = 'existenciaSolicitud';
    const EXISENCIA_RESPUESTA = 'existenciaRespuesta';
    const ID_USUARIO = 'idUsuario';
    const FECHA_SOLICITUD = 'fechaSolicitud';
    const FECHA_RESPUESTA = 'fechaRespuesta';
    const EXISTENCIA_EJECUCION = 'existenciaEjecucion';

    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;

    const INSERCIONES = "inserciones";
    const MODIFICACIONES = "modificaciones";
    const ELIMINACIONES = 'eliminaciones';


    public static function get($peticion)
    {
        $idUsuario = usuario::autorizar();

        if (empty($peticion[0]))
            return self::obtenerContactos($idUsuario);
        else
            return self::obtenerContactos($idUsuario, $peticion[0]);

    }

    public static function post($segmentos)
    {
        $idUsuario = usuario::autorizar();

        $payload = file_get_contents('php://input');

        $payload = json_decode($payload);

        $idContacto = articulo::insertar($idUsuario, $payload);

        http_response_code(201);
        return [
            "estado" => self::ESTADO_EXITO,
            "mensaje" => "Informacion guardada correctamente",
            "id" => $idContacto
        ];


    }

    public static function put($peticion)
    {
        $idUsuario = usuario::autorizar();

        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $contacto = json_decode($body);

            if (self::modificar($idUsuario, $contacto, $peticion[0]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_EXITO,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El contacto al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_MALA_SINTAXIS, "Falta id", 422);
        }
    }

    public static function delete($peticion)
    {
        $idUsuario = usuario::autorizar();

        if (!empty($peticion[0])) {
            if (self::eliminar($idUsuario, $peticion[0]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El contacto al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_MALA_SINTAXIS, "Falta id", 422);
        }

    }

    /**
     * Obtiene la colecci�n de contactos o un solo contacto indicado por el identificador
     * @param int $idUsuario identificador del usuario
     * @param null $idContacto identificador del contacto (Opcional)
     * @return array registros de la tabla contacto
     * @throws Exception
     */
    public static function obtenerContactos($idUsuario, $idContacto = NULL)
    {
        try {
            if (!$idContacto) {
                $comando = "SELECT * FROM " . self::TABLA_INVENTARIO .
                    " sy inner join articulo a on (a.art_id = sy.art_id)";

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idUsuario
                $sentencia->bindParam(1, $idUsuario, PDO::PARAM_INT);

            } else {
                $comando = "SELECT * FROM " . self::TABLA_INVENTARIO .
                    " WHERE " . self::ID_INVENTARIO . "=? AND " .
                    self::ID_USUARIO . "=?";

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idContacto e idUsuario
                $sentencia->bindParam(1, $idContacto, PDO::PARAM_INT);
                $sentencia->bindParam(2, $idUsuario, PDO::PARAM_INT);
            }

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    /**
     * A�ade un nuevo contacto asociado a un usuario
     * @param int $idUsuario identificador del usuario
     * @param mixed $contacto datos del contacto
     * @return string identificador del contacto
     * @throws ExcepcionApi
     */
    public static function insertar($idUsuario, $contacto)
    {
        if ($contacto) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = 'INSERT INTO ' . self::TABLA_INVENTARIO . ' ( ' .
                    self::ID_INVENTARIO . ',' .
                    self::PRIMER_NOMBRE . ',' .
                    self::EXISTENCIA_SOLICITUD . ',' .
                    self::EXISENCIA_RESPUESTA . ',' .
                    self::FECHA_SOLICITUD . ',' .
                    self::ID_USUARIO . ')' .
                    ' VALUES(?,?,?,?,?,?)';

                // Preparar la sentencia
                $sentencia = $pdo->prepare($comando);

                // Generar Pk
                $idContacto = 'C-'.self::generarUuid();

                $sentencia->bindParam(1, $idContacto);
                $sentencia->bindParam(2, $primerNombre);
                $sentencia->bindParam(3, $primerApellido);
                $sentencia->bindParam(4, $telefono);
                $sentencia->bindParam(5, $correo);
                $sentencia->bindParam(6, $idUsuario);


                $primerNombre = $contacto->primerNombre;
                $primerApellido = $contacto->primerApellido;
                $telefono = $contacto->telefono;
                $correo = $contacto->correo;

                $sentencia->execute();

                // Retornar en el �ltimo id insertado
                return $idContacto;

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
        } else {
            throw new ExcepcionApi(
                self::ESTADO_MALA_SINTAXIS,
                utf8_encode("Error en existencia o sintaxis de par�metros"));
        }

    }

    /**
     * Actualiza el contacto especificado por idUsuario
     * @param int $idUsuario
     * @param object $contacto objeto con los valores nuevos del contacto
     * @param int $idContacto
     * @return PDOStatement
     * @throws Exception
     */
    public static function modificar($idUsuario, $contacto, $idContacto)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::TABLA_INVENTARIO .
                " SET " . self::PRIMER_NOMBRE . "=?," .
                self::EXISTENCIA_SOLICITUD . "=?," .
                self::EXISENCIA_RESPUESTA . "=?," .
                self::FECHA_SOLICITUD . "=? " .
                " WHERE " . self::ID_INVENTARIO . "=? AND " . self::ID_USUARIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);

            $sentencia->bindParam(1, $primerNombre);
            $sentencia->bindParam(2, $primerApellido);
            $sentencia->bindParam(3, $telefono);
            $sentencia->bindParam(4, $correo);
            $sentencia->bindParam(5, $idContacto);
            $sentencia->bindParam(6, $idUsuario);

            $primerNombre = $contacto->primerNombre;
            $primerApellido = $contacto->primerApellido;
            $telefono = $contacto->telefono;
            $correo = $contacto->correo;

            // Ejecutar la sentencia
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }


    /**
     * Elimina un contacto asociado a un usuario
     * @param int $idUsuario identificador del usuario
     * @param int $idContacto identificador del contacto
     * @return bool true si la eliminaci�n se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
    public static function eliminar($idUsuario, $idContacto)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::TABLA_INVENTARIO .
                " WHERE " . self::ID_INVENTARIO . "=? AND " .
                self::ID_USUARIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idContacto);
            $sentencia->bindParam(2, $idUsuario);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    /**
     * Inserta n elementos de seguidos en la tabla contactos
     * @param int $idUsuario identificador del usuario
     * @param mixed $contacto datos del contacto
     * @return string identificador del contacto
     * @throws ExcepcionApi
     */
    public static function insertarEnBatch(PDO $pdo, $listaContactos, $idUsuario)
    {
        // Sentencia INSERT
        $comando = 'INSERT INTO ' . self::TABLA_INVENTARIO . ' ( ' .
            self::ID_INVENTARIO . ',' .
            self::PRIMER_NOMBRE . ',' .
            self::EXISTENCIA_SOLICITUD . ',' .
            self::EXISENCIA_RESPUESTA . ',' .
            self::FECHA_SOLICITUD . ',' .
            self::ID_USUARIO . ',' .
            self::FECHA_RESPUESTA . ')' .
            ' VALUES(?,?,?,?,?,?,?)';

        // Preparar la sentencia
        $sentencia = $pdo->prepare($comando);

        $sentencia->bindParam(1, $idContacto);
        $sentencia->bindParam(2, $primerNombre);
        $sentencia->bindParam(3, $primerApellido);
        $sentencia->bindParam(4, $telefono);
        $sentencia->bindParam(5, $correo);
        $sentencia->bindParam(6, $idUsuario);
        $sentencia->bindParam(7, $version);

        foreach ($listaContactos as $item) {
            $idContacto = $item[self::ID_INVENTARIO];
            $primerNombre = $item[self::PRIMER_NOMBRE];
            $primerApellido = $item[self::EXISTENCIA_SOLICITUD];
            $telefono = $item[self::EXISENCIA_RESPUESTA];
            $correo = $item[self::FECHA_SOLICITUD];
            $version = $item[self::FECHA_RESPUESTA];
            $sentencia->execute();

        }

    }

    /**
     * Aplica n modificaciones de contactos
     * @param PDO $pdo instancia controlador de base de datos
     * @param $arrayContactos lista de contactos
     * @param $idUsuario identificador del usuario
     */
    public static function modificarEnBatch(PDO $pdo, $arrayContactos, $idUsuario)
    {

        // Preparar operaci�n de modificaci�n para cada contacto
        $comando = 'UPDATE ' . self::TABLA_INVENTARIO . ' SET ' .
            self::PRIMER_NOMBRE . '=?,' .
            self::EXISTENCIA_SOLICITUD . '=?,' .
            self::EXISENCIA_RESPUESTA . '=?,' .
            self::FECHA_SOLICITUD . '=?,' .
            self::FECHA_RESPUESTA . '=? ' .
            ' WHERE ' . self::ID_INVENTARIO . '=? AND ' . self::ID_USUARIO . '=?';

        // Preparar la sentencia update
        $sentencia = $pdo->prepare($comando);

        // Ligar parametros
        $sentencia->bindParam(1, $primerNombre);
        $sentencia->bindParam(2, $primerApellido);
        $sentencia->bindParam(3, $telefono);
        $sentencia->bindParam(4, $correo);
        $sentencia->bindParam(5, $version);
        $sentencia->bindParam(6, $idContacto);
        $sentencia->bindParam(7, $idUsuario);

        // Procesar array de contactos
        foreach ($arrayContactos as $contacto) {
            $idContacto = $contacto[self::ID_INVENTARIO];
            $primerNombre = $contacto[self::PRIMER_NOMBRE];
            $primerApellido = $contacto[self::EXISTENCIA_SOLICITUD];
            $telefono = $contacto[self::EXISENCIA_RESPUESTA];
            $correo = $contacto[self::FECHA_SOLICITUD];
            $version = $contacto[self::FECHA_RESPUESTA];
            $sentencia->execute();
        }

    }

    /**
     * Aplina n elminaciones a la tabla 'contacto'
     * @param PDO $pdo instancia controlador de base de datos
     * @param $arrayIds lista de contactos
     * @param $idUsuario identificador del usuario
     */
    public static function eliminarEnBatch(PDO $pdo, $arrayIds, $idUsuario)
    {
        // Crear sentencia DELETE
        $comando = 'DELETE FROM ' . self::TABLA_INVENTARIO .
            ' WHERE ' . self::ID_INVENTARIO . ' = ? AND ' . self::ID_USUARIO . '=?';

        // Preparar sentencia en el contenedor
        $sentencia = $pdo->prepare($comando);


        // Procesar todas las ids
        foreach ($arrayIds as $id) {
            $sentencia->execute(array($id, $idUsuario));
        }

    }

    /**
     * Genera id aleatoria con formato UUID
     * @return string identificador
     */
    function generarUuid()
    {
        return sprintf('%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
            mt_rand(0, 0xffff), mt_rand(0, 0xffff),
            mt_rand(0, 0xffff),
            mt_rand(0, 0x0fff) | 0x4000,
            mt_rand(0, 0x3fff) | 0x8000,
            mt_rand(0, 0xffff), mt_rand(0, 0xffff), mt_rand(0, 0xffff)
        );
    }
}

