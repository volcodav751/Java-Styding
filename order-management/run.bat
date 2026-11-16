@echo off
chcp 65001 >nul
cd /d "%~dp0"
call mvn -q clean package
if errorlevel 1 (
    pause
    exit /b 1
)
java -Dfile.encoding=UTF-8 --module-path "order-threads\target\order-threads-1.0.0.jar;order-threads\target\lib" -m order.threads/com.example.order.threads.Main
if errorlevel 1 (
    pause
    exit /b 1
)
pause
