package com.easyhz.picly.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentSettingsBinding
import com.easyhz.picly.helper.DEVELOPER_URL
import com.easyhz.picly.helper.OPENSOURCE_LICENSE_URL
import com.easyhz.picly.helper.PRIVACY_POLICY_URL
import com.easyhz.picly.helper.TERMS_OF_SERVICE_URL
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class SettingsFragment: Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        settingsAdapter = SettingsAdapter(requireActivity(), requireActivity().supportFragmentManager)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        setSettingRecyclerView()
    }

    private fun setSettingRecyclerView() {
        binding.settingRecyclerView.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        settingsAdapter.setList(Settings.entries)
    }
}

enum class Settings(val icon : Int? = null, val title: Int, var version: Float? = null) {
    ACCOUNT(
        icon = R.drawable.icon_setting_person,
        title = R.string.setting_account,
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            NavControllerManager.navigateSettingToAccount()
        }
    }, TUTORIAL(
        icon = R.drawable.icon_setting_tutorial,
        title = R.string.setting_tutorial
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            NavControllerManager.navigateSettingToOnboarding()
        }
    }, TERMS_OF_SERVICE(
        title = R.string.setting_terms_of_service
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            showBottomSheetFragment(fragmentManager, TERMS_OF_SERVICE_URL)
        }
    }, PRIVACY_POLICY(
        title = R.string.setting_privacy_policy
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            showBottomSheetFragment(fragmentManager, PRIVACY_POLICY_URL)
        }
    }, OPENSOURCE_LICENSE(
        title = R.string.setting_opensource_license
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            showBottomSheetFragment(fragmentManager, OPENSOURCE_LICENSE_URL)
        }
    }, DEVELOPER(
        title = R.string.setting_developer
    ) {
        override fun onClick(fragmentManager: FragmentManager) {
            showBottomSheetFragment(fragmentManager, DEVELOPER_URL)
        }
    }, VERSION(
        title = R.string.setting_version,
        version = 1.0F
    ) {
        override fun onClick(fragmentManager: FragmentManager) { }
    };

    abstract fun onClick(fragmentManager: FragmentManager)

    fun showBottomSheetFragment(fragmentManager: FragmentManager, url: String) {
        val bottomSheetFragment = WebBottomSheet(url)
        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
    }
}