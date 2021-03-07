package top.harumill.getto

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.contact.getMemberOrFail
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.info
import top.harumill.getto.bot.Getto
import top.harumill.getto.bot.ImgInfo
import top.harumill.getto.botInfo.GettoInfo
import top.harumill.getto.bot.MessagesPool
import java.io.File
import java.net.InetAddress
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.harumill.getto",
        name = "Getto",
        version = "0.1.1"
    )
) {
    const val voiceDir = "data/voice/"

    const val imgDir = "data/img/"

    const val pcrDir = "${imgDir}pcr/"
    val setuDir = "${imgDir}setu/no/"

    val comicDir = "${pcrDir}comic/"
    val stampDir = "${pcrDir}stamp/"
    val cardDir = "${pcrDir}card/"

    val catsDir = "${imgDir}cats/"
    val atDir = "${imgDir}atbot/"
    val sdDir = "${imgDir}sbdog/"
    val azDir = "${imgDir}az/"

    val bangbgDir = "${imgDir}bang_bg/"
    var files: MutableList<String> = mutableListOf()

    val startTime:LocalDateTime = LocalDateTime.now()

    @MiraiExperimentalApi
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        /**
         * 群消息
         */
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            MessagesPool.insert(message, LocalDateTime.now())
            /**
             * 仅复读图片和文字
             */
            var repeatOrNot: Boolean = true
            var isSrc: Boolean = false
            message.forEach {
                if (it is FlashImage) {
                    val dtm = LocalDateTime.now()
                    bot.getFriendOrFail(GettoInfo.authorId)
                        .sendMessage(PlainText("${dtm.toLocalDate()} ${dtm.hour}:${dtm.minute}:${dtm.second}\n${sender.nick}(${sender.id})在群${group.name}(${group.id})发送了一张闪照") + it.image)
                }
                if (isSrc == false) {
                    isSrc = true
                } else if (it !is Image && it !is PlainText && it !is Face) {
                    repeatOrNot = false
                }
            }
            if (repeatOrNot == true) {
                if ((0..1000).random() < 7) {
                    group.sendMessage(message)
                }
            }
            var msg = message.contentToString()
//            if(msg == "gacha"){
//                val gacha = File(voiceDir+"gacha_1.amr").toExternalResource().uploadAsVoice(group)
//                group.sendMessage(gacha)
//            }
            if (msg.startsWith("#")) {
                msg = msg.removePrefix("#")
                when (true) {
                    msg == "help" -> {
                        group.sendImage(File("${imgDir}help.png"))
                    }
                    msg == "setu" -> {
                        if ((0..1000).random() < 993) {
                            files = Getto.getImgList(setuDir)
                            group.sendImage(File(setuDir + files.random()))
                        }
                    }
                    msg.startsWith("pcr") -> {
                        msg = msg.removePrefix("pcr").trim()
                        when (true) {
                            msg.startsWith("comic") -> {
                                files = Getto.getImgList(comicDir)
                                if (msg.removePrefix("comic").trim().isEmpty()) {
                                    group.sendMessage("已更新到${files.size - 1}话,请输入相应话数，如\"#pcr comic 2\"")
                                } else {
                                    var fileName = "episode_${msg.removePrefix("comic").trim().toInt()}.png"
                                    if (fileName !in files) {
                                        group.sendMessage("没有你要找的")
                                    } else {
                                        group.sendImage(File(comicDir + fileName))
                                    }
                                }
                            }
                            msg.startsWith("stamp") -> {
                                files = Getto.getImgList(stampDir)
                                val fileName =
                                    if (msg.removePrefix("stamp").trim().isEmpty())
                                        files.random()
                                    else
                                        msg.removePrefix("stamp").trim() + ".png"

                                if (fileName !in files) {
                                    group.sendMessage("没有你要找的")
                                } else {
                                    group.sendImage(File(stampDir + fileName))
                                }
                            }
                            msg.startsWith("card") -> {
                                group.sendMessage(At(sender)+PlainText("图片较大，请稍等"))
                                files = Getto.getImgList(cardDir)
                                group.sendMessage(At(sender)+ group.uploadImage(File(cardDir + files.random())))
                            }
                        }
                    }
                    msg.startsWith("bang") -> {
                        msg = msg.removePrefix("bang").trim()
                        when(true){
                            msg.startsWith("bg") -> {
                                group.sendMessage(At(sender)+PlainText("图片较大，请稍等"))
                                files = Getto.getImgList(bangbgDir)
                                group.sendMessage(At(sender)+ group.uploadImage(File(bangbgDir + files.random())))
                            }
                        }
                    }
                }
            } else {
                when (msg) {
                    "hi", "早","早上好", "中午好", "下午好", "晚上好" -> {
                        val curTime = LocalTime.now().hour
                        val meet = when (curTime) {
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
                                "晚安,好好睡觉"
                            }
                        }
                        group.sendMessage(At(sender) + meet)
                    }
                    "@${bot.id}", "@${bot.id} " -> {
                        if (sender.id == GettoInfo.wifeID) {
                            group.sendMessage(At(sender) + group.uploadImage(File("${imgDir}wife.jpg")))
                        } else {
                            files = Getto.getImgList(atDir)
                            group.sendMessage(At(sender) + group.uploadImage(File(atDir + files.random())))
                        }
                    }
                    "@${bot.id}爬", "@${bot.id} 爬" -> {
                        group.sendMessage(At(sender) + group.uploadImage(File("${imgDir}pa.jpg")))
                    }
                    "签到" -> {
                        val signStr = Getto.sign(sender.id)
                        group.sendMessage(At(sender)+signStr)
                    }
                }
                when {
                    msg.startsWith("留言") -> {
                        bot.getFriendOrFail(GettoInfo.authorId).sendMessage(
                            "${sender.nick}(${sender.id})从群${group.name}(${group.id})发了条消息:\n${
                                msg.removePrefix("留言")
                            }"
                        )
                        group.sendMessage("已发送")
                    }
                    msg.startsWith("re") -> {
                        group.sendMessage(msg.removePrefix("re").trim())
                    }
                    msg.startsWith("来一份禁言") -> {
                        val botLv = group.botPermission.level
                        val sendLv = group.getMemberOrFail(sender.id).permission.level
                        val time =
                            if (msg.removePrefix("来一份禁言").trim().isEmpty())
                                (0..30 * 24 * 60 * 60).random()
                            else
                                msg.removePrefix("来一份禁言").trim().toInt()
                        if (time !in (0..30 * 24 * 60 * 60)) {
                            group.sendMessage("请输入0到2592000范围内的整数")
                        } else {
                            when {
                                botLv > sendLv -> {
                                    sender.mute(time)
                                    group.sendImage(File("${imgDir}mute.jpg"))
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
                        val face: Int =
                            if (msg.removePrefix(".r").trim().isEmpty())
                                6
                            else
                                msg.removePrefix(".r").trim().toInt()

                        group.sendMessage("${sender.nameCard}掷出了${(1..face).random()}")
                    }
                    msg.contains("啊这") || msg.contains("az") -> {
                        files = Getto.getImgList(imgDir + "az")
                        group.sendImage(File(azDir + files.random()))
                    }
                    msg.startsWith("举牌") -> {
                        msg = msg.removePrefix("举牌").trim()
                        val info:ImgInfo = when(msg.length) {
                            1 -> ImgInfo(250,60,120)
                            2 -> ImgInfo(200,60,120)
                            3 -> ImgInfo(155,60,110)
                            4 -> ImgInfo(155,85,85)
                            else -> {
                                ImgInfo(170,85,50)
                            }
                        }
                        Getto.addTextToImage(
                            File("${imgDir}jupai_ori.jpg"),
                            msg,
                            info,
                            File("${imgDir}jupai.jpg"))
                        group.sendImage(File("${imgDir}jupai.jpg"))
                    }
                    msg.startsWith("计算") -> {}
                    msg.startsWith("随机") -> {
                        when(msg.removePrefix("随机")){
                            "猫猫" -> {
                                files = Getto.getImgList(catsDir)
                                val cat = files.random()
                                if (cat.startsWith("dog")) {
                                    group.sendMessage(PlainText("恭喜${sender.nameCard}随机出一只狗狗") + group.uploadImage(File(catsDir + cat)))
                                } else {
                                    group.sendImage(File(catsDir + cat))
                                }
                            }
                            "傻狗" -> {
                                files = Getto.getImgList(sdDir)
                                val dog = files.random()
                                group.sendImage(File(sdDir + dog))
                            }
                            "色图" -> {
                                group.sendMessage("开发中,欢迎投稿色图")
                            }
                            "pcr卡面" -> {
                                files = Getto.getImgList(cardDir)
                                group.sendImage(File(cardDir + files.random()))
                            }
                            "头像" -> {
                                val ava = Getto.getDownloadFile(group.members.random().avatarUrl, imgDir+"tmp")
                                group.sendImage(ava)
                                ava.deleteRecursively()
                            }
                        }
                    }
                }
            }
            
        }
        /**
         * 好友消息
         */
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            if (sender.id == GettoInfo.authorId) {
                val msg = message.contentToString()
                when (true) {
                    msg.startsWith("回复") -> {
                        val list = msg.removePrefix("回复").trim().split(' ')
                        val receiver = bot.getFriendOrFail(list[0].toLong())
                        val replyMsg = list[1]
                        receiver.sendMessage("来自作者的回复:\n" + replyMsg)
                    }
                    msg.startsWith("upcat") -> {
                        var uped = 0
                        var catNum = 0
                        val catList = Getto.getImgList(catsDir)
                        catList.forEach {
                            if (it.startsWith("cat"))
                                catNum++
                        }
                        message.forEach {
                            if (it is Image) {
                                Getto.downloadFile(it.queryUrl(), catsDir + "cat (${catNum++})")
                                uped++
                            }
                        }
                        sender.sendMessage("已上传${uped}张猫猫，现有猫猫${catNum}张")
                    }
                    msg.startsWith("updog") -> {
                        var uped = 0
                        var dogNum = 0
                        val catList = Getto.getImgList(catsDir)
                        catList.forEach {
                            if (it.startsWith("dog"))
                                dogNum++
                        }
                        message.forEach {
                            if (it is Image) {
                                Getto.downloadFile(it.queryUrl(), catsDir + "dog${dogNum++}")
                                uped++
                            }
                        }
                        sender.sendMessage("已上传${uped}张狗狗，现有狗狗${dogNum}张")
                    }
                    msg == "help" -> {
                        sender.sendImage(File("${imgDir}help.png"))
                    }
                    msg.startsWith("广播") -> {
                        val content = msg.removePrefix("广播").trim()
                        bot.groups.forEach {
                            it.sendMessage(PlainText("来自作者的广播消息:")+content)
                        }
                    }
                    msg == "status" -> {
                        var duration = Duration.between(startTime,LocalDateTime.now())
                        sender.sendMessage("登录IP:${InetAddress.getLocalHost().hostAddress}\n" +
                            "好友数:${bot.friends.size}\n" +
                            "已加入群:${bot.groups.size}\n" +
                            "Java版本:${System.getProperties().getProperty("java.version")}\n" +
                            "操作系统名称:${System.getProperties().getProperty("os.name")}\n" +
                            "操作系统版本:${System.getProperties().getProperty("os.version")}\n" +
                            "上次登陆时间:${startTime.year}年${startTime.monthValue}月${startTime.dayOfMonth}日${startTime.hour}时${startTime.minute}分${startTime.second}秒\n"+
                            "已运行时间:${duration.toDaysPart()}天${duration.toHoursPart()}小时${duration.toMinutesPart()}分钟${duration.toSecondsPart()}秒")
                    }
                }
            } else {
                val msg = message.contentToString()
                if (msg == "#help") {
                    sender.sendImage(File("${imgDir}help.png"))
                } else if (msg == "\$") {
                    sender.sendImage(File("${imgDir}money.jpg"))
                } else {
                    sender.sendMessage("bot暂无私聊功能哦,请输入#help以获取最新指令功能表,若想打赏作者请输入\$获取作者收款码,记得拉我进群哦")
                }
            }
        }
        /**
         * 临时消息
         */
        globalEventChannel().subscribeAlways<GroupTempMessageEvent> {
            val msg = message.contentToString()
            if (msg == "#help") {
                sender.sendImage(File("${imgDir}help.png"))
            } else if (msg == "\$") {
                sender.sendImage(File("${imgDir}money.jpg"))
            } else {
                sender.sendMessage("bot暂无私聊功能哦,请输入#help以获取最新指令功能表,若想打赏作者请输入\$获取作者收款码,记得拉我进群哦")
            }
        }
        /**
         * 加群邀请
         */
        globalEventChannel().subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            accept()
        }
        /**
         * 接受邀请入群
         */
        globalEventChannel().subscribeAlways<BotJoinGroupEvent.Invite> {
            bot.getFriendOrFail(GettoInfo.authorId)
                .sendMessage("接受${invitor.nick}(${invitor.id})邀请已加入群${group.name}(${group.id})")
             group.sendMessage("bot已加入群聊，请输入#help获取最新指令功能表")
        }
        /**
         * 成员退群
         */
        globalEventChannel().subscribeAlways<MemberLeaveEvent> {
            group.sendMessage("${member.nick}离开了我们。。。 ")
        }
        /**
         * 被踢出群
         */
        globalEventChannel().subscribeAlways<BotLeaveEvent.Kick> {
            bot.getFriendOrFail(GettoInfo.authorId)
                .sendMessage("被${operator.nick}(${operator.id})踢出群${group.name}(${group.id})")
        }
        /**
         * 成员加群
         */
        globalEventChannel().subscribeAlways<MemberJoinEvent> {
            group.sendMessage(
                PlainText("欢迎新人") +
                    At(member) +
                    PlainText("加群,要和大家愉快玩耍哦，发送#help可获取bot可用指令")
            )
        }
        /**
         * 戳一戳消息
         */
        globalEventChannel().subscribeAlways<NudgeEvent> {
            if (target == bot) {
                from.nudge().sendTo(subject)
            }
        }
        /**
         * 群撤回消息
         */
        globalEventChannel().subscribeAlways<MessageRecallEvent.GroupRecall> {
            val dtm = LocalDateTime.now()
            bot.getFriendOrFail(GettoInfo.authorId)
                .sendMessage(PlainText("${dtm.toLocalDate()} ${dtm.hour}:${dtm.minute}:${dtm.second}\n${operator?.nick}(${operator?.id})\n在群${group.name}(${group.id})撤回了一条\n${author.nick}(${authorId})发送的消息"))
            val msg = MessagesPool.getById(messageIds[0])
            println("撤回消息ID:${messageIds[0]}")
            if (msg != null)
                msg.sendTo(bot.getFriendOrFail(GettoInfo.authorId))
            else
                bot.getFriendOrFail(GettoInfo.authorId).sendMessage("该撤回消息已过期")
        }
        /**
         * 被挤下线时自动重连
         */
        globalEventChannel().subscribeAlways<BotOfflineEvent.Force> {
            reconnect = true
        }
        /**
         * 添加新好友
         */
        globalEventChannel().subscribeAlways<FriendAddEvent> {
            friend.sendMessage("bot暂无私聊功能哦,请输入#help以获取最新指令功能表,若想打赏作者请输入\$获取作者收款码,记得拉我进群哦")
            bot.getFriendOrFail(GettoInfo.authorId).sendMessage("已添加${friend.nick}(${friend.id})为好友")
        }
        /**
         * 添加好友请求
         */
        globalEventChannel().subscribeAlways<NewFriendRequestEvent> {
            bot.getFriendOrFail(GettoInfo.authorId).sendMessage("${fromNick}(${fromId})请求添加好友")
            bot.eventChannel.subscribeAlways<FriendMessageEvent> {
                if(sender.id == GettoInfo.authorId){
                    if(message.contentToString() == "ok"){
                        accept()
                    }
                }
            }
        }
        /**
         * 上线
         */
        globalEventChannel().subscribeAlways<BotOnlineEvent> {
//            startTime = LocalDateTime.now()
        }
    }
}

