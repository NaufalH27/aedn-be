@echo off
setlocal enabledelayedexpansion

:: Get git repo root
for /f "delims=" %%i in ('git rev-parse --show-toplevel 2^>nul') do set root=%%i

if not defined root (
    echo Not inside a Git repository 1>&2
    exit /b 1
)

call "%root%\gradlew.bat" build

:: Get version from Gradle properties
for /f "tokens=2 delims=: " %%i in ('"%root%\gradlew.bat" -q properties ^| findstr "^version:"') do set version=%%i

set jar=%root%\build\libs\aedn-%version%.jar

java -jar "%jar%" createAdmin
