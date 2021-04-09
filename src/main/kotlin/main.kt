import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

class Machine(private val id: Int) {
    fun start() {
        GlobalScope.launch {
            while (true) {
                println(id)// + Random.nextInt(0, 5))
                delay(3000)
            }
        }
    }

    suspend fun process(lot: Int) {
        delay(9100)
        println("Machine: $id, Lot: $lot")// + lot)
    }
}

class Line(private var machines: List<Machine>) {
    fun start() {
        machines.forEach { machine ->
            machine.start()
        }
    }

    fun process(lots: List<Int>) {
        val d: MutableList<Deferred<Any>> = mutableListOf()
        runBlocking {
            for (i in lots) {
                machines.forEach {
                    d.add(async {
                        it.process(i)
                    })
                }
                d.forEach {
                    it.await()
                }
            }
        }
    }

}

fun main() {

    val machines: MutableList<Machine> = mutableListOf()
    for (i in 1..3) {
        machines.add(Machine(i))
    }

    val line = Line(machines)
    line.start()
    val lots = listOf(1, 2, 3, 4, 5)
    line.process(lots)
}