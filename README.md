# ConvertWebViewToPdf
Library to create pdf file from webview

Add the following to your project level build.gradle:

        allprojects {	
	        repositories {
		        maven { url "https://jitpack.io" }
	        }
        }

Add this to your app build.gradle:

        implementation 'com.github.thefaisalurrehman:Webview-to-pdf:Tag'

Permission in Manifest

     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

     
If your targetSdkVersion >= 24, then we have to use FileProvider class to give access to the particular file or folder to make them accessible for other apps. 
Add a FileProvider tag in AndroidManifest.xml under tag.

    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    ...
    <application
    android:requestLegacyExternalStorage="true">
        ...
             <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.package.name.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>
    </application>
    </manifest>
    

Then create a provider_paths.xml file in res/xml folder.

    <?xml version="1.0" encoding="utf-8"?>
    <paths xmlns:android="http://schemas.android.com/apk/res/android">
     <external-path name="external_files" path="."/>
    </paths>

Before Create pdf check this permission for Android 11 devices.
   
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
	    // check permission is Granting
        if (requestCode == 1 && Environment.isExternalStorageManager()){
            //write create pdf code here
        }
    }
     



Sample code :
                
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




