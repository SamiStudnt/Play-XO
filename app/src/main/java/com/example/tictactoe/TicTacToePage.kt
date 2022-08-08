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
    private var prediction: Boolean = false
    private var userImageId: Int = R.drawable.x
    private var cpuImageId: Int = R.drawable.o

    private fun usermove(line: Int, col: Int, imageview: ImageView){
        if (gameStatus != "idle"){
            return
        }

        if (grid!![line][col] == 0){
            count += 1
            grid!![line][col] = 1
            imageview.setImageResource(userImageId)

            verifytable()

            if(gameStatus == "idle"){
                binding.gameStatus.setText("CPU's turn")

                cpumove()
            }
        }
    }

    private fun calculatebestmoves(): List<Pair<Int, Int>>{
        var moves: MutableList<Pair<Int, Int>> = mutableListOf()

        //check if the cpu can win after its turn
        for(i in 0..2) {
            for (j in 0..2) {
                if (grid!![i][j] == 0){
                    grid!![i][j] = 2
                    verifytable()
                    if(gameStatus == "L"){
                        moves.add(Pair(i, j))
                        gameStatus = "idle"
                        binding.gameStatus.setText("")
                    }
                    grid!![i][j] = 0
                }
            }
        }

        if(moves.size > 0)
            return moves

        //check if the user can win on the next move, and try to stop him from winning
        for(i in 0..2) {
            for (j in 0..2) {
                if (grid!![i][j] == 0){
                    grid!![i][j] = 1
                    verifytable()
                    if(gameStatus == "W"){
                        moves.add(Pair(i, j))
                        gameStatus = "idle"
                        binding.gameStatus.setText("")
                    }
                    grid!![i][j] = 0
                }
            }
        }

        if(moves.size > 0)
            return moves

        var maxNo = 1

        //get best possible moves for the cpu
        for(i in 0..2) {
            for(j in 0..2) {
                if (grid!![i][j] == 0){
                    var noElem = 1
                    if (i==0){
                        if(grid!![i+1][j] == 2){
                            noElem += 1
                        }
                        if(grid!![i+2][j] == 2){
                            noElem += 1
                        }
                    }
                    else if (i==1){
                        if(grid!![i-1][j] == 2){
                            noElem += 1
                        }
                        if(grid!![i+1][j] == 2){
                            noElem += 1
                        }
                    }
                    else if (i==2){
                        if(grid!![i-2][j] == 2){
                            noElem += 1
                        }
                        if(grid!![i-1][j] == 2){
                            noElem += 1
                        }
                    }
                    if(noElem > maxNo){
                        maxNo = noElem
                        moves = mutableListOf()
                        moves.add(Pair(i,j))
                    }
                    else if (noElem == maxNo){
                        moves.add(Pair(i,j))
                    }

                    noElem = 1
                    if (j==0){
                        if(grid!![i][j+1] == 2){
                            noElem += 1
                        }
                        if(grid!![i][j+2] == 2){
                            noElem += 1
                        }
                    }
                    else if (j==1){
                        if(grid!![i][j-1] == 2){
                            noElem += 1
                        }
                        if(grid!![i][j+1] == 2){
                            noElem += 1
                        }
                    }
                    else if (j==2){
                        if(grid!![i][j-2] == 2){
                            noElem += 1
                        }
                        if(grid!![i][j-1] == 2){
                            noElem += 1
                        }
                    }
                    if(!moves.contains(Pair(i,j))){
                        if(noElem > maxNo){
                            maxNo = noElem
                            moves = mutableListOf()
                            moves.add(Pair(i,j))
                        }
                        else if (noElem == maxNo){
                            moves.add(Pair(i,j))
                        }
                    }

                    if(i==j){
                        if(i==0){
                            if(grid!![i+1][j+1] == 2){
                                noElem += 1
                            }
                            if(grid!![i+2][j+2] == 2){
                                noElem += 1
                            }
                        }
                        else if(i==1){
                            if(grid!![i-1][j-1] == 2){
                                noElem += 1
                            }
                            if(grid!![i+1][j+1] == 2){
                                noElem += 1
                            }
                        }
                        else if(i==2){
                            if(grid!![i-2][j-2] == 2){
                                noElem += 1
                            }
                            if(grid!![i-1][j-1] == 2){
                                noElem += 1
                            }
                        }
                        if(!moves.contains(Pair(i,j))){
                            if(noElem > maxNo){
                                maxNo = noElem
                                moves = mutableListOf()
                                moves.add(Pair(i,j))
                            }
                            else if (noElem == maxNo){
                                moves.add(Pair(i,j))
                            }
                        }
                    }
                }
            }
        }
        return moves
    }

    private fun cpumove(){
        val moves: List<Pair<Int, Int>> = calculatebestmoves()
        val ranNo: Int = (moves.indices).random()
        val cpuLine: Int = moves[ranNo].first
        val cpuCol: Int = moves[ranNo].second

        count += 1
        grid!![cpuLine][cpuCol] = 2
        if (cpuLine == 0 && cpuCol == 0){
            binding.imageView00.setImageResource(cpuImageId)
        } else if (cpuLine == 0 && cpuCol == 1){
            binding.imageView01.setImageResource(cpuImageId)
        } else if (cpuLine == 0 && cpuCol == 2){
            binding.imageView02.setImageResource(cpuImageId)
        } else if (cpuLine == 1 && cpuCol == 0){
            binding.imageView10.setImageResource(cpuImageId)
        } else if (cpuLine == 1 && cpuCol == 1){
            binding.imageView11.setImageResource(cpuImageId)
        } else if (cpuLine == 1 && cpuCol == 2){
            binding.imageView12.setImageResource(cpuImageId)
        } else if (cpuLine == 2 && cpuCol == 0){
            binding.imageView20.setImageResource(cpuImageId)
        } else if (cpuLine == 2 && cpuCol == 1){
            binding.imageView21.setImageResource(cpuImageId)
        } else if (cpuLine == 2 && cpuCol == 2){
            binding.imageView22.setImageResource(cpuImageId)
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
                gameStatus = "W"
        } else if (
            (grid!![0][0] == 2 && grid!![0][1] == 2 && grid!![0][2] == 2) ||
            (grid!![0][0] == 2 && grid!![1][0] == 2 && grid!![2][0] == 2) ||
            (grid!![0][0] == 2 && grid!![1][1] == 2 && grid!![2][2] == 2) ||
            (grid!![0][1] == 2 && grid!![1][1] == 2 && grid!![2][1] == 2) ||
            (grid!![0][2] == 2 && grid!![1][2] == 2 && grid!![2][2] == 2) ||
            (grid!![0][2] == 2 && grid!![1][1] == 2 && grid!![2][0] == 2) ||
            (grid!![1][0] == 2 && grid!![1][1] == 2 && grid!![1][2] == 2) ||
            (grid!![2][0] == 2 && grid!![2][1] == 2 && grid!![2][2] == 2) ){
                gameStatus = "L"
        } else if (count == 9){
            gameStatus = "D"
        }

        when (gameStatus) {
            "W" -> {
                binding.gameStatus.setText("You won")
            }
            "L" -> {
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

        prediction = arguments!!.get("prediction") as Boolean

        grid = Array(3) {IntArray(3)}

        binding.buttonReload.setOnClickListener {
            findNavController().navigate(R.id.tic_tac_toe_page_restart)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.tic_tac_toe_page_to_home_page)
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

        if(prediction == false){
            userImageId = R.drawable.o
            cpuImageId = R.drawable.x

            cpumove()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        grid = null
        _binding = null
    }
}