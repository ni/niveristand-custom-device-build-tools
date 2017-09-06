package org.nicommonbuild

class TestClass {
  String name
  def script
  
  TestClass(script) {
    this.script = script
  }
  
  void execute() {
	script.echo "Name is $name."
  }
}