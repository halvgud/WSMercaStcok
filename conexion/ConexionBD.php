    <?php




class ConexionBD
{
    static $NOMBRE_HOST ="192.168.1.185";// Nombre del host
    static $USUARIO ="root"; // Nombre del usuario
    static $CONTRASENA ="sysadmin"; // Constraseña

    static $DB_NOMBRE = "sicar"; // Nombre de la base de controladores


    /**
     * Única instancia de la clase
     */
    private static $db = null;

    /**
     * Instancia de PDO
     */
    private static $pdo;

    final private function __construct()
    {
        try {
            // Crear nueva conexión PDO
            self::obtenerBD();
        } catch (PDOException $e) {
            // Manejo de excepciones
        }


    }

    /**
     * Retorna en la única instancia de la clase
     * @return ConexionBD|null
     */
    public static function obtenerInstancia()
    {
        if (self::$db === null) {
            self::$db = new self();
        }
        return self::$db;
    }
    static $FLAG_SUCURSAL=false;
    /**
     * Crear una nueva conexión PDO basada
     * en las constantes de conexión
     * @return PDO Objeto PDO
     */
    public static function obtenerBD()
    {
        try{
        if (self::$pdo == null) {
            self::$DB_NOMBRE=isset($_SESSION['DB'])?$_SESSION['DB']:self::$DB_NOMBRE;
            self::$pdo = new PDO(
                'mysql:dbname=' . self::$DB_NOMBRE .
                ';host=' . self::$NOMBRE_HOST . ";",
                self::$USUARIO,
                self::$CONTRASENA,
                array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
            );

            // Habilitar excepciones
            self::$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        }

        return self::$pdo;
        }catch (mysqli_sql_exception $e)
        {
            return json_encode(var_dump(($e)));
        }
    }

    /**
     * Evita la clonación del objeto
     */
    final protected function __clone()
    {
    }

    function _destructor()
    {
        self::$pdo = null;
    }
}