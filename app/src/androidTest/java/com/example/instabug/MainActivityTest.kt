package com.example.instabug


import android.graphics.drawable.Drawable
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {


        IdlingRegistry.getInstance().register(MyIdlingResource.getIdlingResource())

    }


    @Test
    fun mainActivityTest() {


        val search = onView(
            allOf(
                withId(R.id.searchicon),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cardView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        search.check(matches(hasDrawablesearch()))
        search.perform(click())
        search.check(matches(hasDrawablesearch()))
        val searchtext = onView(
            allOf(
                withId(R.id.search),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cardView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchtext.perform(replaceText("text"), closeSoftKeyboard())
        search.check(matches(hasDrawablesearch()))



        val sort = onView(
            allOf(
                withId(R.id.sorticon),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cardView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        sort.perform(click())
           sort.check(matches(hasDrawablesort()))

    }


    fun hasDrawablesort(): BoundedMatcher<View?, ImageView> {
        return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                val ic_asce: Drawable? =mActivityTestRule.activity.resources.getDrawable(R.drawable.ic_asce_sort_24)
                val ic_d: Drawable? =mActivityTestRule.activity.resources.getDrawable(R.drawable.ic_desce_sort_24)
                val otherBitmap = imageView.drawable.toBitmap()
                val bit_a = ic_asce!!.toBitmap()
                val bit_d = ic_d!!.toBitmap()

                   return if (mActivityTestRule.activity.model.loadsort(mActivityTestRule.activity) == "a"&&otherBitmap.sameAs(bit_a)){
                        true
                    }else mActivityTestRule.activity.model.loadsort(mActivityTestRule.activity) == "d"&&otherBitmap.sameAs(bit_d)


            }
        }
    }
    fun hasDrawablesearch(): BoundedMatcher<View?, ImageView> {
        return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has drawable")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                val ic_search: Drawable? =mActivityTestRule.activity.resources.getDrawable(R.drawable.ic_search)
                val ic_x: Drawable? =mActivityTestRule.activity.resources.getDrawable(R.drawable.ic_x)
                val otherBitmap = imageView.drawable.toBitmap()
                val bit_s = ic_search!!.toBitmap()
                val bit_x = ic_x!!.toBitmap()


                return if (mActivityTestRule.activity.search!!.visibility==View.GONE&& otherBitmap.sameAs(bit_s)){
                    true
                } else if (mActivityTestRule.activity.search!!.text.isNullOrEmpty()&&otherBitmap.sameAs(bit_x)){
                    true
                } else mActivityTestRule.activity.search!!.text.isNotEmpty()&&otherBitmap.sameAs(bit_s)
            }
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(MyIdlingResource.getIdlingResource())
    }
}
