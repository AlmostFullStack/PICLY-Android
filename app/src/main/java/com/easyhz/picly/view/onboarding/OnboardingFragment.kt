package com.easyhz.picly.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentOnboardingBinding
import com.easyhz.picly.view.navigation.NavControllerManager
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment: Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var viewPagerAdapter: OnboardingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        viewPagerAdapter = OnboardingViewPagerAdapter(requireActivity(),
            OnboardingFragmentPage.entries.toTypedArray()
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        setViewPager()
        setOnPageChangeListener()
        setSkipButton()
    }

    private fun setViewPager() {
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.dotIndicator, binding.viewPager) { _, _ -> }.attach()
    }

    private fun setOnPageChangeListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateNextButton(position)
            }
        })
    }

    private fun setSkipButton() {
        binding.toolbar.skipTextView.setOnClickListener {
            NavControllerManager.navigateToBack()
        }
    }

    private fun updateNextButton(position: Int) {
        val buttonTextResId = if (position == OnboardingFragmentPage.entries.size - 1) {
            R.string.onboarding_button_start
        } else {
            R.string.onboarding_button_next
        }
        val buttonText = getString(buttonTextResId)

        binding.nextButton.button.text = buttonText
        binding.nextButton.button.setOnClickListener {
            handleNextButtonClick(position)
        }
    }

    private fun handleNextButtonClick(position: Int) {
        if (position == OnboardingFragmentPage.entries.size - 1) {
            NavControllerManager.navigateToBack()
        } else {
            binding.viewPager.setCurrentItem(position + 1, true)
        }
    }
}

enum class OnboardingFragmentPage(
    val title: Int,
    val message: Int,
    val image: Int,
) {
    SHARE(
        title = R.string.onboarding_title_share,
        message = R.string.onboarding_message_share,
        image = R.drawable.image_onboarding_share
    ), ANONYMITY(
        title = R.string.onboarding_title_anonymity,
        message = R.string.onboarding_message_anonymity,
        image = R.drawable.image_onboarding_anonymity
    ), EXPIRED_ALBUM(
        title = R.string.onboarding_title_expired_album,
        message = R.string.expired_album,
        image = R.drawable.image_onboarding_expired_album
    )
}