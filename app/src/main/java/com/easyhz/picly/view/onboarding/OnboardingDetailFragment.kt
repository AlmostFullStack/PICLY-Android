package com.easyhz.picly.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.easyhz.picly.databinding.FragmentOnboardingDetailBinding

class OnboardingDetailFragment(
    private val title: Int = OnboardingFragmentPage.SHARE.title,
    private val message: Int = OnboardingFragmentPage.SHARE.message,
    private val image: Int = OnboardingFragmentPage.SHARE.image
) : Fragment() {
    private lateinit var binding: FragmentOnboardingDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnboarding()
    }

    private fun setOnboarding() {
        binding.apply {
            onboardingImageView.setImageResource(image)
            titleTextView.text = getString(title)
            messageTextView.text = getString(message)
        }
    }
}