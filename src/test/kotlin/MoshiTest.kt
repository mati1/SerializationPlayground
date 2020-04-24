import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import MoshiTest.NetworkResponse3.Type3
import MoshiTest.NetworkResponse4.Type4
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class MoshiTest {

    private val moshiBuilder: Moshi.Builder by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()) }

    private val moshi: Moshi by lazy { moshiBuilder.add(KotlinJsonAdapterFactory()).build() }

    @JsonClass(generateAdapter = true)
    data class NetworkResponse1(val value: String)

    @JsonClass(generateAdapter = true)
    data class NetworkResponse2(val value: String = "")

    @Test(expected = JsonDataException::class)
    fun jsonDataException() {
        moshi.adapter(NetworkResponse1::class.java).fromJson("{}".trimIndent())?.value
    }

    @Test
    fun noCrash() {
        val actual = moshi.adapter(NetworkResponse2::class.java).fromJson("{}".trimIndent())

        assertEquals("", actual?.value)
    }

    @JsonClass(generateAdapter = true)
    data class NetworkResponse3(val type: Type3) {

        enum class Type3 { A, B, UNKNOWN }
    }

    @Test
    fun enumDefaultValue() {
        val actualType = moshiBuilder
            .add(Type3::class.java, EnumJsonAdapter.create(Type3::class.java).withUnknownFallback(Type3.UNKNOWN))
            .build()
            .adapter(NetworkResponse3::class.java).fromJson(
                """
                {
                    "type": "C"
                }
            """.trimIndent()
            )
            ?.type

        assertEquals(Type3.UNKNOWN, actualType)
    }

    @JsonClass(generateAdapter = true)
    data class NetworkResponse4(val type: Type4?) {

        enum class Type4 { A, B }
    }

    @Test
    fun enumDefaultNullValue() {
        val actualType = moshiBuilder
            .add(Type4::class.java, EnumJsonAdapter.create(Type4::class.java).withUnknownFallback(null))
            .build()
            .adapter(NetworkResponse4::class.java).fromJson(
                """
                {
                    "type": "C"
                }
            """.trimIndent()
            )
            ?.type

        Assert.assertNull(actualType)
    }
}
