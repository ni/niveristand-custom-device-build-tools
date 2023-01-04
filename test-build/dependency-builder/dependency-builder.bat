@echo off
echo "Removing old build artifacts..."
rmdir /s /q "dependency-test-1"
rmdir /s /q "dependency-test-2"
rmdir /s /q "dependency-test-3"
rmdir /s /q "output"


echo "Building in LabVIEW 2020 32-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files (x86)\National Instruments\LabVIEW 2020\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories for library 1..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2020\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2020\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2020\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2020\x86\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2020\x86\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2020\x86\Linux_x64"

echo "Creating directories for library 2..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2020\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2020\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2020\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2020\x86\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2020\x86\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2020\x86\Linux_x64"

echo "Creating directories for library 3..."
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2020\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2020\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2020\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2020\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2020\x86\Linux_x64"
copy "output\Windows\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2020\x86\Windows"
copy "output\Linux_x64\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2020\x86\Linux_x64"


echo "Building in LabVIEW 2021 32-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files (x86)\National Instruments\LabVIEW 2021\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories for library 1..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2021\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2021\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2021\x86\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x86\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x86\Linux_x64"

echo "Creating directories for library 2..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2021\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2021\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2021\x86\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x86\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x86\Linux_x64"

echo "Creating directories for library 3..."
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2021\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2021\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2021\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2021\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2021\x86\Linux_x64"
copy "output\Windows\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2021\x86\Windows"
copy "output\Linux_x64\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2021\x86\Linux_x64"

echo "Building in LabVIEW 2021 64-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files\National Instruments\LabVIEW 2021\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2021\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2021\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x64\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2021\x64\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2021\x64\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x64\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2021\x64\Linux_x64"

echo "Creating directories..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2021\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2021\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x64\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2021\x64\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2021\x64\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x64\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2021\x64\Linux_x64"


echo "Building in LabVIEW 2023 32-bit..."
LabVIEWCLI ^
  -PortNumber 3363 ^
  -LabVIEWPath "C:\Program Files (x86)\National Instruments\LabVIEW 2023\LabVIEW.exe" ^
  -AdditionalOperationDirectory "%cd%\..\..\lv\operations" ^
  -OperationName "ExecuteAllBuildSpecs" ^
  -ProjectPath "%cd%\DependencyBuilder.lvproj"
taskkill /im labview.exe /f

echo "Creating directories for library 1..."
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2023\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.1\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2023\x86\Windows"
mkdir "%cd%\dependency-test-1\ni\export\main\12345678.3\2023\x86\Linux_x64"
copy "output\Windows\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x86\Windows"
copy "output\Linux_x64\DependencyLibrary1.lvlibp" "%cd%\dependency-test-1\ni\export\main\12345678.2\2023\x86\Linux_x64"

echo "Creating directories for library 2..."
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2023\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.1\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2023\x86\Windows"
mkdir "%cd%\dependency-test-2\ni\export\main\12345678.3\2023\x86\Linux_x64"
copy "output\Windows\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x86\Windows"
copy "output\Linux_x64\DependencyLibrary2.lvlibp" "%cd%\dependency-test-2\ni\export\main\12345678.2\2023\x86\Linux_x64"

echo "Creating directories for library 3..."
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2023\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.1\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2023\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.2\2023\x86\Linux_x64"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2023\x86\Windows"
mkdir "%cd%\dependency-test-3\ni\export\main\12345678.3\2023\x86\Linux_x64"
copy "output\Windows\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2023\x86\Windows"
copy "output\Linux_x64\DependencyLibrary3.lvlibp" "%cd%\dependency-test-3\ni\export\main\12345678.2\2023\x86\Linux_x64"

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

echo "add .finished documents..."
type nul > "%cd%\dependency-test-1\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-1\ni\export\main\12345678.2\.finished"
type nul > "%cd%\dependency-test-2\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-2\ni\export\main\12345678.2\.finished"
type nul > "%cd%\dependency-test-3\ni\export\main\12345678.1\.finished"
type nul > "%cd%\dependency-test-3\ni\export\main\12345678.2\.finished"
echo "complete!"
pause