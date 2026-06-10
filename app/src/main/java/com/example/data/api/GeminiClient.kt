package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    // Use the mandatory gemini-3.5-flash for basic text & parsing tasks
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(prompt: String, systemInstruction: String? = null, responseJson: Boolean = false): String {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isEmpty() || key == "MY_GEMINI_API_KEY") {
                Log.e(TAG, "Gemini API key is not configured or uses a placeholder.")
                return@withContext "ERROR_MISSING_API_KEY"
            }

            try {
                val url = "$BASE_URL?key=$key"

                // Create the request JSON
                val root = JSONObject()
                
                // contents
                val contentsArray = JSONArray()
                val contentObj = JSONObject()
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", prompt)
                partsArray.put(partObj)
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
                root.put("contents", contentsArray)

                // systemInstruction
                if (systemInstruction != null) {
                    val sysObj = JSONObject()
                    val sysParts = JSONArray()
                    val sysPart = JSONObject()
                    sysPart.put("text", systemInstruction)
                    sysParts.put(sysPart)
                    sysObj.put("parts", sysParts)
                    root.put("systemInstruction", sysObj)
                }

                // generationConfig
                if (responseJson) {
                    val configObj = JSONObject()
                    configObj.put("responseMimeType", "application/json")
                    root.put("generationConfig", configObj)
                }

                val requestBodyStr = root.toString()
                val body = requestBodyStr.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        val errBody = response.body?.string() ?: ""
                        Log.e(TAG, "Request failed (code ${response.code}): $errBody")
                        return@withContext "ERROR_API_${response.code}"
                    }

                    val respBody = response.body?.string() ?: ""
                    val jsonResp = JSONObject(respBody)
                    val candidates = jsonResp.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.optJSONObject("content")
                        if (content != null) {
                            val parts = content.optJSONArray("parts")
                            if (parts != null && parts.length() > 0) {
                                return@withContext parts.getJSONObject(0).optString("text", "")
                            }
                        }
                    }
                    return@withContext "ERROR_NO_CONTENT"
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception during API call", e)
                return@withContext "ERROR_EXCEPTION_${e.localizedMessage}"
            }
        }
    }
}
