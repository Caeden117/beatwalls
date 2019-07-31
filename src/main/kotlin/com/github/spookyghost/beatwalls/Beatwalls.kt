package com.github.spookyghost.beatwalls

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.file
import mu.KotlinLogging
import java.io.File
import kotlin.system.exitProcess

val logger = KotlinLogging.logger{}

class Beatwalls : CliktCommand() {
    //TODO ADD ICON


    private val file: File by argument(help = "difficulty File (e.G expertPlus.dat)").file().validate {
        require((it.isDifficulty()) || it.isSong() ) { "Use a SongFolder or DifficultyFile"}
    }

    private val keepFiles by option ("--keepFiles", "-k",help = "keeps original files as backups").flag(default = false)

    private val dryRun by option("--dryRun", "-d",help = "Do not modify filesystem, only log output").flag(default = false)

    private val keepWalls by option("--keepWalls","-w",help = "keeps the original walls instead of deleting them").flag(default = false)

    private val quiet by option("--quiet", "-q",help = "NOT IMPLEMENTED - Do not print to sdtout").flag(default = false)

    private val yes by option("--yes", "-y",help = "skips confirmation").flag(default = false)

    private val bpm by option("--bpm", "-b",help="Beats per minute").double()

    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true)
        }

    }

    override fun run() {
        var beatsPerMinute = 0.0
        val difficultyList = mutableMapOf<Difficulty,File>()

        try {
            WallStructureManager.loadManager(readAssets())
            when {
                file.isSong() -> {
                    logger.info { "Detected Song. Running the program through all Difficulties which have commands" }
                    val map = Song(file)
                    beatsPerMinute = bpm?:map.info._beatsPerMinute
                    map.difficultyList.forEach {
                        if (it.component1().containsCommand("bw"))
                            difficultyList += it.toPair()
                    }
                }
                file.isDifficulty() -> {
                    logger.info{"Detected Difficulty"}
                    if (bpm == null)
                        logger.error { "No BPM detected, pls use the -b option" }
                    else
                        beatsPerMinute = bpm as Double
                    difficultyList += Pair(readDifficulty(file),file)
                }
            }

        }catch (e:Exception){
            println("\nFAILED\n")
        }


        difficultyList.forEach {

            //prints stuff if the quiet option is false
            printWarnings()

            //clears the wall if the keepwallsflag is false

            if(keepFiles){
                val diff = it.component1().copy()
                val path = File(it.component2().toString()+".old")
                writeDifficulty(Pair(diff,path))
                println("Written Backup to $path")
            }

            if (!keepWalls) it.component1()._obstacles.clear()

            it.component1().createWalls(beatsPerMinute)
            if(!dryRun)
                writeDifficulty(it.toPair())
        }
        println("Here comes Text displaying what just happened. \n\n")
        println("press enter to exit")
        readLine()
    }
    private fun printWarnings() {
        if (!quiet) {
            println("keep old Files: $keepFiles")
            println("dry run: $dryRun")
            println("keep old Walls: $keepWalls")
            if (!yes) {
                println("continue? (y/n)")
                if (readLine()?.toLowerCase() ?: "n" != "y") exitProcess(0)
            }
        }
    }
}
