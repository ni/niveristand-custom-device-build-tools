@echo off
echo "Removing old build artifacts..."
rmdir /s /q "dependency-test-1"
rmdir /s /q "dependency-test-2"
rmdir /s /q "dependency-test-3"
rmdir /s /q "output"

echo "Building in LabVIEW 2023 64-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files\National Instruments\LabVIEW 2023\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2023\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2023\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2023\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2023\x64\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x64\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x64\Linux_x64"

echo "Creating directories..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2023\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2023\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2023\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2023\x64\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x64\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x64\Linux_x64"

echo "Building in LabVIEW 2024 64-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files\National Instruments\LabVIEW 2024\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2024\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2024\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2024\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2024\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2024\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2024\x64\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2024\x64\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2024\x64\Linux_x64"

echo "Creating directories..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2024\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2024\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2024\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2024\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2024\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2024\x64\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2024\x64\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2024\x64\Linux_x64"

echo "Building in LabVIEW 2025 64-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files\National Instruments\LabVIEW 2025\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2025\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2025\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2025\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2025\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2025\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2025\x64\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2025\x64\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2025\x64\Linux_x64"

echo "Creating directories..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2025\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2025\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2025\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2025\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2025\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2025\x64\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2025\x64\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2025\x64\Linux_x64"

echo "add .finished documents..."
type nul > "%cd%\dependency-test-1\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-1\ni\export\main\12345678.2\.finished"
type nul > "%cd%\dependency-test-2\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-2\ni\export\main\12345678.2\.finished"
type nul > "%cd%\dependency-test-3\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-3\ni\export\main\12345678.2\.finished"
echo "complete!"
pause