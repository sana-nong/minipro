@echo off
title GovOpp Demo Launcher
echo ======================================================================
echo  Centralized Government Opportunity Platform - Live Demo Launcher
echo  Siddaganga Institute of Technology, Tumkur (Dept. of CSE)
echo ======================================================================
echo.

echo [1/2] Launching Spring Boot Backend (Port 8081)...
start "GovOpp Backend (8081)" cmd /k "cd /d C:\Users\nanhn\.gemini\antigravity\scratch\gov-opportunity-platform\backend && java -jar target\gov-opportunity-backend-1.0.0.jar"

echo Waiting for backend initialization...
ping 127.0.0.1 -n 5 >nul

echo [2/2] Launching React Vite Frontend (Port 5173)...
start "GovOpp Frontend (5173)" cmd /k "cd /d C:\Users\nanhn\.gemini\antigravity\scratch\gov-opportunity-platform\frontend && npm.cmd run dev -- --host"

echo.
echo ======================================================================
echo  Systems Launched Successfully!
echo  Opening http://localhost:5173 in your default browser...
echo ======================================================================
ping 127.0.0.1 -n 3 >nul
start http://localhost:5173
