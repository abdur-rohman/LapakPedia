package dev.rohman.lapakpedia.utils

import dev.rohman.lapakpedia.BuildConfig

class ConstantUtil {
    companion object {
        const val SHARED_NAME = "dev.rohman.lapakpedia"
        const val SHARED_IMAGE_NAME = "dev.rohman.lapakpedia.image"

        const val MIDTRANS_CLIENT_KEY = "SB-Mid-client-qtWJQAQ_uxILhRR0"

        const val LAPAK_PEDIA_RETROFIT = "LAPAK_PEDIA_RETROFIT"
        const val LAPAK_PEDIA_BASE_URL = "https://lapak-pedia-api.herokuapp.com/api/"
        const val LAPAK_PEDIA_CLIENT = "BASIC_CLIENT"

        const val RAJA_ONGKIR_RETROFIT = "RAJA_ONGKIR_RETROFIT"
        const val RAJA_ONGKIR_BASE_URL = "https://api.rajaongkir.com/starter/"
        const val RAJA_ONGKIR_API_KEY = "d4bb9252bfe68b20fecb0846e4d7754f"
        const val RAJA_ONGKIR_CLIENT = "RAJA_ONGKIR_CLIENT"

        const val NOTIFICATION_ID = 123
        const val NOTIFICATION_CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.fcm"
        const val NOTIFICATION_CHANNEL_NAME = "Android Material Push Notification"
    }
}