package com.example.pocketgames

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
class PageNavigationTests{

    @Test
    fun homePage_to_CoinFlipPage(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val homePageScenario = launchFragmentInContainer<HomePage>()

        homePageScenario.onFragment { fragment -> navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        onView(ViewMatchers.withId(R.id.buttonStart)).perform(ViewActions.click(), ViewActions.closeSoftKeyboard())

        assertEquals(navController.currentDestination?.id, R.id.CoinFlipPage)
    }

    @Test
    fun coinFlipPage_to_TicTacToePage(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val coinFlipPageScenario = launchFragmentInContainer<CoinFlipPage>()

        coinFlipPageScenario.onFragment { fragment -> navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        onView(ViewMatchers.withId(listOf(R.id.button_heads, R.id.button_tails)[Random.nextInt(2)])).perform(ViewActions.click())

        Handler(Looper.getMainLooper()).postDelayed({
            assertEquals(navController.currentDestination?.id, R.id.TicTacToePage)
        }, 10000)
    }

    @Test
    fun ticTacToePage_restart(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val bundle = Bundle()
        bundle.putBoolean("prediction", listOf(false, true)[Random.nextInt(2)])
        val ticTacToePageScenario = launchFragmentInContainer<TicTacToePage>(bundle)

        ticTacToePageScenario.onFragment { fragment -> navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onView(ViewMatchers.withId(R.id.buttonReload)).perform(ViewActions.click(), ViewActions.closeSoftKeyboard())
            assertEquals(navController.currentDestination?.id, R.id.CoinFlipPage)
        }, 1000)

        coinFlipPage_to_TicTacToePage()
    }

    @Test
    fun ticTacToePage_to_HomePage(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val bundle = Bundle()
        bundle.putBoolean("prediction", listOf(false, true)[Random.nextInt(2)])
        val ticTacToePageScenario = launchFragmentInContainer<TicTacToePage>(bundle)

        ticTacToePageScenario.onFragment { fragment -> navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click())
        }, 1000)

        assertEquals(navController.currentDestination?.id, R.id.HomePage)
    }
}