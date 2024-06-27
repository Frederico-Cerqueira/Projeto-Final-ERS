package ers.app.service.http
import ers.app.domainEntities.outputModels.TaskStartOutputModel
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RequestTask {
    private val baseUrl = "http://localhost:5000/"

    private fun sendHttpRequest(urlPath: String, method: String, requestBody: String? = null): String {
        val url = URL(baseUrl + urlPath)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.setRequestProperty("Content-Type", "application/json; utf-8")
        conn.doOutput = requestBody != null

        requestBody?.let {
            val os = conn.outputStream
            os.write(it.toByteArray(Charsets.UTF_8))
            os.flush()
        }


        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        var inputLine: String?
        val response = StringBuffer()

        while (reader.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        reader.close()

        println(response.toString())

        return response.toString()
    }

    fun startTask(taskData: TaskStartOutputModel) {
        val jsonInputString = Json.encodeToString(TaskStartOutputModel.serializer(), taskData)
        sendHttpRequest("start", "POST", jsonInputString)
    }

    fun stopTask(id: Int) {
        sendHttpRequest("stop/$id", "GET")
    }
}

