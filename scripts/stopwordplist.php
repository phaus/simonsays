<?php
date_default_timezone_set('Europe/Berlin');
require_once("stopwordplist.inc");
 

echo "public class StopList {\n";

foreach($stopwords as $lang => $arr){
	echo "\tpublic final static String WORDS_".strtoupper($lang)."[] = {\n";
    foreach($arr as $value){
        echo "\t\t\"".$value."\",\n";
    }
	echo "\t\t\"\"\n";
}
echo "\t};\n";
echo "}\n";   
?>