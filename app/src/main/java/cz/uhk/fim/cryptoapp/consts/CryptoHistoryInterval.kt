package cz.uhk.fim.cryptoapp.consts

enum class CryptoHistoryInterval(val value: String) {
    MINUTE_1("m1"),
    MINUTE_5("m5"),
    MINUTE_15("m15"),
    MINUTE_30("m30"),
    HOUR_1("h1"),
    HOUR_2("h2"),
    HOUR_6("h6"),
    HOUR_12("h12"),
    DAY_1("d1");

    override fun toString(): String {
        return value
    }
}