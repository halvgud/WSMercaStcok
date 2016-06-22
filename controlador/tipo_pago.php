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


    }