package com.marcinmoskala.arcseekbar

import android.graphics.RectF

internal data class ArcSeekBarData(
        val dx: Float,
        val dy: Float,
        val width: Float,
        val height: Float,
        val progress: Int,
        val maxProgress: Int
) {
    private val pi = Math.PI.toFloat()
    private val zero = 0.0001F
    val r: Float = height / 2 + width * width / 8 / height
    private val circleCenterX: Float = width / 2 + dy
    private val circleCenterY: Float = r + dx
    private val alphaRad: Float = bound(zero, Math.acos((r - height).toDouble() / r).toFloat(), 2 * pi)
    val arcRect: RectF = RectF(circleCenterX - r, circleCenterY - r, circleCenterX + r, circleCenterY + r)
    val startAngle: Float = bound(180F, 270 - alphaRad / 2 / pi * 360F, 360F)
    val sweepAngle: Float = bound(zero, (2F * alphaRad) / 2 / pi * 360F, 180F)
    val progressSweepRad = if(maxProgress == 0) zero else bound(zero, progress.toFloat() / maxProgress * 2 * alphaRad, 2 * pi)
    val progressSweepAngle: Float = progressSweepRad / 2 / pi * 360F
    val thumbX: Int = (r * Math.cos(alphaRad + Math.PI / 2 - progressSweepRad).toFloat() + circleCenterX).toInt()
    val thumbY: Int = (-r * Math.sin(alphaRad + Math.PI / 2 - progressSweepRad).toFloat() + circleCenterY).toInt()

    val thirdRad = 33.toFloat() / 100 * 2 * alphaRad
    val twoThirdRad = 67.toFloat() / 100 * 2 * alphaRad
    val endRad = 2 * alphaRad

    val thirdXStart: Float =  (r * Math.cos(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterX)
    val thirdYStart: Float =  (-r * Math.sin(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterY)
    val thirdXEnd: Float =  ((r+25) * Math.cos(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterX)
    val thirdYEnd: Float =  ((-r-25) * Math.sin(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterY)

    val twoThirdXStart: Float =  (r * Math.cos(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterX)
    val twothirdYStart: Float =  (-r * Math.sin(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterY)
    val twothirdXEnd: Float =  ((r+25) * Math.cos(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterX)
    val twothirdYEnd: Float =  ((-r-25) * Math.sin(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterY)

    val zeroXText: Float = ((r+20) * Math.cos(alphaRad + Math.PI / 2 - zero).toFloat() + circleCenterX)
    val zeroYText: Float = ((-r) * Math.sin(alphaRad + Math.PI / 2 - zero).toFloat() + circleCenterY) + 70f

    val thirdXText: Float =  ((r+40) * Math.cos(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterX) - 20f
    val thirdYText: Float =  ((-r-40) * Math.sin(alphaRad + Math.PI / 2 - thirdRad).toFloat() + circleCenterY)

    val twoThirdXText: Float =  ((r+40) * Math.cos(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterX) - 20f
    val twoThirdYText: Float =  ((-r-40) * Math.sin(alphaRad + Math.PI / 2 - twoThirdRad).toFloat() + circleCenterY)

    val endXText: Float =  ((r) * Math.cos(alphaRad + Math.PI / 2 - endRad).toFloat() + circleCenterX)
    val endYText: Float =  ((-r) * Math.sin(alphaRad + Math.PI / 2 - endRad).toFloat() + circleCenterY) + 70f

    fun progressFromClick(x: Float, y: Float, thumbHeight: Int): Int? {
        if (y > height + dy * 2) return null
        val distToCircleCenter = Math.sqrt(Math.pow(circleCenterX - x.toDouble(), 2.0) + Math.pow(circleCenterY - y.toDouble(), 2.0))
        if (Math.abs(distToCircleCenter - r) > thumbHeight) return null
        val innerWidthHalf = width / 2
        val xFromCenter = bound(-innerWidthHalf, x - circleCenterX, innerWidthHalf).toDouble()
        val touchAngle = Math.acos(xFromCenter / r) + alphaRad - Math.PI / 2
        val angleToMax = 1.0 - touchAngle / (2 * alphaRad)
        return bound(0, ((maxProgress + 1) * angleToMax).toInt(), maxProgress)
    }
}