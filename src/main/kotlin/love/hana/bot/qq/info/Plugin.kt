package love.hana.bot.qq.info


import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.util.Calendar

object Plugin : KotlinPlugin(JvmPluginDescription("love.hana.bot.qq.info", "2.0.0", "bot自述")) {
    override fun onEnable() {
        GlobalEventChannel.subscribeAlways<MessageEvent> {event ->
            try {
                for (i in event.message) {
                    if (Regex("关于(机器人|bot|hana bot)").matches(i.content)) {
                        val resource = this@Plugin.getResourceAsStream("image.jpg")
                        val stream = resource?.toExternalResource()
                        if (stream != null) {
                            val image = event.subject.uploadImage(stream)
                            val message = buildMessageChain {
                                add(PlainText("${event.bot.nick}\n"))
                                add(Image.fromId(image.imageId))
                                add(PlainText("请问能给我一些点心吗OVO？\n\n"))
                                add(PlainText("Built on July 27, 2021\n\n"))
                                add(PlainText("Runtime version: ${System.getProperty("java.version")} ${System.getProperty("os.arch")}\n"))
                                add(PlainText("VM: ${System.getProperty("java.vm.name")}\n\n"))
                                add(PlainText("Powered by Mirai Framework\n"))
                                add(PlainText("Copyright ${Calendar.getInstance().get(Calendar.YEAR)} 250king."))
                            }
                            event.subject.sendMessage(message)
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