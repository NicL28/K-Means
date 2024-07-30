@echo off

REM viene avviato il client MySQL e eseguito lo script sql
mysql -u root -p < createDB.sql
pause