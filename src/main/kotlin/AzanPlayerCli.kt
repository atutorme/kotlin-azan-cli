import kuusisto.tinysound.Music
import kuusisto.tinysound.TinySound
import java.util.*
import kotlin.concurrent.timerTask


val salatNamesToPlay = listOf(SalatNames.FAJR, SalatNames.DHUHR, SalatNames.ASR, SalatNames.MAGHRIB, SalatNames.ISHA)
    .map { it.niceName }

fun main() {
    val location = Location(
        "Brisbane",
        -27.468968,
        153.023499,
        17.0,
        10.0
    )

    val salatTimes = SalatTimes(location = location)

    val timer = Timer("Azan Player Cli", false)

    TinySound.init()
    val adhan = TinySound.loadMusic("AdhanMedina.wav")

    scheduleNextAzan(salatTimes, timer, adhan)
}

fun scheduleNextAzan(salatTimes: SalatTimes, timer: Timer, adhan: Music) {
    println("Scheduling salat: ${salatTimes.nextSalatTime()}")
    println("in: ${salatTimes.secondsToNextSalat()} seconds")

    timer.schedule(
        timerTask {
            println("It's azan time!")
            println("Salat: ${salatTimes.currentSalatTime()}")
            if (salatTimes.currentSalatTime().first in salatNamesToPlay) {
                adhan.rewind()
                adhan.play(false)
            }

            scheduleNextAzan(SalatTimes(location = salatTimes.location), timer, adhan)

        }, salatTimes.secondsToNextSalat() * 1000
    )
}