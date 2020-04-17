package com.jekro.lesjardindecaro

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.PictureDrawable
import com.caverock.androidsvg.SVG
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import java.net.URL

class SVGRequestHandler : RequestHandler() {
    override fun canHandleRequest(data: Request?): Boolean = data?.uri.toString().contains(".svg")

    override fun load(request: Request, networkPolicy: Int): Result? {
        val ucon = URL(request.uri.toString()).openConnection()
        val svg = SVG.getFromInputStream(ucon.getInputStream())
        svg.documentWidth = request.targetWidth.toFloat()
        svg.documentHeight = request.targetHeight.toFloat()
        val picture = PictureDrawable(svg.renderToPicture())
        val returnedBitmap =
            Bitmap.createBitmap(request.targetWidth, request.targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawPicture(picture.picture)
        return Result(returnedBitmap, Picasso.LoadedFrom.NETWORK)
    }

}