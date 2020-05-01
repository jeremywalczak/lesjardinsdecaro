package com.jekro.lesjardindecaro

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jekro.lesjardindecaro.ui.GenericBottomSheetDialogFragment
import com.jekro.lesjardindecaro.ui.home.HomePageFragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.list_item_product.*
import java.util.*

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun ImageView.load(
    value: String?,
    transformation: Transformation? = null,
    fitOrCrop: String? = Constants.IMAGE_FIT,
    placeholder: Int? = null
) {
    if (value.isNullOrBlank()) {
        this.setImageDrawable(null)
        return
    }
    CustomPicasso(context).with(context)!!.cancelRequest(this)
    var creator: RequestCreator? = null
    if (value.startsWith("http") || value.startsWith("https") || value.contains(".svg")) {
        creator = if (placeholder != null) {
            CustomPicasso(context).with(context)!!.load(value).error(placeholder).placeholder(placeholder)
                .config(Bitmap.Config.RGB_565)
        } else {
            CustomPicasso(context).with(context)!!.load(value).config(Bitmap.Config.RGB_565)
        }
    } else if (!value.startsWith("svg_") && !value.startsWith("ic_")) {
        val identifier = context.resources.getIdentifier(value, "drawable", context.packageName)
        if (identifier != 0) {
            creator = if (placeholder != null) {
                CustomPicasso(context).with(context)!!.load(identifier).error(placeholder).placeholder(placeholder).config(
                    Bitmap.Config.RGB_565
                )
            } else {
                CustomPicasso(context).with(context)!!.load(identifier).config(Bitmap.Config.RGB_565)
            }
        }
    }
    if (creator != null) {
        if (fitOrCrop == Constants.IMAGE_FIT) {
            if (!this.layoutParamsHasWrapContent()) {
                creator.fit().centerInside()
            }
        } else if (fitOrCrop == Constants.IMAGE_CROP) {
            if (!this.layoutParamsHasWrapContent()) {
                creator.fit().centerCrop()
            }
        }
        if (transformation != null) {
            creator.transform(transformation)
        }
        creator.into(this)
    } else {
        var identifier =
            context.resources.getIdentifier("ic_$value", "drawable", context.packageName)
        if (identifier == 0) {
            identifier =
                context.resources.getIdentifier("svg_$value", "drawable", context.packageName)
        }
        if (identifier != 0) {
            setImageResource(identifier)
        }
    }
}

fun EditText.addSearchDelayListener(duration: Long, function: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        private var timer: Handler = Handler(Looper.getMainLooper())

        override fun afterTextChanged(text: Editable) {
            timer.removeCallbacksAndMessages(null)
            timer.postDelayed({
                function.invoke(text.toString())
            }, duration)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
    }
    this.addTextChangedListener(textWatcher)
    return textWatcher
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}


fun ImageView.layoutParamsHasWrapContent(): Boolean {
    return if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
        layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT
    ) {
        Log.e("Picasso", "You cannot call fit() on a view width wrap_content")
        true
    } else {
        false
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

@Suppress("DEPRECATION")
fun TextView.setHtmlText(html: String?) {
    html?.let {
        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(it)
        }
        movementMethod = if (html.toLowerCase(Locale.getDefault()).contains("<a href")) {
            LinkMovementMethod.getInstance()
        } else {
            null
        }
    }
}

fun AppCompatActivity.showCartDialog() {
    val frag = GenericBottomSheetDialogFragment.newInstance(
        HomePageFragment::class.java.name,
        ""
    )
    frag.show(supportFragmentManager, "cartDialog")
}