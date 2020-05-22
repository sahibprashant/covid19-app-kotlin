package com.development.covid19

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.development.covid19.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "COVID - 19"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)


        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.who -> moveToPage(Constants.WHO_URL, resources.getString(R.string.who))
                R.id.info -> moveToPage(Constants.INFO_RESOURCES_URL, resources.getString(R.string.google_data))
                R.id.news -> moveToPage(Constants.NEWS_URL, resources.getString(R.string.news))
                R.id.india -> moveToPage(Constants.INDIA_URL, resources.getString(R.string.covid_ind))
                else -> spreadPositivity()
            }
        }

        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = MyWebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.setAppCacheEnabled(true)

        binding.webView.loadUrl(Constants.COVID_WORLD_MAP_URL)

    }

    private fun spreadPositivity(): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun moveToPage(url: String, name : String): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, WebViewActivity::class.java)
            .putExtra("url", url)
            .putExtra("name", name))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home && !binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            view?.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.evaluateJavascript("javascript:(function() { " +
                    "document.getElementsByClassName('gb_Pd gb_8d gb_Zd gb_Xd gb_1e')[0].style.display='none';" +
                    "document.getElementsByClassName('nJnAMd')[0].style.display='none';" +
                    "document.getElementsByClassName('gb_Sd')[0].style.display='none';" +
                    "document.getElementsByClassName('VjFXz')[0].style.display='none'; })()"){
                view.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }

        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return true
        }
    }


}
