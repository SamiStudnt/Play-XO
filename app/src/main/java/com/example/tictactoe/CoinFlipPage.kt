package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.databinding.CoinFlipPageBinding

class CoinFlipPage : Fragment() {

    private var _binding: CoinFlipPageBinding? = null

    private val binding get() = _binding!!

    private var prediction: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CoinFlipPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonHeads.setOnClickListener {
            prediction = "Heads"

            setElementsVisible()
        }

        binding.buttonTails.setOnClickListener {
            prediction = "Tails"

            setElementsVisible()
        }

        binding.coinButton.setOnClickListener {
            val randomNumber: Int = (0..1).random()

            if (randomNumber == 0){
                coinFlip(R.drawable.heads, "Heads")
            } else {
                coinFlip(R.drawable.tails, "Tails")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setElementsVisible() {
        binding.buttonHeads.visibility = View.INVISIBLE
        binding.buttonTails.visibility = View.INVISIBLE
        binding.titleCoinflip.visibility = View.INVISIBLE
        binding.imageHeads.visibility = View.INVISIBLE
        binding.imageTails.visibility = View.INVISIBLE

        binding.coin.visibility = View.VISIBLE
        binding.coinButton.visibility = View.VISIBLE
    }

    private fun coinFlip(imageId: Int, side: String) {
        binding.coin.animate().apply {
            duration = 2000
            rotationXBy(1800f)
            binding.coinButton.isClickable = false
        }.withEndAction {
            binding.coin.setImageResource(imageId)
            if (prediction == side) {
                Toast.makeText(context, String.format("%s. Correct prediction!", side), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, String.format("%s. Incorrect prediction!", side), Toast.LENGTH_LONG).show()
            }
            Handler(Looper.getMainLooper()).postDelayed({
                val bundle = Bundle()
                bundle.putBoolean("prediction", prediction == side)
                findNavController().navigate(R.id.coin_flip_page_to_tic_tac_toe_page, bundle)
            }, 4000)
        }.start()
    }

}