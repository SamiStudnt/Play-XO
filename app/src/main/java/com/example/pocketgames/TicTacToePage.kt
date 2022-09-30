package com.example.pocketgames

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.pocketgames.databinding.TicTacToePageBinding

class TicTacToePage : Fragment() {
    private var _binding: TicTacToePageBinding? = null
    private val binding get() = _binding!!
    private var grid: Array<IntArray> = Array(3) {IntArray(3)}
    private var gameStatus: String = "idle"
    private var count: Int = 0
    private var prediction: Boolean = false
    private var userImageId: Int = R.drawable.x
    private var cpuImageId: Int = R.drawable.o

    @Override
    override fun onSaveInstanceState(outState: Bundle) {}

    private fun usermove(line: Int, col: Int, imageview: ImageView){
        if (grid[line][col] == 0){  //check if grid spot is empty
            count += 1
            grid[line][col] = 1
            imageview.setImageResource(userImageId)

            verifytable()   //check if the game is over or not

            if(gameStatus == "idle"){   //if the game is not over, it's the CPU's turn
                binding.gameStatus.text = getString(R.string.game_status_cpu_turn)
                toggleboard(false)

                Handler(Looper.getMainLooper()).postDelayed({
                    cpumove()
                }, 2000)

            }
        }
    }

    //function that helps the CPU make the best decision
    private fun calculatebestmoves(): List<Pair<Int, Int>>{
        var moves: MutableList<Pair<Int, Int>> = mutableListOf()    //list that contains the best positions

        //check if the cpu can win after its turn
        for(i in 0..2) {
            for (j in 0..2) {
                if (grid[i][j] == 0){   //if the grid spot is empty, we simulate the CPU turn and see if it wins
                    grid[i][j] = 2
                    verifytable()
                    if(gameStatus == "L"){
                        moves.add(Pair(i, j))
                        gameStatus = "idle"
                        binding.gameStatus.setText(R.string.game_status_cpu_turn)
                    }
                    grid[i][j] = 0
                }
            }
        }

        if(moves.size > 0)  //if the CPU can win with one move, I return the possible moves that the CPU can make to win
            return moves

        //check if the user can win on the next move, and try to stop him from winning
        for(i in 0..2) {
            for (j in 0..2) {
                if (grid[i][j] == 0){
                    grid[i][j] = 1
                    verifytable()
                    if(gameStatus == "W"){  //if the grid spot is empty, we simulate the user turn and see if the user wins
                        moves.add(Pair(i, j))
                        gameStatus = "idle"
                        binding.gameStatus.setText(R.string.game_status_cpu_turn)
                    }
                    grid[i][j] = 0
                }
            }
        }

        if(moves.size > 0)  //if the user can win with one move, I return the possible moves that the CPU can make to prevent the user from winning
            return moves

        var maxNo = 0   //maximum number of CPU symbols on one line/column/diagonal

        //get the best possible moves for the cpu
        for(i in 0..2) {
            for(j in 0..2) {
                if (grid[i][j] == 0){
                    if(noSymbolsColumn(i, j, 1) == 0){
                        val noElem: Int = noSymbolsColumn(i, j, 2)
                        val res: Pair<MutableList<Pair<Int, Int>>, Int> = addToList(moves, i, j, noElem, maxNo)
                        moves = res.first
                        maxNo = res.second
                    }

                    if(noSymbolsLine(i, j, 1) == 0){
                        val noElem: Int = noSymbolsLine(i, j, 2)
                        if(!moves.contains(Pair(i,j))) {
                            val res: Pair<MutableList<Pair<Int, Int>>, Int> = addToList(moves, i, j, noElem, maxNo)
                            moves = res.first
                            maxNo = res.second
                        }
                    }

                    if(i==j){
                        if(noSymbolsMDiag(i, j, 1) == 0){
                            val noElem: Int = noSymbolsMDiag(i, j, 2)
                            if(!moves.contains(Pair(i,j))) {
                                val res: Pair<MutableList<Pair<Int, Int>>, Int> = addToList(moves, i, j, noElem, maxNo)
                                moves = res.first
                                maxNo = res.second
                            }
                        }
                    }

                    if(i+j == 2){
                        if(noSymbolsSDiag(i, j, 1) == 0){
                            val noElem: Int = noSymbolsSDiag(i, j, 2)
                            if(!moves.contains(Pair(i,j))) {
                                val res: Pair<MutableList<Pair<Int, Int>>, Int> = addToList(moves, i, j, noElem, maxNo)
                                moves = res.first
                                maxNo = res.second
                            }
                        }
                    }
                }
            }
        }
        if(moves.size > 0)
            return moves

        //if any move does not help, choose from the remaining combinations
        for(i in 0..2) {
            for (j in 0..2) {
                if (grid[i][j] == 0) {
                    moves.add(Pair(i, j))
                }
            }
        }
        return moves
    }

    private fun noSymbolsColumn(i:Int, j:Int, value:Int): Int{
        var noElem = 0  //number of symbols on the current column

        //verify current column
        if (i==0){
            if(grid[1][j] == value){
                noElem += 1
            }
            if(grid[2][j] == value){
                noElem += 1
            }
        }
        else if (i==1){
            if(grid[0][j] == value){
                noElem += 1
            }
            if(grid[2][j] == value){
                noElem += 1
            }
        }
        else if (i==2){
            if(grid[0][j] == value){
                noElem += 1
            }
            if(grid[1][j] == value){
                noElem += 1
            }
        }

        return noElem
    }

