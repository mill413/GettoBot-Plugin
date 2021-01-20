package top.harumill.getto

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.utils.info

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
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            Getto.parseCmd(message,group,this)
        }
        /**
         * 好友消息
         */
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            friend.sendMessage("hi")
        }
        /**
         * 临时消息
         */
        globalEventChannel().subscribeAlways<GroupTempMessageEvent> {
            sender.sendMessage("hi")
        }
        /**
         * 加群邀请
         */
        globalEventChannel().subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            accept()
        }
        /**
         * 成员退群
         */
        globalEventChannel().subscribeAlways<MemberLeaveEvent> {
            group.sendMessage("${member.nick}离开了我们。。。 ")
        }
        /**
         * 成员加群
         */
        globalEventChannel().subscribeAlways<MemberJoinEvent> {
            group.sendMessage("欢迎新人"+ At(member)+"加群，要和大家愉快玩耍哦，发送#help可获取bot可用指令")
        }
    }
}
