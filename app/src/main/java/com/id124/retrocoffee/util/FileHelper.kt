package com.id124.retrocoffee.util

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FileHelper {
    companion object {
        fun createPartFromString(descriptionString: String): RequestBody {
            return descriptionString.toRequestBody(MultipartBody.FORM)
        }

        fun createPartFromFile(path: String): MultipartBody.Part {
            val file = File(path)
            val reqFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            return MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        fun getPathFromURI(context: Context, uri: Uri): String {
            var realPath = String()
            uri.path?.let { path ->

                val databaseUri: Uri
                val selection: String?
                val selectionArgs: Array<String>?
                if (path.contains("/document/image:")) {
                    databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    selection = "_id=?"
                    selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
                } else {
                    databaseUri = uri
                    selection = null
                    selectionArgs = null
                }
                try {
                    val column = "_data"
                    val projection = arrayOf(column)
                    val cursor = context.contentResolver.query(
                        databaseUri,
                        projection,
                        selection,
                        selectionArgs,
                        null
                    )
                    cursor?.let {
                        if (it.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            realPath = cursor.getString(columnIndex)
                        }
                        cursor.close()
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }

            return realPath
        }
    }
}