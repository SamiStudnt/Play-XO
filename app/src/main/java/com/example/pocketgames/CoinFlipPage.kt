package com.example.pocketgames

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pocketgames.databinding.CoinFlipPageBinding


class CoinFlipPage : Fragment() {
    private var _binding: CoinFlipPageBinding? = null
    private val binding get() = _binding!!
    private var prediction: String? = null

    @Override
    override fun onSaveInstanceState(outState: Bundle) {}

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

        binding.coinButton.setOnClickListener { //function that randomly picks the coin flip result
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
        prediction = null
    }

    private fun setElementsVisible() {
        binding.buttonHeads.visibility = View.GONE
        binding.buttonTails.visibility = View.GONE
        binding.titleCoinflip.visibility = View.INVISIBLE
        binding.imageHeads.visibility = View.GONE
        binding.imageTails.visibility = View.GONE

        binding.coin.visibility = View.VISIBLE
        binding.coinButton.visibility = View.VISIBLE
    }

    //function that animates both sides of the coin
    private fun animateCoinSides(time: Long, imageId: Int){
        binding.coin.animate().apply {
            duration = time
        }.withEndAction {
            binding.coin.setImageResource(imageId)
        }.start()
    }

    //function that animates the coin flip
    private fun coinFlip(imageId: Int, side: String) {
        animateCoinSides(1800, imageId)
        animateCoinSides(250, R.drawable.tails)
        animateCoinSides(500, R.drawable.heads)
        animateCoinSides(750, R.drawable.tails)
        animateCoinSides(1000, R.drawable.heads)
        animateCoinSides(1250, R.drawable.tails)
        animateCoinSides(1500, R.drawable.heads)

        binding.coin.animate().apply {
            duration = 1000
            rotationXBy(1800f)
            translationY(-750f)
            binding.coinButton.isClickable = false
            binding.coinButton.visibility = View.GONE
        }.start()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.coin.animate().apply {
                duration = 1000
                rotationXBy(1800f)
                translationY(0f)
            }.withEndAction {
                if (prediction == side) {
                    Toast.makeText(
                        context,
                        String.format("%s. Correct prediction!", side),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        String.format("%s. Incorrect prediction!", side),
                        Toast.LENGTH_LONG
                    ).show()
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    val bundle = Bundle()
                    bundle.putBoolean("prediction", prediction == side)
                    findNavController().navigate(R.id.coin_flip_page_to_tic_tac_toe_page, bundle)
                }, 4000)
            }.start()
        }, 1000)
    }
}