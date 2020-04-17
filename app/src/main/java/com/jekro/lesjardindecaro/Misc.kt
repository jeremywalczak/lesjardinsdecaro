package com.jekro.lesjardindecaro

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Transformation

class Constants {
    companion object {
        const val IMAGE_FIT = "FIT"
        const val IMAGE_CROP = "CROP"
    }
}

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

    Picasso.get().cancelRequest(this)
    var creator: RequestCreator? = null
    if (value.startsWith("http") || value.startsWith("https") || value.contains(".svg")) {
        creator = if (placeholder != null) {
            Picasso.get().load(value).error(placeholder).placeholder(placeholder)
                .config(Bitmap.Config.RGB_565)
        } else {
            Picasso.get().load(value).config(Bitmap.Config.RGB_565)
        }
    } else if (!value.startsWith("svg_") && !value.startsWith("ic_")) {
        val identifier = context.resources.getIdentifier(value, "drawable", context.packageName)
        if (identifier != 0) {
            creator = if (placeholder != null) {
                Picasso.get().load(identifier).error(placeholder).placeholder(placeholder).config(
                    Bitmap.Config.RGB_565
                )
            } else {
                Picasso.get().load(identifier).config(Bitmap.Config.RGB_565)
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