import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.ui.fragment.SearchUserFragment
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DarkModeUITest {

    @Test
    fun testFiturDarkMode() {
        val scenario: FragmentScenario<SearchUserFragment> = launchFragmentInContainer()
        val darkModeTag = "darkModeIcon"
        val lightModeTag = "lightModeIcon"
        onView(withId(R.id.image_dark)).check(matches(withTagValue(`is`(lightModeTag))))
    }
}
