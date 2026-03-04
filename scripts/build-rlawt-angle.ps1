# Build rlawt from Adam-/rlawt gles branch (ANGLE/EGL support)
# Requires: Git, CMake, Visual Studio Build Tools (or VS with C++ workload), JDK 11+
# See: https://github.com/Adam-/rlawt/commits/gles

param(
    [string]$RlawtDir = (Join-Path $PSScriptRoot "..\rlawt"),
    [string]$BuildDir = (Join-Path $PSScriptRoot "..\rlawt\build"),
    [string]$OutputDir = (Join-Path $PSScriptRoot "..\libs\rlawt"),
    [switch]$Clean
)

$ErrorActionPreference = "Stop"

Write-Host "=== rlawt ANGLE Build Script ===" -ForegroundColor Cyan
Write-Host ""

# Check prerequisites
function Test-Command($cmd) {
    try {
        Get-Command $cmd -ErrorAction Stop | Out-Null
        return $true
    } catch {
        return $false
    }
}

if (-not (Test-Command "git")) {
    Write-Host "ERROR: Git is required. Install from https://git-scm.com/" -ForegroundColor Red
    exit 1
}

if (-not (Test-Command "cmake")) {
    Write-Host "ERROR: CMake is required. Install from https://cmake.org/download/" -ForegroundColor Red
    exit 1
}

# Find Visual Studio and add to PATH (wildcard must be resolved - * doesn't expand in PATH)
$vsWhere = "${env:ProgramFiles(x86)}\Microsoft Visual Studio\Installer\vswhere.exe"
if (Test-Path $vsWhere) {
    $vsPath = & $vsWhere -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath 2>$null
    if ($vsPath) {
        $msvcDir = Get-ChildItem -Path (Join-Path $vsPath "VC\Tools\MSVC") -Directory -ErrorAction SilentlyContinue | Sort-Object Name -Descending | Select-Object -First 1
        if ($msvcDir) {
            $clPath = Join-Path $msvcDir.FullName "bin\Hostx64\x64"
            $env:PATH = "$clPath;$env:PATH"
        }
    }
}

if (-not (Test-Command "cl")) {
    Write-Host "ERROR: Visual Studio C++ compiler (cl.exe) not found." -ForegroundColor Red
    Write-Host "Try running from 'Developer PowerShell for VS' or 'x64 Native Tools Command Prompt'." -ForegroundColor Yellow
    Write-Host "Or ensure 'Desktop development with C++' workload is installed." -ForegroundColor Yellow
    exit 1
}

