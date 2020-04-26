package compiler.parser

import org.junit.Test
import structure.Wall
import java.io.File
import kotlin.reflect.full.memberProperties

class LineParserTest {

    @Test
    fun getStructList() {
    }

    @Test
    fun getLastStructure() {
    }

    @Test
    fun setLastStructure() {
    }

    @Test
    fun getBaseStructures() {
    }

    @Test
    fun getCustomStructures() {
    }

    @Test
    fun getInterfaces() {
    }

    @Test
    fun getConstantList() {
    }

    @Test
    fun getFunctionList() {
    }

    @Test
    fun getWallStructurePropertyNames() {
    }

    @Test
    fun parseLine() {
        val t = """
interface hyper
  a=10
const b = 20
fun f(x) = x*x
10 wall
  a += 5
  p1 += f(a),a,b
  a += 10
        """.trimIndent()
        //todo clean up
        val lp = LineParser()

        val l = t.lines().map{ Line(it,0,File("")) }
        lp.parseLines(l)
        val ws = lp.structList[0].create()
        println(ws.a)
    }

    @Test
    fun isConstant() {
        val l = Wall()
        val ws = l::class.memberProperties

        println()
    }

    @Test
    fun defineConstant() {
    }

    @Test
    fun isFunction() {
    }

    @Test
    fun defineFunction() {
    }

    @Test
    fun isDefineCustomStruct() {
    }

    @Test
    fun defineCustomStruct() {
    }

    @Test
    fun isInterface() {
    }

    @Test
    fun defineInterface() {
    }

    @Test
    fun isProperty() {
    }

    @Test
    fun addPropertytoWs() {
    }

    @Test
    fun isDefaultStructure() {
    }

    @Test
    fun addDefaultStruct() {
    }

    @Test
    fun isCustomStructure() {
    }

    @Test
    fun addStoredStruct() {
    }

    @Test
    fun addLastStruct() {
    }

    @Test
    fun isWallstructInHashmap() {
    }

    @Test
    fun wsNameOfLine() {
    }

    @Test
    fun wsBeatOfLine() {
    }

    @Test
    fun interfacesOfLine() {
    }

    @Test
    fun propNameOfLine() {
    }

    @Test
    fun propValueOfLine() {
    }

    @Test
    fun assignOfLine() {
    }

    @Test
    fun assignProperty() {
    }
}