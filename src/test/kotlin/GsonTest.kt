import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class GsonTest {

    /**
     * Deserialize an Object with a non-nullable field
     */
    @Test(expected = NullPointerException::class)
    fun npeInKotlin() {
        data class NetworkResponse(val value: String)

        Gson().fromJson<NetworkResponse>("{}", NetworkResponse::class.java).value
    }

    /**
     * Wait.. what if we add a default value?
     */
    @Test(expected = NullPointerException::class)
    fun npeInKotlinAgain() {
        data class NetworkResponse(val value: String = "")

        Gson().fromJson<NetworkResponse>("{}", NetworkResponse::class.java).value
    }

    /**
     * Ok you win!!! Make it nullable... but I want an empty string as a default value
     */
    @Test(expected = AssertionError::class)
    fun npeInKotlinAgainAgain() {
        data class NetworkResponse(val value: String? = "")

        assertEquals(
            Gson().fromJson<NetworkResponse>("{}", NetworkResponse::class.java)?.value,
            ""
        )
    }

    /**
     * C'mon Gson you are killing me! All null!
     */
    @Test
    fun noCrash() {
        data class NetworkResponse(val value: String? = null)

        assertEquals(Gson().fromJson<NetworkResponse>("{}", NetworkResponse::class.java)?.value, null)
    }
}