    private fun noSymbolsLine(i:Int, j:Int, value:Int): Int{
        var noElem = 0  //number of symbols on the current line

        //verify current line
        if (j==0){
            if(grid[i][1] == value){
                noElem += 1
            }
            if(grid[i][2] == value){
                noElem += 1
            }
        }
        else if (j==1){
            if(grid[i][0] == value){
                noElem += 1
            }
            if(grid[i][2] == value){
                noElem += 1
            }
        }
        else if (j==2){
            if(grid[i][0] == value){
                noElem += 1
            }
            if(grid[i][1] == value){
                noElem += 1
            }
        }

        return noElem
    }

    private fun noSymbolsMDiag(i:Int, j:Int, value:Int): Int{
        var noElem = 0  //number of symbols on the main diagonal

        //verify main diagonal
        if(i==0){
            if(grid[1][j+1] == value){
                noElem += 1
            }
            if(grid[2][j+2] == value){
                noElem += 1
            }
        }
        else if(i==1){
            if(grid[0][j-1] == value){
                noElem += 1
            }
            if(grid[2][j+1] == value){
                noElem += 1
            }
        }
        else if(i==2){
            if(grid[0][j-2] == value){
                noElem += 1
            }
            if(grid[1][j-1] == value){
                noElem += 1
            }
        }

        return noElem
    }

    private fun noSymbolsSDiag(i:Int, j:Int, value:Int): Int{
        var noElem = 0  //number of symbols on the secondary diagonal

        //verify secondary diagonal
        if(i==0){
            if(grid[1][j-1] == value){
                noElem += 1
            }
            if(grid[2][j-2] == value){
                noElem += 1
            }
        }
        else if(i==1){
            if(grid[0][j+1] == value){
                noElem += 1
            }
            if(grid[2][j-1] == value){
                noElem += 1
            }
        }
        else if(i==2){
            if(grid[0][j+2] == value){
                noElem += 1
            }
            if(grid[1][j+1] == value){
                noElem += 1
            }
        }

        return noElem
    }

    //function that adds a move to the list
    private fun addToList(moves: MutableList<Pair<Int, Int>>, i: Int, j: Int, noElem: Int, maxNo: Int): Pair<MutableList<Pair<Int, Int>>, Int>{
        if(noElem > maxNo){
            val newMoves: MutableList<Pair<Int, Int>> = mutableListOf()
            newMoves.add(Pair(i,j))
            return Pair(newMoves, noElem)
        }
        else if (noElem == maxNo){
            moves.add(Pair(i,j))
        }
        return Pair(moves, maxNo)
    }

    //function that simulates the CPU turn
    private fun cpumove(){
        val moves: List<Pair<Int, Int>> = calculatebestmoves()
        val ranNo: Int = (moves.indices).random()
        val cpuLine: Int = moves[ranNo].first
        val cpuCol: Int = moves[ranNo].second

        count += 1
        grid[cpuLine][cpuCol] = 2

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

        verifytable()   //check if the game is over or not

        if(gameStatus == "idle"){
            binding.gameStatus.text = getString(R.string.game_status_user_turn)
            toggleboard(true)
        }
    }

    //check if someone(user/CPU) won the game
    private fun checkforwin(value: Int): Boolean{
        return  (grid[0][0] == value && grid[0][1] == value && grid[0][2] == value) ||
                (grid[0][0] == value && grid[1][0] == value && grid[2][0] == value) ||
                (grid[0][0] == value && grid[1][1] == value && grid[2][2] == value) ||
                (grid[0][1] == value && grid[1][1] == value && grid[2][1] == value) ||
                (grid[0][2] == value && grid[1][2] == value && grid[2][2] == value) ||
                (grid[0][2] == value && grid[1][1] == value && grid[2][0] == value) ||
                (grid[1][0] == value && grid[1][1] == value && grid[1][2] == value) ||
                (grid[2][0] == value && grid[2][1] == value && grid[2][2] == value)
    }

    //check for game status
    private fun verifytable(){
        if(checkforwin(1)){
                gameStatus = "W"
        } else if (checkforwin(2)){
                gameStatus = "L"
        } else if (count == 9){
            gameStatus = "D"
        }

        when (gameStatus) {
            "W" -> {
                binding.gameStatus.text = getString(R.string.game_status_W)
            }
            "L" -> {
                binding.gameStatus.text = getString(R.string.game_status_L)
            }
            "D" -> {
                binding.gameStatus.text = getString(R.string.game_status_D)
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

        prediction = requireArguments().getBoolean("prediction")

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

        if(!prediction){
            userImageId = R.drawable.o
            cpuImageId = R.drawable.x
            binding.gameStatus.text = getString(R.string.game_status_cpu_turn)
            toggleboard(false)

            Handler(Looper.getMainLooper()).postDelayed({
                cpumove()
            }, 2000)
        }
    }

    //make board active/inactive after/before the CPU makes a move
    private fun toggleboard(value: Boolean) {
        binding.imageView00.isClickable = value
        binding.imageView01.isClickable = value
        binding.imageView02.isClickable = value
        binding.imageView10.isClickable = value
        binding.imageView11.isClickable = value
        binding.imageView12.isClickable = value
        binding.imageView20.isClickable = value
        binding.imageView21.isClickable = value
        binding.imageView22.isClickable = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}