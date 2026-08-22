fun main() {
    val raw = 2400L
    val mhz = if (raw > 10_000_000L) raw / 1_000_000.0 else if (raw > 10_000L) raw / 1000.0 else raw.toDouble()
    val curFreqGHz = mhz / 1000.0
    println(String.format(java.util.Locale.US, "%.2f GHz", curFreqGHz))
}
