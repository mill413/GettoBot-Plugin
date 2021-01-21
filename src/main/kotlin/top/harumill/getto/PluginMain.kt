package top.harumill.getto

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.contact.getMemberOrFail
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.info
import java.io.File
import java.time.LocalTime

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.harumill.getto",
        version = "0.1.1"
    )
) {
    const val imgDir = "data/img/"
    const val pcrDir = "${imgDir}/pcr/"
    val setuDir = "${imgDir}setu/no/"
    val comicDir = "${pcrDir}comic/"
    val stampDir = "${pcrDir}stamp/"
    val catsDir = "${imgDir}cats/"
    val atDir = "${imgDir}atbot/"
    var files:MutableList<String> = mutableListOf()

    override fun onEnable() {
        logger.info { "Plugin loaded" }
        /**
         * 群消息
         */
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            /**
             * 仅复读图片和文字
             */
            var repeatOrNot:Boolean = true
            message.forEach { if (it !is PlainText || it !is Image) repeatOrNot = false }
            if(repeatOrNot == true){
                if((0..1000).random() < 7){
                    group.sendMessage(message)
                }
            }

            var msg = message.contentToString()
//            println(msg)
            if(msg.startsWith("#")){
                msg = msg.removePrefix("#")
                when(true){
                    msg == "help" -> {
                        group.sendImage(File("${imgDir}/help.png"))
                    }
                    msg == "setu" -> {
                        if((0..1000).random() < 14){
                            files = Getto.getImgList(setuDir)
                            group.sendImage(File(imgDir+files[(0..files.size).random()]))
                        }
                    }
                    msg.startsWith("pcr") -> {
                        msg = msg.removePrefix("pcr").trim()
                        when(true){
                            msg.startsWith("comic") -> {
                                files = Getto.getImgList(comicDir)
                                val fileName =
                                    if(msg.removePrefix("comic").trim().isEmpty())
                                        files.random()
                                    else
                                        "episode_${msg.removePrefix("comic").trim().toInt()}.png"

                                if(fileName !in files){
                                    group.sendMessage("没有你要找的")
                                }
                                else{
                                    group.sendImage(File(comicDir+fileName))
                                }
                            }
                            msg.startsWith("stamp") -> {
                                files = Getto.getImgList(stampDir)
                                val fileName =
                                    if(msg.removePrefix("stamp").trim().isEmpty())
                                        files.random()
                                    else
                                        msg.removePrefix("stamp").trim()+".png"

                                if(fileName !in files) {
                                    group.sendMessage("没有你要找的")
                                }
                                else {
                                    group.sendImage(File(stampDir+fileName))
                                }
                            }
                        }
                    }

                }
            }
            else{
                when(msg){
                    "hi","早上好","中午好","下午好","晚上好" -> {
                        val curTime = LocalTime.now().hour
                        val meet = when(curTime){
                            in 5..10 -> {
                                "早上好!"
                            }
                            in 11..14 -> {
                                "中午好!"
                            }
                            in 15..18 -> {
                                "下午好!"
                            }
                            in 19..23 -> {
                                "晚上好!"
                            }
                            else -> {
                                "晚上好!好好睡觉"
                            }
                        }
                        group.sendMessage(At(sender)+meet)
                    }
                    "随机猫猫" -> {
                        files = Getto.getImgList(catsDir)
                        val cat = files.random()
                        if(cat.startsWith("dog")){
                            group.sendMessage(PlainText("恭喜${sender.nameCard}随机出一只狗狗")+group.uploadImage(File(catsDir+cat)))
                        }
                        else{
                            group.sendImage(File(catsDir+cat))
                        }
                    }
                    "@${bot.id}","@${bot.id} "-> {
                        if(sender.id == Getto.wifeID){
                            group.sendMessage(At(sender)+group.uploadImage(File("${imgDir}wife.jpg")))
                        }
                        else{
                            files = Getto.getImgList(atDir)
                            group.sendMessage(At(sender)+group.uploadImage(File(atDir+ files.random())))
                        }
                    }
                }
                when{
                    msg.startsWith("留言") -> {
                        bot.getFriendOrFail(Getto.authorId).sendMessage("${sender.nick}(${sender.id})从群${group.name}(${group.id})发了条消息:\n${msg.removePrefix("留言")}")
                    }
                    msg.startsWith("re") -> {
                        group.sendMessage(msg.removePrefix("re"))
                    }
                    msg.startsWith("来一份禁言") -> {
                        val botLv =group.botPermission.level
                        val sendLv = group.getMemberOrFail(sender.id).permission.level
                        val time =
                            if(msg.removePrefix("来一份禁言").trim().isEmpty())
                                (0..30*24*60*60).random()
                            else
                                msg.removePrefix("来一份禁言").trim().toInt()
                        if(time !in (0..30*24*60*60)){
                            group.sendMessage("请输入0到2592000范围内的整数")
                        }
                        else{
                            when{
                                botLv > sendLv -> {
                                    sender.mute(time)
                                }
                                sendLv == 2 -> {
                                    group.sendMessage("我不能禁言群主哦，建议群主退下群")
                                }
                                sendLv == 1 -> {
                                    group.sendMessage("权限不够，建议让我当群主")
                                }
                                else -> {
                                    group.sendMessage("权限不够，建议让我当管理")
                                }
                            }
                        }

                    }
                    msg.startsWith(".r") -> {
                        val face:Int =
                            if(msg.removePrefix(".r").isEmpty())
                                6
                            else
                                msg.removePrefix(".r").toInt()

                        group.sendMessage("${sender.nameCard}掷出了${(1..face).random()}")
                    }
                }
            }
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
        /**
         * 戳一戳消息
         */
        globalEventChannel().subscribeAlways<NudgeEvent> {
            if(target == bot){
                from.nudge().sendTo(subject)
            }
        }
    }
}
