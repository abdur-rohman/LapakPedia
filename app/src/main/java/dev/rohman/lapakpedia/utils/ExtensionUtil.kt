package dev.rohman.lapakpedia.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dev.rohman.lapakpedia.R
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*


fun CharSequence?.toFile(): File = File(toString())
val CharSequence?.isValid get() = this != null && isNotEmpty()
val CharSequence?.isEmail
    get() = isValid && Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches()

val CharSequence?.isAlphanumeric
    get() = toString().toCharArray().all { it.isLetterOrDigit() } &&
            toString().toCharArray().any { it.isDigit() } &&
            toString().toCharArray().any { it.isLetter() }

fun Number.toLocalNumber(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale("in", "ID"))
    return numberFormat.format(this)
}

fun Number.toRupiah(): String = "Rp. ${toLocalNumber()},00"

fun Context.getSpinnerAdapter(list: List<String>) = ArrayAdapter(
    this,
    android.R.layout.simple_spinner_item,
    list
).apply {
    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
}

fun String.toRequestBody(): RequestBody = toRequestBody(MultipartBody.FORM)
fun String.toMultiPartBody(name: String = "image"): MultipartBody.Part? {
    val file = toFile()

    return if (file.isExist) {
        val bitmap = BitmapFactory.decodeFile(this)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)

        FileOutputStream(this, true).apply {
            write(bos.toByteArray())
            flush()
            close()
        }

        MultipartBody.Part.createFormData(name, file.name, file.toRequestBody())
    } else null
}

fun File.toRequestBody(): RequestBody = asRequestBody(MultipartBody.FORM)
val File.isExist get() = exists()

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(requireActivity(), message, duration).show()

fun View.showError(message: String?, onAction: () -> Unit = { }) =
    Snackbar.make(this, message ?: "Oops something went wrong!", Snackbar.LENGTH_INDEFINITE).also {
        it.setTextColor(context.getColor(R.color.colorBackgroundSecondary))
        it.setActionTextColor(context.getColor(R.color.colorError))
        it.setAction("Dismiss") { _ -> it.dismiss(); onAction() }
    }.show()

fun View.dismissKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.run {
        hideSoftInputFromWindow(windowToken, 0)
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> SharedPreferences.save(key: String, value: T) {
    edit {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Set<*> -> putStringSet(key, value as? Set<String> ?: setOf())
        }
    }
}