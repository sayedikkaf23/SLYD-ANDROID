package chat.hola.com.app.calling.video.call

import chat.hola.com.app.calling.video.janus.JanusConnection
import com.kotlintestgradle.remote.model.response.calling.CallerDetailsResponse


data class CallUserDetails(
        var janusConnection: JanusConnection?,
        var userDetails: CallerDetailsResponse?,
        var isToShowPlaceHodler: Boolean
)