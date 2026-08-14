@echo off
echo Construyendo AAB de release para Play Store...
if not exist keystore.properties (
  echo Falta keystore.properties.
  echo Copia keystore.properties.example a keystore.properties y genera el upload key:
  echo   keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
  echo La validez debe superar el 22 oct 2033. No subas el .jks a git.
  pause
  exit /b 1
)
call gradlew.bat bundleRelease --console=plain
if errorlevel 1 (
  echo Fallo bundleRelease.
  pause
  exit /b 1
)
echo.
echo AAB en: app\build\outputs\bundle\release\app-release.aab
echo Sube ese archivo a Play Console. No subas APK debug.
pause
