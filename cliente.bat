@echo off
IF "%1"=="" goto noparam 
java -cp .;vrml97.jar Juego %1 %2
goto fin

:noparam
java -cp .;vrml97.jar Juego localhost %2

:fin