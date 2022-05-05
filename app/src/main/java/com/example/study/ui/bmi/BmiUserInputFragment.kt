package com.example.study.ui.bmi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.study.R
import com.example.study.databinding.FragmentBmiBinding
import com.example.study.ui.bmi.domain.User

class BmiUserInputFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this)[BmiViewModel::class.java]

        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calculateBtn: Button = binding.calculateBtn
        calculateBtn.setOnClickListener {
            Log.d("calculateBtn", "clicked")

            showBmiResult()
        }

        return root
    }

    private fun showBmiResult() {
        activity?.let {
            val intent = Intent(it, BmiResultActivity::class.java)

            val userHeight = binding.heightEdTxt.text.toString()
            val userWeight = binding.weightEdTxt.text.toString()

            if (userHeight.isEmpty() || userWeight.isEmpty()) {
                Toast.makeText(this.activity, getString(R.string.input_empty), Toast.LENGTH_SHORT)
                    .show()
                return@showBmiResult
            }

            val user =
                User(
                    weight = userWeight.toInt(),
                    height = userHeight.toInt(),
                )

            Log.d("calculateBtn", user.toString())

            intent.putExtra("bmi", user)
            it.startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}