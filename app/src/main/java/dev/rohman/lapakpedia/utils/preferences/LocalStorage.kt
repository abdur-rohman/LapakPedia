package dev.rohman.lapakpedia.utils.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.utils.save

class LocalStorage(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val NOTIFICATION_TOKEN_KEY = "NOTIFICATION_TOKEN_KEY"
        private const val FIRST_NAME_KEY = "FIRST_NAME_KEY"
        private const val LAST_NAME_KEY = "LAST_NAME_KEY"
        private const val PHONE_KEY = "PHONE_KEY"
        private const val EMAIL_KEY = "EMAIL_KEY"
        private const val ROLE_KEY = "ROLE_KEY"
        private const val IMAGE_KEY = "IMAGE_KEY"
        private const val IMAGE_NAME_KEY = "IMAGE_NAME_KEY"
        private const val POSTAL_CODE_KEY = "POSTAL_CODE_KEY"
        private const val CITY_ID_KEY = "CITY_ID_KEY"
        private const val PROVINCE_ID_KEY = "PROVINCE_ID_KEY"
        private const val PROVINCE_NAME_KEY = "PROVINCE_NAME_KEY"
        private const val CITY_NAME_KEY = "CITY_NAME_KEY"
    }

    var user: UserModel
        set(value) {
            cityId = value.cityId
            provinceId = value.provinceId
            cityName = value.cityName
            provinceName = value.provinceName
            postalCode = value.postalCode
            image = value.image
            imageName = value.imageName
            role = value.role
            phone = value.phone
            token = value.token
            notificationToken = value.notificationToken
            email = value.email
            firstName = value.firstName
            lastName = value.lastName
        }
        get() {
            return UserModel(
                lastName = lastName,
                firstName = firstName,
                notificationToken = notificationToken,
                token = token,
                cityId = cityId,
                cityName = cityName,
                postalCode = postalCode,
                phone = phone,
                email = email,
                id = 0,
                image = image,
                imageName = imageName,
                provinceId = provinceId,
                provinceName = provinceName,
                role = role
            )
        }

    var cityName: String
        set(value) = sharedPreferences.save(CITY_NAME_KEY, value)
        get() = sharedPreferences.getString(CITY_NAME_KEY, "") ?: ""

    var provinceName: String
        set(value) = sharedPreferences.save(PROVINCE_NAME_KEY, value)
        get() = sharedPreferences.getString(PROVINCE_NAME_KEY, "") ?: ""

    var provinceId: Int
        set(value) = sharedPreferences.save(PROVINCE_ID_KEY, value)
        get() = sharedPreferences.getInt(PROVINCE_ID_KEY, 0)

    var cityId: Int
        set(value) = sharedPreferences.save(CITY_ID_KEY, value)
        get() = sharedPreferences.getInt(CITY_ID_KEY, 0)

    var postalCode: String
        set(value) = sharedPreferences.save(POSTAL_CODE_KEY, value)
        get() = sharedPreferences.getString(POSTAL_CODE_KEY, "") ?: ""

    var imageName: String
        set(value) = sharedPreferences.save(IMAGE_NAME_KEY, value)
        get() = sharedPreferences.getString(IMAGE_NAME_KEY, "") ?: ""

    var image: String
        set(value) = sharedPreferences.save(IMAGE_KEY, value)
        get() = sharedPreferences.getString(IMAGE_KEY, "") ?: ""

    var role: String
        set(value) = sharedPreferences.save(ROLE_KEY, value)
        get() = sharedPreferences.getString(ROLE_KEY, "") ?: ""

    var token: String
        set(value) = sharedPreferences.save(TOKEN_KEY, value)
        get() = sharedPreferences.getString(TOKEN_KEY, "") ?: ""

    var notificationToken: String
        set(value) = sharedPreferences.save(NOTIFICATION_TOKEN_KEY, value)
        get() = sharedPreferences.getString(NOTIFICATION_TOKEN_KEY, "") ?: ""

    var firstName: String
        set(value) = sharedPreferences.save(FIRST_NAME_KEY, value)
        get() = sharedPreferences.getString(FIRST_NAME_KEY, "") ?: ""

    var lastName: String
        set(value) = sharedPreferences.save(LAST_NAME_KEY, value)
        get() = sharedPreferences.getString(LAST_NAME_KEY, "") ?: ""

    var phone: String
        set(value) = sharedPreferences.save(PHONE_KEY, value)
        get() = sharedPreferences.getString(PHONE_KEY, "") ?: ""

    var email: String
        set(value) = sharedPreferences.save(EMAIL_KEY, value)
        get() = sharedPreferences.getString(EMAIL_KEY, "") ?: ""

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}