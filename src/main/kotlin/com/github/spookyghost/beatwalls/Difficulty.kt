package com.github.spookyghost.beatwalls
import com.google.gson.annotations.SerializedName

data class Difficulty (

    @SerializedName("_version") var _version : String,
    @SerializedName("_BPMChanges") val _BPMChanges : ArrayList<_BPMChanges>,
    @SerializedName("_bookmarks") val _bookmarks : ArrayList<_bookmarks>,
    @SerializedName("_events") var _events : ArrayList<_events>,
    @SerializedName("_notes") var _notes : ArrayList<_notes>,
    @SerializedName("_obstacles") var _obstacles : ArrayList<_obstacles>
)

data class _BPMChanges (

    @SerializedName("_BPM") val _BPM : Double,
    @SerializedName("_time") val _time : Double,
    @SerializedName("_beatsPerBar") val _beatsPerBar : Int,
    @SerializedName("_metronomeOffset") val _metronomeOffset : Int
)

data class _bookmarks (

    @SerializedName("_time") val _time : Double,
    @SerializedName("_name") val _name : String
)

data class _events (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_type") var _type : Int,
    @SerializedName("_value") val _value : Int
)

data class _notes (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_lineIndex") val _lineIndex : Int,
    @SerializedName("_lineLayer") val _lineLayer : Int,
    @SerializedName("_type") val _type : Int,
    @SerializedName("_cutDirection") val _cutDirection : Int
)

data class _obstacles (

    @SerializedName("_time") var _time : Double,
    @SerializedName("_lineIndex") val _lineIndex : Int,
    @SerializedName("_type") val _type : Int,
    @SerializedName("_duration") var _duration : Double,
    @SerializedName("_width") val _width : Int
)


fun Difficulty.createWalls(bpm:Double, spawnDistance:Int){
    val marks = _bookmarks.filter { it._name.startsWith("/bw") }
    marks.forEach { it ->

        val tempbpm =_BPMChanges.findLast{ bpmChanges -> bpmChanges._time < it._time }?._BPM ?: bpm

        val bpmMultiplier =  bpm / tempbpm


        val struct = it._name.removePrefix("/bw ").substringBefore(' ')

        val parameters= it._name.substringAfter("$struct ").split(" ")

        val list = when(struct.toLowerCase()){
            "floor" -> Floor(parameters[0].toDouble(), parameters[1].toDouble()).set
            else -> arrayListOf()
            //TODO find a way to set default parameters, when no parameters are given
        }

        val time = it._time


        list.forEach {
            it._duration * bpmMultiplier
            it._time+=time
            _obstacles.add(it)
        }


    }
}


