package com.practice.ocr

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.acos
import kotlin.math.round
import kotlin.math.sqrt

private const val TAG ="MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun translate(result : String){

        val interpreter = getTfliteInterpreter("ocr_converted_model_acc_0.98_loss_0.03.tflite")
        val byteBuffer = ByteBuffer.allocateDirect(32*32*3*4).order(ByteOrder.nativeOrder())

//        val data = FloatArray(15)
//        for(i in 0..14) {
//            angle[i] = Math.toDegrees(acos(einsum[i]).toDouble()).toFloat()
//            data[i] = round(angle[i] * 100000) / 100000
//        }
//        for(d in data) {
//            byteBuffer.putFloat(d)
//        }

        val modelOutput = ByteBuffer.allocateDirect(2350*4).order(ByteOrder.nativeOrder())
        modelOutput.rewind()

        interpreter!!.run(byteBuffer,modelOutput)

        val outputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1,26), DataType.FLOAT32)
        outputFeature0.loadBuffer(modelOutput)

        // ByteBuffer to FloatBuffer
        val outputsFloatBuffer = modelOutput.asFloatBuffer()
        val outputs = mutableListOf<Float>()
        for(i in 1..2350) {
            outputs.add(outputsFloatBuffer.get())
        }

        val sortedOutput = outputs.sortedDescending()
        val index = outputs.indexOf(sortedOutput[0])
        val result = classes[index]
        runOnUiThread {
            //binding.tvLiveTranslate.text = classes[index]
        }
//        if(System.currentTimeMillis() - startTime >= 2000) {
//            startTime = System.currentTimeMillis()
//            //hangulMaker.commit(classes[index][0])
//            hangulMaker.commit(gara[garaIndex][0])
//            garaIndex++
//        }


    //        input =
//        name: serving_default_input_1:0
//        type: float32[1,32,32,3]
//        location: 0

    //        output =
//        name: StatefulPartitionedCall:0
//        type: float32[1,2350]
//        location: 35
    }


    private fun getTfliteInterpreter(path: String): Interpreter? {
        try {
            return Interpreter(loadModelFile(this, path)!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun loadModelFile(activity: Activity, path: String): MappedByteBuffer? {
        val fileDescriptor = activity.assets.openFd(path)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}