package top.harumill.getto

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.at
import kotlin.coroutines.EmptyCoroutineContext

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.harumill.getto",
        version = "0.1.1"
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        /**
         * 群消息
         */
        globalEventChannel().subscribeAlways(
            GroupMessageEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ) {
            group.sendMessage("hello")
        }
        /**
         * 好友消息
         */
        globalEventChannel().subscribeAlways(
            FriendMessageEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ) {
            
        }
        /**
         * 加群邀请
         */
        globalEventChannel().subscribeAlways(
            BotInvitedJoinGroupRequestEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ) {
            accept()
        }
        /**
         * 临时消息
         */
        globalEventChannel().subscribeAlways(
            TempMessageEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ) {

        }
        /**
         * 成员退群
         */
        globalEventChannel().subscribeAlways(
            MemberLeaveEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ){
            group.sendMessage("${member.nick}离开了我们。。。 ")
        }
        /**
         * 成员加群
         */
        globalEventChannel().subscribeAlways(
            MemberJoinEvent::class,
            EmptyCoroutineContext,
            Listener.ConcurrencyKind.CONCURRENT
        ){
            group.sendMessage("欢迎新人"+ At(member)+"加群，要和大家愉快玩耍哦，发送#help可获取bot可用指令")
        }
    }
}
