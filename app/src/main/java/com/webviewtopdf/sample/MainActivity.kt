package com.webviewtopdf.sample

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.print.PdfView
import com.webviewtopdf.sample.ui.theme.ConvertWebViewToPdfDemomasterTheme
import java.io.File

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConvertWebViewToPdfDemomasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    MyContent()
}

// Creating a composable
// function to create WebView
// Calling this function as
// content in the above function
@Composable
fun MyContent() {
    val activity = LocalContext.current as Activity
    // Declare a string that contains a url
    val mUrl = "https://medium.com/@faisaldeveloper493"
    var webView: WebView? = null
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Adding a WebView inside AndroidView
        // with layout as full screen
        AndroidView(modifier = Modifier.weight(1f), factory = {

            WebView(it).apply {
                webView = this
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(mUrl)
            }

        }, update = {
            it.loadUrl(mUrl)
        })
        val context = LocalContext.current
        OutlinedButton(modifier = Modifier.width(250.dp), onClick = {
            val fileName = "Test.pdf"
            val directory = ContextCompat.getExternalFilesDirs(
                context,
                Environment.DIRECTORY_DOCUMENTS + "/PDFTest/"
            )
            PdfView.createWebPrintJob(
                activity,
                webView!!,
                directory[0],
                fileName
            ) { path ->
                path?.let { it ->
                    fileChooser(context,it)
                }
            }
        }) {
            Text(text = "Convert to Pdf")
        }
    }

}


private fun fileChooser(context: Context, path: String) {
    val file = File(path)
    val target = Intent("android.intent.action.VIEW")
    val uri = FileProvider.getUriForFile(context, "com.package.name.fileprovider", file)
    target.setDataAndType(uri, "application/pdf")
    target.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val intent = Intent.createChooser(target, "Open File")
    try {
        context.startActivity(intent)
    } catch (var6: ActivityNotFoundException) {
        var6.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConvertWebViewToPdfDemomasterTheme {
        Greeting()
    }
}
