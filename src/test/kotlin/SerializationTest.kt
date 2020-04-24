import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.assertEquals
import org.junit.Test

class SerializationTest {

    private val json = Json(JsonConfiguration.Stable)

    @Test(expected = SerializationException::class)
    fun serializationException() {
        @Serializable
        data class NetworkResponse(val value: String)

        val actual = json.parse(NetworkResponse.serializer(), "{}")

        assertEquals(actual.value, "")
    }

    @Test
    fun noCrash() {
        @Serializable
        data class NetworkResponse(val value: String = "")

        val actual = json.parse(NetworkResponse.serializer(), "{}")

        assertEquals(actual.value, "")
    }

    @Test(expected = SerializationException::class)
    fun enumDefaultValue() {
        @Serializable
        data class NetworkResponse(val type: Type = Type.UNKNOWN)

        json.parse(NetworkResponse.serializer(), """{"type": "C"}""")

        // Waiting for https://github.com/Kotlin/kotlinx.serialization/issues/90
    }

    @Serializable
    enum class Type { A, B, UNKNOWN }
}
