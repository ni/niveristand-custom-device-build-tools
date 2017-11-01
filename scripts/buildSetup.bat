@echo off
set "venv_path=env"

pip install virtualenv

virtualenv %venv_path%

call %venv_path%\\Scripts\\activate.bat

pip install toml
python commonbuild\\scripts\\toml2json.py build.toml

call %venv_path%\\Scripts\\deactivate.bat

rmdir /s /q %venv_path%

@echo on
