package difficulty

import com.google.gson.annotations.SerializedName
import mu.KotlinLogging
import structure.SpookyWall
import java.text.DecimalFormat
import kotlin.math.abs

private val logger = KotlinLogging.logger {}

data class _obstacles (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_lineIndex") val _lineIndex : Int,
    @SerializedName("_type") val _type : Int,
    @SerializedName("_duration") var _duration : Double,
    @SerializedName("_width") val _width : Int
){

    fun adjustBPM(bpm: Double, newBpm: Double, timeOffset: Double){
        if(this._duration>0) {
            this._duration = this._duration / newBpm * bpm
            this._time = this._time / newBpm * bpm
            this._time += timeOffset
        }else{
            this._time += timeOffset
        }
    }

    fun toWall(): SpookyWall {
        val startTime = this._time
        val duration = this._duration
        val startRow =  getLineIndex(_lineIndex)
        val height = getHeight(_type)
        val startHeight = getStartHeight(_type)
        val width = getWidth(_width)

        //todo Translate Starttime to Beat
        return SpookyWall(startRow, duration, width, height, startHeight, startTime)
    }

    private fun getHeight(i:Int):Double{
        var a = 0.0
        when {
            i < 1000 -> logger.warn { "Normal Walls are currently not supported, skipping" }
            i < 4000 -> a = (i - 1000 )/4.0*3 //todo fix
            else -> {
                a = ((i-4001)/1000).toDouble()
                a = a/1000 / (1.0/3.0) * (4.0/3.0)
            }
        }
        return a
    }
    private fun getStartHeight(i:Int):Double{
        if(i<500) return 0.0
        var a = (i % 1000 -1).toDouble()
        a= a/250 * (4.0/3.0)
        return a
    }

    private fun getLineIndex(i:Int):Double =
        if(i>=0)
            (i.toDouble() - 1000)/1000.0 -2.0
        else
            (i.toDouble() +1000)/1000 - 2.0

    private fun getWidth(i:Int):Double =
        (i.toDouble() -1000) / 1000

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as _obstacles
        if (abs(_time - other._time) < 0.001) return false
        if (abs(_lineIndex - other._lineIndex)< 0.001) return false
        if (abs(_type - other._type) < 10) return false
        if (abs(_duration - other._duration) < 0.001) return false
        if (abs(_width - other._width) < 0.001) return false
        return true
    }

    override fun hashCode(): Int {
        var result = _time.hashCode()
        result = 31 * result + _lineIndex
        result = 31 * result + _type
        result = 31 * result + _duration.hashCode()
        result = 31 * result + _width
        return result
    }
}
