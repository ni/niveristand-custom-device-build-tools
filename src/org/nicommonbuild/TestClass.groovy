package org.vscommonbuild

class TestClass {
  String name
  def script
  
  TestClass(script, name) {
    this.script = script
	this.name = name
  }
  
  void execute() {
	script.echo "Name is $name."
  }
}