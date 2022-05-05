package com.example.study.ui.bmi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study.R
import com.example.study.databinding.ActivityBmiResultBinding
import com.example.study.ui.bmi.domain.User
import java.io.Serializable

class BmiResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user: User? = intent.getParcelableExtra<User>("bmi")

        if (user != null) {
            renderBmi(user)
        }
    }

    private fun renderBmi(user: User) {
        val bmi = user.getBmiValue()
        binding.bmiValue.text = bmi.toString()

        when {
            bmi >= 30.0 -> binding.bmiResult.text = getString(R.string.obese)
            bmi >= 25.0 -> binding.bmiResult.text = getString(R.string.over_weight)
            bmi >= 18.5 -> binding.bmiResult.text = getString(R.string.normal_weight)
            else -> {
                binding.bmiResult.text = getString(R.string.under_weight)
            }
        }
    }
}