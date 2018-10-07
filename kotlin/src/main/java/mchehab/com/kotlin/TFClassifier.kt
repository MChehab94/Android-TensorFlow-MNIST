package mchehab.com.kotlin

import android.content.Context

import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class TFClassifier(context: Context) {

    companion object {
        init {
            System.loadLibrary("tensorflow_inference")
        }
    }

    private val inferenceInterface: TensorFlowInferenceInterface
    private val SIZE = 28 * 28
    private val MODEL_FILE = "file:///android_asset/tensorflow_lite_model.pb"
    private val INPUT_NODE = "x"
    private val OUTPUT_NODES = arrayOf("y")
    private val OUTPUT_NODE = "y"
    private val INPUT_SIZE = longArrayOf(1, SIZE.toLong())
    private val OUTPUT_SIZE = 10

    init {
        inferenceInterface = TensorFlowInferenceInterface(context.assets, MODEL_FILE)
    }

    fun predict(data: FloatArray): FloatArray {
        val result = FloatArray(OUTPUT_SIZE)
        inferenceInterface.feed(INPUT_NODE, data, *INPUT_SIZE)
        inferenceInterface.run(OUTPUT_NODES)
        inferenceInterface.fetch(OUTPUT_NODE, result)

        return result
    }
}
