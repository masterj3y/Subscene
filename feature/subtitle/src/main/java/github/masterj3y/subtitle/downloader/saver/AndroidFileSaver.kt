package github.masterj3y.subtitle.downloader.saver

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidFileSaver(private val contentResolver: ContentResolver) : FileSaver {

    override fun save(path: String, displayName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            saveMediaInMediaStore(path, displayName)
        else
            saveFileToExternalStorage(path, displayName)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveMediaInMediaStore(
        path: String,
        displayName: String
    ) {

        val file = File(path)

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        }

        try {
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?.also { uri ->
                    contentResolver.openOutputStream(uri).use { outputStream ->
                        outputStream?.write(file.readBytes())

                        file.delete()
                    }
                } ?: throw IOException("Couldn't create MediaStore entry")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveFileToExternalStorage(path: String, displayName: String) {
        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            displayName
        )

        val file = File(path)

        file.inputStream().use { input ->
            FileOutputStream(target).use { output ->
                input.copyTo(output)
            }
        }
        file.delete()
    }
}