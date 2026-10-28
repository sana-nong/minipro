@echo off
echo ======================================================================
echo  Centralized Government Opportunity Platform - Live Demo Launcher
echo  Siddaganga Institute of Technology, Tumkur (Dept. of CSE)
echo ======================================================================
echo.

echo Starting Spring Boot Backend (Port 8081)...
start "GovOpp Backend (Spring Boot)" cmd /k "cd backend && mvn spring-boot:run"

echo Waiting for backend initialization...
timeout /t 6 /nobreak >nul

echo Starting React Vite Frontend (Port 5173)...
start "GovOpp Frontend (React Vite)" cmd /k "cd frontend && npm.cmd run dev"

echo.
echo ======================================================================
echo  Systems Launched Successfully!
echo  Frontend UI: http://localhost:5173
echo  Backend API: http://localhost:8081/api/opportunities
echo  H2 DB Console: http://localhost:8081/h2-console
echo ======================================================================
pause
