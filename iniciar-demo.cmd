@echo off
cd /d "%~dp0"
echo FutureVet - demonstracao local. Acesse http://localhost:8080
echo Tutor: tutor@futurevet.local  Clinica: clinica@futurevet.local
echo Senha de demonstracao: Futurevet123!
java -Dfile.encoding=UTF-8 -jar executavel/futurevet.jar --spring.profiles.active=demo
pause
