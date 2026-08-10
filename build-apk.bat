@echo off
echo Construyendo APK de debug para testing...
gradlew.bat assembleDebug --console=plain
echo.
echo APK generado en: app\build\outputs\apk\debug\app-debug.apk
echo Compartilo por WhatsApp / email y que lo instalen como APK normal.
pause
