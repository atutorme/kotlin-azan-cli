import kuusisto.tinysound.Music
import kuusisto.tinysound.TinySound
import java.util.*
import kotlin.concurrent.timerTask


val salatNamesToPlay = listOf(SalatNames.FAJR, SalatNames.DHUHR, SalatNames.ASR, SalatNames.MAGHRIB, SalatNames.ISHA)
    .map { it.niceName }

val adhanFiles = listOf(
    "AdhanMakkah.wav",
    "AdhanMedina.wav",
    "AdhanAlaqsa.wav",
    "AdhanEgypt.wav",
)
val adhanFajrFile = "AdhanFajr.wav"
val beepFile = "beep.wav"

lateinit var adhan: List<Music>
lateinit var adhanFajr: Music
lateinit var beep: Music

val r = Random()

fun getAdhan() : Music {
    val i = r.nextInt() % adhan.size
    println("Selected adhan: ${adhanFiles[i]}")
    return adhan[i]
}

fun getAdhan(salatNiceName: String) : Music =
    when (salatNiceName) {
        !in salatNamesToPlay -> {
            println("Selected beep")
            beep
        }
        SalatNames.FAJR.niceName -> {
            println("Selected adhan fajr")
            adhanFajr
        }
        else -> getAdhan()
    }

fun playAdhan(salatNiceName: String) {
    val adhanToPlay = getAdhan(salatNiceName)
    adhanToPlay.rewind()
    adhanToPlay.play(false)
}

fun main() {
    TinySound.init()
    adhan = adhanFiles.map(TinySound::loadMusic)
    adhanFajr = TinySound.loadMusic(adhanFajrFile)

    val location = Location(
        "Brisbane",
        -27.468968,
        153.023499,
        17.0,
        10.0
    )

    val salatTimes = SalatTimes(location = location)

    println("Today's salat times:")
    println("-------------------------------")
    salatTimes.salatTimestamps.forEach(::println)
    println("-------------------------------")

    val timer = Timer("Azan Player Cli", false)

    scheduleNextAzan(salatTimes, timer)
}

fun scheduleNextAzan(salatTimes: SalatTimes, timer: Timer) {
    println("Scheduling salat: ${salatTimes.nextSalatTime()}")
    println("in: ${salatTimes.secondsToNextSalat()} seconds")
    println("-------------------------------")

    timer.schedule(
        timerTask {
            val currentSalat = salatTimes.currentSalatTime()
            println("It's azan time!")
            println("Salat: $currentSalat")

            playAdhan(currentSalat.first)

            println("-------------------------------")

            scheduleNextAzan(SalatTimes(location = salatTimes.location), timer)

        }, salatTimes.secondsToNextSalat() * 1000
    )
}