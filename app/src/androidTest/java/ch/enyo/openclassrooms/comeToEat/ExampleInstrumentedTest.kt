package ch.enyo.openclassrooms.comeToEat

import ch.enyo.openclassrooms.comeToEat.utils.hasNetworkAvailable

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.enyo.openclassrooms.comeToEat.api.RecipeStream
import ch.enyo.openclassrooms.comeToEat.models.Result
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private var mContext: Context? = null

    @Before
    fun setup() {
        mContext = InstrumentationRegistry.getInstrumentation()
            .targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ch.enyo.openclassrooms.comeToEat", appContext.packageName)
    }
    @Test
    fun isInternetAvailableText(){
        assertTrue(hasNetworkAvailable(mContext))
    }

    @Test
    fun fetchRecipesTest(){
        //1 - Get the stream
        val map = mutableMapOf<String,String>()
        map["q"]= "Fish"
      val observableResult: Observable<Result> = RecipeStream.getRecipeResult(map)
        //2 - Create a new TestObserver

        val testObserver :TestObserver<Result> =TestObserver<Result>()

        observableResult.subscribeWith(testObserver)
            .assertNoErrors() // 3.1 - Check if no errors
            .assertNoTimeout() // 3.2 - Check if no Timeout
            .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get the recipes fetched
        assertTrue(testObserver.values()[0].count>10)
        assertTrue(testObserver.values()[0].from==0)
        assertTrue(testObserver.values()[0].to ==10)
        assertTrue(testObserver.values()[0].q=="Fish")

    }


}
