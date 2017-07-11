class MyClass implements Serializable {
  String name
  Integer age
  
  def increaseAge(Integer years){
    this.age += years
  }
}
