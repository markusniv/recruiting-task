import java.util.*

/**
 *      (c) Markus Nivasalo / 2022
 *
 *      This is a console application client made for a Visma Solutions recruiting programming task. It utilizes the
 *      Identify class included in this package. Main documentation and thoughts on the project are included in the class
 *      file Identify.kt.
 *
 *      The client allows the user to enter a scheme and a location to log into, after which it allows the user to confirm
 *      payments and sign documents in that source until they decide quit the application.
 */

fun main(args: Array<String>) {

    println("-- VISMA IDENTITY MANAGEMENT --")
    while(true) {
        println("Enter a scheme: ")
        val scheme = readLine()
        println("Where do you wish to login?")
        val loginSource = readLine()
        try {
            val login = Identify("$scheme://login?source=$loginSource")
            val result = login.action()
            println("Logged in succesfully to ${result!!.source}!")
        } catch (e : Exception) {
            println("Login failed!")
            continue
        }
        while (true) {
            println("What do you wish to do?")
            println("a) Confirm")
            println("b) Sign")
            println("q) Quit")
            when (readLine()) {
                "a" -> {
                    println("Please enter a payment number: ")
                    val paymentNumber = readLine()
                    try {
                        val confirm = Identify("$scheme://confirm?source=$loginSource&paymentnumber=$paymentNumber")
                        val result = confirm.action()
                        println("Payment confirmed succesfully in ${result!!.source} on number ${result.key}!")
                    } catch (e : Exception) {
                        println("Payment failed!")
                    }
                }
                "b" -> {
                    println("Generating a UUID for the document...")
                    try {
                        val uuid : UUID = UUID.randomUUID()
                        val sign = Identify("$scheme://sign?source=$loginSource&documentid=$uuid")
                        val result = sign.action()
                        println("Signature completed succesfully in ${result!!.source} with UUID ${result.key}")
                    } catch (e : Exception) {
                        println("Error generating a proper UUID!")
                    }
                }
                "q" -> break
                else -> {
                    println("Invalid choice!")
                    continue
                }
            }
            println("Do you wish to do more actions?")
            println("a) Yes")
            println("b) No")
            when (readLine()) {
                "a" -> continue
                "b" -> break
                else -> break
            }
        }
        println("Do you wish to continue using the application?")
        println("a) Yes")
        println("b) No")
        when (readLine()) {
            "a" -> continue
            "b" -> break
            else -> break
        }
    }

}
