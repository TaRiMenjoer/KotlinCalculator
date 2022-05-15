package code.with.cal.kotlincalculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import code.with.cal.kotlincalculatorapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var input: String = ""
    private var sign: String = ""

    private var isSetSign = false

    private var isAddedSign = false
    private var isCannCount = false
    private var isSettedComma = false
    private var isFirstNumbers = false
    private var isCanEquals = false
    private var countNumbers = 0
    private var countAllNumbers = 0
    private var commaPosition = 0
    private var signPosition = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if (savedInstanceState != null) {
            binding.workingsTV.setText(savedInstanceState.getString("tv_workings"))
            binding.tvAnswer.setText(savedInstanceState.getString("tv_answer"))

            isSetSign = savedInstanceState.getBoolean("isSetSign")
            isAddedSign = savedInstanceState.getBoolean("isAddedSign")

            isCannCount = savedInstanceState.getBoolean("isCannCount")
            isSettedComma = savedInstanceState.getBoolean("isSettedComma")
            isFirstNumbers = savedInstanceState.getBoolean("isFirstNumbers")
            isCanEquals = savedInstanceState.getBoolean("isCanEquals")
            countNumbers = savedInstanceState.getInt("countNumbers")
            countAllNumbers = savedInstanceState.getInt("countAllNumbers")
            commaPosition = savedInstanceState.getInt("commaPosition")
            signPosition = savedInstanceState.getInt("signPosition")
            input = savedInstanceState.getString("input" , "")
            sign = savedInstanceState.getString("sign" , "")

        }

        setListeners()

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tv_workings", binding.workingsTV.text.toString())
        outState.putString("tv_answer", binding.tvAnswer.text.toString())
        outState.putBoolean("isSetSign", isSetSign)
        outState.putBoolean("isAddedSign", isAddedSign)
        outState.putBoolean("isCannCount", isCannCount)
        outState.putBoolean("isSettedComma", isSettedComma)
        outState.putBoolean("isFirstNumbers", isFirstNumbers)
        outState.putBoolean("isCanEquals", isCanEquals)
        outState.putInt("countNumbers", countNumbers)
        outState.putInt("countAllNumbers", countAllNumbers)
        outState.putInt("commaPosition", commaPosition)
        outState.putInt("signPosition", signPosition)
        outState.putString("input", input)
        outState.putString("sign", sign)

    }


    private fun setListeners() {
        binding.btn0.setOnClickListener {

            input += "0"
            countingNumbers()
        }
        binding.btn1.setOnClickListener {

            input += "1"
            countingNumbers()
        }
        binding.btn2.setOnClickListener {

            input += "2"
            countingNumbers()

        }
        binding.btn3.setOnClickListener {

            input += "3"
            countingNumbers()
        }
        binding.btn4.setOnClickListener {

            input += "4"
            countingNumbers()
        }
        binding.btn5.setOnClickListener {

            input += "5"
            countingNumbers()
        }
        binding.btn6.setOnClickListener {

            input += "6"
            countingNumbers()

        }
        binding.btn7.setOnClickListener {

            input += "7"
            countingNumbers()
        }
        binding.btn8.setOnClickListener {

            input += "8"
            countingNumbers()
        }
        binding.btn9.setOnClickListener {

            input += "9"
            countingNumbers()
        }

        binding.btnComma?.setOnClickListener {

            if (!isSettedComma) {
                input += "."
                countAllNumbers++
                commaPosition = countAllNumbers
                isSettedComma = true

                setInput()
            }
        }


        binding.btnClear.setOnClickListener {

            clear()

            setInput()
        }

        binding.btnBack.setOnClickListener {
            if (!input.isNullOrBlank()) {
                input = input.dropLast(1)
                countAllNumbers--
                if (countAllNumbers == 0) {
                    clear()
                }
                setInput()
            }
        }

        binding.btnPlus.setOnClickListener {



            permission("+")


        }

        binding.btnMinus.setOnClickListener {


            permission("-")

        }

        binding.btnDivide.setOnClickListener {


            permission("/")

        }
        binding.btnMultiply.setOnClickListener {


            permission("*")

        }

        binding.btnAnswer.setOnClickListener {
            if (!input.isNullOrBlank()  && /*countNumbers > 0 &&*/ commaPosition != 1 && isCanEquals && input.last().isDigit()) {

                val listOfNumbers = parseListOfNumbers(binding.workingsTV.text.toString())

                val listOfRightNumbers = getListWithoutDivideAndMultiple(listOfNumbers)

                val answer = getAnswer(listOfRightNumbers)

                binding.tvAnswer.setText(answer)

            } else {
                Toast.makeText(this, "Неправильный формат ввода", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun countingNumbers() {
        isCanEquals = true
        isSetSign = false
        isAddedSign = true
        countAllNumbers++
        setInput()

    }

    private fun permission(signString: String) {
        if(!isSetSign && isAddedSign){
            isCanEquals = false
            isSetSign = true
            isSettedComma = false
            input += signString
            countAllNumbers++
            setInput()
        }

    }

    private fun clear() {

        input = ""
        sign = ""
        isAddedSign = false
        isCannCount = false
        isSettedComma = false
        isFirstNumbers = false
        isCanEquals = false
        commaPosition = 0
        countNumbers = 0
        countAllNumbers = 0
        signPosition = 0

        binding.tvAnswer.setText("")
    }

    private fun setInput() {
        binding.workingsTV.setText(input)
    }

    private fun getAnswer(listOfRightNumbers: MutableList<Any>): String {
        var answer = listOfRightNumbers[0].toString()


        for (index in 0 until listOfRightNumbers.size) {
            if (listOfRightNumbers[index] == '+' || listOfRightNumbers[index] == '-') {
                val previouseNumber = answer.toDouble()
                val nextNumber = listOfRightNumbers[index + 1].toString().toDouble()

                val result = calculateTheResult(previouseNumber, nextNumber, listOfRightNumbers[index].toString())
                answer = result.toString()
            }

        }

        return answer
    }

    private fun getListWithoutDivideAndMultiple(listOfNumbers: MutableList<Any>): MutableList<Any> {

        var listOfRightNumbers = mutableListOf<Any>()
        var isBlockedSign = false

        for (index in 0 until listOfNumbers.size) {
            if (listOfNumbers[index] != '/' && listOfNumbers[index] != '*') {
                if (!isBlockedSign) {
                    listOfRightNumbers.add(listOfNumbers[index])

                } else {
                    isBlockedSign = false

                }

            } else {
                val previouseNumber = listOfRightNumbers.last().toString().toDouble()
                listOfRightNumbers = listOfRightNumbers.dropLast(1).toMutableList()
                val nextNumber = listOfNumbers[index + 1].toString().toDouble()
                val result = calculateTheResult(previouseNumber, nextNumber, listOfNumbers[index].toString())
                listOfRightNumbers.add(result)
                isBlockedSign = true

            }
        }
        return listOfRightNumbers

    }

    private fun parseListOfNumbers(example: String): MutableList<Any> {
        var listOfNumbers = mutableListOf<Any>()
        var numbers = ""
        for (char in example) {
            if (char.isDigit() || char == '.') {
                numbers += char
            } else {
                listOfNumbers.add(numbers)
                listOfNumbers.add(char)
                numbers = ""
            }
        }
        listOfNumbers.add(numbers)
        return listOfNumbers

    }


    private fun calculateTheResult(previouseNumber: Double, nextNumber: Double, sign: String): Double {
        val result: Double
        when (sign) {
            "/" -> {
                result = previouseNumber / nextNumber
            }
            "*" -> {
                result = previouseNumber * nextNumber
            }
            "+" -> {
                result = previouseNumber + nextNumber
            }
            "-" -> {
                result = previouseNumber - nextNumber
            }
            else -> {
                result = 0.0
            }
        }
        return result

    }
}



















