package android.print

import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintDocumentAdapter.LayoutResultCallback
import android.print.PrintDocumentAdapter.WriteResultCallback
import android.util.Log
import java.io.File

class PdfPrint(private val printAttributes: PrintAttributes) {
    fun print(
        printAdapter: PrintDocumentAdapter,
        path: File,
        fileName: String,
        callback: (path: String?) -> Unit
    ) {
        printAdapter.onLayout(null, printAttributes, null, object : LayoutResultCallback() {
            override fun onLayoutFinished(info: PrintDocumentInfo, changed: Boolean) {
                printAdapter.onWrite(
                    arrayOf(PageRange.ALL_PAGES),
                    getOutputFile(path, fileName),
                    CancellationSignal(),
                    object : WriteResultCallback() {
                        override fun onWriteFinished(pages: Array<PageRange>) {
                            super.onWriteFinished(pages)
                            if (pages.isNotEmpty()) {
                                val file = File(path, fileName)
                                callback(file.absolutePath)
                            } else {
                                callback(null)
                            }
                        }
                    })
            }
        }, null)
    }

    private fun getOutputFile(
        path: File,
        fileName: String
    ): ParcelFileDescriptor? {
        if (!path.exists()) {
            path.mkdirs()
        }
        return try {
            val file = File(path, fileName)
            file.createNewFile()
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e)
            null
        }
    }

    companion object {
        private val TAG = PdfPrint::class.java.simpleName
    }
}