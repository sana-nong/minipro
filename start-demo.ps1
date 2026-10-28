Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host " Centralized Government Opportunity Platform - Live Demo Launcher" -ForegroundColor Yellow
Write-Host " Siddaganga Institute of Technology, Tumkur (Dept. of CSE)" -ForegroundColor White
Write-Host "======================================================================" -ForegroundColor Cyan

Write-Host "`n[1/2] Starting Spring Boot Backend on Port 8081..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; mvn spring-boot:run"

Write-Host "[2/2] Waiting for backend to bind, then starting React Frontend..." -ForegroundColor Yellow
Start-Sleep -Seconds 6

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm.cmd run dev"

Write-Host "`n✓ System services started!" -ForegroundColor Green
Write-Host "  Frontend: http://localhost:5173" -ForegroundColor White
Write-Host "  Backend API: http://localhost:8081/api/opportunities" -ForegroundColor White
Write-Host "  Database Console: http://localhost:8081/h2-console" -ForegroundColor White
