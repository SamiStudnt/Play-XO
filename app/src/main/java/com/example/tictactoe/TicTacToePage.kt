package com.example.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.databinding.TicTacToePageBinding


class TicTacToePage : Fragment() {

    private var _binding: TicTacToePageBinding? = null

    private val binding get() = _binding!!

    private var grid: Array<IntArray>? = null

    private var gameStatus: String = "idle"

    private var count: Int = 0

    private fun usermove(line: Int, col: Int, imageview: ImageView){
        if (gameStatus != "idle"){
            return
        }

        if (grid!![line][col] == 0){
            count += 1
            grid!![line][col] = 1
            imageview.setImageResource(R.drawable.x)

            verifytable()

            if(gameStatus == "idle"){
                binding.gameStatus.setText("CPU's turn")

                cpumove()
            }
        }
    }

    private fun cpumove(){
        var cpuLine: Int = (0..2).random()
        var cpuCol: Int = (0..2).random()

        while(grid!![cpuLine][cpuCol] != 0){
            cpuLine = (0..2).random()
            cpuCol = (0..2).random()
        }

        count += 1
        grid!![cpuLine][cpuCol] = 2
        if (cpuLine == 0 && cpuCol == 0){
            binding.imageView00.setImageResource(R.drawable.o)
        } else if (cpuLine == 0 && cpuCol == 1){
            binding.imageView01.setImageResource(R.drawable.o)
        } else if (cpuLine == 0 && cpuCol == 2){
            binding.imageView02.setImageResource(R.drawable.o)
        } else if (cpuLine == 1 && cpuCol == 0){
            binding.imageView10.setImageResource(R.drawable.o)
        } else if (cpuLine == 1 && cpuCol == 1){
            binding.imageView11.setImageResource(R.drawable.o)
        } else if (cpuLine == 1 && cpuCol == 2){
            binding.imageView12.setImageResource(R.drawable.o)
        } else if (cpuLine == 2 && cpuCol == 0){
            binding.imageView20.setImageResource(R.drawable.o)
        } else if (cpuLine == 2 && cpuCol == 1){
            binding.imageView21.setImageResource(R.drawable.o)
        } else if (cpuLine == 2 && cpuCol == 2){
            binding.imageView22.setImageResource(R.drawable.o)
        }

        verifytable()

        if(gameStatus == "idle"){
            binding.gameStatus.setText("Your turn")
        }
    }

    private fun verifytable(){
        if( (grid!![0][0] == 1 && grid!![0][1] == 1 && grid!![0][2] == 1) ||
            (grid!![0][0] == 1 && grid!![1][0] == 1 && grid!![2][0] == 1) ||
            (grid!![0][0] == 1 && grid!![1][1] == 1 && grid!![2][2] == 1) ||
            (grid!![0][1] == 1 && grid!![1][1] == 1 && grid!![2][1] == 1) ||
            (grid!![0][2] == 1 && grid!![1][2] == 1 && grid!![2][2] == 1) ||
            (grid!![0][2] == 1 && grid!![1][1] == 1 && grid!![2][0] == 1) ||
            (grid!![1][0] == 1 && grid!![1][1] == 1 && grid!![1][2] == 1) ||
            (grid!![2][0] == 1 && grid!![2][1] == 1 && grid!![2][2] == 1) ){
                gameStatus = "X"
        } else if (
            (grid!![0][0] == 2 && grid!![0][1] == 2 && grid!![0][2] == 2) ||
            (grid!![0][0] == 2 && grid!![1][0] == 2 && grid!![2][0] == 2) ||
            (grid!![0][0] == 2 && grid!![1][1] == 2 && grid!![2][2] == 2) ||
            (grid!![0][1] == 2 && grid!![1][1] == 2 && grid!![2][1] == 2) ||
            (grid!![0][2] == 2 && grid!![1][2] == 2 && grid!![2][2] == 2) ||
            (grid!![0][2] == 2 && grid!![1][1] == 2 && grid!![2][0] == 2) ||
            (grid!![1][0] == 2 && grid!![1][1] == 2 && grid!![1][2] == 2) ||
            (grid!![2][0] == 2 && grid!![2][1] == 2 && grid!![2][2] == 2) ){
                gameStatus = "O"
        } else if (count == 9){
            gameStatus = "D"
        }

        when (gameStatus) {
            "X" -> {
                binding.gameStatus.setText("You won")
            }
            "O" -> {
                binding.gameStatus.setText("You lost")
            }
            "D" -> {
                binding.gameStatus.setText("DRAW")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = TicTacToePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        grid = Array(3) {IntArray(3)}

        binding.buttonReload.setOnClickListener {
            this.activity?.recreate()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.imageView00.setOnClickListener {
            usermove(0, 0, binding.imageView00)
        }

        binding.imageView01.setOnClickListener {
            usermove(0, 1, binding.imageView01)
        }

        binding.imageView02.setOnClickListener {
            usermove(0, 2, binding.imageView02)
        }

        binding.imageView10.setOnClickListener {
            usermove(1, 0, binding.imageView10)
        }

        binding.imageView11.setOnClickListener {
            usermove(1, 1, binding.imageView11)
        }

        binding.imageView12.setOnClickListener {
            usermove(1, 2, binding.imageView12)
        }

        binding.imageView20.setOnClickListener {
            usermove(2, 0, binding.imageView20)
        }

        binding.imageView21.setOnClickListener {
            usermove(2, 1, binding.imageView21)
        }

        binding.imageView22.setOnClickListener {
            usermove(2, 2, binding.imageView22)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        grid = null
        _binding = null
    }
}