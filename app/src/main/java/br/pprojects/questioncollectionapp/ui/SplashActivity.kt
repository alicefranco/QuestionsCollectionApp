package br.pprojects.questioncollectionapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.pprojects.questioncollectionapp.R
import br.pprojects.questioncollectionapp.util.createDialog
import br.pprojects.questioncollectionapp.util.gone
import br.pprojects.questioncollectionapp.util.invisible
import br.pprojects.questioncollectionapp.util.visible
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
                val intent = Intent(this, QuestionsActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        splashViewModel.loading.observe(this, Observer {
            if (it) {
                iv_refresh.gone()
                pb_reload.visible()
            } else {
                pb_reload.invisible()
            }
        })

        splashViewModel.error.observe(this, Observer {
            if (!it.isEmpty()) {
                iv_refresh.visible()
                createDialog(this, "Error", it)
            }
        })

        iv_refresh.setOnClickListener {
            splashViewModel.checkHealth()
        }
    }
}