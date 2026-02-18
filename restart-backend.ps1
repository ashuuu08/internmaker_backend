# Backend Restart Script
# This script will stop the current backend and restart it

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  InternMaker Backend Restart Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Stop existing Java processes
Write-Host "[1/4] Stopping existing backend server..." -ForegroundColor Yellow
$javaProcesses = Get-Process -Name java -ErrorAction SilentlyContinue

if ($javaProcesses) {
    Write-Host "Found $($javaProcesses.Count) Java process(es). Stopping..." -ForegroundColor Yellow
    $javaProcesses | Stop-Process -Force
    Start-Sleep -Seconds 2
    Write-Host "✓ Backend stopped successfully" -ForegroundColor Green
} else {
    Write-Host "No Java processes found (backend may not be running)" -ForegroundColor Gray
}

# Step 2: Navigate to backend directory
Write-Host ""
Write-Host "[2/4] Navigating to backend directory..." -ForegroundColor Yellow
$backendDir = "c:\Users\pc\Desktop\intern_maker\internmaker-backend\internmaker-backend"

if (Test-Path $backendDir) {
    Set-Location $backendDir
    Write-Host "✓ In directory: $backendDir" -ForegroundColor Green
} else {
    Write-Host "✗ Backend directory not found!" -ForegroundColor Red
    Write-Host "Expected: $backendDir" -ForegroundColor Red
    exit 1
}

# Step 3: Check if Maven wrapper exists
Write-Host ""
Write-Host "[3/4] Checking Maven..." -ForegroundColor Yellow

if (Test-Path ".\mvnw.cmd") {
    Write-Host "✓ Maven wrapper found" -ForegroundColor Green
    $mavenCmd = ".\mvnw.cmd"
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "✓ Maven found in PATH" -ForegroundColor Green
    $mavenCmd = "mvn"
} else {
    Write-Host "✗ Maven not found!" -ForegroundColor Red
    Write-Host "Please install Maven or use your IDE to run the backend" -ForegroundColor Red
    exit 1
}

# Step 4: Start the backend
Write-Host ""
Write-Host "[4/4] Starting backend server..." -ForegroundColor Yellow
Write-Host "This may take 30-60 seconds..." -ForegroundColor Gray
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Backend Starting..." -ForegroundColor Cyan
Write-Host "  Press Ctrl+C to stop" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANT: Look for these lines in the output:" -ForegroundColor Yellow
Write-Host "  - Mapped '{[/api/dashboard/me],methods=[GET]}'" -ForegroundColor Cyan
Write-Host "  - Mapped '{[/api/dashboard/enrollments],methods=[GET]}'" -ForegroundColor Cyan
Write-Host "  - Started InternmakerBackendApplication" -ForegroundColor Cyan
Write-Host ""

# Run Maven
& $mavenCmd spring-boot:run