# Find JDK (CMake needs it for JNI headers)
$jdkFound = $false
if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME "bin\javac.exe"))) {
    $jdkFound = $true
    Write-Host "Using JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Gray
}
if (-not $jdkFound) {
    $javaExe = (Get-Command java -ErrorAction SilentlyContinue).Path
    if ($javaExe) {
        $javaDir = (Get-Item $javaExe).Directory.FullName
        $candidateHome = (Get-Item $javaDir).Parent.FullName
        if (Test-Path (Join-Path $candidateHome "bin\javac.exe")) {
            $env:JAVA_HOME = $candidateHome
            $jdkFound = $true
            Write-Host "Using JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Gray
        }
    }
}
if (-not $jdkFound) {
    $searchPaths = @(
        (Join-Path ${env:ProgramFiles} "Java\*"),
        (Join-Path ${env:ProgramFiles} "Eclipse Adoptium\*"),
        (Join-Path ${env:ProgramFiles} "Microsoft\jdk-*"),
        (Join-Path ${env:ProgramFiles} "Eclipse Foundation\*")
    )
    foreach ($pattern in $searchPaths) {
        $dirs = Get-ChildItem -Path $pattern -Directory -ErrorAction SilentlyContinue
        foreach ($dir in $dirs) {
            if (Test-Path (Join-Path $dir.FullName "bin\javac.exe")) {
                $env:JAVA_HOME = $dir.FullName
                $jdkFound = $true
                Write-Host "Using JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Gray
                break
            }
        }
        if ($jdkFound) { break }
    }
}
if (-not $jdkFound) {
    Write-Host "ERROR: JDK not found. Set JAVA_HOME or install JDK." -ForegroundColor Red
    Write-Host "  Example: `$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'" -ForegroundColor Yellow
    Write-Host "  Download: https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

# Clone or update rlawt
if (-not (Test-Path $RlawtDir)) {
    Write-Host "Cloning rlawt gles branch..." -ForegroundColor Green
    git clone --branch gles --depth 1 https://github.com/Adam-/rlawt.git $RlawtDir
} else {
    Write-Host "Updating rlawt..." -ForegroundColor Green
    Push-Location $RlawtDir
    git fetch origin gles
    git checkout gles
    git pull origin gles
    Pop-Location
}

if ($Clean -and (Test-Path $BuildDir)) {
    Write-Host "Cleaning build directory..." -ForegroundColor Yellow
    Remove-Item -Recurse -Force $BuildDir
}

# Create build directory
New-Item -ItemType Directory -Force -Path $BuildDir | Out-Null

# Configure and build
Write-Host "Configuring with CMake..." -ForegroundColor Green
Push-Location $BuildDir

try {
    $generators = @(
        @{ Name = "Visual Studio 17 2022"; Arch = "x64" },
        @{ Name = "Visual Studio 16 2019"; Arch = "x64" },
        @{ Name = "Visual Studio 15 2017"; Arch = "x64" }
    )
    $configured = $false
    foreach ($gen in $generators) {
        Write-Host "Trying generator: $($gen.Name)..." -ForegroundColor Gray
        $cmakeOut = cmake -G $gen.Name -A $gen.Arch $RlawtDir 2>&1
        if ($LASTEXITCODE -eq 0) {
            $configured = $true
            break
        }
    }
    if (-not $configured) {
        Write-Host "ERROR: CMake configuration failed. Install Visual Studio with C++ workload." -ForegroundColor Red
        exit 1
    }

    Write-Host "Building..." -ForegroundColor Green
    cmake --build . --config Release
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Build failed." -ForegroundColor Red
        exit 1
    }
} finally {
    Pop-Location
}

# Copy output
$rlawtDll = Get-ChildItem -Path $BuildDir -Recurse -Filter "rlawt.dll" | Select-Object -First 1
if (-not $rlawtDll) {
    Write-Host "ERROR: rlawt.dll not found in build output." -ForegroundColor Red
    exit 1
}

New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null
$arch = if ([Environment]::Is64BitOperatingSystem) { "x86_64" } else { "x86" }
$targetDir = "$OutputDir\windows-$arch"
New-Item -ItemType Directory -Force -Path $targetDir | Out-Null

Copy-Item $rlawtDll.FullName -Destination $targetDir -Force
Write-Host ""
Write-Host "Build complete! Output: $(Join-Path $targetDir 'rlawt.dll')" -ForegroundColor Green
Write-Host ""

# ANGLE DLLs - use existing in output dir, else fetch there
$angleEgl = Join-Path $targetDir "libEGL.dll"
if (Test-Path $angleEgl) {
    Write-Host "ANGLE DLLs already in output" -ForegroundColor Green
} else {
    Write-Host "Fetching ANGLE DLLs (Brave/Chrome/Edge or vcpkg)..." -ForegroundColor Cyan
    & (Join-Path $PSScriptRoot "fetch-angle-binaries.ps1") -OutputDir $targetDir
    if (Test-Path $angleEgl) {
        Write-Host "ANGLE DLLs ready" -ForegroundColor Green
    } else {
        Write-Host "ANGLE DLLs not found. Install Brave, Chrome, or Edge, or run: .\scripts\fetch-angle-binaries.ps1" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "To use with RuneLite, add to JVM arguments:" -ForegroundColor Cyan
$rlawtPath = Join-Path (Resolve-Path $targetDir).Path "rlawt.dll"
Write-Host "  -Drunelite.rlawtpath=$rlawtPath" -ForegroundColor White
Write-Host ""
