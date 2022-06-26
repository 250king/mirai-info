package com.king250.bot.info

import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.text.Regex
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import okhttp3.OkHttpClient
import okhttp3.Request


object Plugin : KotlinPlugin(JvmPluginDescription("com.king250.bot.info", "1.0.0", "关于bot")) {
    override fun onEnable() {
        GlobalEventChannel.subscribeAlways<MessageEvent> {event ->
            val client = OkHttpClient()
            try {
                for (i in event.message) {
                    if (Regex("关于(机器人|bot|Cocoa|cocoa)").matches(i.content)) {
                        val request = Request.Builder().url(event.bot.avatarUrl).build()
                        val response = client.newCall(request).execute()
                        val stream = response.body?.byteStream()
                        val resource = stream?.toExternalResource()
                        if (resource != null) {
                            val image = event.subject.uploadImage(resource)
                            val message = buildMessageChain {
                                add(PlainText("${event.bot.nick}\n"))
                                add(Image.fromId(image.imageId))
                                add(PlainText("做最好的姐姐！\n\n"))
                                add(PlainText("Runtime version: ${System.getProperty("java.version")} ${System.getProperty("os.arch")}\n"))
                                add(PlainText("VM: ${System.getProperty("java.vm.name")}\n\n"))
                                add(PlainText("Powered by Mirai Framework\n"))
                                add(PlainText("©${Calendar.getInstance().get(Calendar.YEAR)} 250king."))
                            }
                            event.subject.sendMessage(message)
                            withContext(Dispatchers.IO) {
                                resource.close()
                                stream.close()
                                response.close()
                            }
                            break
                        }
                    }
                }
            }
            catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}