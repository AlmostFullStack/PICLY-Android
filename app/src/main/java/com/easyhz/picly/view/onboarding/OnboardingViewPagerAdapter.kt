package com.easyhz.picly.view.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 1. [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2?hl=ko)
 */
class OnboardingViewPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val tabs: Array<OnboardingFragmentPage>
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment =
        OnboardingDetailFragment(
            tabs[position].title,
            tabs[position].message,
            tabs[position].image
        )
}