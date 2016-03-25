<?php

require 'controlador/articulo.php';
require 'controlador/usuario.php';
require 'controlador/sincronizar.php';
require 'controlador/sucursal.php';
require 'controlador/categoria.php';
require 'vista/VistaJson.php';
require 'utilidad/ExcepcionApi.php';



// Constantes de estado
const ESTADO_URL_INCORRECTA = 2;
const ESTADO_EXISTENCIA_RECURSO = 3;
const ESTADO_METODO_NO_PERMITIDO = 4;

// Preparar manejo de excepciones
$formato = isset($_GET['formato']) ? $_GET['formato'] : 'json';

switch ($formato) {
    /*case 'xml':
        $vista = new VistaXML();
        break;*/
    case 'json':
    default:
        $vista = new VistaJson();
}

set_exception_handler(function ($exception) use ($vista) {
    $cuerpo = array(
        "estado" => $exception->estado,
        "mensaje" => utf8_encode($exception->getMessage())
    );
    if ($exception->getCode()) {
        $vista->estado = $exception->getCode();
    } else {
        $vista->estado = 500;
    }

    $vista->imprimir($cuerpo);
}
);
// Extraer segmento de la url
if (isset($_GET['PATH_INFO'])) {
    $segmentos = explode('/', $_GET['PATH_INFO']);

} else {
    throw new ExcepcionApi(ESTADO_URL_INCORRECTA, utf8_encode("No se reconoce la petición"));
}

// Obtener recurso
$recurso = array_shift($segmentos);
$recursos_existentes = array('articulo', 'usuario', 'sincronizar','sucursal','categoria');

// Comprobar si existe el recurso
if (!in_array($recurso, $recursos_existentes)) {
    throw new ExcepcionApi(ESTADO_EXISTENCIA_RECURSO,
        "No se reconoce el recurso al que intentas acceder");
}

$metodo = strtolower($_SERVER['REQUEST_METHOD']);

// Filtrar metodo
switch ($metodo) {
    case 'get':
    case 'post':
    case 'put':
    case 'delete':
        if (method_exists($recurso, $metodo)) {
            $respuesta = call_user_func(array($recurso, $metodo), $segmentos);
            $vista->imprimir($respuesta);
            break;
        }
    default:
        // Método no aceptado
        $vista->estado = 405;
        $cuerpo = [
            "estado" => ESTADO_METODO_NO_PERMITIDO,
            "mensaje" => utf8_encode("Método no permitido")
        ];
        $vista->imprimir($cuerpo);

}


