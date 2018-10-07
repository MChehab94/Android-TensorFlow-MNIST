package mchehab.com.kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var textViewResult: TextView? = null
    private var drawView: DrawView? = null
    private var buttonClear: Button? = null
    private var buttonPredict: Button? = null

    private var tfClassifier: TFClassifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tfClassifier = TFClassifier(applicationContext)

        textViewResult = findViewById(R.id.textViewResult)
        drawView = findViewById(R.id.drawView)
        buttonClear = findViewById(R.id.buttonClear)
        buttonPredict = findViewById(R.id.buttonPredict)

        buttonClear!!.setOnClickListener { e -> drawView!!.clearCanvas() }

        buttonPredict!!.setOnClickListener { e ->
            val result = tfClassifier!!.predict(drawView!!.pixels)
            var max = -1f
            var answer = -1
            for (i in result.indices) {
                if (result[i] > max) {
                    max = result[i]
                    answer = i
                }
            }
            textViewResult!!.text = "Model Prediction: $answer\nConfidence: $max"
        }
    }
}