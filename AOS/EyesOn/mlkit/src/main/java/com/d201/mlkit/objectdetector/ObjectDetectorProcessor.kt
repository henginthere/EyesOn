/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.kotlin.objectdetector

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.d201.mlkit.GraphicOverlay
import com.d201.mlkit.GraphicOverlay.Graphic
import com.d201.mlkit.VisionProcessorBase
import com.d201.mlkit.objectdetector.ObjectGraphic
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

/** A processor to run object detector.  */
const val TAG = "ObjectDetectorProcessor__"
class ObjectDetectorProcessor(context: Context, options: ObjectDetectorOptionsBase) :
  VisionProcessorBase<List<DetectedObject>>(context){

  private val detector: ObjectDetector = ObjectDetection.getClient(options)
  val resultData = MutableLiveData<String>()
  private var _centerX = MutableLiveData<Float>()
  val centerX get() = _centerX
  private var _centerY = MutableLiveData<Float>()
  val centerY get() = _centerY
  override fun stop() {
    super.stop()
    try {
      detector.close()
    } catch (e: IOException) {
      Log.e(
        TAG,
        "Exception thrown while trying to close object detector!",
        e
      )
    }
  }

  override fun detectInImage(image: InputImage?): Task<List<DetectedObject>> {
    return detector.process(image!!)
  }

  override fun onSuccess(results: List<DetectedObject>, graphicOverlay: GraphicOverlay) {
    for (result in results) {
      var objectGraphic  = ObjectGraphic(graphicOverlay, result)
      graphicOverlay.add(ObjectGraphic(graphicOverlay, result))

      if(result.labels.isNotEmpty()) {
        resultData.postValue(result.labels[0].text)
        Log.d(TAG, "postValue___centerX : ${objectGraphic.centerX}, centerY : ${objectGraphic.centerY}")

        val rect = RectF(result.boundingBox)
        val x0 = objectGraphic.translateX(rect.left)
        val x1 = objectGraphic.translateX(rect.right)
        rect.left = min(x0, x1)
        rect.right = max(x0, x1)
        rect.top = objectGraphic.translateY(rect.top)
        rect.bottom = objectGraphic.translateY(rect.bottom)

        _centerX.postValue((rect.right-rect.left)/2 + rect.left)
        _centerY.postValue((rect.bottom-rect.top)/2 + rect.top)
        Log.d(TAG, "centerX : ${_centerX.value}, centerY : ${_centerY.value}");
      }
    }
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Object detection failed!", e)
  }

}
