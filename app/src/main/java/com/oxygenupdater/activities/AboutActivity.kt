package com.oxygenupdater.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.oxygenupdater.ActivityLauncher
import com.oxygenupdater.BuildConfig
import com.oxygenupdater.R
import com.oxygenupdater.viewmodels.AboutViewModel
import kotlinx.android.synthetic.main.activity_about.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutActivity : SupportActionBarActivity() {

    private val aboutViewModel by viewModel<AboutViewModel>()

    override fun onCreate(
        savedInstanceSate: Bundle?
    ) = super.onCreate(savedInstanceSate).also {
        setContentView(R.layout.activity_about)

        val activityLauncher = ActivityLauncher(this)

        // Set the version number of the app as the subtitle
        collapsingToolbarLayout.subtitle = getString(R.string.summary_oxygen, BuildConfig.VERSION_NAME)
        // Make the links in the background story clickable.
        backgroundStoryTextView.movementMethod = LinkMovementMethod.getInstance()

        discordButton.setOnClickListener { activityLauncher.openDiscord(this) }
        githubButton.setOnClickListener { activityLauncher.openGitHub(this) }
        websiteButton.setOnClickListener { activityLauncher.openWebsite(this) }
        emailButton.setOnClickListener { activityLauncher.openEmail(this) }
        rateButton.setOnClickListener { activityLauncher.openPlayStorePage(this) }

        // banner is displayed if app version is outdated
        bannerLayout.setOnClickListener { activityLauncher.openPlayStorePage(this) }

        aboutViewModel.fetchServerStatus().observe(this) {
            if (!it.checkIfAppIsUpToDate()) {
                updateBannerText(it.latestAppVersion!!)
            }
        }
    }

    override fun onBackPressed() = finish()

    /**
     * Respond to the action bar's Up/Home button.
     * Delegate to [onBackPressed] if [android.R.id.home] is clicked, otherwise call `super`
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun updateBannerText(latestAppVersion: String) {
        if (isFinishing) {
            return
        }

        bannerLayout.isVisible = true
        bannerTextView.text = getString(R.string.new_app_version_long, latestAppVersion)
    }
}
