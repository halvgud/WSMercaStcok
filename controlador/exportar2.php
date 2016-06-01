<?php

    class exportar2{
        public static function post($peticion)
        {
            if ($peticion[0] == 'exportarInventarioWS') {
                return self::exportarInventarioWS();
            }else {
                throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
            }
        }


    }