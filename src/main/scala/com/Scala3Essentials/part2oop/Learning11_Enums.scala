package com.Scala3Essentials.part2oop

object Learning11_Enums {


  enum Permissions { // enums are used to define type and all the possible instances of that type
    case READ, WRITE, EXECUTE, NONE

    // since enum are used to define type we can have methods inside it
    def openDocument() : Unit =
      if(this == READ) println("opening document")
      else println("reading not allowed")
  }

  // we can have constructor arguments for enums
  enum PermissionWithBits(bits: Int) {
    case READ extends PermissionWithBits(4)
    case WRITE extends PermissionWithBits(2)
    case EXECUTE extends PermissionWithBits(1)
    case NONE extends PermissionWithBits(0)
  }

  // we can have companion objects for enums just like another classes
  object PermissionWithBits {
    def fromBits(bits : Int) : PermissionWithBits =
      PermissionWithBits.NONE
  }

  def main(args: Array[String]): Unit = {

    val somePermissions = Permissions.READ
    println(somePermissions)
    somePermissions.openDocument()
    Permissions.NONE.openDocument()

    val permissionWithBits = PermissionWithBits.EXECUTE
    println(permissionWithBits)
    println(PermissionWithBits.fromBits(4))

    // standard API of enums
    println(somePermissions.ordinal) // ordinal shows the position where the instance of the enum was declared starts with 0
    println(PermissionWithBits.values.toList)  // array of all possible values of the enum
    println(Permissions.valueOf("EXECUTE"))  // returns the case defined in enum which matches with the one we sent

  }
}
