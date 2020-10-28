package com.punuo.sys.sdk.httplib

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import java.io.*
import java.util.concurrent.TimeUnit

/**
 * Created by han.chen.
 * Date on 2020/10/28.
 **/
class DownloadManager {
    var DEBUG = true

    private val TAG = "DownloadManager"

    private val mHandler = Handler(Looper.getMainLooper())

    private var call: Call? = null
    private var downloadListener: DownloadListener? = null
    private var mOkHttpClient: OkHttpClient? = null
    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
        mOkHttpClient = builder.build()
    }

    fun download(url: String, savePath: String, listener: DownloadListener) {
        if (DEBUG) {
            Log.d(TAG, "downloadurl is : $url")
            Log.d(TAG, "savePath is : $savePath")
        }
        downloadListener = listener
        val builder = Request.Builder()
        builder.url(url)
        call = mOkHttpClient?.newCall(builder.build())
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (DEBUG) {
                        Log.d(TAG, "server contacted and has file")
                    }
                    val writtenToDisk = writeResponseBodyToDisk(response.body(), savePath)
                    if (DEBUG) {
                        Log.d(TAG, "file download was a success? $writtenToDisk")
                    }
                    if (writtenToDisk) {
                        callDownloadSuccess()
                    } else {
                        callDownloadFailed()
                    }
                } else {
                    callDownloadFailed()
                    if (DEBUG) {
                        Log.d(TAG, "server contact failed")
                    }
                }
                response.body()?.close()
            }
        })
    }

    private fun callDownloadSuccess() {
        mHandler.post {
            downloadListener?.onDownloadSuccess()
        }
    }

    private fun callDownloadFailed() {
        mHandler.post {
            downloadListener?.onDownloadFailed()
        }
    }

    private fun callDownloadProgress(progress: Float) {
        mHandler.post {
            downloadListener?.onDownloadProgress(progress)
        }
    }


    fun cancel() {
        call?.cancel()
    }

    fun release() {
        mHandler.removeCallbacksAndMessages(null)
        downloadListener = null
    }

    private fun writeResponseBodyToDisk(body: ResponseBody?, savePath: String): Boolean {
        return try {
            val saveFile = File(savePath)
            if (saveFile.exists()) {
                saveFile.delete()
            }
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body!!.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(saveFile)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    callDownloadProgress(fileSizeDownloaded * 1f / fileSize)
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    interface DownloadListener {
        fun onDownloadSuccess()
        fun onDownloadFailed()
        fun onDownloadProgress(progress: Float)
    }
}