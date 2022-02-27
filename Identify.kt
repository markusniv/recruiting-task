/**
 *      (c) Markus Nivasalo / 2022
 *
 *      This is a class made for a Visma Solutions recruiting programming task. The task was to create a class that
 *      receives an uri in a string form and picks up the three main parts of the uri, for example:
 *
 *      uri: visma-identity://login?source=severa
 *
 *      Scheme: visma-identity
 *      Path(action): login
 *      Parameters: source=severa
 *
 *      The class then acts accordingly depending on the action given to it. This was to be used in a client that could be
 *      a console application for example. The client can be found in Main.kt.
 *
 *      I personally decided to write the class and client in Kotlin as I feel the most comfortable with it. I understood
 *      the task so that I was to simply parse through the uri to pick up the correct parts. Therefore, most of the logic
 *      is done via string splitting since we know what kind of uri's to expect, so we can just do the splits accordingly.
 *      I first check if the schema is correct and then pick the source parameter as it is in all the actions. After that,
 *      I use a when expression to go through the different actions to parse and do the action specific things.
 *
 *      The class was to 'return an action' according to the instructions, so I decided to create a data class Action that
 *      includes as variables the action which was taken, the source it was done to as well as a key of Any type which
 *      gets the value of the action specific parameters in confirm and sign; therefore it can either be a UUID or a String.
 *      This can, however, be null, as the login action doesn't utilize these. Each of the actions returns a new Action with
 *      the parsed parameters. These are then used in the client side.
 *
 *      I don't think I had any particular challenges implementing the program, though I do think I might've taken some
 *      liberties with it. For instance, I wasn't sure if the class was only supposed to return the action in a string form
 *      like 'return "confirm"', so I decided to simply make a data class to include everything relevant. I tried to make
 *      the application relatively safe with many try/catches; I could've perhaps added more simple null checks instead of
 *      utilizing the !! operator but I decided it wasn't needed here because the NPE's would be caught by the try/catches
 *      regardless. This could be improved upon, however, were I to make the application bigger than just a simple test piece.
 *
 */

class Identify(uri: String) {
    private val uri: String

    init {
        this.uri = uri
    }

    // Main private function for parsing through the uri
    private fun validate() : Action? {
        try {
            val arr = uri.split("://")
            val scheme = arr[0]
            if (scheme != "visma-identity") {
                println("Scheme not proper! Should be 'visma-identity', found '$scheme'!")
                return null
            }
            val action = arr[1].split("?")[0]
            val params = arr[1].split("?")[1]
            val source = params.split("=")[1].split("&")[0]
            when(action) {
                "login" -> {
                    return Action(action, source)
                }
                "confirm" -> {
                    val paymentNumber = params.split("paymentnumber=")[1]
                    try {
                        paymentNumber.toInt() // Checking that the input is an actual number
                    } catch (e : Exception) {
                        println("Payment number not a valid number!")
                        return null
                    }
                    return Action(action, source, paymentNumber)
                }
                "sign" -> {
                    val documentId = params.split("documentid=")[1]
                    return Action(action, source, documentId)
                }
                else -> {
                    println("URI not proper!")
                    return null
                }

            }
        } catch (e : Exception) {
            println("URI not proper!")
            return null
        }
    }

    // Public function used by client for uri validation
    fun action() : Action?{
        return validate()
    }
}

// Data class which the validation creates an object of using the parsed uri data to return to the client
data class Action (
    val action: String,
    val source: String,
    val key: Any? = null,
)
