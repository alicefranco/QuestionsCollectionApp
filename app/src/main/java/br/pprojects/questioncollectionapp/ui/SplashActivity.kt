package br.pprojects.questioncollectionapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.util.createDialog
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.checkHealth()

        splashViewModel.healthOk.observe(this, Observer {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        splashViewModel.loading.observe(this, Observer {
            if (it) {
                iv_refresh.visibility = View.GONE
                pb_reload.visibility = View.VISIBLE
            } else {
                pb_reload.visibility = View.INVISIBLE
            }
        })

        splashViewModel.error.observe(this, Observer {
            if (!it.isEmpty()) {
                iv_refresh.visibility = View.VISIBLE
                createDialog(this, "Error", it)
            }
        })

        iv_refresh.setOnClickListener {
            splashViewModel.checkHealth()
        }
    }
}