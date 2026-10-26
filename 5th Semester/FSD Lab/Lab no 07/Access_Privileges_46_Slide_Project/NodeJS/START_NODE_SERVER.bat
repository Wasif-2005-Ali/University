@echo off
title Access Privileges - Lecture 46 Node Server
cd /d "%~dp0"
where node >nul 2>nul
if errorlevel 1 (
    echo Node.js was not found. Install Node.js and try again.
    pause
    exit /b 1
)
if not exist "node_modules\express" (
    echo Installing dependencies for the first run...
    call npm install
    if errorlevel 1 (
        echo Installation failed. Check your internet connection.
        pause
        exit /b 1
    )
)
echo Starting Node API on http://localhost:3000
node access-forRequest.js
pause
