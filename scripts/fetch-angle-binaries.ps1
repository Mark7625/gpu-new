# Fetch ANGLE prebuilt binaries for Windows (libEGL.dll, libGLESv2.dll)
# Required for rlawt gles EGL/GLES mode on Windows
# Places them in libs/rlawt/windows-x86_64/ alongside rlawt.dll

param(
    [string]$OutputDir = (Join-Path $PSScriptRoot "..\libs\rlawt\windows-x86_64")
)

$ErrorActionPreference = "Stop"

Write-Host "=== ANGLE Binaries Fetch ===" -ForegroundColor Cyan

# Try Chromium-based browsers (Brave, Chrome, Edge) - they bundle ANGLE
$pf = $env:ProgramFiles
$pfx86 = [Environment]::GetFolderPath('ProgramFilesX86')
$browserPaths = @(
    (Join-Path $pf "BraveSoftware\Brave-Browser\Application"),
    (Join-Path $pf "Google\Chrome\Application"),
    (Join-Path $pf "Microsoft\Edge\Application")
)
if ($pfx86) {
    $browserPaths += (Join-Path $pfx86 "Google\Chrome\Application"), (Join-Path $pfx86 "Microsoft\Edge\Application")
}
foreach ($appDir in $browserPaths) {
    if (Test-Path $appDir) {
        $versionDirs = Get-ChildItem -Path $appDir -Directory | Sort-Object Name -Descending
        foreach ($verDir in $versionDirs) {
            $libEgl = Join-Path $verDir.FullName "libEGL.dll"
            $libGles = Join-Path $verDir.FullName "libGLESv2.dll"
            if ((Test-Path $libEgl) -and (Test-Path $libGles)) {
                Write-Host "Found ANGLE in: $($verDir.FullName)" -ForegroundColor Green
                New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
                Copy-Item $libEgl -Destination $OutputDir -Force
                Copy-Item $libGles -Destination $OutputDir -Force
                Write-Host "Copied libEGL.dll and libGLESv2.dll to $OutputDir" -ForegroundColor Green
                exit 0
            }
        }
    }
}

# Try vcpkg if available
if (Get-Command vcpkg -ErrorAction SilentlyContinue) {
    Write-Host "Using vcpkg to install ANGLE..." -ForegroundColor Green
    $vcpkgRoot = & vcpkg version 2>$null | Select-String "vcpkg version" | ForEach-Object { $_.Line }
    $angleDir = "$env:VCPKG_ROOT\installed\x64-windows\bin"
    if (Test-Path $angleDir) {
        New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
        Copy-Item "$angleDir\libEGL.dll" -Destination $OutputDir -Force -ErrorAction SilentlyContinue
        Copy-Item "$angleDir\libGLESv2.dll" -Destination $OutputDir -Force -ErrorAction SilentlyContinue
        if (Test-Path "$OutputDir\libEGL.dll") {
            Write-Host "Copied ANGLE DLLs from vcpkg to $OutputDir" -ForegroundColor Green
            exit 0
        }
    }
    vcpkg install angle:x64-windows
    $angleDir = "$env:VCPKG_ROOT\installed\x64-windows\bin"
    if (Test-Path "$angleDir\libEGL.dll") {
        New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
        Copy-Item "$angleDir\libEGL.dll" -Destination $OutputDir -Force
        Copy-Item "$angleDir\libGLESv2.dll" -Destination $OutputDir -Force
        Write-Host "Installed and copied ANGLE DLLs to $OutputDir" -ForegroundColor Green
        exit 0
    }
}

# Fallback: manual setup instructions
Write-Host "ANGLE DLLs not found in browsers or vcpkg." -ForegroundColor Yellow
Write-Host ""
Write-Host "Options:" -ForegroundColor Cyan
Write-Host "1. Install Brave, Chrome, or Edge - this script will copy their bundled ANGLE" -ForegroundColor White
Write-Host "2. vcpkg: vcpkg install angle:x64-windows" -ForegroundColor White
Write-Host "3. Place libEGL.dll and libGLESv2.dll in: $OutputDir" -ForegroundColor White
