package co.uniquindio.qgame.ui.components

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import co.uniquindio.qgame.model.SimonColor
import kotlin.math.PI
import kotlin.math.sin

object SoundManager {

    private const val SAMPLE_RATE = 44100
    private const val TONE_DURATION_MS = 300
    private const val ERROR_FREQUENCY = 150

    fun playTone(color: SimonColor) {
        playFrequency(color.toneFrequency, TONE_DURATION_MS)
    }

    fun playErrorTone() {
        playFrequency(ERROR_FREQUENCY, 400)
    }

    private fun playFrequency(frequency: Int, durationMs: Int) {
        Thread {
            try {
                val numSamples = SAMPLE_RATE * durationMs / 1000
                val samples = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val angle = 2.0 * PI * frequency * i / SAMPLE_RATE
                    val fade = if (i < numSamples / 10) {
                        i.toDouble() / (numSamples / 10)
                    } else if (i > numSamples * 9 / 10) {
                        (numSamples - i).toDouble() / (numSamples / 10)
                    } else {
                        1.0
                    }
                    samples[i] = (sin(angle) * Short.MAX_VALUE * 0.6 * fade).toInt().toShort()
                }

                val bufferSize = samples.size * 2
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setSampleRate(SAMPLE_RATE)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(samples, 0, samples.size)
                audioTrack.play()

                Thread.sleep(durationMs.toLong() + 50)
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
                // Silently ignore audio errors
            }
        }.start()
    }
}