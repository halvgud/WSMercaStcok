<?php

/**
 * Created by PhpStorm.
 * User: Ryu
 * Date: 2016-08-15
 * Time: 15:26
 */
class ajuste
{
    public static function post($peticion)
    {
        if ($peticion[0] == 'actualizar') {
            return self::actualizar();
        }
        else {
            throw new ExcepcionApi(404, "Url mal formada", 404);
        }
    }

    public static function actualizar(){
        $bandera            = false;
        $dbAjusteNegativo   = null;
        $dbAjustePositivo   = null;
        $arreglo            =
            ["estado"       => 200,
                "success"       => "OK",
                "data"          => "OK"];
        $codigo             = 200;


        return "";
    }
}